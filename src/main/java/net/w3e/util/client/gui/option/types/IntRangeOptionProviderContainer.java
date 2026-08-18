package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.Font;
import net.minecraft.server.dialog.input.NumberRangeInput;
import net.skds.lib2.mat.FastMath;
import net.w3e.util.client.gui.element.WSlider;
import net.w3e.util.client.gui.option.OptionProviderContainer;
import net.w3e.util.client.gui.option.RangeOption;
import net.w3e.util.common.gui.option.OptionProvider;

import java.util.Optional;

public class IntRangeOptionProviderContainer<OBJECT> extends OptionProviderContainer<RangeOption<Integer, IntRangeOptionProviderContainer.IntRangeOption>, OBJECT, Integer> {

	public IntRangeOptionProviderContainer(OptionProvider<?, OBJECT, ?, Integer> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Font font) {
		super(provider, container);

		var args = this.provider.getArgs();
		var range = args.value();
		NumberRangeInput.RangeInfo rangeInfo = new NumberRangeInput.RangeInfo(range.min(), range.max(), Optional.of(this.value.floatValue()), Optional.of(range.step()));
		var slider = new WSlider(0, 0, 120, 20,
				e -> args.textGetter().apply(this.provider.getTitle().copy(), FastMath.round(e)), rangeInfo, e -> this.value = FastMath.round(e)
		);
		this.addChild(slider);
	}

	public record IntRangeOption(int min, int max, float step) {
	}
}
