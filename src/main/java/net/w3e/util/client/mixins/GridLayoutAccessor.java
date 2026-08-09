package net.w3e.util.client.mixins;

import net.minecraft.client.gui.layouts.GridLayout;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(GridLayout.class)
public interface GridLayoutAccessor {
	@Accessor("children")
	List<?> w3e$getChildren();
}
