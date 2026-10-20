package net.w3e.util.common.gui.option.container;

import net.minecraft.client.gui.layouts.LinearLayout;

import java.util.List;

public record ContainerPair<OBJECT>(List<OptionProviderContainer> options, LinearLayout contents,
									OBJECT object, int tab) {
	public ContainerPair<OBJECT> newTab() {
		return new ContainerPair<>(this.options, this.contents, this.object, this.tab + 1);
	}

	public int getTab() {
		return OptionProviderContainer.TAB * this.tab();
	}
}
