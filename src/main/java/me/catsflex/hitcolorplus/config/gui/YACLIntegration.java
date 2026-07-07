package me.catsflex.hitcolorplus.config.gui;

import dev.isxander.yacl3.api.YetAnotherConfigLib;
import me.catsflex.hitcolorplus.config.ModConfig;
import net.minecraft.client.gui.screens.Screen;

public final class YACLIntegration {
	private YACLIntegration() {}
	
	public static Screen createScreen(Screen parent) {
		final var config = ModConfig.getInstance();
		
		return YetAnotherConfigLib.createBuilder().title(YACLHelper.createTitle())
			
			.category(YACLHelper.createCategory("general")
				
				.group(YACLHelper.createGroup("main")
					.option(YACLHelper.tickBoxOption(config.isEnabled))
					.option(YACLHelper.colorPickerOption(config.globalHitColor, true))
					.build())
				
				.group(YACLHelper.createGroup("entity")
					.option(YACLHelper.tickBoxOption(config.shouldColorEntities))
					.build())
				
				.group(YACLHelper.createGroup("armor")
					.option(YACLHelper.tickBoxOption(config.shouldColorArmor))
					.build())
				
				.build())
			
			.save(config::save)
			.build()
			.generateScreen(parent);
	}
}
