package com.hijackster99.core.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.hijackster99.core.References;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class RelicListConfig {

	private static List<String> defaultValues = Arrays.asList(new String[] {
		"HELMET-minecraft:leather_helmet",
		"HELMET-minecraft:iron_helmet",
		"HELMET-minecraft:chainmail_helmet",
		"HELMET-minecraft:golden_helmet",
		"HELMET-minecraft:diamond_helmet",
		"CHESTPLATE-minecraft:leather_chestplate",
		"CHESTPLATE-minecraft:iron_chestplate",
		"CHESTPLATE-minecraft:chainmail_chestplate",
		"CHESTPLATE-minecraft:golden_chestplate",
		"CHESTPLATE-minecraft:diamond_chestplate",
		"LEGGINGS-minecraft:leather_leggings",
		"LEGGINGS-minecraft:iron_leggings",
		"LEGGINGS-minecraft:chainmail_leggings",
		"LEGGINGS-minecraft:golden_leggings",
		"LEGGINGS-minecraft:diamond_leggings",
		"BOOTS-minecraft:leather_boots",
		"BOOTS-minecraft:iron_boots",
		"BOOTS-minecraft:chainmail_boots",
		"BOOTS-minecraft:golden_boots",
		"BOOTS-minecraft:diamond_boots",
		"TOOL-minecraft:wooden_hoe",
		"TOOL-minecraft:stone_hoe",
		"TOOL-minecraft:iron_hoe",
		"TOOL-minecraft:golden_hoe",
		"TOOL-minecraft:diamond_hoe",
		"MELEE-minecraft:wooden_pickaxe",
		"MELEE-minecraft:stone_pickaxe",
		"MELEE-minecraft:iron_pickaxe",
		"MELEE-minecraft:golden_pickaxe",
		"MELEE-minecraft:diamond_pickaxe",
		"MELEE-minecraft:wooden_axe",
		"MELEE-minecraft:stone_axe",
		"MELEE-minecraft:iron_axe",
		"MELEE-minecraft:golden_axe",
		"MELEE-minecraft:diamond_axe",
		"MELEE-minecraft:wooden_shovel",
		"MELEE-minecraft:stone_shovel",
		"MELEE-minecraft:iron_shovel",
		"MELEE-minecraft:golden_shovel",
		"MELEE-minecraft:diamond_shovel",
		"MELEE-minecraft:wooden_sword",
		"MELEE-minecraft:stone_sword",
		"MELEE-minecraft:iron_sword",
		"MELEE-minecraft:golden_sword",
		"MELEE-minecraft:diamond_sword",
		"RANGED-minecraft:bow",
		"TOOL-minecraft:fishing_rod",
		"SHIELD-minecraft:shield",
		"HELMET-minecraft:turtle_helmet"
	});

	
	enum EnumType{
		TOOL,
		HELMET,
		CHESTPLATE,
		LEGGINGS,
		BOOTS,
		MELEE,
		RANGED,
		MAGIC,
		SHIELD,
		BAUBLE
	};
	
	public static List<String> bakedList;
	public static Map<EnumType, String> bakedMap;
	
	final ForgeConfigSpec.ConfigValue<List<String>> configList;
	
	public static final ForgeConfigSpec SERVER_SPEC;
	static final RelicListConfig SERVER;
	static {
		{
			final Pair<RelicListConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(RelicListConfig::new);
			SERVER = specPair.getLeft();
			SERVER_SPEC = specPair.getRight();
		}
	}
	
	public static void bake(final ModConfig config) {
		bakedList = SERVER.configList.get();
	}
	
	RelicListConfig(final ForgeConfigSpec.Builder builder){
		builder.push("general");
		configList = builder
					 .comment("/****************************************", 
							  " *     This is a mapping of", 
							  " *     what items are allowed", 
							  " *     to have relics attached", 
							  " *     ", 
							  " *     This maps type to the", 
							  " *     unlocalized name of the", 
							  " *     item. format: type-unloc name", 
							  " *     Types are: TOOL, HELMET, ", 
							  " *     CHESTPLATE, LEGGINGS, ", 
							  " *     BOOTS, MELEE, RANGED, MAGIC, ", 
							  " *     SHIELD, and BAUBLE", 
							  " *     ALL ITEMS MUST BE EXPLICITELY", 
							  " *     LISTED HERE!", 
							  "****************************************/")
					 .translation(References.MODID + ".config.relic_list_config")
					 .define("configList", defaultValues);
		builder.pop();
		
	}
	
}
