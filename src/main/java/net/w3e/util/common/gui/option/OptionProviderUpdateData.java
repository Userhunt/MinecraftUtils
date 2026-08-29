package net.w3e.util.common.gui.option;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class OptionProviderUpdateData<OBJECT, V> implements Comparable<OptionProviderUpdateData<?, ?>> {

	private final OptionProvider<?, OBJECT, ?, V> provider;
	private final V value;

	@Override
	public int compareTo(@NotNull OptionProviderUpdateData<?, ?> o) {
		return this.provider.compareTo(o.provider);
	}

	public void apply(OBJECT object) {
		this.provider.setValue(object, this.value);
	}

}
