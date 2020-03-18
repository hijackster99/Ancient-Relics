package com.hijackster99.blocks.containers;

import com.hijackster99.core.References;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ObjectHolder;

public class PedestalContainer extends Container{

	@ObjectHolder(References.MODID + ":pedestal")
	public static ContainerType<PedestalContainer> ctPedestal;
	
	private ItemStackHandler inv;
	
	public PedestalContainer(int windowId, PlayerInventory inv, ItemStackHandler inventory) {
		super(ctPedestal, windowId);
		this.inv = inventory;
		
		addSlot(new SlotItemHandler(this.inv, 0, 80, 35));
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
		for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                addSlot(new Slot(playerInventory, j + i * 9 + 10, x + j * 18, y + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            addSlot(new Slot(playerInventory, k + 1, x + k * 18, y + 58));
        }
	}

}
