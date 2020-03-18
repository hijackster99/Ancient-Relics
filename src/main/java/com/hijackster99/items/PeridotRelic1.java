package com.hijackster99.items;

import com.hijackster99.blocks.ARBlocks;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;

public class PeridotRelic1 extends ARItems{

	public PeridotRelic1(String registryName, int maxStack, ItemGroup tab) {
		super(registryName, maxStack, tab);
	}
	
	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		BlockPos placePos = context.getPos().add(context.getFace().getDirectionVec());
		if(context.getWorld().getBlockState(context.getPos()).getBlock() == ARBlocks.RITUAL_STONE) {
			context.getWorld().setBlockState(placePos, ARBlocks.PERIDOT_RELIC_1.getState(context.getFace()));
			context.getItem().setCount(context.getItem().getCount() - 1);
			return ActionResultType.SUCCESS;
		}
		return super.onItemUse(context);
	}
	
}
