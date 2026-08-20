package net.w3e.util.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.fabricmc.fabric.impl.creativetab.FabricCreativeModeTabImpl;
import net.fabricmc.fabric.mixin.creativetab.CreativeModeTabAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.skds.lib2.reflection.FindOptions;
import net.skds.lib2.reflection.ReflectUtils;
import net.w3e.util.mixins.CreativeTabHack;

import java.util.*;

import static net.fabricmc.fabric.impl.creativetab.FabricCreativeModeTabImpl.TABS_PER_PAGE;

public class CreativeModeTabManager {

	private static final Map<Identifier, CreativeModeTabHolder> TABS = new TreeMap<>();

	private static boolean needInitialize;

	public static List<CreativeModeTab> modifyTabs(List<CreativeModeTab> list, boolean filter) {
		if (TABS.isEmpty()) {
			return list;
		}
		Collection<CreativeModeTabHolder> values = TABS.values();
		if (filter) {
			values = values.stream().filter(CreativeModeTabHolder::shouldDisplay).toList();
			if (values.isEmpty()) {
				return list;
			}
		}
		values.forEach(CreativeModeTabHolder::initialize);
		list = new ArrayList<>(list);
		list.addAll(values.stream().map(CreativeModeTabHolder::getTab).toList());
		if (needInitialize) {
			int count = (int) BuiltInRegistries.CREATIVE_MODE_TAB.listElements().count() - ReflectUtils.getAllFields(CreativeModeTabs.class, new FindOptions(ResourceKey.class)).size();
			needInitialize = false;
			for (CreativeModeTabHolder holder : TABS.values()) {
				var creativeModeTab = holder.tab;

				((FabricCreativeModeTabImpl) creativeModeTab).fabric_setPage((count / TABS_PER_PAGE) + 1);
				int pageIndex = count % TABS_PER_PAGE;
				CreativeModeTab.Row row = pageIndex < (TABS_PER_PAGE / 2) ? CreativeModeTab.Row.TOP : CreativeModeTab.Row.BOTTOM;

				final CreativeModeTabAccessor creativeModeTabAccessor = (CreativeModeTabAccessor) creativeModeTab;
				creativeModeTabAccessor.setRow(row);
				creativeModeTabAccessor.setColumn(row == CreativeModeTab.Row.TOP ? pageIndex % TABS_PER_PAGE : (pageIndex - TABS_PER_PAGE / 2) % (TABS_PER_PAGE));

				count++;
			}
		}
		return list;
	}

	public static void add(Identifier id, CreativeModeTab tab) {
		TABS.put(id, new CreativeModeTabHolder(tab));
	}

	public static void remove(Identifier id) {
		TABS.remove(id);
	}

	@RequiredArgsConstructor
	private static class CreativeModeTabHolder {
		@Getter
		private final CreativeModeTab tab;
		private boolean init;

		private void initialize() {
			if (!this.init) {
				this.init = true;
				var minecraft = Minecraft.getInstance();
				assert minecraft.player != null;
				var player = minecraft.player;
				try {
					((CreativeTabHack) this.tab).w3e$buildContents(new CreativeModeTab.ItemDisplayParameters(
							player.connection.enabledFeatures(),
							player.canUseGameMasterBlocks(),
							player.level().registryAccess()
					));
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
				needInitialize = true;
			}
		}

		public boolean shouldDisplay() {
			this.initialize();
			return this.tab.shouldDisplay();
		}
	}

}
