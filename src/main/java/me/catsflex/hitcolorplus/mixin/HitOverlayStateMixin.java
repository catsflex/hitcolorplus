package me.catsflex.hitcolorplus.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.catsflex.hitcolorplus.config.ModConfig;
import me.catsflex.hitcolorplus.util.HitOverlayState;
import me.catsflex.hitcolorplus.util.LocalHitTracker;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class HitOverlayStateMixin {
	
	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
		at = @At("TAIL")
	)
	private void filterLocalHits(LivingEntity entity, LivingEntityRenderState state, float partialTick, CallbackInfo ci) {
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get() || !config.shouldColorOnlyOnOwnHit.get()) { return; }
		
		if (!state.hasRedOverlay || LocalHitTracker.isRecentLocalHit(entity.getId())) { return; }
		
		// Disable the overlay if the entity wasn't damaged by local player.
		state.hasRedOverlay = false;
	}
	
	@Inject(
		method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
		at = @At("HEAD")
	)
	private void captureDamageState(LivingEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
		HitOverlayState.set(state.hasRedOverlay);
	}
	
	@Inject(
		method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
		at = @At("TAIL")
	)
	private void clearDamageState(LivingEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
		HitOverlayState.clear();
	}
}
