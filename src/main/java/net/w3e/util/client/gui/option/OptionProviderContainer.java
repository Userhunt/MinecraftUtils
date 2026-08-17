package net.w3e.util.client.gui.option;

import lombok.Getter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.network.chat.Component;
import net.w3e.util.client.gui.container.WLayoutProviderContainer;
import net.w3e.util.common.gui.option.OptionProvider;
import net.w3e.util.common.gui.option.OptionProviderBuilder;
import net.w3e.util.common.gui.option.OptionProviderUpdateData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OptionProviderContainer<ARGS, OBJECT, VALUE> extends WLayoutProviderContainer {

	public static <OBJECT> ContainerPair<OBJECT> create(Component title, Font font, OBJECT object, OptionProviderBuilder<OBJECT, ?, ?> builder) {
		return create(title, font, object, builder.build());
	}

	@SuppressWarnings("unchecked")
	public static <OBJECT> @NotNull ContainerPair<OBJECT> create(Component title, Font font, OBJECT object, List<OptionProvider<?, OBJECT, ?, ?>> builder) {
		List<OptionProviderContainer<?, OBJECT, ?>> options = new ArrayList<>();

		LinearLayout contents = LinearLayout.vertical().spacing(5);
		contents.addChild(new StringWidget(title, font), LayoutSettings::alignHorizontallyCenter);

		ContainerPair<OBJECT> container = new ContainerPair<>(new ArrayList<>(), contents, object);

		for (var optionProvider : builder) {
			LayoutElement option = optionProvider.createOption(container, font);
			if (option == null) {
				option = new StringWidget(optionProvider.getTitle().copy().append("(" + optionProvider.getType() + ")"), font);
			} else {
				options.add((OptionProviderContainer<?, OBJECT, ?>) option);
			}
			contents.addChild(option);
		}

		return new ContainerPair<>(options, contents, object);
	}

	public record ContainerPair<OBJECT>(List<OptionProviderContainer<?, OBJECT, ?>> options, LinearLayout contents,
										OBJECT object) {
	}

	protected final OptionProviderContainer.ContainerPair<OBJECT> container;
	protected final OptionProvider<ARGS, OBJECT, ?, VALUE> provider;
	protected final VALUE valueCache;
	@Getter
	protected VALUE value;

	public OptionProviderContainer(OptionProvider<?, OBJECT, ?, VALUE> provider, OptionProviderContainer.ContainerPair<OBJECT> container) {
		this(provider, container, 100);
	}

	@SuppressWarnings("unchecked")
	public OptionProviderContainer(OptionProvider<?, OBJECT, ?, VALUE> provider, OptionProviderContainer.ContainerPair<OBJECT> container, int maxHeight) {
		super(LinearLayout.vertical().spacing(5), maxHeight);
		this.container = container;
		this.provider = (OptionProvider<ARGS, OBJECT, ?, VALUE>) provider;
		this.valueCache = provider.getValue(container.object);
		this.value = this.valueCache;
	}

	protected void addChild(LayoutElement widget) {
		((LinearLayout) this.contents).addChild(widget);
	}

	public boolean equals() {
		return this.provider.equals(this.valueCache, this.value);
	}

	public OptionProviderUpdateData<OBJECT, VALUE> createUpdateData() {
		return new OptionProviderUpdateData<>(this.provider, this.value);
	}

}
