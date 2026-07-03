package me.catsflex.hitcolorplus.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import me.catsflex.hitcolorplus.config.option.*;
import net.minecraft.client.gui.components.debug.DebugScreenEntryStatus;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class YACLIntegration {
	
	public static Screen createScreen(Screen parent) {
		final var config = ModConfig.getInstance();
		
		return YetAnotherConfigLib.createBuilder().title(createTitle())
			
			.category(createCategory("general")
				
				.group(createGroup("main")
					.option(createBooleanTickBoxOption(config.isEnabled))
					.option(createColorOption(config.globalHitColor, true))
					.build())
				
				.group(createGroup("entity")
					.option(createBooleanTickBoxOption(config.shouldColorEntities))
					.build())
				
				.group(createGroup("armor")
					.option(createBooleanTickBoxOption(config.shouldColorArmor))
					.build())
				
				.build())
			
			.save(config::save)
			.build()
			.generateScreen(parent);
	}
	
	private static Option<Integer> createIntegerSliderOption(IntegerOption option, int step) {
		final var key = ConfigKeyType.OPTION.buildKey(option.getKey());
		
		return Option.<Integer>createBuilder()
			.name(Component.translatable(key + ".name"))
			.description(OptionDescription.of(Component.translatable(key + ".description")))
			.binding(option.getDefault(), option::get, option::set)
			.controller(opt -> IntegerSliderControllerBuilder.create(opt)
				.range(option.getMin(), option.getMax())
				.step(step)
			)
			.build();
	}
	
	private static Option<Float> createFloatSliderOption(FloatOption option, float step) {
		final var key = ConfigKeyType.OPTION.buildKey(option.getKey());
		
		return Option.<Float>createBuilder()
			.name(Component.translatable(key + ".name"))
			.description(OptionDescription.of(Component.translatable(key + ".description")))
			.binding(option.getDefault(), option::get, option::set)
			.controller(opt -> FloatSliderControllerBuilder.create(opt)
				.range(option.getMin(), option.getMax())
				.step(step)
			)
			.build();
	}
	
	private static <T extends Enum<T>> Option<T> createEnumOption(EnumOption<T> option, Function<T, String> formatter) {
		final var key = ConfigKeyType.OPTION.buildKey(option.getKey());
		
		return Option.<T>createBuilder()
			.name(Component.translatable(key + ".name"))
			.description(OptionDescription.of(Component.translatable(key + ".description")))
			.binding(option.getDefault(), option::get, option::set)
			.controller(opt -> EnumControllerBuilder.create(opt)
				.enumClass(option.getEnumClass())
				.formatValue(val -> Component.translatable(key + "." + formatter.apply(val)))
			)
			.build();
	}
	
	private static Option<Color> createColorOption(ColorOption option, boolean hasAlpha) {
		final var key = ConfigKeyType.OPTION.buildKey(option.getKey());
		
		return Option.<Color>createBuilder()
			.name(Component.translatable(key + ".name"))
			.description(OptionDescription.of(Component.translatable(key + ".description")))
			.binding(option.getDefault(), option::get, option::set)
			.controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(hasAlpha))
			.build();
	}
	
	private static Option<Boolean> createBooleanTickBoxOption(BooleanOption option) {
		final var key = ConfigKeyType.OPTION.buildKey(option.getKey());
		
		return Option.<Boolean>createBuilder()
			.name(Component.translatable(key + ".name"))
			.description(OptionDescription.of(Component.translatable(key + ".description")))
			.binding(option.getDefault(), option::get, option::set)
			.controller(TickBoxControllerBuilder::create)
			.build();
	}
	
	private static Option<DebugScreenEntryStatus> createDebugOverlayOption(String relativeKey, DebugScreenEntryStatus defaultValue, Supplier<DebugScreenEntryStatus> getter, Consumer<DebugScreenEntryStatus> setter) {
		final var key = ConfigKeyType.DEBUG_OVERLAY_OPTION.buildKey(relativeKey);
		
		return Option.<DebugScreenEntryStatus>createBuilder()
			.name(Component.translatable(key + ".name"))
			.description(OptionDescription.of(Component.translatable(key + ".description")))
			.binding(defaultValue, getter, setter)
			.controller(opt -> EnumControllerBuilder.create(opt)
				.enumClass(DebugScreenEntryStatus.class)
				.formatValue(name -> Component.translatable(key + "." + name.getSerializedName()))
			)
			.build();
	}
	
	private static <T extends Enum<T>> Option<T> createVanillaEnumOption(String vanillaNameKey, String relativeKey, T defaultValue, Supplier<T> getter, Consumer<T> setter, Class<T> enumClass, ValueFormatter<T> valueFormatter) {
		final var descriptionKey = ConfigKeyType.VANILLA_OPTION.buildKey(relativeKey) + ".description";
		
		return Option.<T>createBuilder()
			.name(Component.translatable(vanillaNameKey))
			.description(OptionDescription.of(Component.translatable(descriptionKey)))
			.binding(defaultValue, getter, setter)
			.controller(opt -> EnumControllerBuilder.create(opt)
				.enumClass(enumClass)
				.formatValue(valueFormatter)
			)
			.build();
	}
	
	private static OptionGroup.Builder createGroup(String groupRelativeKey) {
		return OptionGroup.createBuilder().name(Component.translatable(ConfigKeyType.GROUP.buildKey(groupRelativeKey)));
	}
	
	private static ConfigCategory.Builder createCategory(String categoryRelativeKey) {
		return ConfigCategory.createBuilder().name(Component.translatable(ConfigKeyType.CATEGORY.buildKey(categoryRelativeKey)));
	}
	
	private static Component createTitle() {
		return Component.translatable(ConfigKeyType.getTitleKey());
	}
}
