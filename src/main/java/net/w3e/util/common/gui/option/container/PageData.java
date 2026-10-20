package net.w3e.util.common.gui.option.container;

import net.w3e.util.common.gui.option.OptionProvider;
import net.w3e.util.common.gui.option.RangeOption;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public record PageData<VALUE, RANGE, OBJECT, VALUE_HOLDER>(RangeOption<VALUE, RANGE> rangeOption,
														   Map<PageKey<VALUE, ?>, PageFactoryData<OBJECT, VALUE_HOLDER>> pageFactoryMap) {

	public static <VALUE, RANGE, OBJECT, VALUE_HOLDER> PageData<VALUE, RANGE, OBJECT, VALUE_HOLDER> of(RangeOption<VALUE, RANGE> rangeOption,
																									   Map<VALUE, PageFactoryData<OBJECT, VALUE_HOLDER>> pageFactoryMap) {
		if (pageFactoryMap == null) {
			return new PageData<>(rangeOption, null);
		}
		Map<PageKey<VALUE, ?>, PageFactoryData<OBJECT, VALUE_HOLDER>> map = pageFactoryMap.entrySet().stream()
				.collect(Collectors.toMap(
						entry -> new PageKey<>(entry.getKey(), null),
						Map.Entry::getValue
				));

		return new PageData<>(rangeOption, map);
	}

	public record PageFactoryData<OBJECT, VALUE_HOLDER>(Consumer<VALUE_HOLDER> onSet,
														List<OptionProvider<?, OBJECT, ?, ?>> provider) {

		public PageFactoryData(Consumer<VALUE_HOLDER> onSet) {
			this(onSet, null);
		}
	}

	public record PageKey<K, V>(K key, V value) {
		@Override
		public boolean equals(Object object) {
			if (object == null || getClass() != object.getClass()) return false;

			PageKey<?, ?> pageKey = (PageKey<?, ?>) object;
			return key.equals(pageKey.key);
		}

		@Override
		public int hashCode() {
			return key.hashCode();
		}

		public PageKey<K, V> copy(Function<V, V> copy) {
			return new PageKey<>(this.key, this.value == null ? null : copy.apply(this.value));
		}

	}

}
