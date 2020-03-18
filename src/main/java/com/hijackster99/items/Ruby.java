package com.hijackster99.items;

import com.hijackster99.blocks.ARBlocks;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class Ruby extends ARItems {

	public Ruby(String registryName, int maxStack, ItemGroup tab) {
		super(registryName, maxStack, tab);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if(context.getWorld().getBlockState(context.getPos()).getBlock().equals(Blocks.BEDROCK)) {
			context.getWorld().setBlockState(context.getPos().add(context.getFace().getDirectionVec()), ARBlocks.RUBY.getState(context.getFace()));
			return ActionResultType.SUCCESS;
		}
		return super.onItemUse(context);
	}

}
