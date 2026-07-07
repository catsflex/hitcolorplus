package me.catsflex.hitcolorplus.util;

import com.mojang.blaze3d.platform.NativeImage;

import java.awt.*;

public final class RenderUtil {
	public static final Color VANILLA_OVERLAY_COLOR = new Color(0x4DFF0000, true);
	
	private RenderUtil() {}
	
	public static void fillOverlayRow(NativeImage image, int y, int hitColor) {
		final int width = image.getWidth();
		final int overlayColor = colorToOverlayARGB(hitColor);
		
		// Minecraft uses a 16x16 texture to apply color overlays to entities (like the red flash when taking damage).
		// The game typically picks a single pixel from the texture (for the standard red flash, it's the pixel at (0, 3)).
		//
		// However, we must fill the entire row instead of just that single pixel. 
		// This is because entities with an overlay progress (e.g., exploding creepers or spawning withers) 
		// shift their X-coordinate horizontally.
		// For example, if a creeper takes damage halfway through its explosion animation,
		// it will sample the pixel at (7, 3) (notice the same Y-coordinate being used).
		//
		// Filling the whole row ensures these entities don't sample the unpainted vanilla red pixels.
		for (int x = 0; x < width; ++x) {
			image.setPixel(x, y, overlayColor);
		}
	}
	
	private static int colorToOverlayARGB(int color) {
		// Minecraft overlay expects inverted alpha channel. 
		return color ^ 0xFF000000;
	}
}
