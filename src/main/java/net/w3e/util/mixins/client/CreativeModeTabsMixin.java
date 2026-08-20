package net.w3e.util.mixins.client;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.w3e.util.client.CreativeModeTabManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(CreativeModeTabs.class)
public class CreativeModeTabsMixin {

	@Inject(method = "tabs", at = @At(value = "RETURN"), cancellable = true)
	private static void w3e$tabs(CallbackInfoReturnable<List<CreativeModeTab>> cir) {
		var o = cir.getReturnValue();
		var n = CreativeModeTabManager.modifyTabs(o, true);
		if (o != n) {
			cir.setReturnValue(n);
		}
	}

	@Inject(method = "allTabs", at = @At(value = "RETURN"), cancellable = true)
	private static void w3e$allTabs(CallbackInfoReturnable<List<CreativeModeTab>> cir) {
		var o = cir.getReturnValue();
		var n = CreativeModeTabManager.modifyTabs(o, false);
		if (o != n) {
			cir.setReturnValue(n);
		}
	}

}
