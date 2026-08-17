package net.w3e.util.mixins.client;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MouseHandler.class)
public interface MouseHandlerAccessor {

	@Accessor("accumulatedDX")
	double w3e$getAccumulatedDX();

	@Accessor("accumulatedDY")
	double w3e$getAccumulatedDY();

	@Accessor("accumulatedDX")
	void w3e$setAccumulatedDX(double accumulatedDX);

	@Accessor("accumulatedDY")
	void w3e$setAccumulatedDY(double accumulatedDY);

	@Accessor("lastHandleMovementTime")
	double w3e$getLastHandleMovementTime();

	@Accessor("lastHandleMovementTime")
	void w3e$setLastHandleMovementTime(double lastHandleMovementTime);

	@Invoker("turnPlayer")
	void w3e$turnPlayer(double mousea);

}
