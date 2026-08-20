package net.w3e.util.client.gui.screen.scrollable;

import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.network.chat.Component;
import net.w3e.util.client.gui.container.WLayoutProviderContainer;

public class WScrollableScreen extends WAbstractScrollableScreen<LayoutElement> {

	private WLayoutProviderContainer container;

	protected WScrollableScreen(Component title) {
		super(title);
	}

	protected void setContents(Layout contents) {
		this.container = new WLayoutProviderContainer(contents);
	}

	@Override
	protected void onUpdateBody(boolean init) {
		this.addElement(this.container);
	}

}
