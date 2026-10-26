package net.w3e.util.client.gui.option;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.w3e.util.client.gui.container.WLayoutProviderContainer;
import net.w3e.util.common.gui.option.OptionProvider;
import net.w3e.util.common.gui.option.OptionProviderBuilder;
import net.w3e.util.common.gui.option.container.ContainerPair;
import net.w3e.util.common.gui.option.container.OptionProviderContainer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OptionProviderContainerImpl<ARGS, OBJECT, VALUE> extends WLayoutProviderContainer implements OptionProviderContainer {

	public static <OBJECT> ContainerPair<OBJECT> create(Component title, Screen screen, OBJECT object, OptionProviderBuilder<OBJECT, ?, ?> builder) {
		return create(title, screen, object, builder.build());
	}

	@SuppressWarnings("unchecked")
	public static <OBJECT> @NotNull ContainerPair<OBJECT> create(Component title, Screen screen, OBJECT object, List<OptionProvider<?, OBJECT, ?, ?>> builder) {
		LinearLayout contents = LinearLayout.vertical().spacing(5);
		contents.addChild(new StringWidget(title, screen.getFont()), LayoutSettings::alignHorizontallyCenter);

		ContainerPair<OBJECT> container = new ContainerPair<>(new ArrayList<>(), contents, object, 0);

		for (OptionProvider<?, OBJECT, ?, ?> optionProvider : builder) {
			LayoutElement option = null;
			try {
				option = optionProvider.createOption(container, screen);
			} catch (Exception e) {
				System.err.println(object + " " + optionProvider);
				e.printStackTrace(System.err);
			}
			if (option == null) {
				option = new StringWidget(optionProvider.getTitle().copy().append("(" + optionProvider.getType().getKey() + ")"), screen.getFont());
			} else {
				container.options().add((OptionProviderContainerImpl<?, OBJECT, ?>) option);
			}
			contents.addChild(option);
		}

		return container;
	}

	protected final ContainerPair<OBJECT> containerPair;
	protected final OptionProvider<ARGS, OBJECT, ?, VALUE> provider;
	@Getter
	@Setter
	protected VALUE value;

	@SuppressWarnings("unchecked")
	public OptionProviderContainerImpl(OptionProvider<?, OBJECT, ?, VALUE> provider, ContainerPair<OBJECT> containerPair) {
		super(LinearLayout.vertical().spacing(5));
		this.containerPair = containerPair;
		this.provider = (OptionProvider<ARGS, OBJECT, ?, VALUE>) provider;
		this.value = this.provider.getValue(containerPair.object());
		this.setLeftPadding(this.containerPair.getTab());
	}

	protected final int getTabbedWidth() {
		return getTabbedWidth(BUTTON_WIDTH);
	}

	protected final int getTabbedWidth(int width) {
		return getTabbedWidth(1, width);
	}

	protected final int getTabbedWidth(int count, int width) {
		return getTabbedWidth(0, count, width);
	}

	protected final int getTabbedWidth(int offset, int count, int width) {
		int area = WIDTH - this.getLeftPadding() - offset - 10 * (count - 1);
		return Math.clamp(area / count, width / 2, width);
	}

	protected final float round(float value) {
		float scale = 1000f;
		return Math.round(value * scale) / scale;
	}

	protected final double round(double value) {
		double scale = 1000f;
		return Math.round(value * scale) / scale;
	}

	public void createUpdateData() {
		this.provider.setValue(this.containerPair.object(), this.value);
	}
}
