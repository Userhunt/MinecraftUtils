package net.w3e.util.common;

import net.skds.lib2.mat.FastMath;
import net.skds.lib2.mat.quat.Quat;
import net.skds.lib2.mat.vec3.Vec3;
import org.joml.Vector3f;

public class MatrixUtil {

	public static Quat getQuat(float yaw, float pitch, float roll) {
		return Quat.fromAxisDegrees(Vec3.YN, yaw).rotateAxisDegrees(Vec3.XP, pitch).rotateAxisDegrees(Vec3.ZP, roll);
	}

	public static Vec3 getYPR(Quat quat) {
		Vector3f anglesRadians = new Vector3f();

		MinecraftHelper.QUAT.convertOR(quat).getEulerAnglesYXZ(anglesRadians);

		var yaw = FastMath.round(-anglesRadians.y * FastMath.RAD_2_DGR * 100) / 100f;
		var pitch = FastMath.round(anglesRadians.x * FastMath.RAD_2_DGR * 100) / 100f;
		var roll = FastMath.round(anglesRadians.z * FastMath.RAD_2_DGR * 100) / 100f;

		return Vec3.of(yaw, pitch, roll);
	}

}
