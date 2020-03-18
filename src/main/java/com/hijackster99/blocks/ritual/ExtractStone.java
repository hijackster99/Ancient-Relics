package com.hijackster99.blocks.ritual;

import com.hijackster99.tileentities.TileEntityExtractStone;
import com.hijackster99.blocks.ARBlocks;
import com.hijackster99.core.IInteractable;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class ExtractStone extends ARBlocks {

	
	public ExtractStone(String registryName, Material materialIn, float hardnessIn, float resistanceIn, ToolType harvestTool, int miningLevel) {
		super(registryName, materialIn, hardnessIn, resistanceIn, harvestTool, miningLevel);
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityExtractStone();
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if(worldIn.getTileEntity(pos) instanceof IInteractable) {
			return ((IInteractable) worldIn.getTileEntity(pos)).onBlockActivated(state, worldIn, pos, player, handIn, hit);
		}
		return false;
	}

}
