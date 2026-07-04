package me.catsflex.hitcolorplus.mixin;

import me.catsflex.hitcolorplus.config.ModConfig;
import me.catsflex.hitcolorplus.util.OverlayUtil;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class EntityOverlayMixin {
	
	@Inject(method = "getOverlayCoords", at = @At("HEAD"), cancellable = true)
	private static void changeEntityOverlay(LivingEntityRenderState state, float overlayProgress, CallbackInfoReturnable<Integer> cir) {
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get()) return;
		
		final int entityOverlay = getEntityOverlay(config.shouldColorEntities.get());
		cir.setReturnValue(entityOverlay);
	}
	
	@Unique
	private static int getEntityOverlay(boolean shouldColorEntities) {
		return shouldColorEntities
			? OverlayUtil.ENTITY_OVERLAY
			: OverlayUtil.NO_OVERLAY;
	}
}
