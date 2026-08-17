package net.w3e.util.client.gui.container;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ScrollableLayout;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.w3e.util.client.gui.layout.WLayout;
import org.jspecify.annotations.Nullable;

public class WScrollableLayoutContainer extends WLayoutProviderContainer implements WLayout {

	private static final int DEFAULT_SCROLLBAR_SPACING = 4;

	private final ScrollableLayout.ReserveStrategy reserveStrategy = ScrollableLayout.ReserveStrategy.BOTH;
	private final Minecraft minecraft;

	public WScrollableLayoutContainer(final Layout content, final int maxHeight) {
		super(content, maxHeight);
		this.minecraft = Minecraft.getInstance();
	}

	@Override
	public void setContentX(final int x) {
		this.contents.setX(x + (this.reserveStrategy == ScrollableLayout.ReserveStrategy.BOTH ? this.scrollbarReserve() : 0));
	}

	@Override
	public void setContentY(final int y) {
		this.contents.setY(y - (int) this.scrollAmount());
	}

	@Override
	public void arrangeElements() {
		super.arrangeElements();
		int contentWidth = this.contents.getWidth();

		int scrollbarReserve = switch (this.reserveStrategy) {
			case RIGHT -> this.scrollbarReserve();
			case BOTH -> 2 * this.scrollbarReserve();
		};

		this.setWidth(Math.max(contentWidth, this.getMinWidth()) + scrollbarReserve);
		this.refreshScrollAmount();
	}

	@Override
	protected void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
		super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
		this.extractScrollbar(graphics, mouseX, mouseY);
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

	private int scrollbarReserve() {
		return DEFAULT_SCROLLBAR_SPACING + this.scrollbarWidth();
	}

	@Override
	public void setScrollAmount(final double scrollAmount) {
		super.setScrollAmount(scrollAmount);
		this.contents.setY(this.getRectangle().top() - (int) this.scrollAmount());
	}

}

