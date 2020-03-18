package com.hijackster99.items;

import com.hijackster99.blocks.ARBlocks;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;

public class Peridot extends ARItems {
	
	public Peridot(String registryName, int maxStack, ItemGroup tab) {
		super(registryName, maxStack, tab);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		BlockPos placePos = context.getPos().add(context.getFace().getDirectionVec());
		if(context.getWorld().getBlockState(context.getPos()).getBlock().equals(Blocks.BEDROCK) && context.getWorld().getBlockState(placePos).isAir(context.getWorld(), placePos)) {
			context.getWorld().setBlockState(placePos, ARBlocks.PERIDOT.getState(context.getFace()));
			context.getItem().setCount(context.getItem().getCount() - 1);
			return ActionResultType.SUCCESS;
		}
		return super.onItemUse(context);
	}

}
