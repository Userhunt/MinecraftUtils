package net.w3e.util.client.gui.screen.scrollable;

import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.w3e.util.client.gui.container.WScrollableLayoutContainer;
import net.w3e.util.client.gui.layout.WLayoutUtils;

public abstract class WAbstractScrollableScreen<E extends LayoutElement> extends Screen {

	@Getter(AccessLevel.PROTECTED)
	private FrameLayout layout;
	private LinearLayout body;
	private WScrollableLayoutContainer bodyScroll;

	protected WAbstractScrollableScreen(Component title) {
		super(title);
	}

	@Override
	protected void init() {
		this.layout = new FrameLayout();
		this.layout.defaultChildLayoutSetting().alignHorizontallyCenter().alignVerticallyTop().padding(10);

		this.layout.addChild(this.createHeader());
		this.layout.visitWidgets(this::addRenderableWidget);

		this.body = LinearLayout.vertical().spacing(10);
		this.body.defaultCellSetting().alignHorizontallyCenter();

		this.bodyScroll = new WScrollableLayoutContainer(this.body, this.height - 40);
		this.layout.addChild(this.bodyScroll, setting -> setting.alignHorizontallyCenter().alignVerticallyTop().paddingTop(35));

		this.bodyScroll.visitWidgets(this::addRenderableWidget);

		this.updateBody(true);
	}

	protected LayoutElement createHeader() {
		return SpacerElement.height(20);
	}

	protected final void updateBody() {
		updateBody(false);
	}

	private void updateBody(boolean init) {
		WLayoutUtils.clear(this.body);

		onUpdateBody(init);

		this.repositionBodyElements();
	}

	protected abstract void onUpdateBody(boolean init);

	protected void addElement(E element) {
		this.body.addChild(element);
	}

	private void repositionBodyElements() {
		this.bodyScroll.setMaxHeight(this.height - 40);
		this.layout.arrangeElements();
		this.layout.setX(this.width / 2 - this.layout.getWidth() / 2);
	}

	@Override
	protected void repositionElements() {
		repositionBodyElements();
	}
}
