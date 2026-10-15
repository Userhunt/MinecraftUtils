package net.w3e.util.common.gui.option.container;

import net.w3e.util.common.gui.option.OptionProvider;
import net.w3e.util.common.gui.option.RangeOption;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public record PageData<VALUE, RANGE, OBJECT, VALUE_HOLDER>(RangeOption<VALUE, RANGE> rangeOption,
														   Map<VALUE, PageFactoryData<OBJECT, VALUE_HOLDER>> pageFactoryMap) {

	public record PageFactoryData<OBJECT, VALUE_HOLDER>(Consumer<VALUE_HOLDER> onSet,
														List<OptionProvider<?, OBJECT, ?, ?>> provider) {

		public PageFactoryData(Consumer<VALUE_HOLDER> onSet) {
			this(onSet, null);
		}
	}

}
