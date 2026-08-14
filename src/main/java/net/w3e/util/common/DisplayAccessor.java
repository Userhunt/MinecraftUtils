package net.w3e.util.common;

import net.skds.lib2.mat.quat.Quat;
import net.skds.lib2.mat.vec3.Vec3;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;

public interface DisplayAccessor {
	default void w3e$setTranslation(Vec3 translation) {
		w3e$setTranslation(MinecraftHelper.VEC3_VECTOR3F.convertOR(translation));
	}

	void w3e$setTranslation(Vector3fc translation);

	default void w3e$setScale(Vec3 scale) {
		w3e$setScale(MinecraftHelper.VEC3_VECTOR3F.convertOR(scale));
	}

	void w3e$setScale(Vector3fc scale);

	default void w3e$setLeftRotation(Quat leftRotation) {
		w3e$setLeftRotation(MinecraftHelper.QUAT_QUATERNIONF.convertOR(leftRotation));
	}

	void w3e$setLeftRotation(Quaternionfc leftRotation);

	default void w3e$setRightRotation(Quat rightRotation) {
		w3e$setRightRotation(MinecraftHelper.QUAT_QUATERNIONF.convertOR(rightRotation));
	}

	void w3e$setRightRotation(Quaternionfc rightRotation);
}
