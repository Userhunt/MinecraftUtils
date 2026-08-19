package net.w3e.util.client.minimap.example;

import net.minecraft.client.gui.components.ImageWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ChunkPos;
import net.w3e.util.client.gui.container.WLayoutProviderContainer;
import net.w3e.util.client.minimap.MapRadiusPredicate;
import net.w3e.util.client.minimap.MapTextureSegment;
import net.w3e.util.client.texture.WDynamicTexture;

public class MapTestScreen extends Screen {

	public MapTestScreen() {
		super(Component.literal("MapTestScreen"));
	}

	@Override
	protected void init() {
		int spacing = 10;
		GridLayout layout = new GridLayout().spacing(spacing);

		var player = this.minecraft.player;
		assert player != null;

		for (int i = 0; i <= 4; i++) {
			for (boolean ditherBlack : new boolean[]{true, false}) {
				Container container = new Container(i, ditherBlack);

				layout.addChild(container, ditherBlack ? 1 : 0, i);
			}
		}

		int SIZE = Container.IMAGE_SIZE * 3 + Container.OFFSET * 2;

		layout.setPosition(this.width / 2 - (SIZE * 5 + spacing * 4) / 2, this.height / 2 - (SIZE * 2 + spacing) / 2);
		layout.arrangeElements();
		layout.visitWidgets(this::addRenderableWidget);
	}

	private class Container extends WLayoutProviderContainer {

		private static final int IMAGE_SIZE = 32;
		private static final int OFFSET = 4;
		private static final int RADIUS = 24;

		public Container(int scale, boolean ditherBlack) {
			GridLayout contents = new GridLayout();
			super(contents);

			//int textureSize = MapTextureSegment.getTextureSize(scale);
			int textureSize = 16;

			var player = minecraft.player;
			assert player != null;

			var chunkPos = player.chunkPosition();

			var world = player.level();
			var dimension = world.dimension();

			int offset = OFFSET * textureSize / 16;
			offset = Math.max(OFFSET, offset);
			contents.spacing(offset);
			System.out.println(offset);

			var playerX = player.getX();
			var playerZ = player.getZ();

			textureSize = IMAGE_SIZE * textureSize / 16;

			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					var segment = new MapTextureSegment(dimension, new ChunkPos(chunkPos.x() + x, chunkPos.z() + z), scale);
					segment.updateChunkOnMap(world, ditherBlack
							? segment.createRadiusPredicate(world, playerX, playerZ, RADIUS)
							: MapRadiusPredicate.INFINITY
					);

					var texture = WDynamicTexture.createBuilder(Identifier.fromNamespaceAndPath(
									"sd", "map_" + scale + "_" + ditherBlack + "/" + x + "_" + z
							)).fromMapSegment(segment)
							.build();

					var image = ImageWidget.texture(textureSize, textureSize, texture.getTextureId(), textureSize, textureSize);
					contents.addChild(image, z + 1, x + 1);
				}
			}
		}
	}
}
