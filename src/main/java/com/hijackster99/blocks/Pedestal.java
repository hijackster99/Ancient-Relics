package com.hijackster99.blocks;

import com.hijackster99.tileentities.TileEntityPedestal;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class Pedestal extends ARBlocks {

	public Pedestal(String registryName, Material materialIn, float hardnessIn, float resistanceIn, ToolType harvestTool, int miningLevel) {
		super(registryName, materialIn, hardnessIn, resistanceIn, harvestTool, miningLevel);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityPedestal();
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof INamedContainerProvider) {
			INamedContainerProvider ped = (INamedContainerProvider) te;
			player.openContainer(ped);
		}
		return true;
	}

	@Override
	public boolean doesSideBlockRendering(BlockState state, IEnviromentBlockReader world, BlockPos pos, Direction face) {
		return false;
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return VoxelShapes.create(new AxisAlignedBB(0.25, 0, 0.25, 0.875, 0.875, 0.875));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return VoxelShapes.create(new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.875, 0.875));
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		super.onBlockHarvested(worldIn, pos, state, player);
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityPedestal) {
			TileEntityPedestal ped = (TileEntityPedestal) te;
			ItemEntity entity = new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), ped.getInventory().getStackInSlot(0));
			worldIn.addEntity(entity);
		}
	}

}
