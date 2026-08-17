package net.w3e.util.mixins.client;

import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractWidget.class)
public interface AbstractWidgetAccessor {
	@Accessor("focused")
	void w3e$setFocused(boolean bl);
}
