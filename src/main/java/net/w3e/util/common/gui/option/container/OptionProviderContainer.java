package net.w3e.util.common.gui.option.container;

import net.w3e.util.common.gui.option.OptionProviderUpdateData;

import java.util.List;

public interface OptionProviderContainer<OBJECT> {
	int WIDTH = 300;
	int BUTTON_WIDTH = 150;
	int NUMBER_WIDTH = 70;
	int TAB = 24;

	List<OptionProviderUpdateData<OBJECT, ?>> createUpdateData();
}
