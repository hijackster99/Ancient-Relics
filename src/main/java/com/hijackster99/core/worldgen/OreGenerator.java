package com.hijackster99.core.worldgen;

import com.hijackster99.blocks.ARBlocks;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class OreGenerator {

	public static void generate(){
	
		System.out.println("Register World Gen");
		
		for(Biome b : ForgeRegistries.BIOMES) {
			addOreToBiome(b);
		}
	}
	
	public static void addOreToBiome(Biome b) {
		CountRangeConfig SAPPHIRE_ORE_CONFIG = new CountRangeConfig(4, 0, 0, 20);
		CountRangeConfig PERIDOT_ORE_CONFIG = new CountRangeConfig(4, 0, 0, 20);
		CountRangeConfig RUBY_ORE_CONFIG = new CountRangeConfig(4, 0, 0, 20);
		
		b.addFeature(Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(FillerBlockType.NATURAL_STONE, ARBlocks.ORE_SAPPHIRE.getDefaultState(), 4), Placement.COUNT_RANGE, SAPPHIRE_ORE_CONFIG));
		b.addFeature(Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(FillerBlockType.NATURAL_STONE, ARBlocks.ORE_PERIDOT.getDefaultState(), 4), Placement.COUNT_RANGE, PERIDOT_ORE_CONFIG));
		b.addFeature(Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(FillerBlockType.NATURAL_STONE, ARBlocks.ORE_RUBY.getDefaultState(), 4), Placement.COUNT_RANGE, RUBY_ORE_CONFIG));
	}
	
}
