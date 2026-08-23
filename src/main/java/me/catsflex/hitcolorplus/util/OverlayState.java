package me.catsflex.hitcolorplus.util;

public final class OverlayState {
	private static final ThreadLocal<Boolean> HAS_OVERLAY = ThreadLocal.withInitial(() -> false);
	
	private OverlayState() {}
	
	public static boolean get() {
		return HAS_OVERLAY.get();
	}
	
	public static void set(boolean value) {
		HAS_OVERLAY.set(value);
	}
	
	public static void clear() {
		HAS_OVERLAY.remove();
	}
}
