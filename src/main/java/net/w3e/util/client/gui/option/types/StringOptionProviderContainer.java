package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.w3e.util.client.gui.option.OptionProviderContainer;
import net.w3e.util.common.gui.option.OptionProvider;

public class StringOptionProviderContainer<OBJECT> extends OptionProviderContainer<Object, OBJECT, String> {

	public StringOptionProviderContainer(OptionProvider<?, OBJECT, ?, String> provider, ContainerPair<OBJECT> container, Font font) {
		super(provider, container);

		this.addChild(new StringWidget(provider.getTitle(), font));

		EditBox editBox = new EditBox(font, 0, 0, 60, 20, Component.empty());
		editBox.setValue(this.value);
		editBox.setResponder(v -> this.value = v);
		this.addChild(editBox);
	}

}
