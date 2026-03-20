package me.catsflex.hitcolorplus.util;

import com.mojang.logging.LogUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.slf4j.Logger;

import java.awt.*;

public class Helper {
	
	public static final String MOD_ID = "hitcolorplus";
	
	private static final ModContainer MOD = FabricLoader
		.getInstance()
		.getModContainer(MOD_ID)
		.orElseThrow();
	
	public static final String MOD_NAME = MOD.getMetadata().getName();
	public static final String MOD_PREFIX = String.format("[%s]", MOD_NAME);
	
	public static final Logger LOGGER = LogUtils.getLogger();
	public static final String CONFIG_NAME = MOD_ID + ".json";
	public static final String YACL_MOD_ID = "yet_another_config_lib_v3";
	
	// Non-converted colors
	public static final Color VANILLA_OVERLAY_COLOR = new Color(0xFF, 0x00, 0x00, 0x4D);
	
	public static int colorToMinecraftARGB(Color color) {
		
		// Alpha is inverted for some reason??
		return
			(0xFF - color.getAlpha()) << 24 |
			color.getRed() << 16 |
			color.getGreen() << 8 |
			color.getBlue();
	}
}
