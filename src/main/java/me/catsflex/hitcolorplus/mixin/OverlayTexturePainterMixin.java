package me.catsflex.hitcolorplus.mixin;

import com.mojang.blaze3d.textures.GpuTextureView;
import me.catsflex.hitcolorplus.config.ModConfig;
import me.catsflex.hitcolorplus.config.option.BooleanOption;
import me.catsflex.hitcolorplus.config.option.ColorOption;
import me.catsflex.hitcolorplus.util.OverlayColorCache;
import me.catsflex.hitcolorplus.util.OverlayCoords;
import me.catsflex.hitcolorplus.util.OverlayTexturePainter;
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
public abstract class OverlayTexturePainterMixin {
	@Shadow @Final private DynamicTexture texture;
	
	// Cache the current color so we don't paint the same pixels the same color over and over again.
	@Unique private final OverlayColorCache cache = new OverlayColorCache();
	
	@Inject(method = "getTextureView", at = @At("HEAD"))
	private void updateHitColors(CallbackInfoReturnable<GpuTextureView> cir) {
		final var config = ModConfig.getInstance();
		
		final int entityColor = getColor(config.shouldOverrideEntityColor, config.entityHitColor);
		final int armorColor = getColor(config.shouldOverrideArmorColor, config.armorHitColor);
		if (cache.matches(entityColor, armorColor)) { return; }
		
		final var image = texture.getPixels();
		if (image == null) { return; }
		
		OverlayTexturePainter.fillOverlayRow(image, OverlayCoords.ENTITY_Y, entityColor);
		OverlayTexturePainter.fillOverlayRow(image, OverlayCoords.ARMOR_Y, armorColor);
		
		texture.upload();
		cache.update(entityColor, armorColor);
	}
	
	@Unique
	private static int getColor(BooleanOption shouldOverrideColor, ColorOption hitColor) {
		final var config = ModConfig.getInstance();
		if (!config.isEnabled.get()) { return OverlayTexturePainter.VANILLA_OVERLAY_COLOR.getRGB(); }
		
		return shouldOverrideColor.get()
			? hitColor.getAsInt()
			: config.globalHitColor.getAsInt();
	}
}
