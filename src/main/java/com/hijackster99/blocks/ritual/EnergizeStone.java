package com.hijackster99.blocks.ritual;

import com.hijackster99.tileentities.TileEntityEnergizeStone;
import com.hijackster99.blocks.ARBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class EnergizeStone extends ARBlocks {

	public EnergizeStone(String registryName, Material materialIn, float hardnessIn, float resistanceIn, ToolType harvestTool, int miningLevel) {
		super(registryName, materialIn, hardnessIn, resistanceIn, harvestTool, miningLevel);
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityEnergizeStone();
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

}
