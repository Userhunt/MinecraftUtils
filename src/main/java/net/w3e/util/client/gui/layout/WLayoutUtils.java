package net.w3e.util.client.gui.layout;

import net.minecraft.client.gui.layouts.LinearLayout;
import net.w3e.util.mixins.client.GridLayoutAccessor;
import net.w3e.util.mixins.client.LinearLayoutAccessor;

public class WLayoutUtils {

	public static void clear(LinearLayout layout) {
		LinearLayoutAccessor accessor = (LinearLayoutAccessor) layout;
		((GridLayoutAccessor) accessor.w3e$getWrapped()).w3e$getChildren().clear();
		accessor.w3e$setNextChildIndex(0);
	}

}
