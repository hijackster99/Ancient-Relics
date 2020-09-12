package com.hijackster99.items;

import com.hijackster99.core.IVoid;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class InfoStaff extends ARItems {

	public InfoStaff(String registryName, int maxStack, ItemGroup tab) {
		super(registryName, maxStack, tab);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if(playerIn.isSneaking()) {
			playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, new ItemStack(ARItems.LINK_STAFF, 1));
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		if(!context.getWorld().isRemote() && context.getPlayer().getHeldItem(context.getHand()).getItem().equals(ARItems.INFO_STAFF)) {
			TileEntity te = context.getWorld().getTileEntity(context.getPos());
			if(te instanceof IVoid) {
				IVoid iv = (IVoid) te;
				context.getPlayer().sendMessage(new StringTextComponent("Void Energy: " + iv.getVoid() + "/" + iv.getCapacity()));
			}
		}
		return ActionResultType.SUCCESS;
	}

}
