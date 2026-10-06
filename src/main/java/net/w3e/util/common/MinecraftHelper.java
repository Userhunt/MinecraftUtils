package net.w3e.util.common;

import net.minecraft.world.entity.Display;
import net.skds.lib2.mat.FastMath;
import net.skds.lib2.mat.matrix4.Matrix4;
import net.skds.lib2.mat.matrix4.Matrix4F;
import net.skds.lib2.mat.quat.Quat;
import net.skds.lib2.mat.quat.QuatF;
import net.skds.lib2.mat.vec3.Vec3;
import net.skds.lib2.shapes.AABB;
import net.w3e.lib.ObjectWrapper;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MinecraftHelper {

	public static final ObjectWrapper<Vec3, net.minecraft.world.phys.Vec3> VEC3 = new ObjectWrapper<>(Vec3.class, net.minecraft.world.phys.Vec3.class, e -> new net.minecraft.world.phys.Vec3(e.xf(), e.yf(), e.zf()), e -> Vec3.of(e.x, e.y, e.z));
	public static final ObjectWrapper<Vec3, Vector3f> VEC3F = new ObjectWrapper<>(Vec3.class, Vector3f.class, e -> new Vector3f(e.xf(), e.yf(), e.zf()), e -> Vec3.of(e.x, e.y, e.z));
	public static final ObjectWrapper<Quat, Quaternionf> QUAT = new ObjectWrapper<>(Quat.class, Quaternionf.class, e -> new Quaternionf(e.xf(), e.yf(), e.zf(), e.wf()), e -> new QuatF(e.x(), e.y(), e.z(), e.w()));
	public static final ObjectWrapper<AABB, net.minecraft.world.phys.AABB> AABB = new ObjectWrapper<>(AABB.class, net.minecraft.world.phys.AABB.class, e -> new net.minecraft.world.phys.AABB(e.minX, e.minY, e.minZ, e.maxX, e.maxY, e.maxZ), e -> new AABB(e.minX, e.minY, e.minZ, e.maxX, e.maxY, e.maxZ));
	public static final ObjectWrapper<Matrix4, Matrix4fc> MATRIX4 = new ObjectWrapper<>(Matrix4.class, Matrix4fc.class,
			e -> new Matrix4f(
					e.m00f(), e.m01f(), e.m02f(), e.m03f(),
					e.m10f(), e.m11f(), e.m12f(), e.m13f(),
					e.m20f(), e.m21f(), e.m22f(), e.m23f(),
					e.m30f(), e.m31f(), e.m32f(), e.m33f()
			),
			e -> new Matrix4F(
					e.m00(), e.m01(), e.m02(), e.m03(),
					e.m10(), e.m11(), e.m12(), e.m13(),
					e.m20(), e.m21(), e.m22(), e.m23(),
					e.m30(), e.m31(), e.m32(), e.m33()
			)
	);


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
