package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.dialog.input.NumberRangeInput;
import net.skds.lib2.mat.FastMath;
import net.w3e.util.client.gui.element.WSlider;
import net.w3e.util.client.gui.option.OptionProviderContainerImpl;
import net.w3e.util.common.gui.option.OptionProvider;
import net.w3e.util.common.gui.option.RangeOption;
import net.w3e.util.common.gui.option.container.ContainerPair;

import java.util.List;
import java.util.Optional;

public class ValueRangeOptionProviderContainer<E, OBJECT> extends OptionProviderContainerImpl<RangeOption<E, List<E>>, OBJECT, E> {

	public ValueRangeOptionProviderContainer(OptionProvider<?, OBJECT, ?, E> provider, ContainerPair<OBJECT> container, @SuppressWarnings("unused") Screen screen) {
		super(provider, container);

		var args = this.provider.getArgs();
		List<E> values = args.value();
		NumberRangeInput.RangeInfo rangeInfo = new NumberRangeInput.RangeInfo(0, values.size() - 1, Optional.of((float) values.indexOf(this.value)), Optional.of(1f));
		var slider = new WSlider(0, 0, 120, 20,
				e -> args.textGetter().apply(this.provider.getTitle().copy(), values.get(FastMath.round(e))), rangeInfo, e -> this.value = values.get(FastMath.round(e))
		);
		this.addChild(slider);
	}

}
