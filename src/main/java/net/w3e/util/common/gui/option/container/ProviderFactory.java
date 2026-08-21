package net.w3e.util.common.gui.option.container;

import net.w3e.util.common.gui.option.OptionProvider;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface ProviderFactory<E, OBJECT> {
	OptionProvider<?, OBJECT, OBJECT, E> crateProvider(Function<OBJECT, OBJECT> converter, Function<OBJECT, E> getter, BiConsumer<OBJECT, E> setter);
}
