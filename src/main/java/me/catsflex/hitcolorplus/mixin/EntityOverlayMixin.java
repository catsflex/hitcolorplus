package me.catsflex.hitcolorplus.mixin;

import me.catsflex.hitcolorplus.config.ModConfig;
import me.catsflex.hitcolorplus.util.OverlayCoords;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class EntityOverlayMixin {
	
	@Inject(method = "getOverlayCoords", at = @At("HEAD"), cancellable = true)
	private static void removeEntityOverlay(LivingEntityRenderState state, float overlayProgress, CallbackInfoReturnable<Integer> cir) {
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get() || config.shouldColorEntities.get()) { return; }
		
		cir.setReturnValue(OverlayCoords.NO_OVERLAY);
	}
}
