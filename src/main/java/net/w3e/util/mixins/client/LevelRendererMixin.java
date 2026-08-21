package net.w3e.util.mixins.client;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.world.TickRateManager;
import net.skds.lib2.mat.vec3.Vec3;
import net.w3e.util.client.RenderStackImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

	@Shadow
	@Final
	private Minecraft minecraft;

	@Shadow
	protected abstract boolean shouldShowEntityOutlines();

	@Inject(method = "extractVisibleEntities", at = @At(
			value = "INVOKE",
			target = "Ljava/util/List;size()I",
			ordinal = 0
	))
	private void onRender(Camera camera, Frustum frustum, DeltaTracker deltaTracker, LevelRenderState output, CallbackInfo ci) {
		if (this.minecraft.level == null || !RenderStackImpl.hasRenderer()) {
			return;
		}
		var cameraPos = camera.position();
		TickRateManager tickRateManager = this.minecraft.level.tickRateManager();
		boolean shouldShowEntityOutlines = this.shouldShowEntityOutlines();
		@SuppressWarnings("all")
		RenderStackImpl renderStack = new RenderStackImpl(this.minecraft, (LevelRenderer) (Object) this, camera, frustum, output, Vec3.of(cameraPos.x(), cameraPos.y(), cameraPos.z()), tickRateManager, shouldShowEntityOutlines);
		RenderStackImpl.render(renderStack);
	}

}
