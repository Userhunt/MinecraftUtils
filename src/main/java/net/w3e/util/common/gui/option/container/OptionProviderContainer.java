package net.w3e.util.common.gui.option.container;

import net.w3e.util.common.gui.option.OptionProviderUpdateData;

public interface OptionProviderContainer<ARGS, OBJECT, VALUE> {
	<T extends OptionProviderContainer<ARGS, OBJECT, VALUE>> T getAsContainer();

	boolean equals();

	OptionProviderUpdateData<OBJECT, VALUE> createUpdateData();
}
