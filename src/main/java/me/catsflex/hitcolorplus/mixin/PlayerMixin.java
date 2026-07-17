package me.catsflex.hitcolorplus.mixin;

import me.catsflex.hitcolorplus.config.ModConfig;
import me.catsflex.hitcolorplus.util.HitTracker;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
	
	@Inject(method = "attack", at = @At("HEAD"))
	private void registerLocalHit(Entity entity, CallbackInfo ci) {
		var self = (Player) (Object) this;
		if (!(self instanceof LocalPlayer)) return;
		
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get() || !config.shouldColorOnlyOnOwnHit.get()) return;
		
		HitTracker.registerLocalHit(entity.getId());
	}
}
