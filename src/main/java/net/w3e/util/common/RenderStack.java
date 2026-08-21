package net.w3e.util.common;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.Mannequin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.skds.lib2.mat.vec3.Vec3;

public interface RenderStack {
	void render(Entity entity);

	Vec3 camPos();

	Player getPlayer();

	Level getWorld();

	Mannequin createClientMannequin();
}
