package net.w3e.util.common.gui.option;

import net.minecraft.network.chat.Component;

import java.util.function.BiFunction;

public record RangeOption<VALUE, RANGE>(BiFunction<Component, VALUE, Component> textGetter, RANGE range) {

	public static <VALUE, RANGE> RangeOption<VALUE, RANGE> ofRange(RANGE range) {
		return new RangeOption<>((title, v) -> title.copy().append(": " + v), range);
	}

	public static <VALUE, RANGE> RangeOption<VALUE, RANGE> ofCycle(RANGE range) {
		return new RangeOption<>((_, v) -> Component.literal(String.valueOf(v)), range);
	}

	public static <VALUE, RANGE> RangeOption<VALUE, RANGE> ofFlags(RANGE range) {
		return new RangeOption<>((_, v) -> Component.literal(String.valueOf(v)), range);
	}

}
