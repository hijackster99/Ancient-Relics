package com.hijackster99.tileentities;

import com.hijackster99.blocks.containers.PedestalContainer;
import com.hijackster99.core.References;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityPedestal extends TileEntity implements INamedContainerProvider{
	
	@ObjectHolder(References.MODID + ":pedestal")
	public static TileEntityType<TileEntityRelay> tetPedestal;
	
	private final ItemStackHandler inventory = new ItemStackHandler() {
		
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);
			markDirty();
		}
		
	};

	public TileEntityPedestal() {
		super(tetPedestal);
	}

	public ItemStackHandler getInventory() {
		return inventory;
	}

	@Override
	public Container createMenu(int id, PlayerInventory inv, PlayerEntity entity) {
		return new PedestalContainer(id, inv, inventory);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new StringTextComponent("Pedestal");
	}

}
