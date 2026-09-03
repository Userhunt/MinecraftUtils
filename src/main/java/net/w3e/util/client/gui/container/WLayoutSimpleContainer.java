package net.w3e.util.client.gui.container;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.w3e.util.client.gui.layout.WAbstractLayout;
import net.w3e.util.client.gui.layout.WLayout;
import net.w3e.util.client.gui.layout.WLayoutElementData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WLayoutSimpleContainer extends WAbstractLayout {

	@Getter(onMethod_ = @Override)
	@Setter(onMethod_ = @Override)
	private boolean dragging;
	@Getter(onMethod_ = @Override)
	private GuiEventListener focused;

	protected final List<LayoutElement> elements = new ArrayList<>();
	private final List<GuiEventListener> children = new ArrayList<>();
	private final List<Renderable> renderables = new ArrayList<>();

	public void add(WLayoutElementData<?> element) {
		this.addElement(element);
		this.onAddElement(element);
	}

	protected final void addElement(LayoutElement element) {
		this.elements.add(element);
	}

	protected final void onAddElement(LayoutElement element) {
		if (element.getClass() == WLayoutElementData.class) {
			WLayoutElementData<?> elementData = (WLayoutElementData<?>) element;
			if (!elementData.onLayout()) {
				return;
			}
			element = ((WLayoutElementData<?>) element).element();
		}
		if (element instanceof GuiEventListener guiEventListener) {
			this.children.add(guiEventListener);
		}
		if (element instanceof Renderable renderable) {
			this.renderables.add(renderable);
		}
		this.resetSize();
	}

	protected final void resetSize() {
		this.setWidth(-1);
		this.setHeight(-1);
	}

	public <E extends LayoutElement> WLayoutElementData<E> add(int x, int y, E element) {
		WLayoutElementData<E> elementData = new WLayoutElementData<>(x, y, element);
		this.add(elementData);
		return elementData;
	}

	public void clear() {
		this.elements.clear();
		this.children.clear();
		this.renderables.clear();
		this.resetSize();
	}

	@Override
	public void setX(int x) {
		super.setX(x);
		for (LayoutElement element : elements) {
			element.setX(x);
		}
	}

	@Override
	public void setY(int y) {
		super.setY(y);
		for (LayoutElement element : elements) {
			element.setY(y);
		}
	}

	@Override
	public void setPosition(int x, int y) {
		super.setPosition(x, y);
	}

	@Override
	public int getWidth() {
		if (super.getWidth() == -1) {
			if (this.elements.isEmpty()) {
				return 0;
			}
			final var x = this.getX();
			for (LayoutElement element : this.elements) {
				if (element instanceof WLayoutElementData<?> elementData && !elementData.onLayout()) {
					continue;
				}
				var e = Math.max(element.getX() - x, 0);
				var v = element.getWidth();
				var old = super.getWidth();
				this.setWidth(Math.max(old, e + v));
			}
		}
		return super.getWidth();
	}

	@Override
	public int getHeight() {
		if (super.getHeight() == -1) {
			if (this.elements.isEmpty()) {
				return 0;
			}
			final var y = this.getY();
			for (LayoutElement element : this.elements) {
				if (element instanceof WLayoutElementData<?> elementData && !elementData.onLayout()) {
					continue;
				}
				var e = Math.max(element.getY() - y, 0);
				var v = element.getHeight();
				var old = super.getHeight();
				this.setHeight(Math.max(old, e + v));
			}
		}
		return super.getHeight();
	}

	@Override
	public void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {
		super.visitWidgets(widgetVisitor);
	}

	@Override
	public void visitChildren(Consumer<LayoutElement> visitor) {
		for (LayoutElement element : this.elements) {
			if (element.getClass() == WLayoutElementData.class) {
				WLayoutElementData<?> elementData = (WLayoutElementData<?>) element;
				if (!elementData.onLayout()) {
					continue;
				}
				element = elementData.element();
			}
			visitor.accept(element);
		}
	}

	@Override
	public void arrangeElements() {
		this.children.clear();
		this.renderables.clear();
		this.resetSize();
		super.arrangeElements();
		repositionElements();
	}

	protected final void repositionElements() {
		visitChildren(this::arrangeVisitor);
	}

	private void arrangeVisitor(LayoutElement element) {
		if (element instanceof WLayout layout) {
			onAddElement(layout);
			return;
		}
		if (element instanceof Layout layout) {
			layout.visitChildren(this::arrangeVisitor);
		} else {
			onAddElement(element);
		}
	}

	@Override
	protected void extractWidgetRenderStateWithScissor(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		for (Renderable renderable : this.renderables) {
			renderable.extractRenderState(graphics, mouseX, mouseY, a);
		}
	}

	@Override
	public List<? extends GuiEventListener> children() {
		return this.children;
	}

	@Override
	public void setFocused(GuiEventListener focused) {
		this.focused = focused;
	}

	@Override
	public void setFocused(boolean focused) {
		super.setFocused(focused);
	}

}
