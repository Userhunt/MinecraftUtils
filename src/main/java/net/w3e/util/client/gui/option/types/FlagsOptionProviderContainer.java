package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.w3e.util.client.gui.layout.WFlowLayout;
import net.w3e.util.client.gui.option.OptionProviderContainer;
import net.w3e.util.client.gui.option.RangeOption;
import net.w3e.util.common.gui.option.OptionProvider;

import java.util.ArrayList;
import java.util.List;

public class FlagsOptionProviderContainer<E, OBJECT> extends OptionProviderContainer<RangeOption<E, List<E>>, OBJECT, List<E>> {

	@SuppressWarnings("unchecked")
	public FlagsOptionProviderContainer(OptionProvider<?, OBJECT, ?, ?> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Screen screen) {
		super((OptionProvider<?, OBJECT, ?, List<E>>) provider, container);
		this.value = new ArrayList<>(this.value);

		var font = screen.getFont();

		this.addChild(new StringWidget(provider.getTitle(), font));

		var args = this.provider.getArgs();

		WFlowLayout layout = new WFlowLayout();
		for (E arg : args.value()) {
			layout.add(Checkbox.builder(args.textGetter().apply(provider.getTitle(), arg), font)
					.selected(this.value.contains(arg))
					.onValueChange((_, value) -> {
						if (value) {
							this.value.add(arg);
						} else {
							this.value.remove(arg);
						}
					})
					.build()
			);
		}
		this.addChild(layout);
	}

}
