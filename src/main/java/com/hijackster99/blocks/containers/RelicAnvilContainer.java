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

public class RelicAnvilContainer extends Container {
	
	@ObjectHolder(References.MODID + ":anvil")
	public static ContainerType<RelicAnvilContainer> ctAnvil;
	
	private ItemStackHandler inv;
	private int slots = 0;

	public RelicAnvilContainer(int windowId, PlayerInventory inv, ItemStackHandler inventory) {
		super(ctAnvil, windowId);
		this.inv = inventory;
		
		addSlot(new SlotItemHandler(this.inv, 0, 27, 31));
		addSlot(new SlotItemHandler(this.inv, 1, 76, 31));
		addSlot(new SlotItemHandler(this.inv, 2, 134, 31));
		slots++;
		addPlayerSlots(inv, 8, 84);
	}
	
	public RelicAnvilContainer(int windowId, PlayerInventory inv) {
		this(windowId, inv, new ItemStackHandler(3));
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
		System.out.println(1);
		ItemStack stack1 = this.getSlot(index).getStack();
		System.out.println(2);
		if(index > 11) {
			System.out.println(3);
			for(int i = 0; i < 2; i++) {
				if(mergeStacks(playerIn, stack1, index, i)) return ItemStack.EMPTY;
			}
			System.out.println(4);
			for(int i = 11; i > 2; i--) {
				if(mergeStacks(playerIn, stack1, index, i)) return ItemStack.EMPTY;
			}
			System.out.println(5);
		}else if(index > 2) {
			System.out.println(6);
			for(int i = 0; i < 2; i++) {
				if(mergeStacks(playerIn, stack1, index, i)) return ItemStack.EMPTY;
			}
			System.out.println(7);
			for(int i = 12; i < slots; i++) {
				if(mergeStacks(playerIn, stack1, index, i)) return ItemStack.EMPTY;
			}
			System.out.println(8);
		}else {
			System.out.println(9);
			for(int i = 12; i < slots; i++) {
				if(mergeStacks(playerIn, stack1, index, i)) return ItemStack.EMPTY;
			}
			System.out.println(10);
			for(int i = 11; i > 2; i--) {
				if(mergeStacks(playerIn, stack1, index, i)) return ItemStack.EMPTY;
			}
			System.out.println(11);
		}
		return ItemStack.EMPTY;
	}
	
	public boolean mergeStacks(PlayerEntity playerIn, ItemStack stack, int index1, int index2) {
		ItemStack stack2 = this.getSlot(index2).getStack();
		if(stack2.isEmpty()) {
			setItemInSlot(playerIn, stack.copy(), index2);
			setItemInSlot(playerIn, ItemStack.EMPTY, index1);
			return true;
		}else if(ItemStack.areItemsEqual(stack, stack2) && ItemStack.areItemStackTagsEqual(stack, stack2)) {
			if(stack2.getCount() < stack2.getMaxStackSize()) {
				if(stack.getCount() + stack2.getCount() <= stack2.getMaxStackSize()) {
					stack2.setCount(stack.getCount() + stack2.getCount());
					setItemInSlot(playerIn, stack2, index2);
					setItemInSlot(playerIn, ItemStack.EMPTY, index1);
					return true;
				}else {
					int diff = stack2.getMaxStackSize() - stack2.getCount();
					stack2.setCount(stack2.getMaxStackSize());
					setItemInSlot(playerIn, stack2, index2);
					stack.setCount(stack.getCount() - diff);
					setItemInSlot(playerIn, stack, index1);
					transferStackInSlot(playerIn, index1);
					return true;
				}
			}
		}
		return false;
	}
	
	public void setItemInSlot(PlayerEntity playerIn, ItemStack stack, int slot) {
		if(slot > 2) {
			playerIn.inventory.setInventorySlotContents(slot - 3, stack);
		}else {
			inv.setStackInSlot(slot, stack);
		}
	}

}
