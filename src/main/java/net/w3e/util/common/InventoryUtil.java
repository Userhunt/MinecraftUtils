package net.w3e.util.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public class InventoryUtil {

	public static void give(ServerPlayer player, ItemStack itemStack) {
		boolean added = player.getInventory().add(itemStack);
		if (added && itemStack.isEmpty()) {
			ItemEntity drop = player.drop(itemStack.copy(), false);
			if (drop != null) {
				drop.makeFakeItem();
			}

			player.level()
					.playSound(
							null,
							player.getX(),
							player.getY(),
							player.getZ(),
							SoundEvents.ITEM_PICKUP,
							SoundSource.PLAYERS,
							0.2F,
							((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
					);
			player.containerMenu.broadcastChanges();
		} else {
			ItemEntity drop = player.drop(itemStack, false);
			if (drop != null) {
				drop.setNoPickUpDelay();
				drop.setTarget(player.getUUID());
			}
		}
	}
}
