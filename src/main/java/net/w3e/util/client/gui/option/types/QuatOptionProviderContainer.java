package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.network.chat.Component;
import net.skds.lib2.mat.quat.Quat;
import net.w3e.util.client.gui.option.OptionProviderContainer;
import net.w3e.util.common.MatrixUtil;
import net.w3e.util.common.gui.option.OptionProvider;

public class QuatOptionProviderContainer<OBJECT> extends OptionProviderContainer<Object, OBJECT, Quat> {

	private float yaw;
	private float pitch;
	private float roll;

	public QuatOptionProviderContainer(OptionProvider<?, OBJECT, ?, Quat> provider, ContainerPair<OBJECT> container, Font font) {
		super(provider, container);

		this.addChild(new StringWidget(provider.getTitle(), font));

		LinearLayout valueLayout = LinearLayout.horizontal().spacing(5);
		EditBox editBox;
		var angles = MatrixUtil.getYPR(this.value).scale(100);

		this.yaw = angles.roundX() / 100f;
		this.pitch = angles.roundY() / 100f;
		this.roll = angles.roundZ() / 100f;

		editBox = new EditBox(font, 0, 0, 60, 20, Component.empty());
		editBox.setValue(String.valueOf(this.yaw));
		editBox.setResponder(v -> {
			try {
				float value = Float.parseFloat(v);
				if (this.yaw != value) {
					this.yaw = value;
					this.value = MatrixUtil.getQuat(this.yaw, this.pitch, this.roll);
				}
			} catch (Exception _) {
			}
		});
		valueLayout.addChild(editBox);

		editBox = new EditBox(font, 0, 0, 60, 20, Component.empty());
		editBox.setValue(String.valueOf(this.pitch));
		editBox.setResponder(v -> {
			try {
				float value = Float.parseFloat(v);
				if (this.pitch != value) {
					this.pitch = value;
					this.value = MatrixUtil.getQuat(this.yaw, this.pitch, this.roll);
				}
			} catch (Exception _) {
			}
		});
		valueLayout.addChild(editBox);

		editBox = new EditBox(font, 0, 0, 60, 20, Component.empty());
		editBox.setValue(String.valueOf(this.roll));
		editBox.setResponder(v -> {
			try {
				float value = Float.parseFloat(v);
				if (this.roll != value) {
					this.roll = value;
					this.value = MatrixUtil.getQuat(this.yaw, this.pitch, this.roll);
				}
			} catch (Exception _) {
			}
		});
		valueLayout.addChild(editBox);

		this.addChild(valueLayout);
	}

}
