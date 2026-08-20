package net.w3e.util.mixins.common;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.w3e.util.mixins.DisplayAccessor;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Display.class)
public abstract class DisplayMixin extends Entity implements DisplayAccessor {

	@SuppressWarnings("all")
	private DisplayMixin() {
		super(null, null);
	}

	@Final
	@Shadow
	private static EntityDataAccessor<Vector3fc> DATA_TRANSLATION_ID;
	@Final
	@Shadow
	private static EntityDataAccessor<Vector3fc> DATA_SCALE_ID;
	@Final
	@Shadow
	private static EntityDataAccessor<Quaternionfc> DATA_LEFT_ROTATION_ID;
	@Final
	@Shadow
	private static EntityDataAccessor<Quaternionfc> DATA_RIGHT_ROTATION_ID;

	@Override
	public void w3e$setTranslation(Vector3fc translation) {
		this.entityData.set(DATA_TRANSLATION_ID, translation);
	}

	@Override
	public void w3e$setScale(Vector3fc scale) {
		this.entityData.set(DATA_SCALE_ID, scale);
	}

	@Override
	public void w3e$setLeftRotation(Quaternionfc leftRotation) {
		this.entityData.set(DATA_LEFT_ROTATION_ID, leftRotation);
	}

	@Override
	public void w3e$setRightRotation(Quaternionfc rightRotation) {
		this.entityData.set(DATA_RIGHT_ROTATION_ID, rightRotation);
	}

	@Shadow
	protected boolean updateRenderState;

	@Override
	public void w3e$setUpdateRenderState(boolean updateRenderState) {
		this.updateRenderState = updateRenderState;
	}

	@Override
	public void w3e$setSharedFlag(int flag, boolean value) {
		super.setSharedFlag(flag, value);
	}
}
