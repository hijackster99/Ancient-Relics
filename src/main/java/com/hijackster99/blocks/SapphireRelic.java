package com.hijackster99.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
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

public class SapphireRelic extends ARBlocks {

	static DirectionProperty property = DirectionProperty.create("facing", Direction.values());
	
	public SapphireRelic(String registryName, Material materialIn, float hardnessIn, float resistanceIn, ToolType harvestTool, int miningLevel) {
		super(registryName, materialIn, hardnessIn, resistanceIn, harvestTool, miningLevel);
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(property);
	}
	
	public BlockState getState(Direction dir) {
		return getDefaultState().with(property, dir);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
		if(!worldIn.getBlockState(pos.subtract(state.get(property).getDirectionVec())).getBlock().equals(Blocks.BEDROCK) && !worldIn.getBlockState(pos.subtract(state.get(property).getDirectionVec())).getBlock().equals(Blocks.STONE)) {
			worldIn.destroyBlock(pos, true);
		}
	}
	
	@Override
	public Direction[] getValidRotations(BlockState state, IBlockReader world, BlockPos pos) {
		Direction[] directions = new Direction[6];
		int count = 0;
		
		if(world.getBlockState(pos.add(Direction.UP.getDirectionVec())).isSolid()) {
			directions[count] = Direction.UP;
			count++;
		}
		if(world.getBlockState(pos.add(Direction.DOWN.getDirectionVec())).isSolid()) {
			directions[count] = Direction.DOWN;
			count++;
		}
		if(world.getBlockState(pos.add(Direction.NORTH.getDirectionVec())).isSolid()) {
			directions[count] = Direction.NORTH;
			count++;
		}
		if(world.getBlockState(pos.add(Direction.SOUTH.getDirectionVec())).isSolid()) {
			directions[count] = Direction.SOUTH;
			count++;
		}
		if(world.getBlockState(pos.add(Direction.EAST.getDirectionVec())).isSolid()) {
			directions[count] = Direction.EAST;
			count++;
		}
		if(world.getBlockState(pos.add(Direction.WEST.getDirectionVec())).isSolid()) {
			directions[count] = Direction.WEST;
			count++;
		}
		return directions;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		System.out.println(context.getFace());
		return getDefaultState().with(property, context.getFace());
	}
	
	@Override
	public boolean doesSideBlockRendering(BlockState state, IEnviromentBlockReader world, BlockPos pos, Direction face) {
		return false;
	}
	
	
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		if(state.get(property).equals(Direction.UP))
			return VoxelShapes.create(new AxisAlignedBB(0.25, 0, 0.25, 0.75, 0.0625, 0.75));
		else if(state.get(property).equals(Direction.DOWN))
			return VoxelShapes.create(new AxisAlignedBB(0.25, 0.9375, 0.25, 0.75, 1, 0.75));
		else if(state.get(property).equals(Direction.NORTH))
			return VoxelShapes.create(new AxisAlignedBB(0.25, 0.25, 0.9375, 0.75, 0.75, 1));
		else if(state.get(property).equals(Direction.SOUTH))
			return VoxelShapes.create(new AxisAlignedBB(0.25, 0.25, 0, 0.75, 0.75, 0.0625));
		else if(state.get(property).equals(Direction.EAST))
			return VoxelShapes.create(new AxisAlignedBB(0, 0.25, 0.25, 0.0625, 0.75, 0.75));
		else
			return VoxelShapes.create(new AxisAlignedBB(0.9375, 0.25, 0.25, 1, 0.75, 0.75));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		if(state.get(property).equals(Direction.UP))
			return VoxelShapes.create(new AxisAlignedBB(0.25, 0, 0.25, 0.75, 0.0625, 0.75));
		else if(state.get(property).equals(Direction.DOWN))
			return VoxelShapes.create(new AxisAlignedBB(0.25, 0.9375, 0.25, 0.75, 1, 0.75));
		else if(state.get(property).equals(Direction.NORTH))
			return VoxelShapes.create(new AxisAlignedBB(0.25, 0.25, 0.9375, 0.75, 0.75, 1));
		else if(state.get(property).equals(Direction.SOUTH))
			return VoxelShapes.create(new AxisAlignedBB(0.25, 0.25, 0, 0.75, 0.75, 0.0625));
		else if(state.get(property).equals(Direction.EAST))
			return VoxelShapes.create(new AxisAlignedBB(0, 0.25, 0.25, 0.0625, 0.75, 0.75));
		else
			return VoxelShapes.create(new AxisAlignedBB(0.9375, 0.25, 0.25, 1, 0.75, 0.75));
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		BlockPos stonePos = pos.subtract(state.get(property).getDirectionVec());
		if(player.getHeldItem(handIn) == null || player.getHeldItem(handIn).isEmpty() && player.isSneaking() && stonePos.getY() <= 2) {
			if(worldIn.getBlockState(stonePos).getBlock().equals(Blocks.STONE)) {
				worldIn.removeBlock(pos, false);
				worldIn.setBlockState(stonePos, ARBlocks.INFUSED_STONE.getDefaultState());
			}
			return true;
		}
		return false;
	}
	
	@Override
	public PushReaction getPushReaction(BlockState state) {
		return PushReaction.BLOCK;
	}

}
