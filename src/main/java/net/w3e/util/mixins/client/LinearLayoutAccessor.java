package net.w3e.util.mixins.client;

import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LinearLayout.class)
public interface LinearLayoutAccessor {
	@Accessor("wrapped")
	GridLayout w3e$getWrapped();

	@Accessor("nextChildIndex")
	void w3e$setNextChildIndex(int index);
}
