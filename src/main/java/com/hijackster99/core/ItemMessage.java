package com.hijackster99.core;

import java.util.function.Supplier;

import com.hijackster99.tileentities.TileEntityPedestal;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class ItemMessage {

	private ItemStack item;
	private BlockPos pos;
	
	public ItemMessage(ItemStack item, BlockPos pos) {
		this.item = item;
		this.pos = pos;
	}
	
	public static void encode(ItemMessage pkt, PacketBuffer buf) {
		buf.writeItemStack(pkt.item);
		buf.writeBlockPos(pkt.pos);
	}

	public static ItemMessage decode(PacketBuffer buf) {
		ItemStack item = buf.readItemStack();
		BlockPos pos = buf.readBlockPos();
		return new ItemMessage(item, pos);
	}

	public static void handle(final ItemMessage pkt, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			if(Minecraft.getInstance().world.isBlockLoaded(pkt.pos)) {
				TileEntity te = Minecraft.getInstance().world.getTileEntity(pkt.pos);
				if(te instanceof TileEntityPedestal) {
					TileEntityPedestal ped = (TileEntityPedestal) te;
					ped.getInventory().setStackInSlot(0, pkt.item);
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}
	
}
