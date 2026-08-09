package net.w3e.util.client.gui.layout;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WLayoutSimpleContainer extends WAbstractLayoutContainer {

	@Getter(onMethod_ = @Override)
	@Setter(onMethod_ = @Override)
	private boolean dragging;
	@Getter(onMethod_ = @Override)
	private GuiEventListener focused;

	private final List<LayoutElement> elements = new ArrayList<>();
	private final List<GuiEventListener> children = new ArrayList<>();
	private final List<Renderable> renderables = new ArrayList<>();

	public void add(WLayoutElementData<?> element) {
		this.elements.add(element);
		this.onAddElement(element.element());
	}

	private void onAddElement(LayoutElement element) {
		if (element.getClass() == WLayoutElementData.class) {
			element = ((WLayoutElementData<?>) element).element();
		}
		if (element instanceof GuiEventListener guiEventListener) {
			this.children.add(guiEventListener);
		}
		if (element instanceof Renderable renderable) {
			this.renderables.add(renderable);
		}
		this.setWidth(-1);
		this.setHeight(-1);
	}

	public <E extends LayoutElement> WLayoutElementData<E> add(int x, int y, E element) {
		WLayoutElementData<E> elementData = new WLayoutElementData<>(x, y, element);
		elementData.setX(0);
		elementData.setY(0);

		this.add(elementData);

		return elementData;
	}

	protected void clear() {
		this.elements.clear();
		this.children.clear();
		this.renderables.clear();
		this.width = -1;
		this.height = -1;
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
			var x = this.getX();
			for (LayoutElement element : this.elements) {
				this.setWidth(Math.max(super.getWidth(), Math.max(element.getX() - x, 0) + element.getWidth()));
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
			var y = this.getY();
			for (LayoutElement element : this.elements) {
				this.setHeight(Math.max(super.getHeight(), Math.max(element.getY() - y, 0) + element.getHeight()));
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
			if (element instanceof WLayoutElementData<?> elementData) {
				element = elementData.element();
			}
			visitor.accept(element);
		}
	}

	@Override
	public void arrangeElements() {
		this.children.clear();
		this.renderables.clear();
		super.arrangeElements();
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
		if (!focused) {
			this.setFocused(null);
		}
		super.setFocused(focused);
	}

}
