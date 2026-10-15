package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.w3e.util.client.gui.option.OptionProviderContainerImpl;
import net.w3e.util.common.gui.option.OptionProvider;
import net.w3e.util.common.gui.option.container.ContainerPair;

public class BooleanOptionProviderContainer<OBJECT> extends OptionProviderContainerImpl<Object, OBJECT, Boolean> {

	public BooleanOptionProviderContainer(OptionProvider<?, OBJECT, ?, Boolean> provider, ContainerPair<OBJECT> containerPair, Screen screen) {
		super(provider, containerPair);

		this.addChild(Checkbox.builder(provider.getTitle(), screen.getFont())
				.selected(this.value)
				.onValueChange((_, value) -> this.value = value)
				.maxWidth(getTabbedWidth())
				.build()
		);
	}

}
