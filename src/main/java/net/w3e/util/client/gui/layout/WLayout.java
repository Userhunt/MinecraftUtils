package net.w3e.util.client.gui.layout;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.navigation.ScreenRectangle;

public interface WLayout extends Layout, Renderable, ContainerEventHandler {

	@Override
	default ScreenRectangle getRectangle() {
		return Layout.super.getRectangle();
	}

}
