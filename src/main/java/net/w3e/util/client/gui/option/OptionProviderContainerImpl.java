package net.w3e.util.client.gui.option;

import lombok.Getter;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.skds.lib2.mat.FastMath;
import net.w3e.util.client.gui.container.WLayoutProviderContainer;
import net.w3e.util.common.gui.option.OptionProvider;
import net.w3e.util.common.gui.option.OptionProviderBuilder;
import net.w3e.util.common.gui.option.OptionProviderUpdateData;
import net.w3e.util.common.gui.option.container.ContainerPair;
import net.w3e.util.common.gui.option.container.OptionProviderContainer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OptionProviderContainerImpl<ARGS, OBJECT, VALUE> extends WLayoutProviderContainer implements OptionProviderContainer<OBJECT, VALUE> {

	public static <OBJECT> ContainerPair<OBJECT> create(Component title, Screen screen, OBJECT object, OptionProviderBuilder<OBJECT, ?, ?> builder) {
		return create(title, screen, object, builder.build());
	}

	@SuppressWarnings("unchecked")
	public static <OBJECT> @NotNull ContainerPair<OBJECT> create(Component title, Screen screen, OBJECT object, List<OptionProvider<?, OBJECT, ?, ?>> builder) {
		LinearLayout contents = LinearLayout.vertical().spacing(5);
		contents.addChild(new StringWidget(title, screen.getFont()), LayoutSettings::alignHorizontallyCenter);

		ContainerPair<OBJECT> container = new ContainerPair<>(new ArrayList<>(), contents, object);

		for (var optionProvider : builder) {
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

	protected final ContainerPair<OBJECT> container;
	protected final OptionProvider<ARGS, OBJECT, ?, VALUE> provider;
	protected final VALUE valueCache;
	@Getter
	protected VALUE value;

	@SuppressWarnings("unchecked")
	public OptionProviderContainerImpl(OptionProvider<?, OBJECT, ?, VALUE> provider, ContainerPair<OBJECT> container) {
		super(LinearLayout.vertical().spacing(5));
		this.container = container;
		this.provider = (OptionProvider<ARGS, OBJECT, ?, VALUE>) provider;
		this.valueCache = provider.getValue(container.object());
		this.value = this.valueCache;
	}

	protected final float round(float value) {
		float scale = 1000f;
		return FastMath.round(value * scale) / scale;
	}

	protected final double round(double value) {
		double scale = 1000f;
		return FastMath.round(value * scale) / scale;
	}

	@Override
	public OptionProviderUpdateData<OBJECT, VALUE> createUpdateData() {
		if (!this.provider.equals(this.valueCache, this.value)) {
			return new OptionProviderUpdateData<>(this.provider, this.value);
		}
		return null;
	}

}
