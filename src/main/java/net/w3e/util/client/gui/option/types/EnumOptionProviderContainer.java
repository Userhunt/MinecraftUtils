package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.w3e.util.client.gui.option.OptionProviderContainerImpl;
import net.w3e.util.common.gui.option.OptionProvider;
import net.w3e.util.common.gui.option.RangeOption;
import net.w3e.util.common.gui.option.container.ContainerPair;

import java.util.List;

public class EnumOptionProviderContainer<E, OBJECT> extends OptionProviderContainerImpl<RangeOption<E, List<E>>, OBJECT, E> {

	@SuppressWarnings("unchecked")
	public EnumOptionProviderContainer(OptionProvider<?, OBJECT, ?, Object> provider, ContainerPair<OBJECT> containerPair, @SuppressWarnings("unused") Screen screen) {
		super((OptionProvider<?, OBJECT, ?, E>) provider, containerPair);

		RangeOption<E, List<E>> args = this.provider.getArgs();

		this.addChild(CycleButton.builder(e -> args.textGetter().apply(null, e), this.value)
				.withValues(args.range())
				.create(0, 0, getTabbedWidth(), 20, provider.getTitle(), (_, v) -> this.value = v)
		);
	}

}
