package me.catsflex.hitcolorplus.util;

public final class OverlayStateTracker {
	private static final ThreadLocal<Boolean> HAS_OVERLAY = ThreadLocal.withInitial(() -> false);
	
	public static boolean get() {
		return HAS_OVERLAY.get();
	}
	
	public static void set(boolean value) {
		HAS_OVERLAY.set(value);
	}
	
	private OverlayStateTracker() {}
}
