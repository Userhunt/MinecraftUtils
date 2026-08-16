package net.w3e.util.client.gui.screen.scrollable;

import lombok.AccessLevel;
import lombok.Setter;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.network.chat.Component;

import java.util.*;
import java.util.function.Function;

public abstract class WScrollableOptionScreen<E extends LayoutElement> extends WAbstractScrollableScreen<E> {

	private final List<E> active = new ArrayList<>();
	protected final Map<Object, E> options = new HashMap<>();
	@Setter(AccessLevel.PROTECTED)
	private String query = "";

	protected WScrollableOptionScreen(Component title) {
		super(title);
	}

	@Deprecated
	protected boolean addSearchBox() {
		return true;
	}

	@Override
	protected LayoutElement createHeader() {
		EditBox searchBox = new EditBox(this.font, 0, 0, 150, 20, Component.literal("Поиск..."));
		searchBox.setResponder(e -> {
			this.query = e;
			this.updateBody();
		});
		return searchBox;
	}

	@Override
	protected void onUpdateBody(boolean init) {
		String lowerQuery = query.toLowerCase(Locale.ROOT);
		this.active.clear();
		fillBody(query, lowerQuery, init);
	}

	protected abstract void fillBody(String query, String lowerQuery, boolean init);

	protected final void addElement(E element) {
		if (element instanceof Indexed indexed) {
			indexed.setIndex(this.active.size());
		}
		this.active.add(element);
		super.addElement(element);
	}

	protected final E getElement(int index) {
		return this.active.get(index);
	}

	protected final int getIndex(E element) {
		return this.active.indexOf(element);
	}

	protected final int getSize() {
		return this.active.size();
	}

	@SuppressWarnings("unchecked")
	protected final <T> E getOrCreateOption(T key, Function<T, E> factory) {
		return this.options.computeIfAbsent(key, (Function<Object, E>) factory);
	}

	protected interface Indexed {
		void setIndex(int index);
	}
}
