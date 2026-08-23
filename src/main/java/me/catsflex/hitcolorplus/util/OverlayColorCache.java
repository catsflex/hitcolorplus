package me.catsflex.hitcolorplus.util;

public final class OverlayColorCache {
	
	// Default value. Set this to an impossible 32-bit integer value.
	private static final long NULL_COLOR = Long.MAX_VALUE;
	private long entityColor = NULL_COLOR;
	private long armorColor = NULL_COLOR;
	
	public boolean matches(final int newEntityColor, final int newArmorColor) {
		return entityColor == newEntityColor && armorColor == newArmorColor;
	}
	
	public void update(final int newEntityColor, final int newArmorColor) {
		entityColor = newEntityColor;
		armorColor = newArmorColor;
	}
}
