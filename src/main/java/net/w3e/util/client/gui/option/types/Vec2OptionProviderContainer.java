package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.skds.lib2.mat.vec2.Vec2;
import net.w3e.util.client.gui.container.WLayoutProviderContainer;
import net.w3e.util.client.gui.option.OptionProviderContainer;
import net.w3e.util.common.gui.option.OptionProvider;

public class Vec2OptionProviderContainer<OBJECT> extends OptionProviderContainer<Object, OBJECT, Vec2> {

	public Vec2OptionProviderContainer(OptionProvider<?, OBJECT, ?, Vec2> provider, ContainerPair<OBJECT> container, Screen screen) {
		super(provider, container);

		var font = screen.getFont();

		this.addChild(new StringWidget(provider.getTitle(), font));

		LinearLayout valueLayout = LinearLayout.horizontal().spacing(5);
		EditBox editBox;

		editBox = new EditBox(font, 0, 0, 60, 20, Component.empty());
		editBox.setValue(String.valueOf(this.value.x()));
		editBox.setResponder(v -> {
			try {
				float value = Float.parseFloat(v);
				this.value = Vec2.of(value, this.value.y());
			} catch (Exception _) {
			}
		});
		valueLayout.addChild(editBox);

		editBox = new EditBox(font, 0, 0, 60, 20, Component.empty());
		editBox.setValue(String.valueOf(this.value.y()));
		editBox.setResponder(v -> {
			try {
				float value = Float.parseFloat(v);
				this.value = Vec2.of(this.value.x(), value);
			} catch (Exception _) {
			}
		});
		valueLayout.addChild(editBox);

		this.addChild(new WLayoutProviderContainer(valueLayout));
	}

}
