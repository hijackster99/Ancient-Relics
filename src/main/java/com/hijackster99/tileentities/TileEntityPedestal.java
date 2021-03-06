package com.hijackster99.tileentities;

import com.hijackster99.blocks.containers.PedestalContainer;
import com.hijackster99.core.ARPacketHandler;
import com.hijackster99.core.ItemMessage;
import com.hijackster99.core.References;
import com.hijackster99.core.util.PedestalRender;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityPedestal extends TileEntity implements INamedContainerProvider {
	
	@ObjectHolder(References.MODID + ":pedestal")
	public static TileEntityType<TileEntityRelay> tetPedestal;
	
	private final ItemStackHandler inventory = new ItemStackHandler() {
		
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);
			markDirty();
			if(world != null && !world.isRemote)
				ARPacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(PacketDistributor.TargetPoint.p(pos.getX(), pos.getY(), pos.getZ(), 64, world.getDimension().getType())), new ItemMessage(inventory.getStackInSlot(0), pos));
		}
		
	};
	
	public TileEntityPedestal() {
		super(tetPedestal);
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		inventory.deserializeNBT(compound);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound = inventory.serializeNBT();
		return super.write(compound);
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT tag = new CompoundNBT();
		CompoundNBT nbt = write(tag);
		return nbt;
	}
	
	@Override
	public void handleUpdateTag(CompoundNBT tag) {
		inventory.deserializeNBT(tag);
	}
	
	private PedestalRender pr = new PedestalRender();
	
	public PedestalRender getPedestalRender() {
		return pr;
	}

	public void checkEntity() {
		if(pr.getEntity().world == null)
			pr.getEntity().setWorld(getWorld());
		
		if(getWorld().isRemote) {
			if(getInventory().getStackInSlot(0) != null && !ItemStack.areItemStacksEqual(getInventory().getStackInSlot(0), pr.getEntity().getItem()))
				pr.getEntity().setItem(getInventory().getStackInSlot(0));
			}
	}
	
	public void updateEntity(float partialTicks) {
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
