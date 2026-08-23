package me.catsflex.hitcolorplus.util;

import net.minecraft.client.renderer.texture.OverlayTexture;

public final class OverlayCoords {
	private static final int NO_PROGRESS_X = OverlayTexture.NO_WHITE_U;
	private static final int OVERLAY_Y = OverlayTexture.RED_OVERLAY_V;
	
	public static final int ENTITY_Y = OVERLAY_Y;
	public static final int ARMOR_Y = OVERLAY_Y + 1;
	
	public static final int ENTITY_OVERLAY = OverlayTexture.pack(NO_PROGRESS_X, ENTITY_Y);
	public static final int ARMOR_OVERLAY = OverlayTexture.pack(NO_PROGRESS_X, ARMOR_Y);
	public static final int NO_OVERLAY = OverlayTexture.NO_OVERLAY;
	
	private OverlayCoords() {}
}
