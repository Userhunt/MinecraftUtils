package net.w3e.util.common.gui.option.container;

import net.w3e.util.common.gui.option.OptionProviderUpdateData;

public interface OptionProviderContainer<OBJECT, VALUE> {
	int WIDTH = 190;

	OptionProviderUpdateData<OBJECT, VALUE> createUpdateData();
}
