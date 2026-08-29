package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.dialog.input.NumberRangeInput;
import net.w3e.util.client.gui.element.WSlider;
import net.w3e.util.client.gui.option.OptionProviderContainerImpl;
import net.w3e.util.common.gui.option.OptionProvider;
import net.w3e.util.common.gui.option.RangeOption;
import net.w3e.util.common.gui.option.container.ContainerPair;
import net.w3e.util.common.gui.option.container.FloatRangeOption;

import java.util.Optional;

public class FloatRangeOptionProviderContainer<OBJECT> extends OptionProviderContainerImpl<RangeOption<Float, FloatRangeOption>, OBJECT, Float> {

	public FloatRangeOptionProviderContainer(OptionProvider<?, OBJECT, ?, Float> provider, ContainerPair<OBJECT> container, @SuppressWarnings("unused") Screen screen) {
		super(provider, container);

		var args = this.provider.getArgs();
		var range = args.value();
		NumberRangeInput.RangeInfo rangeInfo = new NumberRangeInput.RangeInfo(range.min(), range.max(), Optional.of(this.value), Optional.of(range.step()));
		var slider = new WSlider(0, 0, 120, 20,
				e -> args.textGetter().apply(this.provider.getTitle().copy(), round(e)), rangeInfo, e -> this.value = round(e)
		);
		this.addChild(slider);
	}

}
