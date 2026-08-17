package net.w3e.util.client;

import com.mojang.blaze3d.Blaze3D;
import net.minecraft.client.Minecraft;
import net.w3e.util.mixins.client.MouseHandlerAccessor;
import org.lwjgl.glfw.GLFW;

public class MouseHelper {

	public static void moveToCenter() {
		Minecraft minecraft = Minecraft.getInstance();
		var window = minecraft.getWindow();

		double centerX = window.getWidth() / 2.0;
		double centerY = window.getHeight() / 2.0;

		GLFW.glfwSetCursorPos(window.handle(), centerX, centerY);
	}

	public static void turnPlayer(double x, double y) {
		Minecraft minecraft = Minecraft.getInstance();
		var mouseHandler = minecraft.mouseHandler;
		MouseHandlerAccessor accessor = (MouseHandlerAccessor) mouseHandler;

		var wasX = accessor.w3e$getAccumulatedDX();
		var wasY = accessor.w3e$getAccumulatedDY();
		accessor.w3e$setAccumulatedDX(x);
		accessor.w3e$setAccumulatedDY(y);

		double time = Blaze3D.getTime();
		double mousea = time - accessor.w3e$getLastHandleMovementTime();
		accessor.w3e$setLastHandleMovementTime(time);
		accessor.w3e$turnPlayer(mousea);

		accessor.w3e$setAccumulatedDX(wasX);
		accessor.w3e$setAccumulatedDY(wasY);
	}
}
