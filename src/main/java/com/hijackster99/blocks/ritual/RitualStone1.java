package com.hijackster99.blocks.ritual;

import com.hijackster99.items.ARItems;
import com.hijackster99.blocks.ARBlocks;
import com.hijackster99.core.Ritual;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class RitualStone1 extends ARBlocks {

	public RitualStone1(String registryName, Material materialIn, float hardnessIn, float resistanceIn, ToolType harvestTool, int miningLevel) {
		super(registryName, materialIn, hardnessIn, resistanceIn, harvestTool, miningLevel);
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if(!worldIn.isRemote() && player.getHeldItem(handIn).getItem().equals(ARItems.BUILD_STAFF)) {
			boolean valid = true;
			for(BlockPos bp : Ritual.rituals.get("infuse_ritual").getRelicStones()) {
				if(!worldIn.getBlockState(pos.add(bp)).getBlock().equals(ARBlocks.INFUSED_STONE)) {
					valid = false;
					break;
				}
			}
			for(BlockPos bp : Ritual.rituals.get("infuse_ritual").getRitualStones()) {
				if(!worldIn.getBlockState(pos.add(bp)).getBlock().equals(ARBlocks.RITUAL_STONE)) {
					valid = false;
					break;
				}
			}
			if(valid) {
				worldIn.removeBlock(pos, false);
				worldIn.setBlockState(pos, ARBlocks.INFUSE_STONE.getDefaultState());
				return true;
			}
			valid = true;
			for(BlockPos bp : Ritual.rituals.get("storage_ritual").getRelicStones1()) {
				if(!worldIn.getBlockState(pos.add(bp)).getBlock().equals(ARBlocks.INFUSED_STONE_1)) {
					System.out.println(pos.add(bp) + ": " + worldIn.getBlockState(pos.add(bp)).getBlock());
					valid = false;
					break;
				}
			}
			for(BlockPos bp : Ritual.rituals.get("storage_ritual").getRitualStones()) {
				if(!worldIn.getBlockState(pos.add(bp)).getBlock().equals(ARBlocks.RITUAL_STONE)) {
					System.out.println(pos.add(bp) + ": " + worldIn.getBlockState(pos.add(bp)).getBlock());
					valid = false;
					break;
				}
			}
			if(valid) {
				worldIn.removeBlock(pos, false);
				worldIn.setBlockState(pos, ARBlocks.STORAGE_STONE.getDefaultState());
				return true;
			}
		}
		return false;
	}

}
