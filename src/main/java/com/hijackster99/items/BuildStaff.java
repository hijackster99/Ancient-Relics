package com.hijackster99.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BuildStaff extends ARItems{

	public BuildStaff(String registryName, int maxStack, ItemGroup tab) {
		super(registryName, maxStack, tab);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if(playerIn.isSneaking()) {
			playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, new ItemStack(ARItems.INFO_STAFF, 1));
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

}
