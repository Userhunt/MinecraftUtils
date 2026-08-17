package net.w3e.util.client.gui.option;

import net.minecraft.network.chat.Component;

import java.util.function.BiFunction;

public record RangeOption<VALUE, E>(BiFunction<Component, VALUE, Component> textGetter, E args) {

	public static <VALUE, E> RangeOption<VALUE, E> ofRange(E args) {
		return new RangeOption<>((title, value) -> title.copy().append(": " + value.toString()), args);
	}

	public static <VALUE, E> RangeOption<VALUE, E> ofCycle(E args) {
		return new RangeOption<>((_, value) -> Component.literal(value.toString()), args);
	}

}
