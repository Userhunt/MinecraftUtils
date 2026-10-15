package net.w3e.util.common.gui.option;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class OptionProviderUpdateData<OBJECT, V> implements Comparable<OptionProviderUpdateData<?, ?>> {

	protected final OptionProvider<?, OBJECT, ?, V> provider;

	@Override
	public int compareTo(@NotNull OptionProviderUpdateData<?, ?> o) {
		return this.provider.compareTo(o.provider);
	}

	public abstract void apply(OBJECT object);

	public static class Single<OBJECT, V> extends OptionProviderUpdateData<OBJECT, V> {

		private final V value;

		public Single(OptionProvider<?, OBJECT, ?, V> provider, V value) {
			super(provider);
			this.value = value;
		}

		@Override
		public void apply(OBJECT object) {
			this.provider.setValue(object, this.value);
		}
	}

}
