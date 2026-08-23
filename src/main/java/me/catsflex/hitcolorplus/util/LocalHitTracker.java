package me.catsflex.hitcolorplus.util;

import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import net.minecraft.client.Minecraft;

public final class LocalHitTracker {
	
	// Only local player hits are stored here.
	// Hits from other entities shall NOT be here.
	private static final Int2LongOpenHashMap LOCAL_HITS = new Int2LongOpenHashMap();
	private static final int VANILLA_HURT_TIME_TICKS = 10;
	private static final long NULL_HIT_TICK = -1L;
	
	private LocalHitTracker() {}
	
	public static void registerLocalHit(int entityId) {
		final var level = Minecraft.getInstance().level;
		if (level == null) { return; }
		
		LOCAL_HITS.put(entityId, level.getGameTime());
	}
	
	public static boolean isRecentLocalHit(int entityId) {
		final var level = Minecraft.getInstance().level;
		if (level == null) { return false; }
		
		final long lastHitTick = LOCAL_HITS.getOrDefault(entityId, NULL_HIT_TICK);
		if (lastHitTick == NULL_HIT_TICK) { return false; }
		
		return isWithinHurtDuration(level.getGameTime(), lastHitTick);
	}
	
	public static void tickCleanup() {
		if (LOCAL_HITS.isEmpty()) { return; }
		
		final var level = Minecraft.getInstance().level;
		if (level == null) { return; }
		
		LOCAL_HITS.int2LongEntrySet().removeIf(entry -> !isWithinHurtDuration(level.getGameTime(), entry.getLongValue()));
	}
	
	public static void clear() {
		LOCAL_HITS.clear();
	}
	
	private static boolean isWithinHurtDuration(long currentTick, long lastHitTick) {
		return (currentTick - lastHitTick) <= VANILLA_HURT_TIME_TICKS;
	}
}
