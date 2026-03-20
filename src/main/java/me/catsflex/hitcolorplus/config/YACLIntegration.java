package me.catsflex.hitcolorplus.config;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class YACLIntegration {
	public static Screen createScreen(Screen parent) {
		var config = ModConfiguration.getInstance();
		
		return YetAnotherConfigLib.createBuilder()
			.title(Component.translatable("config.hitcolorplus.title"))
			
			// 'General' category
			.category(ConfigCategory.createBuilder().name(Component.translatable("config.hitcolorplus.category.general"))
				
				// 'Enabled' option
				.option(Option.<Boolean>createBuilder()
					.name(Component.translatable("config.hitcolorplus.option.enabled.name"))
					.description(OptionDescription.of(Component.translatable("config.hitcolorplus.option.enabled.description")))
					.binding(
						ModConfiguration.DEF_IS_ENABLED,
						() -> config.isEnabled,
						v -> config.isEnabled = v)
					.controller(TickBoxControllerBuilder::create)
					.build())
				
				// 'Color armor' option
				.option(Option.<Boolean>createBuilder()
					.name(Component.translatable("config.hitcolorplus.option.color_armor.name"))
					.description(OptionDescription.of(Component.translatable("config.hitcolorplus.option.color_armor.description")))
					.binding(
						ModConfiguration.DEF_SHOULD_COLOR_ARMOR,
						() -> config.shouldColorArmor,
						v -> config.shouldColorArmor = v)
					.controller(TickBoxControllerBuilder::create)
					.build())
				
				// 'Hit color' option
				.option(Option.<Color>createBuilder()
					.name(Component.translatable("config.hitcolorplus.option.hit_color.name"))
					.description(OptionDescription.of(Component.translatable("config.hitcolorplus.option.hit_color.description")))
					.binding(
						ModConfiguration.DEF_HIT_COLOR,
						() -> config.hitColor,
						v -> config.hitColor = v)
					.controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
					.build())
				
				.build())
			.save(config::save)
			.build()
			.generateScreen(parent);
	}
}
