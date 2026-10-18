package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.Screen;
import net.w3e.util.client.gui.option.OptionProviderContainerImpl;
import net.w3e.util.common.gui.option.OptionProvider;
import net.w3e.util.common.gui.option.OptionProviderUpdateData;
import net.w3e.util.common.gui.option.container.ContainerPair;
import net.w3e.util.common.gui.option.container.OptionProviderContainer;
import net.w3e.util.common.gui.option.container.PageData;
import net.w3e.util.mixins.client.ScreenAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EnumPageProviderContainer<E, OBJECT> extends OptionProviderContainerImpl<PageData<E, List<E>, OBJECT, ?>, OBJECT, E> {

	private final Screen screen;
	private final CycleButton<E> button;
	private List<OptionProviderContainer<OBJECT>> contents;

	@SuppressWarnings("unchecked")
	public EnumPageProviderContainer(OptionProvider<?, OBJECT, ?, Object> provider, ContainerPair<OBJECT> containerPair, @SuppressWarnings("unused") Screen screen) {
		super((OptionProvider<?, OBJECT, ?, E>) provider, containerPair);

		this.screen = screen;

		var args = this.provider.getArgs();
		var range = args.rangeOption();
		this.button = CycleButton.builder(e -> range.textGetter().apply(null, e), this.value)
				.withValues(range.range())
				.create(0, 0, BUTTON_WIDTH, 20, provider.getTitle(), (_, v) -> {
					this.value = v;
					init(true);
				});
		init(false);
	}

	@SuppressWarnings("unchecked")
	private void init(boolean reposition) {
		this.clearContents();
		this.addChild(this.button);

		this.contents = null;
		var factoryMap = this.provider.getArgs().pageFactoryMap();
		if (factoryMap != null) {
			var factoryData = factoryMap.get(this.value);
			Consumer<Object> onSet = (Consumer<Object>) factoryData.onSet();
			if (onSet != null) {
				onSet.accept(this.provider.getHolder(this.containerPair.object()));
			}
			var providers = factoryData.provider();
			if (providers != null) {
				var containerPair = this.containerPair.newTab();
				this.contents = new ArrayList<>();
				for (var provider : providers) {
					var content = provider.createOption(containerPair, this.screen);
					this.addChild((LayoutElement) content);
					this.contents.add(content);
				}
			}
		}
		if (reposition) {
			((ScreenAccessor) this.screen).w3e$repositionElements();
		}
	}

	@Override
	public List<OptionProviderUpdateData<OBJECT, ?>> createUpdateData() {
		List<OptionProviderUpdateData<OBJECT, ?>> list = new ArrayList<>(super.createUpdateData());
		if (this.contents != null) {
			for (var content : this.contents) {
				list.addAll(content.createUpdateData());
			}
		}
		return list;
	}

}
