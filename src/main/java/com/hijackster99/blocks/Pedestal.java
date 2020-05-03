package com.hijackster99.blocks;

import com.hijackster99.tileentities.TileEntityPedestal;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
		if(te instanceof TileEntityPedestal) {
			TileEntityPedestal ped = (TileEntityPedestal) te;
			if(player.isSneaking()) {
				if(player.inventory.getStackInSlot(player.inventory.currentItem).isEmpty()) {
					ItemStack stack = ped.getInventory().getStackInSlot(0);
					if(!stack.isEmpty()) {
						if(player.isCreative()) {
							ped.getInventory().setStackInSlot(0, ItemStack.EMPTY);
						}else if(player.addItemStackToInventory(stack))
							ped.getInventory().setStackInSlot(0, ItemStack.EMPTY);
					}
					return true;
				}else {
					return false;
				}
			}else {
				if(player.inventory.getStackInSlot(player.inventory.currentItem).isEmpty()) {
					player.openContainer(ped);
					return true;
				}else {
					if(ped.getInventory().getStackInSlot(0).isEmpty()) {
						if(player.isCreative()) {
							ItemStack stack1 = player.inventory.getStackInSlot(player.inventory.currentItem);
							if(!stack1.isEmpty()) {
								ItemStack stack2 = stack1.copy();
								stack2.setCount(1);
								ped.getInventory().setStackInSlot(0, stack2);
							}
						}else {
							ItemStack stack1 = player.inventory.getStackInSlot(player.inventory.currentItem);
							if(!stack1.isEmpty()) {
								ItemStack stack2 = stack1.copy();
								stack1.setCount(stack1.getCount() - 1);
								stack2.setCount(1);
								if(stack1.getCount() == 0) stack1 = ItemStack.EMPTY;
								player.inventory.setInventorySlotContents(player.inventory.currentItem, stack1);
								ped.getInventory().setStackInSlot(0, stack2);
							}
						}
					}
					return true;
				}
			}
		}
		return false;
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
