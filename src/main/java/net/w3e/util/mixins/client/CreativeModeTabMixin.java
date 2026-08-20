package net.w3e.util.mixins.client;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.w3e.util.mixins.CreativeTabHack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.Set;

@Mixin(CreativeModeTab.class)
public class CreativeModeTabMixin implements CreativeTabHack {

	@Final
	@Shadow
	private CreativeModeTab.DisplayItemsGenerator displayItemsGenerator;
	@Shadow
	private Collection<ItemStack> displayItems;
	@Shadow
	private Set<ItemStack> displayItemsSearchTab;

	@Override
	public void w3e$buildContents(final CreativeModeTab.ItemDisplayParameters parameters) {
		CreativeModeTab.ItemDisplayBuilder displayList = new CreativeModeTab.ItemDisplayBuilder((CreativeModeTab) (Object) this, parameters.enabledFeatures());
		this.displayItemsGenerator.accept(parameters, displayList);
		this.displayItems = displayList.tabContents;
		this.displayItemsSearchTab = displayList.searchTabContents;
	}

}
