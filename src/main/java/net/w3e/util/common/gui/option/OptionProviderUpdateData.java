package net.w3e.util.common.gui.option;

import org.jetbrains.annotations.NotNull;

public record OptionProviderUpdateData<OBJECT, V>(OptionProvider<?, OBJECT, ?, V> provider,
												  V value) implements Comparable<OptionProviderUpdateData<?, ?>> {

	@Override
	public int compareTo(@NotNull OptionProviderUpdateData<?, ?> o) {
		return this.provider.compareTo(o.provider);
	}

	public void apply(OBJECT object) {
		this.provider.setValue(object, this.value);
	}

}
