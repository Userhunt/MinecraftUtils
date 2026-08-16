package net.w3e.util.client.gui.element;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.server.dialog.input.NumberRangeInput;

import java.util.function.Function;

public class WSlider extends AbstractSliderButton {

	private final NumberRangeInput.RangeInfo rangeInfo;
	private final Function<Float, Component> messageFactory;
	private final OnChange onChange;

	public WSlider(int x, int y, int width, int height, Function<Float, Component> messageFactory, NumberRangeInput.RangeInfo rangeInfo, OnChange onChange) {
		float initial = rangeInfo.initialSliderValue();
		this.messageFactory = messageFactory;
		super(x, y, width, height, messageFactory.apply(0f), initial);
		this.rangeInfo = rangeInfo;
		this.onChange = onChange;
		this.updateMessage();
	}

	public float getValue() {
		return this.rangeInfo.computeScaledValue((float) this.value);
	}

	@Override
	protected final void updateMessage() {
		this.setMessage(this.messageFactory.apply(this.getValue()));
	}

	@Override
	protected final void applyValue() {
		this.onChange.onChange(this.getValue());
	}

	public interface OnChange {
		void onChange(float value);
	}
}
