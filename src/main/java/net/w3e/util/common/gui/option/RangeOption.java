package net.w3e.util.common.gui.option;

import net.minecraft.network.chat.Component;

import java.util.function.BiFunction;

public record RangeOption<VALUE, E>(BiFunction<Component, VALUE, Component> textGetter, E value) {

	public static <VALUE, E> RangeOption<VALUE, E> ofRange(E value) {
		return new RangeOption<>((title, v) -> title.copy().append(": " + v), value);
	}

	public static <VALUE, E> RangeOption<VALUE, E> ofCycle(E value) {
		return new RangeOption<>((_, v) -> Component.literal(String.valueOf(v)), value);
	}

	public static <VALUE, E> RangeOption<VALUE, E> ofFlags(E value) {
		return new RangeOption<>((_, v) -> Component.literal(String.valueOf(v)), value);
	}

}
