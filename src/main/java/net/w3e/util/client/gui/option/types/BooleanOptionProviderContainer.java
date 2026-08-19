package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.w3e.util.client.gui.option.OptionProviderContainer;
import net.w3e.util.common.gui.option.OptionProvider;

public class BooleanOptionProviderContainer<OBJECT> extends OptionProviderContainer<Object, OBJECT, Boolean> {

	public BooleanOptionProviderContainer(OptionProvider<?, OBJECT, ?, Boolean> provider, ContainerPair<OBJECT> container, Screen screen) {
		super(provider, container);

		this.addChild(Checkbox.builder(provider.getTitle(), screen.getFont())
				.selected(this.value)
				.onValueChange((_, value) -> this.value = value)
				.build()
		);
	}

}
