package net.w3e.util.common;

import com.mojang.math.Transformation;
import net.minecraft.util.Brightness;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.skds.lib2.shapes.AABB;

public class CollideVisualBox {

	public static Display.ItemDisplay createAABB(Level world, AABB box) {
		Display.ItemDisplay itemDisplay = new Display.ItemDisplay(EntityType.ITEM_DISPLAY, world);
		itemDisplay.setItemStack(new ItemStack(Items.TINTED_GLASS));
		setAABB(itemDisplay, box);
		return itemDisplay;
	}

	public static void setAABB(Display.ItemDisplay itemDisplay, AABB box) {
		itemDisplay.setWidth((float) box.getMaxWidth());
		itemDisplay.setHeight((float) box.sizeY());
		itemDisplay.setTransformation(new Transformation(
				null, null, MinecraftHelper.VEC3_VECTOR3F.convertOR(box.dimensions()), null
		));
		itemDisplay.setBrightnessOverride(Brightness.FULL_BRIGHT);
		itemDisplay.setPos(MinecraftHelper.VEC3_VEC3.convertOR(box.getCenter()));
	}

}
