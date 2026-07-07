package me.catsflex.hitcolorplus.config.gui;

import dev.isxander.yacl3.api.YetAnotherConfigLib;
import me.catsflex.hitcolorplus.config.ModConfig;
import net.minecraft.client.gui.screens.Screen;

public final class YACLIntegration {
	private YACLIntegration() {}
	
	public static Screen createScreen(Screen parent) {
		final var config = ModConfig.getInstance();
		
		final var shouldOverrideEntityColorOption = YACLHelper.tickBoxOption(config.shouldOverrideEntityColor);
		final var entityHitColorOption = YACLHelper.colorPickerOption(config.entityHitColor, true);
		YACLHelper.dependAvailabilityOn(entityHitColorOption, shouldOverrideEntityColorOption);
		
		final var shouldOverrideArmorColorOption = YACLHelper.tickBoxOption(config.shouldOverrideArmorColor);
		final var armorHitColorOption = YACLHelper.colorPickerOption(config.armorHitColor, true);
		YACLHelper.dependAvailabilityOn(armorHitColorOption, shouldOverrideArmorColorOption);
		
		return YetAnotherConfigLib.createBuilder().title(YACLHelper.createTitle())
			
			.category(YACLHelper.createCategory("general")
				
				.group(YACLHelper.createGroup("main")
					.option(YACLHelper.tickBoxOption(config.isEnabled))
					.option(YACLHelper.colorPickerOption(config.globalHitColor, true))
					.build())
				
				.group(YACLHelper.createGroup("entities")
					.option(YACLHelper.tickBoxOption(config.shouldColorEntities))
					.option(shouldOverrideEntityColorOption)
					.option(entityHitColorOption)
					.build())
				
				.group(YACLHelper.createGroup("armor")
					.option(YACLHelper.tickBoxOption(config.shouldColorArmor))
					.option(shouldOverrideArmorColorOption)
					.option(armorHitColorOption)
					.build())
				
				.build())
			
			.save(config::save)
			.build()
			.generateScreen(parent);
	}
}
