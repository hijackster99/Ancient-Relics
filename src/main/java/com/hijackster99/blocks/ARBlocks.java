package com.hijackster99.blocks;

import com.hijackster99.blocks.ritual.EnergizeStone;
import com.hijackster99.blocks.ritual.ExtractStone;
import com.hijackster99.blocks.ritual.RitualStone;
import com.hijackster99.blocks.ritual.RitualStone1;
import com.hijackster99.core.ARBase;
import com.hijackster99.core.References;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class ARBlocks extends Block{

	int miningLevel;
	
	public ARBlocks(String registryName, Material materialIn, float hardnessIn, float resistanceIn, ToolType harvestTool, int miningLevel) {
		super(Block.Properties.create(materialIn).hardnessAndResistance(hardnessIn, resistanceIn).harvestTool(harvestTool));
		setRegistryName(References.MODID, registryName);
		this.miningLevel = miningLevel;
	}
	
	public static Ruby RUBY = new Ruby("placed_ruby", Material.ROCK, 0.2f, 5, null, -1);
	public static Peridot PERIDOT = new Peridot("placed_peridot", Material.ROCK, 0.2f, 5, null, -1);
	public static Sapphire SAPPHIRE = new Sapphire("placed_sapphire", Material.ROCK, 0.2f, 5, null, -1);
	public static RubyRelic RUBY_RELIC = new RubyRelic("placed_ruby_relic", Material.ROCK, 0.2f, 5, null, -1);
	public static PeridotRelic PERIDOT_RELIC = new PeridotRelic("placed_peridot_relic", Material.ROCK, 0.2f, 5, null, -1);
	public static SapphireRelic SAPPHIRE_RELIC = new SapphireRelic("placed_sapphire_relic", Material.ROCK, 0.2f, 5, null, -1);
	public static ARBlocks INFUSED_STONE = new ARBlocks("infused_stone", Material.ROCK, 4, 5, ToolType.PICKAXE, 0);
	public static RitualStone RITUAL_STONE = new RitualStone("ritual_stone", Material.ROCK, 4, 5, ToolType.PICKAXE, 0);
	public static ExtractStone EXTRACT_STONE = new ExtractStone("extract_stone", Material.ROCK, 4, 5, ToolType.PICKAXE, 0);
	public static Relay RELAY = new Relay("relay", Material.ROCK, 4, 5, ToolType.PICKAXE, 0);
	public static EnergizeStone ENERGIZE_STONE = new EnergizeStone("energize_stone", Material.ROCK, 4, 5, ToolType.PICKAXE, 0);
	public static PeridotRelic1 PERIDOT_RELIC_1 = new PeridotRelic1("placed_peridot_relic_1", Material.ROCK, 0.2f, 5, null, -1);
	public static RitualStone1 RITUAL_STONE_1 = new RitualStone1("ritual_stone_1", Material.ROCK, 4, 5, ToolType.PICKAXE, 0);
	public static RitualStone1 ORE_PERIDOT = new RitualStone1("peridot_ore", Material.ROCK, 4, 5, ToolType.PICKAXE, 2);
	public static RitualStone1 ORE_RUBY = new RitualStone1("ruby_ore", Material.ROCK, 4, 5, ToolType.PICKAXE, 2);
	public static RitualStone1 ORE_SAPPHIRE = new RitualStone1("sapphire_ore", Material.ROCK, 4, 5, ToolType.PICKAXE, 2);
	public static EnergizeStone INFUSE_STONE = new EnergizeStone("infuse_stone", Material.ROCK, 4, 5, ToolType.PICKAXE, 0);
	public static Pedestal PEDESTAL = new Pedestal("pedestal", Material.ROCK, 4, 5, ToolType.PICKAXE, 0);
	
	public static ARBlockItems RUBY_BLOCKITEM = new ARBlockItems(RUBY, 64, ARBase.ARGroup);
	public static ARBlockItems PERIDOT_BLOCKITEM = new ARBlockItems(PERIDOT, 64, ARBase.ARGroup);
	public static ARBlockItems SAPPHIRE_BLOCKITEM = new ARBlockItems(SAPPHIRE, 64, ARBase.ARGroup);
	public static ARBlockItems RUBY_RELIC_BLOCKITEM = new ARBlockItems(RUBY_RELIC, 64, ARBase.ARGroup);
	public static ARBlockItems PERIDOT_RELIC_BLOCKITEM = new ARBlockItems(PERIDOT_RELIC, 64, ARBase.ARGroup);
	public static ARBlockItems SAPPHIRE_RELIC_BLOCKITEM = new ARBlockItems(SAPPHIRE_RELIC, 64, ARBase.ARGroup);
	public static ARBlockItems INFUSED_STONE_BLOCKITEM = new ARBlockItems(INFUSED_STONE, 64, ARBase.ARGroup);
	public static ARBlockItems RITUAL_STONE_BLOCKITEM = new ARBlockItems(RITUAL_STONE, 64, ARBase.ARGroup);
	public static ARBlockItems EXTRACT_STONE_BLOCKITEM = new ARBlockItems(EXTRACT_STONE, 64, ARBase.ARGroup);
	public static ARBlockItems RELAY_BLOCKITEM = new ARBlockItems(RELAY, 64, ARBase.ARGroup);
	public static ARBlockItems ENERGIZE_STONE_BLOCKITEM = new ARBlockItems(ENERGIZE_STONE, 64, ARBase.ARGroup);
	public static ARBlockItems PERIDOT_RELIC_1_BLOCKITEM = new ARBlockItems(PERIDOT_RELIC_1, 64, ARBase.ARGroup);
	public static ARBlockItems RITUAL_STONE_1_BLOCKITEM = new ARBlockItems(RITUAL_STONE_1, 64, ARBase.ARGroup);
	public static ARBlockItems ORE_PERIDOT_BLOCKITEM = new ARBlockItems(ORE_PERIDOT, 64, ARBase.ARGroup);
	public static ARBlockItems ORE_RUBY_BLOCKITEM = new ARBlockItems(ORE_RUBY, 64, ARBase.ARGroup);
	public static ARBlockItems ORE_SAPPHIRE_BLOCKITEM = new ARBlockItems(ORE_SAPPHIRE, 64, ARBase.ARGroup);
	public static ARBlockItems INFUSE_STONE_BLOCKITEM = new ARBlockItems(INFUSE_STONE, 64, ARBase.ARGroup);
	public static ARBlockItems PEDESTAL_BLOCKITEM = new ARBlockItems(PEDESTAL, 64, ARBase.ARGroup);
	
	@Override
	public ResourceLocation getLootTable() {
		return new ResourceLocation(References.MODID, getRegistryName().getPath());
	}
	
	@Override
	public boolean canHarvestBlock(BlockState state, IBlockReader world, BlockPos pos, PlayerEntity player) {
		System.out.println(player.getHeldItemMainhand().getItem().getHarvestLevel(player.getHeldItemMainhand(), state.getHarvestTool(), player, state));
		if(state.getHarvestTool() != null) {
			if(player.getHeldItemMainhand().getItem().getHarvestLevel(player.getHeldItemMainhand(), state.getHarvestTool(), player, state) >= miningLevel)
				return true;
		}else
			return true;
		return false;
	}
	
}
