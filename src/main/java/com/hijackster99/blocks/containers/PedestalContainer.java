package com.hijackster99.blocks.containers;

import com.hijackster99.core.References;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ObjectHolder;

public class PedestalContainer extends Container{

	@ObjectHolder(References.MODID + ":pedestal")
	public static ContainerType<PedestalContainer> ctPedestal;
	
	private ItemStackHandler inv;
	private int slots = 0;
	
	public PedestalContainer(int windowId, PlayerInventory inv, ItemStackHandler inventory) {
		super(ctPedestal, windowId);
		this.inv = inventory;
		
		addSlot(new SlotItemHandler(this.inv, 0, 80, 35));
		slots++;
		addPlayerSlots(inv, 8, 84);
	}
	public PedestalContainer(int windowId, PlayerInventory inv) {
		this(windowId, inv, new ItemStackHandler());
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return true;
	}
	
	private void addPlayerSlots(PlayerInventory playerInventory, int x, int y){
        for (int k = 0; k < 9; ++k)
        {
            addSlot(new Slot(playerInventory, k, x + k * 18, y + 58));
            slots++;
        }
		for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, x + j * 18, y + i * 18));
                slots++;
            }
        }
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack stack = this.getSlot(index).getStack();
		//ItemStack copy = stack.copy();
		if(index > 27){
			if(!stack.isEmpty()) {
				for(int i = 0; i <= 27; i++) {
					ItemStack stack2 = this.getSlot(i).getStack();
					if(stack2.isEmpty()) {
						if(i == 0) {
							stack2 = stack.copy();
							stack2.setCount(1);
							stack.setCount(stack.getCount() - 1);
							if(stack.getCount() == 0) stack = ItemStack.EMPTY;
							playerIn.inventory.setInventorySlotContents(index - 1, stack);
							this.inv.setStackInSlot(i, stack2);
							break;
						}else {
							stack2 = stack.copy();
							stack = ItemStack.EMPTY;
							playerIn.inventory.setInventorySlotContents(index - 1, stack);
							playerIn.inventory.setInventorySlotContents(i - 1, stack2);
							break;
						}
					}else if(i != 0 && ItemStack.areItemsEqual(stack, stack2) && ItemStack.areItemStackTagsEqual(stack, stack2) && stack2.getCount() < stack2.getMaxStackSize()) {
						if(stack2.getCount() + stack.getCount() > stack.getMaxStackSize()) {
							int over = stack.getMaxStackSize() - stack2.getCount();
							stack2.setCount(stack.getMaxStackSize());
							stack.setCount(stack.getCount() - over);
							transferStackInSlot(playerIn, index);
							playerIn.inventory.setInventorySlotContents(index - 1, stack);
							playerIn.inventory.setInventorySlotContents(i - 1, stack2);
							break;
						}else {
							stack2.setCount(stack2.getCount() + stack.getCount());
							stack = ItemStack.EMPTY;
							playerIn.inventory.setInventorySlotContents(index - 1, stack);
							playerIn.inventory.setInventorySlotContents(i - 1, stack2);
							break;
						}
					}
				}
			}
		}else if(index > 0) {
			if(!stack.isEmpty()) {
				ItemStack stack2 = this.getSlot(0).getStack();
				if(stack2.isEmpty()) {
					stack2 = stack.copy();
					stack2.setCount(1);
					stack.setCount(stack.getCount() - 1);
					if(stack.getCount() == 0) stack = ItemStack.EMPTY;
					playerIn.inventory.setInventorySlotContents(index - 1, stack);
					this.inv.setStackInSlot(0, stack2);
				}else {
					for(int i = slots - 1; i > 27; i--) {
						stack2 = this.getSlot(i).getStack();
						if(stack2.isEmpty()) {
							stack2 = stack.copy();
							stack = ItemStack.EMPTY;
							playerIn.inventory.setInventorySlotContents(index - 1, stack);
							playerIn.inventory.setInventorySlotContents(i - 1, stack2);
							break;
						}else if(ItemStack.areItemsEqual(stack, stack2) && ItemStack.areItemStackTagsEqual(stack, stack2) && stack2.getCount() < stack2.getMaxStackSize()) {
							if(stack2.getCount() + stack.getCount() > stack.getMaxStackSize()) {
								int over = stack.getMaxStackSize() - stack2.getCount();
								stack2.setCount(stack.getMaxStackSize());
								stack.setCount(stack.getCount() - over);
								playerIn.inventory.setInventorySlotContents(index - 1, stack);
								playerIn.inventory.setInventorySlotContents(i - 1, stack2);
								transferStackInSlot(playerIn, index);
								break;
							}else {
								stack2.setCount(stack2.getCount() + stack.getCount());
								stack = ItemStack.EMPTY;
								playerIn.inventory.setInventorySlotContents(index - 1, stack);
								playerIn.inventory.setInventorySlotContents(i - 1, stack2);
								break;
							}
						}
					}
				}
			}
		}else {
			if(!stack.isEmpty()) {
				for(int i = slots - 1; i > 0; i--) {
					ItemStack stack2 = this.getSlot(i).getStack();
					if(stack2.isEmpty()) {
						stack2 = stack.copy();
						stack = ItemStack.EMPTY;
						this.inv.setStackInSlot(index, stack);
						playerIn.inventory.setInventorySlotContents(i - 1, stack2);
						break;
					}else if(ItemStack.areItemsEqual(stack, stack2) && ItemStack.areItemStackTagsEqual(stack, stack2) && stack2.getCount() < stack2.getMaxStackSize()) {
						stack2.setCount(stack2.getCount() + 1);
						stack = ItemStack.EMPTY;
						this.inv.setStackInSlot(index, stack);
						playerIn.inventory.setInventorySlotContents(i - 1, stack2);
						break;
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}

}
