package net.w3e.util.client.gui.container;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractContainerWidget;
import net.minecraft.client.gui.components.AbstractScrollArea;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.CommonComponents;
import net.w3e.util.client.gui.layout.WLayout;
import net.w3e.util.client.gui.layout.WLayoutUtils;
import net.w3e.util.mixins.client.AbstractWidgetAccessor;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class WLayoutProviderContainer extends AbstractContainerWidget implements WLayout {

	protected final Layout contents;
	@Getter(AccessLevel.PROTECTED)
	private int minWidth;
	@Getter(AccessLevel.PROTECTED)
	private int minHeight;
	@Getter(AccessLevel.PROTECTED)
	private int maxHeight = Integer.MAX_VALUE;

	@Getter(AccessLevel.PROTECTED)
	@Setter
	@Accessors(chain = true)
	private int topPadding;
	@Getter(AccessLevel.PROTECTED)
	@Setter
	@Accessors(chain = true)
	private int bottomPadding;
	@Getter(AccessLevel.PROTECTED)
	@Setter
	@Accessors(chain = true)
	private int leftPadding;
	@Getter(AccessLevel.PROTECTED)
	@Setter
	@Accessors(chain = true)
	private int rightPadding;

	private final List<AbstractWidget> children = new ArrayList<>();

	public WLayoutProviderContainer(final Layout contents) {
		super(0, 0, 0, 0, CommonComponents.EMPTY, AbstractScrollArea.defaultSettings(10));
		this.contents = contents;
	}

	protected <T extends LayoutElement> T addChild(T element) {
		return ((LinearLayout) this.contents).addChild(element);
	}

	protected <T extends LayoutElement> T addChild(T element, final Consumer<LayoutSettings> layoutSettingsAdjustments) {
		return ((LinearLayout) this.contents).addChild(element, layoutSettingsAdjustments);
	}

	protected <T extends LayoutElement> T addChild(T element, final LayoutSettings cellSettings) {
		return ((LinearLayout) this.contents).addChild(element, cellSettings);
	}

	protected void clearContents() {
		WLayoutUtils.clear((LinearLayout) this.contents);
	}

	@Override
	public void setFocused(boolean focused) {
		((AbstractWidgetAccessor) this).w3e$setFocused(focused);
	}

	@Override
	public void setFocused(@Nullable GuiEventListener focused) {
		this.setFocused(focused != null);
		super.setFocused(focused);
	}

	public final void setMinWidth(final int minWidth) {
		this.minWidth = minWidth;
		this.setWidth(Math.max(this.contents.getWidth(), minWidth));
	}

	public final void setMinHeight(final int minHeight) {
		this.minHeight = minHeight;
		this.setHeight(Math.max(this.contents.getHeight(), minHeight));
	}

	public final void setMaxHeight(final int maxHeight) {
		this.maxHeight = maxHeight;
		this.setHeight(Math.min(this.contents.getHeight(), maxHeight));
		this.refreshScrollAmount();
	}

	@Override
	public final void setX(final int x) {
		super.setX(x);
		this.setContentX(x + this.leftPadding);
	}

	protected void setContentX(final int x) {
		this.contents.setX(x);
	}

	@Override
	public void setY(final int y) {
		super.setY(y);
		this.setContentY(y + this.topPadding);
	}

	protected void setContentY(final int y) {
		this.contents.setY(y);
	}

	@Override
	public void arrangeElements() {
		this.contents.arrangeElements();
		this.children.clear();
		this.contents.visitWidgets(this.children::add);

		this.setWidth(Math.max(this.contents.getWidth() + this.rightPadding, this.minWidth));
		this.setHeight(Math.clamp(this.contents.getHeight() + this.bottomPadding, this.minHeight, this.maxHeight));
	}

	@Override
	public void visitChildren(final Consumer<LayoutElement> layoutElementVisitor) {
		layoutElementVisitor.accept(this);
	}

	@Override
	public void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {
		super.visitWidgets(widgetVisitor);
	}

	@Override
	protected final int contentHeight() {
		return this.contents.getHeight() + this.bottomPadding;
	}

	@Override
	protected void extractWidgetRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
		graphics.enableScissor(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height);

		for (AbstractWidget child : this.children) {
			child.extractRenderState(graphics, mouseX, mouseY, a);
		}

		graphics.disableScissor();
	}

	@Override
	protected void updateWidgetNarration(final NarrationElementOutput output) {
	}

	@Override
	public ScreenRectangle getBorderForArrowNavigation(final ScreenDirection opposite) {
		GuiEventListener focused = this.getFocused();
		return focused != null
				? focused.getBorderForArrowNavigation(opposite)
				: new ScreenRectangle(this.getX(), this.getY(), this.width, this.contentHeight()).getBorder(opposite);
	}

	@Override
	public final List<? extends GuiEventListener> children() {
		return this.children;
	}

	@Override
	public Collection<? extends NarratableEntry> getNarratables() {
		return this.children;
	}

	@Override
	public Optional<GuiEventListener> getChildAt(double x, double y) {
		return super.getChildAt(x, y);
	}
}
