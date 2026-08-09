package net.w3e.util.client.gui.element;

import lombok.Setter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.skds.lib2.mat.FastMath;

public class WItemDisplayWidget extends AbstractWidget {

	private final Minecraft minecraft = Minecraft.getInstance();
	private float scale = 1f;
	@Setter
	private float visualScale = 1f;
	@Setter
	private int offsetX = 0;
	@Setter
	private int offsetY = 0;
	private final ItemStack itemStack;
	private final boolean decorations;
	private final boolean tooltip;
	private int actualWidth = 0;
	private int actualHeight = 0;

	public WItemDisplayWidget(ItemStack itemStack, boolean decorations, boolean tooltip) {
		super(0, 0, 16, 16, CommonComponents.EMPTY);
		this.itemStack = itemStack;
		this.decorations = decorations;
		this.tooltip = tooltip;
	}

	public void setScale(float scale) {
		this.scale = scale;
		this.setSize(super.getWidth(), super.getHeight());
	}

	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		this.actualWidth = FastMath.round(width * this.scale);
	}

	@Override
	public void setHeight(int height) {
		super.setHeight(height);
		this.actualHeight = FastMath.round(height * this.scale);
	}

	@Override
	public void setSize(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
	}

	@Override
	public int getWidth() {
		return this.actualWidth;
	}

	@Override
	public int getHeight() {
		return this.actualHeight;
	}

	@Override
	protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		int posX = this.getX() + this.offsetX;
		int posY = this.getY() + this.offsetY;
		boolean transform = this.visualScale != 1 || this.offsetX != 0 || this.offsetY != 0;
		if (transform) {
			var matrixStack = graphics.pose();
			matrixStack.pushMatrix();
			matrixStack.translate(posX, posY);
			matrixStack.scale(this.visualScale, this.visualScale);
			matrixStack.translate(-posX, -posY);
		}
		graphics.item(this.itemStack, posX, posY, 0);
		if (this.decorations) {
			graphics.itemDecorations(this.minecraft.font, this.itemStack, posX, posY, null);
		}

		if (this.isFocused()) {
			graphics.outline(posX, posY, this.getWidth(), this.getHeight(), -1);
		}
		if (transform) {
			graphics.pose().popMatrix();
		}

		if (this.tooltip && this.isHovered()) {
			graphics.setTooltipForNextFrame(this.minecraft.font, this.itemStack, mouseX, mouseY);
		}
	}

	@Override
	protected void updateWidgetNarration(final NarrationElementOutput output) {
		output.add(NarratedElementType.TITLE, Component.translatable("narration.item", this.itemStack.getHoverName()));
	}

	public static class Clickable extends WItemDisplayWidget {

		private final OnPress onPress;

		public Clickable(ItemStack itemStack, boolean decorations, boolean tooltip, OnPress onPress) {
			super(itemStack, decorations, tooltip);
			this.onPress = onPress;
		}

		@Override
		public void onClick(MouseButtonEvent event, boolean doubleClick) {
			this.onPress.onPress(this);
		}

		@Environment(EnvType.CLIENT)
		public interface OnPress {
			void onPress(final Clickable button);
		}
	}
}
