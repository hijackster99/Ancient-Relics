package com.hijackster99.blocks;

import com.hijackster99.tileentities.TileEntityRelicAnvil;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class RelicAnvil extends ARBlocks {

	static DirectionProperty property = DirectionProperty.create("facing", Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
	
	public RelicAnvil(String registryName, Material materialIn, float hardnessIn, float resistanceIn, ToolType harvestTool, int miningLevel) {
		super(registryName, materialIn, hardnessIn, resistanceIn, harvestTool, miningLevel);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityRelicAnvil();
	}

	@Override
	public boolean doesSideBlockRendering(BlockState state, IEnviromentBlockReader world, BlockPos pos, Direction face) {
		return false;
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(property);
	}
	
	public BlockState getState(Direction dir) {
		return getDefaultState().with(property, dir);
	}
	
	@Override
	public Direction[] getValidRotations(BlockState state, IBlockReader world, BlockPos pos) {
		Direction[] directions = new Direction[] {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
		return directions;
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return getDefaultState().with(property, context.getPlacementHorizontalFacing().rotateY());
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		VoxelShape base1 = VoxelShapes.create(new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.25, 0.875));
		VoxelShape base2North = VoxelShapes.create(new AxisAlignedBB(0.25, 0.25, 0.1875, 0.75, 0.3125, 0.8125));
		VoxelShape base2East = VoxelShapes.create(new AxisAlignedBB(0.1875, 0.25, 0.25, 0.8125, 0.3125, 0.75));
		VoxelShape stemNorth = VoxelShapes.create(new AxisAlignedBB(0.375, 0.3125, 0.25, 0.625, 0.625, 0.75));
		VoxelShape stemEast = VoxelShapes.create(new AxisAlignedBB(0.25, 0.3125, 0.375, 0.75, 0.625, 0.625));
		VoxelShape headNorth = VoxelShapes.create(new AxisAlignedBB(0.1875, 0.625, 0, 0.8125, 1, 1));
		VoxelShape headEast = VoxelShapes.create(new AxisAlignedBB(0, 0.625, 0.1875, 1, 1, 0.8125));
		

		if(state.get(property).equals(Direction.NORTH) || state.get(property).equals(Direction.SOUTH)) {
			VoxelShape base = VoxelShapes.combine(base1, base2North, new IBooleanFunction() {
	
				@Override
				public boolean apply(boolean arg0, boolean arg1) {
					return arg0 || arg1;
				}
				
			});
			VoxelShape bottom = VoxelShapes.combine(base, stemNorth, new IBooleanFunction() {
	
				@Override
				public boolean apply(boolean arg0, boolean arg1) {
					return arg0 || arg1;
				}
				
			});
			return VoxelShapes.combine(bottom, headNorth, new IBooleanFunction() {

				@Override
				public boolean apply(boolean arg0, boolean arg1) {
					return arg0 || arg1;
				}
				
			});
		}else {
			VoxelShape base = VoxelShapes.combine(base1, base2East, new IBooleanFunction() {
				
				@Override
				public boolean apply(boolean arg0, boolean arg1) {
					return arg0 || arg1;
				}
				
			});
			VoxelShape bottom = VoxelShapes.combine(base, stemEast, new IBooleanFunction() {
	
				@Override
				public boolean apply(boolean arg0, boolean arg1) {
					return arg0 || arg1;
				}
				
			});
			return VoxelShapes.combine(bottom, headEast, new IBooleanFunction() {

				@Override
				public boolean apply(boolean arg0, boolean arg1) {
					return arg0 || arg1;
				}
				
			});
		}
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		VoxelShape base1 = VoxelShapes.create(new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.25, 0.875));
		VoxelShape base2North = VoxelShapes.create(new AxisAlignedBB(0.25, 0.25, 0.1875, 0.75, 0.3125, 0.8125));
		VoxelShape base2East = VoxelShapes.create(new AxisAlignedBB(0.1875, 0.25, 0.25, 0.8125, 0.3125, 0.75));
		VoxelShape stemNorth = VoxelShapes.create(new AxisAlignedBB(0.375, 0.3125, 0.25, 0.625, 0.625, 0.75));
		VoxelShape stemEast = VoxelShapes.create(new AxisAlignedBB(0.25, 0.3125, 0.375, 0.75, 0.625, 0.625));
		VoxelShape headNorth = VoxelShapes.create(new AxisAlignedBB(0.1875, 0.625, 0, 0.8125, 1, 1));
		VoxelShape headEast = VoxelShapes.create(new AxisAlignedBB(0, 0.625, 0.1875, 1, 1, 0.8125));
		

		if(state.get(property).equals(Direction.NORTH) || state.get(property).equals(Direction.SOUTH)) {
			VoxelShape base = VoxelShapes.combine(base1, base2North, new IBooleanFunction() {
	
				@Override
				public boolean apply(boolean arg0, boolean arg1) {
					return arg0 || arg1;
				}
				
			});
			VoxelShape bottom = VoxelShapes.combine(base, stemNorth, new IBooleanFunction() {
	
				@Override
				public boolean apply(boolean arg0, boolean arg1) {
					return arg0 || arg1;
				}
				
			});
			return VoxelShapes.combine(bottom, headNorth, new IBooleanFunction() {

				@Override
				public boolean apply(boolean arg0, boolean arg1) {
					return arg0 || arg1;
				}
				
			});
		}else {
			VoxelShape base = VoxelShapes.combine(base1, base2East, new IBooleanFunction() {
				
				@Override
				public boolean apply(boolean arg0, boolean arg1) {
					return arg0 || arg1;
				}
				
			});
			VoxelShape bottom = VoxelShapes.combine(base, stemEast, new IBooleanFunction() {
	
				@Override
				public boolean apply(boolean arg0, boolean arg1) {
					return arg0 || arg1;
				}
				
			});
			return VoxelShapes.combine(bottom, headEast, new IBooleanFunction() {

				@Override
				public boolean apply(boolean arg0, boolean arg1) {
					return arg0 || arg1;
				}
				
			});
		}
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityRelicAnvil) {
			TileEntityRelicAnvil anvil = (TileEntityRelicAnvil) te;
			player.openContainer(anvil);
			return true;
		}
		return false;
	}

}
