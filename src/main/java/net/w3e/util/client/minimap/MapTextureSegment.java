package net.w3e.util.client.minimap;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import com.mojang.blaze3d.platform.NativeImage;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;

import java.util.ArrayList;
import java.util.List;

public class MapTextureSegment {

	public static int getTextureSize(int scale) {
		int scaleMultiplier = 1 << scale;
		int textureSize = 16 / scaleMultiplier;
		if (textureSize < 1) textureSize = 1;
		return textureSize;
	}

	private transient final ResourceKey<Level> dimension;

	private final ChunkPos chunkPos;
	private final byte scale;
	@Getter
	private transient final int textureSize;

	private final byte[] colors;

	// TODO
	private transient final List<MapUpdateListener> listeners = new ArrayList<>();

	public MapTextureSegment(ResourceKey<Level> dimension, ChunkPos chunkPos, int scale) {
		this.dimension = dimension;
		this.chunkPos = chunkPos;
		this.scale = (byte) scale;
		this.textureSize = getTextureSize(this.scale);
		this.colors = new byte[textureSize * textureSize];
	}

	public MapRadiusPredicate createRadiusPredicate(Level level, double observerX, double observerZ, float radius) {
		int scaleMultiplier = 1 << this.scale;
		if (level.dimensionType().hasCeiling()) {
			radius /= 2;
		}
		float blockRadius = Mth.square(radius / scaleMultiplier);
		float ditherRadiusBlocks = Mth.square((radius - 2) / scaleMultiplier);

		return (targetBlockX, targetBlockZ) -> {
			double deltaX = observerX - targetBlockX;
			double deltaZ = observerZ - targetBlockZ;
			double distanceSqr = (deltaX * deltaX) + (deltaZ * deltaZ);

			return new MapRadiusPredicate.RadiusState(distanceSqr < blockRadius, distanceSqr > ditherRadiusBlocks);
		};
	}

	public void updateChunkOnMap(final Level level) {
		updateChunkOnMap(level, MapRadiusPredicate.INFINITY);
	}

