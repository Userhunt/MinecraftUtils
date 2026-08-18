package net.w3e.util.client.gui.layout;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.LayoutElement;

import java.util.function.Consumer;

public record WLayoutElementData<E extends LayoutElement>(int x, int y, E element,
														  boolean onLayout) implements LayoutElement {

	public WLayoutElementData(int x, int y, E element) {
		this(x, y, element, true);
		this.setX(0);
		this.setY(0);
	}

	@Override
	public void setX(int x) {
		this.element.setX(this.x + x);
	}

	@Override
	public void setY(int y) {
		this.element.setY(this.y + y);
	}

	@Override
	public int getX() {
		return this.element.getX();
	}

	@Override
	public int getY() {
		return this.element.getY();
	}

	@Override
	public int getWidth() {
		return this.element.getWidth();
	}

	@Override
	public int getHeight() {
		return this.element.getHeight();
	}

	@Override
	public void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {
		this.element.visitWidgets(widgetVisitor);
	}

	public static LayoutElement asElement(LayoutElement element) {
		if (element.getClass() == WLayoutElementData.class) {
			return ((WLayoutElementData<?>) element).element;
		}
		return element;
	}
}

