package me.catsflex.hitcolorplus.mixin;

import com.mojang.blaze3d.textures.GpuTextureView;
import me.catsflex.hitcolorplus.config.ModConfiguration;
import me.catsflex.hitcolorplus.util.Helper;
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
public class EntityHitColor {
	
	@Shadow
	@Final
	private DynamicTexture texture;
	
	@Unique
	private int lastUsedColor = Helper.colorToMinecraftARGB(Helper.VANILLA_OVERLAY_COLOR);
	
	@Inject(method = "getTextureView", at = @At("HEAD"))
	private void changeHitColor(CallbackInfoReturnable<GpuTextureView> cir) {
		var config = ModConfiguration.getInstance();
		
		int convertedColor = Helper.colorToMinecraftARGB(config.isEnabled ? config.hitColor : Helper.VANILLA_OVERLAY_COLOR);
		if (convertedColor == lastUsedColor) return;
		
		var image = this.texture.getPixels();
		if (image == null) return;
		
		// God awful & counter-intuitive decision, requires you to actually dig through original code
		for (int y = 0; y < 8; ++y) {
			for (int x = 0; x < 16; ++x) {
				image.setPixel(x, y, convertedColor);
			}
		}
		
		texture.upload();
		lastUsedColor = convertedColor;
	}
}
