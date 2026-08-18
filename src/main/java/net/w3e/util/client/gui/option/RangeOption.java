package net.w3e.util.client.gui.option;

import net.minecraft.network.chat.Component;

import java.util.function.BiFunction;

public record RangeOption<VALUE, E>(BiFunction<Component, VALUE, Component> textGetter, E value) {

	public static <VALUE, E> RangeOption<VALUE, E> ofRange(E value) {
		return new RangeOption<>((title, v) -> title.copy().append(": " + v.toString()), value);
	}

	public static <VALUE, E> RangeOption<VALUE, E> ofCycle(E value) {
		return new RangeOption<>((_, v) -> Component.literal(v.toString()), value);
	}

	public static <VALUE, E> RangeOption<VALUE, E> ofFlags(E value) {
		return new RangeOption<>((_, v) -> Component.literal(v.toString()), value);
	}

}
