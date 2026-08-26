package net.w3e.util.client;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientMannequin;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.skds.lib2.mat.vec3.Vec3;
import net.w3e.util.common.RenderStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public record RenderStackImpl(Minecraft minecraft, LevelRenderer renderer,
							  Camera camera,
							  Frustum frustum,
							  LevelRenderState output,
							  Vec3 camPos,
							  TickRateManager tickRateManager,
							  boolean shouldShowEntityOutlines
) implements RenderStack {

	@Override
	public Player getPlayer() {
		assert this.minecraft.player != null;
		return this.minecraft.player;
	}

	@Override
	public Level getWorld() {
		assert this.minecraft.level != null;
		return this.minecraft.level;
	}

	@Override
	public Mannequin createClientMannequin() {
		//noinspection DataFlowIssue
		return new ClientMannequin(this.getWorld(), null);
	}

	private static final List<Consumer<RenderStack>> RENDERERS = new ArrayList<>();

	public static boolean hasRenderer() {
		return !RENDERERS.isEmpty();
	}

	public static void register(Consumer<RenderStack> renderer) {
		RENDERERS.add(renderer);
	}

	public static void render(RenderStack renderStack) {
		for (Consumer<RenderStack> renderer : RENDERERS) {
			renderer.accept(renderStack);
		}
	}

	public void render(Entity entity) {
		if (this.minecraft.player == null) {
			return;
		}
		if (entity.level() != minecraft.level) {
			assert minecraft.level != null;
			entity.setLevel(minecraft.level);
		}
		EntityRenderDispatcher entityRenderDispatcher = this.minecraft.getEntityRenderDispatcher();
		if (entityRenderDispatcher.shouldRender(entity, this.frustum, this.camPos.x(), this.camPos.y(), this.camPos.z()) || entity.hasIndirectPassenger(this.minecraft.player)) {
			BlockPos blockPos = entity.blockPosition();
			if ((minecraft.level.isOutsideBuildHeight(blockPos.getY()) || renderer.isSectionCompiledAndVisible(blockPos))
					&& (entity != camera.entity() || camera.isDetached() || camera.entity() instanceof LivingEntity && ((LivingEntity) camera.entity()).isSleeping())
					&& (!(entity instanceof LocalPlayer) || camera.entity() == entity)) {
				if (entity.tickCount == 0) {
					entity.xOld = entity.getX();
					entity.yOld = entity.getY();
					entity.zOld = entity.getZ();
				}

				EntityRenderState state = entityRenderDispatcher.extractEntity(entity, 1);
				this.output.entityRenderStates.add(state);
				if (state.appearsGlowing() && this.shouldShowEntityOutlines) {
					this.output.haveGlowingEntities = true;
				}
			}
		}
	}

}
