package net.w3e.util.common.gui.option.container;

import net.minecraft.client.gui.layouts.LinearLayout;

import java.util.List;

public record ContainerPair<OBJECT>(List<OptionProviderContainer<OBJECT, ?>> options, LinearLayout contents,
									OBJECT object) {
}
