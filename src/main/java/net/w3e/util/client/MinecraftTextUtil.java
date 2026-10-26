package net.w3e.util.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.skds.lib2.utils.Holders;

import java.util.ArrayList;
import java.util.List;

public class MinecraftTextUtil {

	public static List<Component> convertToComponentsOptimized(Component source, int width) {
		List<Component> result = new ArrayList<>();

		for (FormattedCharSequence line : Minecraft.getInstance().font.split(source, width)) {
			MutableComponent rootComponent = Component.empty();

			StringBuilder textBuffer = new StringBuilder();
			final Holders.ObjectHolder<Style> currentStyle = new Holders.ObjectHolder<>();

			line.accept((_, style, codePoint) -> {
				Style value = currentStyle.getValue();
				if (value == null) {
					currentStyle.setValue(style);
				} else {
					if (!value.equals(style)) {
						rootComponent.append(Component.literal(textBuffer.toString()).setStyle(value));
						textBuffer.setLength(0);
						currentStyle.setValue(style);
					}
				}

				textBuffer.appendCodePoint(codePoint);
				return true;
			});

			if (!textBuffer.isEmpty()) {
				rootComponent.append(Component.literal(textBuffer.toString()).setStyle(currentStyle.getValue()));
			}

			result.add(rootComponent);
		}

		return result;
	}


}
