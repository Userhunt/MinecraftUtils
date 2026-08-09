package net.w3e.util.client.gui.container;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractScrollArea;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ScrollableLayout;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.CommonComponents;
import net.w3e.util.client.gui.layout.WLayout;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class WScrollableLayoutContainer extends AbstractContainerWidget implements WLayout {

	private static final int DEFAULT_SCROLLBAR_SPACING = 4;

	private final Layout content;
	private int minWidth;
	private int minHeight;
	private int maxHeight;

	private final ScrollableLayout.ReserveStrategy reserveStrategy = ScrollableLayout.ReserveStrategy.BOTH;
	private final Minecraft minecraft;
	private final List<AbstractWidget> children = new ArrayList<>();

	public WScrollableLayoutContainer(final Layout content, final int maxHeight) {
		super(0, 0, 0, maxHeight, CommonComponents.EMPTY, AbstractScrollArea.defaultSettings(10));
		this.content = content;
		this.maxHeight = maxHeight;
		this.minecraft = Minecraft.getInstance();
	}

	public void setMinWidth(final int minWidth) {
		this.minWidth = minWidth;
		this.setWidth(Math.max(this.content.getWidth(), minWidth));
	}

	public void setMinHeight(final int minHeight) {
		this.minHeight = minHeight;
		this.setHeight(Math.max(this.content.getHeight(), minHeight));
	}

	public void setMaxHeight(final int maxHeight) {
		this.maxHeight = maxHeight;
		this.setHeight(Math.min(this.content.getHeight(), maxHeight));
		this.refreshScrollAmount();
	}

	@Override
	public void arrangeElements() {
		this.content.arrangeElements();
		this.children.clear();
		this.content.visitWidgets(this.children::add);
		int contentWidth = this.content.getWidth();

		int scrollbarReserve = switch (this.reserveStrategy) {
			case RIGHT -> this.scrollbarReserve();
			case BOTH -> 2 * this.scrollbarReserve();
		};

		this.setWidth(Math.max(contentWidth, this.minWidth) + scrollbarReserve);
		this.setHeight(Math.clamp(this.getHeight(), this.minHeight, this.maxHeight));
		this.refreshScrollAmount();
	}

	@Override
	public void visitChildren(final Consumer<LayoutElement> layoutElementVisitor) {
		layoutElementVisitor.accept(this);
	}

	@Override
	public void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {
		super.visitWidgets(widgetVisitor);
	}

	@Override
	protected int contentHeight() {
		return this.content.getHeight();
	}

	@Override
	protected void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
		graphics.enableScissor(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height);

		for (AbstractWidget child : this.children) {
			child.extractRenderState(graphics, mouseX, mouseY, a);
		}

		graphics.disableScissor();
		this.extractScrollbar(graphics, mouseX, mouseY);
	}

	@Override
	protected void updateWidgetNarration(final NarrationElementOutput output) {
	}

	@Override
	public ScreenRectangle getBorderForArrowNavigation(final ScreenDirection opposite) {
		GuiEventListener focused = this.getFocused();
		return focused != null
				? focused.getBorderForArrowNavigation(opposite)
				: new ScreenRectangle(this.getX(), this.getY(), this.width, this.contentHeight()).getBorder(opposite);
	}

	@Override
	public void setFocused(final @Nullable GuiEventListener focused) {
		super.setFocused(focused);
		if (focused != null && this.minecraft.getLastInputType().isKeyboard()) {
			ScreenRectangle area = this.getRectangle();
			ScreenRectangle focusedRect = focused.getRectangle();
			int topDelta = focusedRect.top() - area.top();
			int bottomDelta = focusedRect.bottom() - area.bottom();
			double scrollRate = this.scrollRate();
			if (topDelta < 0) {
				this.setScrollAmount(this.scrollAmount() + topDelta - scrollRate);
			} else if (bottomDelta > 0) {
				this.setScrollAmount(this.scrollAmount() + bottomDelta + scrollRate);
			}
		}
	}

	@Override
	public void setX(final int x) {
		super.setX(x);
		this.content.setX(x + (this.reserveStrategy == ScrollableLayout.ReserveStrategy.BOTH ? this.scrollbarReserve() : 0));
	}

	@Override
	public void setY(final int y) {
		super.setY(y);
		this.content.setY(y - (int) this.scrollAmount());
	}

	private int scrollbarReserve() {
		return DEFAULT_SCROLLBAR_SPACING + this.scrollbarWidth();
	}

	@Override
	public void setScrollAmount(final double scrollAmount) {
		super.setScrollAmount(scrollAmount);
		this.content.setY(this.getRectangle().top() - (int) this.scrollAmount());
	}

	@Override
	public List<? extends GuiEventListener> children() {
		return this.children;
	}

	@Override
	public Collection<? extends NarratableEntry> getNarratables() {
		return this.children;
	}

	@Override
	public Optional<GuiEventListener> getChildAt(double x, double y) {
		return super.getChildAt(x, y);
	}
}

