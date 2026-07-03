package me.catsflex.hitcolorplus.mixin;

import me.catsflex.hitcolorplus.config.ModConfig;
import me.catsflex.hitcolorplus.util.OverlayUtil;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
	
	@Inject(method = "getOverlayCoords", at = @At("HEAD"), cancellable = true)
	private static void disableEntityOverlay(LivingEntityRenderState state, float whiteOverlayProgress, CallbackInfoReturnable<Integer> cir) {
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get()) return;
		
		if (!config.shouldColorEntities.get()) {
			cir.setReturnValue(OverlayUtil.NO_OVERLAY);
		}
	}
}
