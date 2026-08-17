package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.Font;
import net.minecraft.server.dialog.input.NumberRangeInput;
import net.w3e.util.client.gui.element.WSlider;
import net.w3e.util.client.gui.option.OptionProviderContainer;
import net.w3e.util.client.gui.option.RangeOption;
import net.w3e.util.common.gui.option.OptionProvider;

import java.util.Optional;

public class FloatRangeOptionProviderContainer<OBJECT> extends OptionProviderContainer<RangeOption<Float, FloatRangeOptionProviderContainer.FloatRangeOption>, OBJECT, Float> {

	public FloatRangeOptionProviderContainer(OptionProvider<?, OBJECT, ?, Float> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Font font) {
		super(provider, container);

		var args = this.provider.getArgs();
		var range = args.args();
		NumberRangeInput.RangeInfo rangeInfo = new NumberRangeInput.RangeInfo(range.min(), range.max(), Optional.of(this.value), Optional.of(range.step()));
		var slider = new WSlider(0, 0, 120, 20,
				e -> args.textGetter().apply(this.provider.getTitle().copy(), e), rangeInfo, e -> this.value = e
		);
		this.addChild(slider);
	}

	public record FloatRangeOption(float min, float max, float step) {
	}

}
