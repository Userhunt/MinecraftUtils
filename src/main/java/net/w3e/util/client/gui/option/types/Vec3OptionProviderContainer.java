package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.network.chat.Component;
import net.skds.lib2.mat.vec3.Vec3;
import net.w3e.util.client.gui.container.WLayoutProviderContainer;
import net.w3e.util.client.gui.option.OptionProviderContainer;
import net.w3e.util.common.gui.option.OptionProvider;

public class Vec3OptionProviderContainer<OBJECT> extends OptionProviderContainer<Object, OBJECT, Vec3> {

	public Vec3OptionProviderContainer(OptionProvider<?, OBJECT, ?, Vec3> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Font font) {
		super(provider, container);

		this.addChild(new StringWidget(provider.getTitle(), font));

		LinearLayout valueLayout = LinearLayout.horizontal().spacing(5);
		EditBox editBox;

		editBox = new EditBox(font, 0, 0, 60, 20, Component.empty());
		editBox.setValue(String.valueOf(this.value.x()));
		editBox.setResponder(v -> {
			try {
				float value = Float.parseFloat(v);
				this.value = Vec3.of(value, this.value.y(), this.value.z());
			} catch (Exception _) {
			}
		});
		valueLayout.addChild(editBox);

		editBox = new EditBox(font, 0, 0, 60, 20, Component.empty());
		editBox.setValue(String.valueOf(this.value.y()));
		editBox.setResponder(v -> {
			try {
				float value = Float.parseFloat(v);
				this.value = Vec3.of(this.value.x(), value, this.value.z());
			} catch (Exception _) {
			}
		});
		valueLayout.addChild(editBox);

		editBox = new EditBox(font, 0, 0, 60, 20, Component.empty());
		editBox.setValue(String.valueOf(this.value.z()));
		editBox.setResponder(v -> {
			try {
				float value = Float.parseFloat(v);
				this.value = Vec3.of(this.value.x(), this.value.y(), value);
			} catch (Exception _) {
			}
		});
		valueLayout.addChild(editBox);

		this.addChild(new WLayoutProviderContainer(valueLayout, 20));
	}

}
