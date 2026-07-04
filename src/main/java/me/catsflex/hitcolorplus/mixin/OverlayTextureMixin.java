package me.catsflex.hitcolorplus.mixin;

import com.mojang.blaze3d.textures.GpuTextureView;
import me.catsflex.hitcolorplus.config.ModConfig;
import me.catsflex.hitcolorplus.util.OverlayUtil;
import me.catsflex.hitcolorplus.util.RenderUtil;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OverlayTexture.class)
public abstract class OverlayTextureMixin {
	@Shadow @Final private DynamicTexture texture;
	
	@Unique private final ColorCache cache = new ColorCache();
	
	@Inject(method = "getTextureView", at = @At("HEAD"))
	private void updateHitColors(CallbackInfoReturnable<GpuTextureView> cir) {
		final int globalHitColor = getGlobalHitColor();
		if (cache.matches(globalHitColor)) return;
		
		final var image = texture.getPixels();
		if (image == null) return;
		
		RenderUtil.fillOverlayRow(image, OverlayUtil.ENTITY_Y, globalHitColor);
		RenderUtil.fillOverlayRow(image, OverlayUtil.ARMOR_Y, globalHitColor);
		
		texture.upload();
		cache.update(globalHitColor);
	}
	
	@Unique
	private int getGlobalHitColor() {
		final var config = ModConfig.getInstance();
		return config.isEnabled.get()
			? config.globalHitColor.get().getRGB()
			: RenderUtil.VANILLA_OVERLAY_COLOR.getRGB();
	}
	
	// Cache the current color so we don't paint the same pixels the same color over and over again.
	@Unique
	private static class ColorCache {
		
		// Default value. Set this to an impossible 32-bit integer value.
		private long color = Long.MAX_VALUE;
		
		private boolean matches(final int newColor) {
			return color == newColor;
		}
		
		private void update(final int newColor) {
			color = newColor;
		}
	}
}
