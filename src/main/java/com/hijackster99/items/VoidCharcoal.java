package com.hijackster99.items;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class VoidCharcoal extends ARItems{

	public VoidCharcoal(String name, int stacksize, ItemGroup group) {
		super(name, stacksize, group);
	}
	
	@Override
	public int getBurnTime(ItemStack itemStack) {
		return 2400;
	}
	
}
