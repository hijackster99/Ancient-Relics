package com.hijackster99.items;

import com.hijackster99.core.IVoid;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LinkStaff extends ARItems {

	public LinkStaff(String registryName, int maxStack, ItemGroup tab) {
		super(registryName, maxStack, tab);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if(playerIn.isSneaking()) {
			if(playerIn.getHeldItem(handIn).hasTag() && playerIn.getHeldItem(handIn).getTag().contains("link")) 
				playerIn.getHeldItem(handIn).getTag().remove("link");
			else
				playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, new ItemStack(ARItems.BUILD_STAFF, 1));
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
		if(!context.isPlacerSneaking()) {
			if(context.getItem().hasTag() && context.getItem().getTag().contains("link")) {
				CompoundNBT tag = (CompoundNBT) context.getItem().getTag().get("link");
				if(tag.getInt("dim") == context.getPlayer().dimension.getId()) {
					TileEntity te = context.getWorld().getTileEntity(context.getPos());
					if(te instanceof IVoid) {
						IVoid iv = (IVoid) te;
						TileEntity te2 = context.getWorld().getTileEntity(new BlockPos(tag.getInt("coordX"), tag.getInt("coordY"), tag.getInt("coordZ")));
						if(te2 instanceof IVoid) {
							IVoid iv2 = (IVoid) te2;
							iv.addInput(iv2);
							iv2.addOutput(iv);
						}
						te.markDirty();
						te2.markDirty();
						return ActionResultType.SUCCESS;
					}
				}
			}else {
				TileEntity te = context.getWorld().getTileEntity(context.getPos());
				if(te instanceof IVoid) {
					if(!context.getItem().hasTag()) {
						context.getItem().setTag(new CompoundNBT());
					}
					CompoundNBT tag = new CompoundNBT();
					tag.putInt("dim", context.getPlayer().dimension.getId());
					tag.putInt("coordX", context.getPos().getX());
					tag.putInt("coordY", context.getPos().getY());
					tag.putInt("coordZ", context.getPos().getZ());
					context.getItem().getTag().put("link", tag);
					return ActionResultType.SUCCESS;
				}
			}
		}
		return super.onItemUseFirst(stack, context);
	}

}
