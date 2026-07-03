package me.catsflex.hitcolorplus.mixin;

import com.mojang.blaze3d.textures.GpuTextureView;
import me.catsflex.hitcolorplus.config.ModConfig;
import me.catsflex.hitcolorplus.util.OverlayUtil;
import me.catsflex.hitcolorplus.util.RenderUtil;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.util.Objects;

@Mixin(OverlayTexture.class)
public abstract class OverlayTextureMixin {
	@Shadow @Final private DynamicTexture texture;
	
	@Unique private final ColorCache cache = new ColorCache();
	
	@Inject(method = "getTextureView", at = @At("HEAD"))
	private void updateHitColors(CallbackInfoReturnable<GpuTextureView> cir) {
		final var currentColor = getOverlayColor();
		if (cache.matches(currentColor)) return;
		
		final var image = texture.getPixels();
		if (image == null) return;
		
		RenderUtil.fillOverlayRow(image, OverlayUtil.ENTITY_Y, currentColor);
		RenderUtil.fillOverlayRow(image, OverlayUtil.ARMOR_Y, currentColor);
		
		texture.upload();
		cache.update(currentColor);
	}
	
	@Unique
	private Color getOverlayColor() {
		final var config = ModConfig.getInstance();
		return config.isEnabled.get()
			? config.globalHitColor.get()
			: RenderUtil.VANILLA_RED_OVERLAY;
	}
	
	// Cache the current color so we don't paint the same pixels the same color over and over again.
	@Unique
	private static class ColorCache {
		@Nullable Color color = null;
		
		private boolean matches(final Color newColor) {
			return Objects.equals(color, newColor);
		}
		
		private void update(final Color newColor) {
			color = newColor;
		}
	}
}
