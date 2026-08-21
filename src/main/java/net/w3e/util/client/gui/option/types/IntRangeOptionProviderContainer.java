package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.dialog.input.NumberRangeInput;
import net.skds.lib2.mat.FastMath;
import net.w3e.util.client.gui.element.WSlider;
import net.w3e.util.client.gui.option.OptionProviderContainerImpl;
import net.w3e.util.common.gui.option.OptionProvider;
import net.w3e.util.common.gui.option.RangeOption;
import net.w3e.util.common.gui.option.container.ContainerPair;
import net.w3e.util.common.gui.option.container.IntRangeOption;

import java.util.Optional;

public class IntRangeOptionProviderContainer<OBJECT> extends OptionProviderContainerImpl<RangeOption<Integer, IntRangeOption>, OBJECT, Integer> {

	public IntRangeOptionProviderContainer(OptionProvider<?, OBJECT, ?, Integer> provider, ContainerPair<OBJECT> container, @SuppressWarnings("unused") Screen screen) {
		super(provider, container);

		var args = this.provider.getArgs();
		var range = args.value();
		NumberRangeInput.RangeInfo rangeInfo = new NumberRangeInput.RangeInfo(range.min(), range.max(), Optional.of(this.value.floatValue()), Optional.of(range.step()));
		var slider = new WSlider(0, 0, 120, 20,
				e -> args.textGetter().apply(this.provider.getTitle().copy(), FastMath.round(e)), rangeInfo, e -> this.value = FastMath.round(e)
		);
		this.addChild(slider);
	}

}
