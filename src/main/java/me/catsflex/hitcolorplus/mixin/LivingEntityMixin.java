package me.catsflex.hitcolorplus.mixin;

import me.catsflex.hitcolorplus.config.ModConfig;
import me.catsflex.hitcolorplus.util.HitTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	
	@Inject(method = "handleDamageEvent", at = @At("HEAD"))
	private void registerAnyDamage(DamageSource source, CallbackInfo ci) {
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get() || !config.shouldColorOnlyOnOwnHit.get()) return;
		
		if (source.getEntity() != Minecraft.getInstance().player) return;
		
		final var self = (LivingEntity) (Object) this;
		HitTracker.registerLocalHit(self.getId());
	}
}
