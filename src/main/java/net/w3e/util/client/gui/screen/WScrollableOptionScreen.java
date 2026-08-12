package net.w3e.util.client.gui.screen;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.w3e.util.client.gui.container.WScrollableLayoutContainer;
import net.w3e.util.client.gui.layout.WLayoutUtils;

import java.util.*;
import java.util.function.Function;

public abstract class WScrollableOptionScreen<E extends LayoutElement> extends Screen {

	@Getter(AccessLevel.PROTECTED)
	private FrameLayout layout;
	private LinearLayout body;
	private final List<E> active = new ArrayList<>();
	private WScrollableLayoutContainer bodyScroll;
	protected final Map<Object, E> options = new HashMap<>();
	@Setter(AccessLevel.PROTECTED)
	private String query = "";

	protected WScrollableOptionScreen(Component title) {
		super(title);
	}

	@Override
	protected void init() {
		super.init();
		EditBox searchBox = new EditBox(this.font, 0, 0, 150, 20, Component.literal("Поиск..."));
		searchBox.setResponder(e -> this.updateSearchResult(e, false));

		this.layout = new FrameLayout();
		this.layout.defaultChildLayoutSetting().alignHorizontallyCenter().alignVerticallyTop().padding(10);
		this.layout.addChild(searchBox);

		this.body = LinearLayout.vertical().spacing(10);
		this.body.defaultCellSetting().alignHorizontallyCenter();

		this.bodyScroll = new WScrollableLayoutContainer(this.body, this.height - 40);
		this.layout.addChild(this.bodyScroll, setting -> setting.alignHorizontallyCenter().alignVerticallyTop().paddingTop(35));

		this.addRenderableWidget(searchBox);
		this.bodyScroll.visitWidgets(this::addRenderableWidget);

		this.updateSearchResult(this.query, true);
	}

	protected void rebuildSearch() {
		updateSearchResult(this.query, false);
	}

	private void repositionBodyElements() {
		this.bodyScroll.setMaxHeight(this.height - 40);
		this.layout.arrangeElements();
		this.layout.setX(this.width / 2 - this.layout.getWidth() / 2);
	}

	@Override
	protected void repositionElements() {
		repositionBodyElements();
	}

	private void updateSearchResult(String query, boolean init) {
		this.query = query;

		WLayoutUtils.clear(this.body);
		String lowerQuery = query.toLowerCase(Locale.ROOT);
		this.active.clear();
		fillBody(query, lowerQuery, init);

		this.repositionBodyElements();
	}

	protected abstract void fillBody(String query, String lowerQuery, boolean init);

	protected final void addElement(E element) {
		if (element instanceof Indexed indexed) {
			indexed.setIndex(this.active.size());
		}
		this.body.addChild(element);
		this.active.add(element);
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
