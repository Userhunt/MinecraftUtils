package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.Font;
import net.minecraft.server.dialog.input.NumberRangeInput;
import net.skds.lib2.mat.FastMath;
import net.w3e.util.client.gui.element.WSlider;
import net.w3e.util.client.gui.option.OptionProviderContainer;
import net.w3e.util.client.gui.option.RangeOption;
import net.w3e.util.common.gui.option.OptionProvider;

import java.util.List;
import java.util.Optional;

public class ValueRangeOptionProviderContainer<E, OBJECT> extends OptionProviderContainer<RangeOption<E, List<E>>, OBJECT, E> {

	public ValueRangeOptionProviderContainer(OptionProvider<?, OBJECT, ?, E> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Font font) {
		super(provider, container);

		var args = this.provider.getArgs();
		List<E> range = args.args();
		NumberRangeInput.RangeInfo rangeInfo = new NumberRangeInput.RangeInfo(0, range.size() - 1, Optional.of((float) range.indexOf(this.value)), Optional.of(1f));
		var slider = new WSlider(0, 0, 120, 20,
				e -> args.textGetter().apply(this.provider.getTitle().copy(), range.get(FastMath.round(e))), rangeInfo, e -> this.value = range.get(FastMath.round(e))
		);
		this.addChild(slider);
	}

}
