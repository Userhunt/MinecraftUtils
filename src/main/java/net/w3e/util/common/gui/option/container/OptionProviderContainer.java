package net.w3e.util.common.gui.option.container;

import net.w3e.util.common.gui.option.OptionProviderUpdateData;

public interface OptionProviderContainer<ARGS, OBJECT, VALUE> {
	OptionProviderUpdateData<OBJECT, VALUE> createUpdateData();
}
