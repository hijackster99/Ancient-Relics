package com.hijackster99.tileentities;

import com.hijackster99.blocks.containers.RelicAnvilContainer;
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

public class TileEntityRelicAnvil extends TileEntity implements INamedContainerProvider {

	@ObjectHolder(References.MODID + ":anvil")
	public static TileEntityType<TileEntityRelay> tetAnvil;
	
	private final ItemStackHandler inventory = new ItemStackHandler(3) {
		
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);
			markDirty();
		}
		
	};
	
	public TileEntityRelicAnvil() {
		super(tetAnvil);
	}

	public ItemStackHandler getInventory() {
		return inventory;
	}

	@Override
	public Container createMenu(int id, PlayerInventory inv, PlayerEntity entity) {
		return new RelicAnvilContainer(id, inv, inventory);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new StringTextComponent("Relic Anvil");
	}

}