	public void updateChunkOnMap(final Level level, MapRadiusPredicate radiusPredicate) {
		if (level.dimension() != this.dimension) {
			return;
		}

		int scaleMultiplier = 1 << this.scale;

		int chunkMinBlockX = chunkPos.getMinBlockX();
		int chunkMinBlockZ = chunkPos.getMinBlockZ();

		BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
		BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos();

		boolean hasCelling = level.dimensionType().hasCeiling();

		List<MapUpdateListener.DirtyData> dirtyDataList = new ArrayList<>();

		for (int imgX = 0; imgX < this.textureSize; imgX++) {
			double previousAverageAreaHeight = 0.0;

			for (int imgY = -1; imgY < this.textureSize; imgY++) {
				int averagingAreaMinX = chunkMinBlockX + (imgX * scaleMultiplier);
				int averagingAreaMinZ = chunkMinBlockZ + (imgY * scaleMultiplier);

				Multiset<MapColor> colorCount = LinkedHashMultiset.create();
				LevelChunk chunk = level.getChunk(SectionPos.blockToSectionCoord(averagingAreaMinX), SectionPos.blockToSectionCoord(averagingAreaMinZ));

				if (chunk.isEmpty()) {
					continue;
				}
				int waterDepth = 0;
				double averageAreaHeight = 0.0;

				if (hasCelling) {
					int ceilingNoise = averagingAreaMinX + averagingAreaMinZ * 231871;
					ceilingNoise = ceilingNoise * ceilingNoise * 31287121 + ceilingNoise * 11;
					if ((ceilingNoise >> 20 & 1) == 0) {
						colorCount.add(Blocks.DIRT.defaultBlockState().getMapColor(level, BlockPos.ZERO), 10);
					} else {
						colorCount.add(Blocks.STONE.defaultBlockState().getMapColor(level, BlockPos.ZERO), 100);
					}
					averageAreaHeight = 100.0;
				} else {
					for (int averagingAreaDeltaX = 0; averagingAreaDeltaX < scaleMultiplier; averagingAreaDeltaX++) {
						for (int averagingAreaDeltaZ = 0; averagingAreaDeltaZ < scaleMultiplier; averagingAreaDeltaZ++) {
							blockPos.set(averagingAreaMinX + averagingAreaDeltaX, 0, averagingAreaMinZ + averagingAreaDeltaZ);

							int columnY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, blockPos.getX(), blockPos.getZ()) + 1;
							BlockState state;

							if (columnY <= level.getMinY()) {
								state = Blocks.BEDROCK.defaultBlockState();
							} else {
								do {
									blockPos.setY(--columnY);
									state = chunk.getBlockState(blockPos);
								} while (state.getMapColor(level, blockPos) == MapColor.NONE && columnY > level.getMinY());

								if (columnY > level.getMinY() && !state.getFluidState().isEmpty()) {
									int solidY = columnY - 1;
									belowPos.set(blockPos);
									BlockState belowBlock;
									do {
										belowPos.setY(solidY--);
										belowBlock = chunk.getBlockState(belowPos);
										waterDepth++;
									} while (solidY > level.getMinY() && !belowBlock.getFluidState().isEmpty());

									state = this.getCorrectStateForFluidBlock(level, state, blockPos);
								}
							}

							//data.checkBanners(level, blockPos.getX(), blockPos.getZ());
							averageAreaHeight += (double) columnY / (scaleMultiplier * scaleMultiplier);
							colorCount.add(state.getMapColor(level, blockPos));
						}
					}
				}

				waterDepth /= scaleMultiplier * scaleMultiplier;
				MapColor color = Iterables.getFirst(Multisets.copyHighestCountFirst(colorCount), MapColor.NONE);
				MapColor.Brightness brightness;

				if (color == MapColor.WATER) {
					double diff = waterDepth * 0.1 + (imgX + imgY & 1) * 0.2;
					if (diff < 0.5) {
						brightness = MapColor.Brightness.HIGH;
					} else if (diff > 0.9) {
						brightness = MapColor.Brightness.LOW;
					} else {
						brightness = MapColor.Brightness.NORMAL;
					}
				} else {
					double diff = (averageAreaHeight - previousAverageAreaHeight) * 4.0 / (scaleMultiplier + 4) + ((imgX + imgY & 1) - 0.5) * 0.4;
					if (diff > 0.6) {
						brightness = MapColor.Brightness.HIGH;
					} else if (diff < -0.6) {
						brightness = MapColor.Brightness.LOW;
					} else {
						brightness = MapColor.Brightness.NORMAL;
					}
				}

				previousAverageAreaHeight = averageAreaHeight;

				var state = radiusPredicate.getState(averagingAreaMinX, averagingAreaMinZ);

				if (imgY >= 0 && state.isInRadius() && (!state.ditherBlack() || (imgX + imgY & 1) != 0)) {
					byte newColor = color.getPackedId(brightness);
					int index = imgX + imgY * this.textureSize;
					byte oldColor = this.colors[index];
					if (oldColor != newColor) {
						this.colors[index] = newColor;
						dirtyDataList.add(new MapUpdateListener.DirtyData(imgX, imgY, newColor));
					}
				}
			}
		}
		//System.out.println(dirtyDataList);
	}

	private BlockState getCorrectStateForFluidBlock(final Level level, final BlockState state, final BlockPos pos) {
		FluidState fluidState = state.getFluidState();
		return !fluidState.isEmpty() && !state.isFaceSturdy(level, pos, Direction.UP) ? fluidState.createLegacyBlock() : state;
	}

	public void applyToTexture(NativeImage image) {
		int size = this.getTextureSize();
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				int i = x + y * size;
				image.setPixel(x, y, MapColor.getColorFromPackedId(this.colors[i]));
			}
		}
	}
}
