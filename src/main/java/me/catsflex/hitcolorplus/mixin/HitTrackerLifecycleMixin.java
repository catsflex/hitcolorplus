package me.catsflex.hitcolorplus.mixin;

import me.catsflex.hitcolorplus.util.LocalHitTracker;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class HitTrackerLifecycleMixin {
	
	// Remove obsolete hit ticks every tick.
	// Should be a light task.
	@Inject(method = "tick", at = @At("TAIL"))
	private void onClientTick(CallbackInfo ci) {
		LocalHitTracker.tickCleanup();
	}
	
	// Flush all stored hit ticks when leaving the world.
	@Inject(method = "clearClientLevel", at = @At("HEAD"))
	private void onClearClientLevel(CallbackInfo ci) {
		LocalHitTracker.clear();
	}
}
