package com.hijackster99.items;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class CheatCoal extends ARItems {

	public CheatCoal(String registryName, int maxStack, ItemGroup tab) {
		super(registryName, maxStack, tab);
	}

	@Override
	public int getBurnTime(ItemStack itemStack) {
		return 1000000;
	}

}
