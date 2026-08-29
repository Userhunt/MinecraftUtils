package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.skds.lib2.shapes.AABB;
import net.w3e.util.client.gui.option.OptionProviderContainerImpl;
import net.w3e.util.common.gui.option.OptionProvider;
import net.w3e.util.common.gui.option.container.ContainerPair;

public class AABBOptionProviderContainer<OBJECT> extends OptionProviderContainerImpl<Object, OBJECT, AABB> {

	public AABBOptionProviderContainer(OptionProvider<?, OBJECT, ?, AABB> provider, ContainerPair<OBJECT> container, Screen screen) {
		super(provider, container);

		var font = screen.getFont();

		this.addChild(new StringWidget(provider.getTitle(), font));

		GridLayout valueLayout = new GridLayout().spacing(10);
		EditBox editBox;

		valueLayout.addChild(new StringWidget(Component.literal("Min"), font), 0, 0);

		editBox = new EditBox(font, 0, 0, 60, 20, Component.empty());
		editBox.setValue(String.valueOf(round(this.value.minX)));
		editBox.setResponder(v -> {
			try {
				float value = round(Float.parseFloat(v));
				this.value = AABB.fromToNormalized(value, this.value.minY, this.value.minZ, this.value.maxX, this.value.maxY, this.value.maxZ);
			} catch (Exception _) {
			}
		});
		valueLayout.addChild(editBox, 0, 1);

		editBox = new EditBox(font, 0, 0, 60, 20, Component.empty());
		editBox.setValue(String.valueOf(round(this.value.minY)));
		editBox.setResponder(v -> {
			try {
				float value = round(Float.parseFloat(v));
				this.value = AABB.fromToNormalized(this.value.minX, value, this.value.minZ, this.value.maxX, this.value.maxY, this.value.maxZ);
			} catch (Exception _) {
			}
		});
		valueLayout.addChild(editBox, 0, 2);

		editBox = new EditBox(font, 0, 0, 60, 20, Component.empty());
		editBox.setValue(String.valueOf(round(this.value.minZ)));
		editBox.setResponder(v -> {
			try {
				float value = round(Float.parseFloat(v));
				this.value = AABB.fromToNormalized(this.value.minX, this.value.minY, value, this.value.maxX, this.value.maxY, this.value.maxZ);
			} catch (Exception _) {
			}
		});
		valueLayout.addChild(editBox, 0, 3);

		valueLayout.addChild(new StringWidget(Component.literal("Max"), font), 1, 0);

		editBox = new EditBox(font, 0, 0, 60, 20, Component.empty());
		editBox.setValue(String.valueOf(round(this.value.maxX)));
		editBox.setResponder(v -> {
			try {
				float value = round(Float.parseFloat(v));
				this.value = AABB.fromToNormalized(this.value.minX, this.value.minY, this.value.minZ, value, this.value.maxY, this.value.maxZ);
			} catch (Exception _) {
			}
		});
		valueLayout.addChild(editBox, 1, 1);

		editBox = new EditBox(font, 0, 0, 60, 20, Component.empty());
		editBox.setValue(String.valueOf(round(this.value.maxY)));
		editBox.setResponder(v -> {
			try {
				float value = round(Float.parseFloat(v));
				this.value = AABB.fromToNormalized(this.value.minX, this.value.minY, this.value.minZ, this.value.maxX, value, this.value.maxZ);
			} catch (Exception _) {
			}
		});
		valueLayout.addChild(editBox, 1, 2);

		editBox = new EditBox(font, 0, 0, 60, 20, Component.empty());
		editBox.setValue(String.valueOf(round(this.value.maxZ)));
		editBox.setResponder(v -> {
			try {
				float value = round(Float.parseFloat(v));
				this.value = AABB.fromToNormalized(this.value.minX, this.value.minY, this.value.minZ, this.value.maxX, this.value.maxY, value);
			} catch (Exception _) {
			}
		});
		valueLayout.addChild(editBox, 1, 3);

		this.addChild(valueLayout);
	}

}
