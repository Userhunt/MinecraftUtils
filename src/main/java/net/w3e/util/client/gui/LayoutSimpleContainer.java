package net.w3e.util.client.gui;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.PreeditEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class LayoutSimpleContainer extends AbstractWidget implements Layout, ContainerEventHandler {

	@Getter(onMethod_ = @Override)
	@Setter(onMethod_ = @Override)
	private boolean dragging;
	@Getter(onMethod_ = @Override)
	private GuiEventListener focused;

	private final List<LayoutElement> elements = new ArrayList<>();
	private final List<GuiEventListener> children = new ArrayList<>();
	private final List<Renderable> renderables = new ArrayList<>();

	@Setter
	@Getter
	private boolean scissor = false;

	public LayoutSimpleContainer() {
		super(0, 0, -1, -1, Component.empty());
	}

	public void add(LayoutElementData<?> element) {
		this.elements.add(element);
		if (element.element instanceof GuiEventListener guiEventListener) {
			this.children.add(guiEventListener);
		}
		if (element.element instanceof Renderable renderable) {
			this.renderables.add(renderable);
		}
		this.setWidth(-1);
		this.setHeight(-1);
	}

	public <E extends LayoutElement> LayoutElementData<E> add(int x, int y, E element) {
		LayoutElementData<E> elementData = new LayoutElementData<>(x, y, element);
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
	public void visitChildren(Consumer<LayoutElement> visitor) {
		for (LayoutElement element : this.elements) {
			if (element instanceof LayoutElementData<?> elementData) {
				element = elementData.element;
			}
			visitor.accept(element);
		}
	}

	@Override
	public void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {
		super.visitWidgets(widgetVisitor);
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
	}

	@Override
	protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		var scissor = this.scissor;
		if (scissor) {
			int scissorX = this.getX();
			int scissorY = this.getY();
			int scissorW = this.getWidth();
			int scissorH = this.getHeight();
			graphics.enableScissor(scissorX, scissorY, scissorX + scissorW, scissorY + scissorH);
		}
		for (Renderable renderable : this.renderables) {
			renderable.extractRenderState(graphics, mouseX, mouseY, a);
		}
		if (scissor) {
			graphics.disableScissor();
		}
	}

	@Override
	public List<? extends GuiEventListener> children() {
		return this.children;
	}

	@Override
	public Optional<GuiEventListener> getChildAt(double x, double y) {
		return ContainerEventHandler.super.getChildAt(x, y);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		return ContainerEventHandler.super.mouseClicked(event, doubleClick);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		return ContainerEventHandler.super.mouseReleased(event);
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
		return ContainerEventHandler.super.mouseDragged(event, dx, dy);
	}

	@Override
	public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
		return ContainerEventHandler.super.mouseScrolled(x, y, scrollX, scrollY);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		return ContainerEventHandler.super.keyPressed(event);
	}

	@Override
	public boolean keyReleased(KeyEvent event) {
		return ContainerEventHandler.super.keyReleased(event);
	}

	@Override
	public boolean charTyped(CharacterEvent event) {
		return ContainerEventHandler.super.charTyped(event);
	}

	@Override
	public boolean preeditUpdated(@Nullable PreeditEvent event) {
		return ContainerEventHandler.super.preeditUpdated(event);
	}

	@Override
	public ScreenRectangle getBorderForArrowNavigation(ScreenDirection opposite) {
		return ContainerEventHandler.super.getBorderForArrowNavigation(opposite);
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

	@Override
	public @Nullable ComponentPath getCurrentFocusPath() {
		return ContainerEventHandler.super.getCurrentFocusPath();
	}

	@Override
	public @Nullable ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
		return ContainerEventHandler.super.nextFocusPath(navigationEvent);
	}

	@Override
	public void mouseMoved(double x, double y) {
		ContainerEventHandler.super.mouseMoved(x, y);
	}

	@Override
	public boolean shouldTakeFocusAfterInteraction() {
		return ContainerEventHandler.super.shouldTakeFocusAfterInteraction();
	}

	@Override
	public ScreenRectangle getRectangle() {
		return super.getRectangle();
	}

	public record LayoutElementData<E extends LayoutElement>(int x, int y, E element) implements LayoutElement {

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
	}

}
