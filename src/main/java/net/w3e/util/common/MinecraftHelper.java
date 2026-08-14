package net.w3e.util.common;

import net.minecraft.world.entity.Display;
import net.skds.lib2.mat.FastMath;
import net.skds.lib2.mat.quat.Quat;
import net.skds.lib2.mat.quat.QuatF;
import net.skds.lib2.mat.vec3.Vec3;
import net.w3e.lib.ObjectWrapper;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MinecraftHelper {

	public static final ObjectWrapper<Vec3, Vector3f> VEC3_VECTOR3F = new ObjectWrapper<>(Vec3.class, Vector3f.class, e -> new Vector3f(e.xf(), e.yf(), e.zf()), e -> Vec3.of(e.x, e.y, e.z));
	public static final ObjectWrapper<Vec3, net.minecraft.world.phys.Vec3> VEC3_VEC3 = new ObjectWrapper<>(Vec3.class, net.minecraft.world.phys.Vec3.class, e -> new net.minecraft.world.phys.Vec3(e.xf(), e.yf(), e.zf()), e -> Vec3.of(e.x, e.y, e.z));
	public static final ObjectWrapper<Quat, Quaternionf> QUAT_QUATERNIONF = new ObjectWrapper<>(Quat.class, Quaternionf.class, e -> new Quaternionf(e.xf(), e.yf(), e.zf(), e.wf()), e -> new QuatF(e.x(), e.y(), e.z(), e.z()));

	public static void setTextAlign(Display.TextDisplay entity, Display.TextDisplay.Align align) {
		byte b = entity.getFlags();
		b &= ~(3 << 3);
		b |= (byte) (align.ordinal() << 3);
		entity.setFlags(b);
	}

	public static float getYaw(Vec3 direction) {
		return (float) Math.atan2(-direction.x(), direction.z()) * FastMath.RAD_2_DGR;
	}

	public static float getPitch(Vec3 direction) {
		float x = (float) direction.x();
		float z = (float) direction.z();
		float d = (float) Math.sqrt(x * x + z * z);
		return (float) -Math.atan2(direction.y(), d) * FastMath.RAD_2_DGR;
	}

}
