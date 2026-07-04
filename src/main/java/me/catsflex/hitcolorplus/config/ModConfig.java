package me.catsflex.hitcolorplus.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.catsflex.hitcolorplus.Main;
import me.catsflex.hitcolorplus.config.option.BooleanOption;
import me.catsflex.hitcolorplus.config.option.ColorOption;
import me.catsflex.hitcolorplus.config.option.ConfigOption;
import me.catsflex.hitcolorplus.util.RenderUtil;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ModConfig {
	
	// Config saving stuff.
	private static final Gson _GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final String _CONFIG_NAME = Main.MOD_ID + ".json";
	private static final Path _CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(_CONFIG_NAME);
	private static final List<ConfigOption> _ALL_OPTIONS = new ArrayList<>();
	private static final ModConfig _instance = new ModConfig();
	
	public final BooleanOption isEnabled = new BooleanOption("isEnabled", true);
	public final ColorOption globalHitColor = new ColorOption("color", RenderUtil.VANILLA_OVERLAY_COLOR);
	public final BooleanOption shouldColorEntities = new BooleanOption("shouldColorEntities", true);
	public final BooleanOption shouldColorArmor = new BooleanOption("shouldColorArmor", false);
	
	private ModConfig() {}
	
	public static ModConfig getInstance() {
		return _instance;
	}
	
	public static void registerOption(ConfigOption option) {
		_ALL_OPTIONS.add(option);
	}
	
	public void load() {
		if (!Files.exists(_CONFIG_PATH)) {
			save();
			return;
		}
		
		try (var reader = Files.newBufferedReader(_CONFIG_PATH)) {
			var element = JsonParser.parseReader(reader);
			if (!element.isJsonObject()) {
				throw new IllegalStateException("Config root is not a JSON object!");
			}
			
			var json = element.getAsJsonObject();
			for (var option : _ALL_OPTIONS) {
				option.read(json);
			}
			
		} catch (Exception e) {
			Main.LOGGER.warn("Failed to load config, using defaults!", e);
			save();
		}
	}
	
	public void save() {
		var json = new JsonObject();
		
		for (var option : _ALL_OPTIONS) {
			option.write(json);
		}
		
		try (var writer = Files.newBufferedWriter(_CONFIG_PATH)) {
			_GSON.toJson(json, writer);
		} catch (Exception e) {
			Main.LOGGER.warn("Failed to save config!", e);
		}
	}
}
