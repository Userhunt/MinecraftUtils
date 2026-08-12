package net.w3e.util.client.gui.layout;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.PreeditEvent;
import net.minecraft.network.chat.CommonComponents;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class WAbstractLayout extends AbstractWidget implements WLayout {

	@Setter
	@Getter
	private boolean scissor = false;

	public WAbstractLayout() {
		super(0, 0, -1, -1, CommonComponents.EMPTY);
	}

	@Override
	public void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {
		super.visitWidgets(widgetVisitor);
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
	}

	@Override
	public Optional<GuiEventListener> getChildAt(double x, double y) {
		return WLayout.super.getChildAt(x, y);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		return WLayout.super.mouseClicked(event, doubleClick);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		return WLayout.super.mouseReleased(event);
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
		return WLayout.super.mouseDragged(event, dx, dy);
	}

	@Override
	public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
		return WLayout.super.mouseScrolled(x, y, scrollX, scrollY);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		return WLayout.super.keyPressed(event);
	}

	@Override
	public boolean keyReleased(KeyEvent event) {
		return WLayout.super.keyReleased(event);
	}

	@Override
	public boolean charTyped(CharacterEvent event) {
		return WLayout.super.charTyped(event);
	}

	@Override
	public boolean preeditUpdated(@Nullable PreeditEvent event) {
		return WLayout.super.preeditUpdated(event);
	}

	@Override
	public ScreenRectangle getBorderForArrowNavigation(ScreenDirection opposite) {
		return WLayout.super.getBorderForArrowNavigation(opposite);
	}

	@Override
	public @Nullable ComponentPath getCurrentFocusPath() {
		return WLayout.super.getCurrentFocusPath();
	}

	@Override
	public @Nullable ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
		return WLayout.super.nextFocusPath(navigationEvent);
	}

	@Override
	public void mouseMoved(double x, double y) {
		WLayout.super.mouseMoved(x, y);
	}

	@Override
	public boolean shouldTakeFocusAfterInteraction() {
		return WLayout.super.shouldTakeFocusAfterInteraction();
	}

	@Override
	public ScreenRectangle getRectangle() {
		return super.getRectangle();
	}

	@Override
	protected final void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		var scissor = this.scissor;
		if (scissor) {
			int scissorX = this.getX();
			int scissorY = this.getY();
			int scissorW = this.getWidth();
			int scissorH = this.getHeight();
			graphics.enableScissor(scissorX, scissorY, scissorX + scissorW, scissorY + scissorH);
		}
		extractWidgetRenderStateWithScissor(graphics, mouseX, mouseY, a);
		if (scissor) {
			graphics.disableScissor();
		}
	}

	protected abstract void extractWidgetRenderStateWithScissor(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a);

}
