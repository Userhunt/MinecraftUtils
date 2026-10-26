package net.w3e.util.client;

import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.w3e.util.mixins.client.MouseHandlerAccessor;
import org.lwjgl.glfw.GLFW;

public class MouseHelper {

	public static void moveToCenter() {
		Minecraft minecraft = Minecraft.getInstance();
		Window window = minecraft.getWindow();

		double centerX = window.getWidth() / 2.0;
		double centerY = window.getHeight() / 2.0;

		GLFW.glfwSetCursorPos(window.handle(), centerX, centerY);
	}

	public static void turnPlayer(double x, double y) {
		Minecraft minecraft = Minecraft.getInstance();
		MouseHandler mouseHandler = minecraft.mouseHandler;
		MouseHandlerAccessor accessor = (MouseHandlerAccessor) mouseHandler;

		double wasX = accessor.w3e$getAccumulatedDX();
		double wasY = accessor.w3e$getAccumulatedDY();
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
