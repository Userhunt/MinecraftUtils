package net.w3e.util.common.gui.option.container;

import java.util.function.Function;

public record ListData<E, OBJECT>(Function<OBJECT, E> factory, Function<E, E> copy,
								  ProviderFactory<E, OBJECT> provider) {
}
