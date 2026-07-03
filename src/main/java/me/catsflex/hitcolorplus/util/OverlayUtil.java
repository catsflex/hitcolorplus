package me.catsflex.hitcolorplus.util;

import net.minecraft.client.renderer.texture.OverlayTexture;

public abstract class OverlayUtil {
	private static final int PURE_COLOR_X = OverlayTexture.NO_WHITE_U;
	private static final int OVERLAY_Y = OverlayTexture.RED_OVERLAY_V;
	
	public static final int ENTITY_Y = OVERLAY_Y;
	public static final int ARMOR_Y = OVERLAY_Y + 1;
	public static final int ITEM_Y = OVERLAY_Y + 2;
	public static final int COSMETIC_Y = OVERLAY_Y + 3;
	
	public static final int ENTITY_OVERLAY = OverlayTexture.pack(PURE_COLOR_X, ENTITY_Y);
	public static final int ARMOR_OVERLAY = OverlayTexture.pack(PURE_COLOR_X, ARMOR_Y);
	public static final int ITEM_OVERLAY = OverlayTexture.pack(PURE_COLOR_X, ITEM_Y);
	public static final int COSMETIC_OVERLAY = OverlayTexture.pack(PURE_COLOR_X, COSMETIC_Y);
	public static final int NO_OVERLAY = OverlayTexture.NO_OVERLAY;
}
