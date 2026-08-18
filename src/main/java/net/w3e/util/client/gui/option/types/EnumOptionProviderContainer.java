package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.CycleButton;
import net.w3e.util.client.gui.option.OptionProviderContainer;
import net.w3e.util.client.gui.option.RangeOption;
import net.w3e.util.common.gui.option.OptionProvider;

import java.util.List;

public class EnumOptionProviderContainer<E, OBJECT> extends OptionProviderContainer<RangeOption<E, List<E>>, OBJECT, Object> {

	@SuppressWarnings("unchecked")
	public EnumOptionProviderContainer(OptionProvider<?, OBJECT, ?, Object> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Font font) {
		super(provider, container);

		var args = this.provider.getArgs();
		this.addChild(CycleButton.builder(e -> args.textGetter().apply(null, (E) e), this.value)
				.withValues(args.value().toArray())
				.create(provider.getTitle(), (_, v) -> this.value = v)
		);
	}

}
