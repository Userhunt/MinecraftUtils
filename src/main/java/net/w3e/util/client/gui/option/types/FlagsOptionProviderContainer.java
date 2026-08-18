package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.StringWidget;
import net.w3e.util.client.gui.layout.WFlowLayout;
import net.w3e.util.client.gui.option.OptionProviderContainer;
import net.w3e.util.client.gui.option.RangeOption;
import net.w3e.util.common.gui.option.OptionProvider;

import java.util.List;

public class FlagsOptionProviderContainer<E, OBJECT> extends OptionProviderContainer<RangeOption<E, List<E>>, OBJECT, List<? extends E>> {

	@SuppressWarnings("unchecked")
	public FlagsOptionProviderContainer(OptionProvider<?, OBJECT, ?, List<? extends E>> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Font font) {
		super(provider, container);

		this.addChild(new StringWidget(provider.getTitle(), font));

		var args = this.provider.getArgs();

		WFlowLayout layout = new WFlowLayout();
		//LinearLayout valueLayout = LinearLayout.horizontal().spacing(5);
		for (E arg : args.value()) {
			layout.add(Checkbox.builder(args.textGetter().apply(provider.getTitle(), arg), font)
					.selected(this.value.contains(arg))
					.onValueChange((_, value) -> {
						if (value) {
							((List<E>) this.value).add(arg);
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
