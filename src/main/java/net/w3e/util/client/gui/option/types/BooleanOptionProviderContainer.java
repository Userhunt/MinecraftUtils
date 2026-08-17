package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Checkbox;
import net.w3e.util.client.gui.option.OptionProviderContainer;
import net.w3e.util.common.gui.option.OptionProvider;

public class BooleanOptionProviderContainer<OBJECT> extends OptionProviderContainer<Object, OBJECT, Boolean> {

	public BooleanOptionProviderContainer(OptionProvider<?, OBJECT, ?, Boolean> provider, ContainerPair<OBJECT> container, Font font) {
		super(provider, container);

		this.addChild(Checkbox.builder(provider.getTitle(), font)
				.selected(this.value)
				.onValueChange((_, value) -> this.value = value)
				.build()
		);
	}

}
