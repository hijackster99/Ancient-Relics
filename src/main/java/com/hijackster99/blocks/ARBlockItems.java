package com.hijackster99.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ARBlockItems extends BlockItem{
	
	public ARBlockItems(Block block, int stackSize, ItemGroup tab) {
		super(block, new Item.Properties().maxStackSize(stackSize).group(tab));
		setRegistryName(block.getRegistryName());
	}

}
