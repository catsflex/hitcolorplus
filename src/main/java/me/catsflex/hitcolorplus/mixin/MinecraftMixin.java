package me.catsflex.hitcolorplus.mixin;

import me.catsflex.hitcolorplus.util.HitTracker;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
	
	// Remove obsolete hit ticks every tick.
	// Should be a light task.
	@Inject(method = "tick", at = @At("TAIL"))
	private void onClientTick(CallbackInfo ci) {
		HitTracker.tickCleanup();
	}
	
	// Flush all stored hit ticks when leaving the world.
	@Inject(method = "clearClientLevel", at = @At("HEAD"))
	private void onClearClientLevel(CallbackInfo ci) {
		HitTracker.clear();
	}
}
