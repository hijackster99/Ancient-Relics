package com.hijackster99.items;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class VoidCoal extends ARItems {

	public VoidCoal(String registryName, int maxStack, ItemGroup tab) {
		super(registryName, maxStack, tab);
	}
	
	@Override
	public int getBurnTime(ItemStack itemStack) {
		return 2400;
	}

}
