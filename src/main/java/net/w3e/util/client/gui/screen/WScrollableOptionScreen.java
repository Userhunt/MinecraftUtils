package net.w3e.util.client.gui.screen;

import lombok.AccessLevel;
import lombok.Setter;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.w3e.util.client.gui.container.WScrollableLayoutContainer;
import net.w3e.util.client.gui.layout.WLayoutUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public abstract class WScrollableOptionScreen<E> extends Screen {

	private EditBox searchBox;
	private FrameLayout layout;
	private LinearLayout body;
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
		this.searchBox = new EditBox(this.font, 0, 0, 150, 20, Component.literal("Поиск..."));
		this.searchBox.setResponder(e -> this.updateSearchResult(e, false));

		this.layout = new FrameLayout();
		this.layout.defaultChildLayoutSetting().alignHorizontallyCenter().alignVerticallyTop().padding(10);
		this.layout.addChild(this.searchBox);

		this.body = LinearLayout.vertical().spacing(10);
		this.body.defaultCellSetting().alignHorizontallyCenter();

		this.bodyScroll = new WScrollableLayoutContainer(body, this.height - 40);
		this.layout.addChild(this.bodyScroll, setting -> setting.alignHorizontallyCenter().alignVerticallyTop().paddingTop(35));

		this.updateSearchResult(this.query, true);
	}

	protected void rebuild() {
		updateSearchResult(this.query, false);
	}

	private void updateSearchResult(String query, boolean init) {
		this.query = query;
		this.clearWidgets();

		WLayoutUtils.clear(this.body);
		String lowerQuery = query.toLowerCase(Locale.ROOT);
		fillBody(query, lowerQuery, this.body, init);

		this.addRenderableWidget(this.searchBox);
		this.bodyScroll.visitWidgets(this::addRenderableWidget);

		this.repositionElements();
	}

	protected abstract void fillBody(String query, String lowerQuery, LinearLayout body, boolean init);

	@SuppressWarnings("unchecked")
	protected <T> E createOption(T key, Function<T, E> factory) {
		return this.options.computeIfAbsent(key, (Function<Object, E>) factory);
	}

	@Override
	protected void repositionElements() {
		this.layout.arrangeElements();
		this.bodyScroll.arrangeElements();
		this.bodyScroll.setMaxHeight(this.height - 40);
		this.layout.setX(this.width / 2 - this.layout.getWidth() / 2);
	}

}
