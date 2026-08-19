package net.w3e.util.client.gui.option.types;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.w3e.util.client.MouseHelper;
import net.w3e.util.client.gui.container.WLayoutProviderContainer;
import net.w3e.util.client.gui.option.OptionProviderContainer;
import net.w3e.util.client.gui.option.OptionProviderScreen;
import net.w3e.util.common.gui.option.OptionProvider;
import org.jspecify.annotations.Nullable;

public class RotationOptionProviderContainer<OBJECT> extends OptionProviderContainer<Object, OBJECT, RotationOptionProviderContainer.RotationData> {

	@SuppressWarnings("unchecked")
	public RotationOptionProviderContainer(OptionProvider<?, OBJECT, ?, RotationData> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Screen screen) {
		super(provider, container);

		var font = screen.getFont();

		this.addChild(new StringWidget(provider.getTitle(), font));

		LinearLayout valueLayout = LinearLayout.horizontal().spacing(5);
		EditBox editBox;

		editBox = new EditBox(font, 0, 0, 60, 20, Component.empty());
		editBox.setValue(String.valueOf(this.value.yaw()));
		editBox.setResponder(v -> {
			try {
				float value = Float.parseFloat(v);
				this.value = this.value.withYaw(value);
			} catch (Exception _) {
			}
		});
		valueLayout.addChild(editBox);

		if (this.value.hasPitch()) {
			editBox = new EditBox(font, 0, 0, 60, 20, Component.empty());
			editBox.setValue(String.valueOf(this.value.pitch()));
			editBox.setResponder(v -> {
				try {
					float value = Float.parseFloat(v);
					this.value = this.value.withPitch(value);
				} catch (Exception _) {
				}
			});
			valueLayout.addChild(editBox);
		}

		valueLayout.addChild(Button.builder(Component.literal("Настроить"), _ -> {
			Vec3OptionProviderContainer<OBJECT> posContainer = (Vec3OptionProviderContainer<OBJECT>) this.container.options().getFirst();
			var posOption = posContainer.getValue();
			Minecraft minecraft = Minecraft.getInstance();
			assert minecraft.player != null;
			minecraft.player.connection.sendCommand("tp @s " + posOption.x() + " " + posOption.y() + " " + posOption.z() + " " + this.value.yaw() + " " + this.value.pitch());
			minecraft.setScreen(new EditRotationScreen());
		}).width(100).build());

		this.addChild(new WLayoutProviderContainer(valueLayout));
	}

	public class EditRotationScreen extends Screen implements OptionProviderScreen<OBJECT> {

		private final @Nullable Screen screen;

		protected EditRotationScreen() {
			super(Component.empty());
			this.screen = Minecraft.getInstance().screen;
		}

		@Override
		protected void init() {
			MouseHelper.moveToCenter();
		}

		@Override
		public void mouseMoved(double x, double y) {
			x -= this.width / 2f;
			y -= this.height / 2f;
			MouseHelper.turnPlayer(x, y);
			MouseHelper.moveToCenter();
		}

		@Override
		public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		}

		@Override
		public void onClose() {
			var player = this.minecraft.player;
			assert player != null;
			RotationOptionProviderContainer.this.value = new RotationData(player.getYRot(), player.getXRot(), RotationOptionProviderContainer.this.value.hasPitch());
			this.minecraft.setScreen(this.screen);
		}

		@Override
		public OBJECT getObject() {
			return RotationOptionProviderContainer.this.container.object();
		}

	}

	public record RotationData(float yaw, float pitch, boolean hasPitch) {
		public RotationData withYaw(float yaw) {
			return new RotationData(yaw, this.pitch, this.hasPitch);
		}

		public RotationData withPitch(float pitch) {
			return new RotationData(this.yaw, pitch, this.hasPitch);
		}
	}

}
