package net.w3e.util.common.gui.option;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.w3e.util.client.gui.option.OptionProviderContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

@RequiredArgsConstructor
public class OptionProvider<ARGS, OBJECT, VALUE_HOLDER, VALUE> implements Comparable<OptionProvider<?, ?, ?, ?>> {

	@Getter
	private final OptionProviderType<VALUE> type;
	@Getter(AccessLevel.PACKAGE)
	private final int ordinal;
	@Getter
	private final Component title;

	@Getter
	private final ARGS args;

	@Getter(AccessLevel.PACKAGE)
	private final Function<OBJECT, VALUE_HOLDER> converter;
	@Getter(AccessLevel.PACKAGE)
	private final Function<VALUE_HOLDER, VALUE> getter;
	@Getter(AccessLevel.PACKAGE)
	private final BiConsumer<VALUE_HOLDER, VALUE> setter;
	@Getter(AccessLevel.PACKAGE)
	private final BiPredicate<VALUE, VALUE> equals;

	public OptionProvider(OptionProviderType<VALUE> type, Component title, ARGS args, Function<OBJECT, VALUE_HOLDER> converter, Function<VALUE_HOLDER, VALUE> getter, BiConsumer<VALUE_HOLDER, VALUE> setter) {
		this(type, 0, title, args, converter, getter, setter, Objects::equals);
	}

	@SuppressWarnings("unchecked")
	private VALUE_HOLDER getHolder(Object object) {
		return this.converter.apply((OBJECT) object);
	}

	public final VALUE getValue(Object pidod) {
		VALUE_HOLDER valueHolder = this.getHolder(pidod);
		return this.getter.apply(valueHolder);
	}

	public final void setValue(Object pidod, VALUE value) {
		VALUE_HOLDER valueHolder = this.getHolder(pidod);
		this.setter.accept(valueHolder, value);
	}

	public final boolean equals(VALUE a, VALUE b) {
		return this.equals.test(a, b);
	}

	@Override
	public int compareTo(@NotNull OptionProvider<?, ?, ?, ?> o) {
		return Integer.compare(this.ordinal, o.ordinal);
	}

	public OptionProviderContainer<?, OBJECT, VALUE> createOption(OptionProviderContainer.ContainerPair<OBJECT> container, Font font) {
		return this.getType().createOption(this, container, font);
	}

}
