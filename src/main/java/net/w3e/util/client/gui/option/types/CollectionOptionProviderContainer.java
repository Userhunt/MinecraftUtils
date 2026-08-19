package net.w3e.util.client.gui.option.types;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.w3e.util.client.gui.container.WLayoutProviderContainer;
import net.w3e.util.client.gui.option.OptionProviderContainer;
import net.w3e.util.common.gui.option.OptionProvider;
import net.w3e.util.mixins.client.ScreenAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CollectionOptionProviderContainer<E, OBJECT> extends OptionProviderContainer<CollectionOptionProviderContainer.ListData<E, OBJECT>, OBJECT, List<E>> {

	private final Screen screen;
	private final StringWidget title;
	private final Button addButton;
	private final List<ElementContainer> containers = new ArrayList<>();

	@SuppressWarnings("unchecked")
	public CollectionOptionProviderContainer(OptionProvider<?, OBJECT, ?, ?> provider, OptionProviderContainer.ContainerPair<OBJECT> container, Screen screen) {
		super((OptionProvider<?, OBJECT, ?, List<E>>) provider, container);

		this.value = new ArrayList<>(this.value);

		this.screen = screen;

		var args = this.provider.getArgs();
		for (E v : this.value) {
			this.containers.add(new ElementContainer(args.copy.apply(v), args));
		}

		this.title = new StringWidget(this.provider.getTitle(), screen.getFont());
		this.addButton = Button.builder(Component.literal("Добавить"), _ -> {
			add();
			init(true);
		}).build();

		this.init(false);
	}

	@Override
	public boolean equals() {
		this.value.clear();
		for (ElementContainer container : this.containers) {
			this.value.add(container.container.getValue());
		}
		return super.equals();
	}

	private void add() {
		var args = this.provider.getArgs();
		E newValue = args.factory.apply(this.container.object());
		this.containers.add(new ElementContainer(newValue, args));
		init(true);
	}

	private void init(boolean reposition) {
		this.clearContents();
		this.addChild(this.title);
		for (ElementContainer container : this.containers) {
			this.addChild(container);
		}
		this.addChild(addButton);
		if (reposition) {
			((ScreenAccessor) this.screen).w3e$repositionElements();
		}
	}

	public record ListData<E, OBJECT>(Function<OBJECT, E> factory, Function<E, E> copy,
									  ProviderFactory<E, OBJECT> provider) {
	}

	public interface ProviderFactory<E, OBJECT> {
		OptionProvider<?, OBJECT, OBJECT, E> crateProvider(Function<OBJECT, OBJECT> converter, Function<OBJECT, E> getter, BiConsumer<OBJECT, E> setter);
	}

	private class ElementContainer extends WLayoutProviderContainer {

		private final OptionProviderContainer<?, OBJECT, E> container;

		public ElementContainer(E value, CollectionOptionProviderContainer.ListData<E, OBJECT> args) {
			super(LinearLayout.vertical().spacing(5));

			this.container = args.provider.crateProvider(Function.identity(), (_) -> value, null).createOption(CollectionOptionProviderContainer.this.container, CollectionOptionProviderContainer.this.screen);
			this.addChild(this.container);

			this.addChild(Button.builder(Component.literal("Удалить элемент"), _ -> {
				CollectionOptionProviderContainer.this.containers.remove(this);
				init(true);
			}).build());
		}

	}

}
