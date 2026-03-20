package me.catsflex.hitcolorplus;

import me.catsflex.hitcolorplus.config.ModConfiguration;
import net.fabricmc.api.ClientModInitializer;

public class ModInitializer implements ClientModInitializer {
	
	@Override
	public void onInitializeClient() {
		ModConfiguration.load();
	}
}
