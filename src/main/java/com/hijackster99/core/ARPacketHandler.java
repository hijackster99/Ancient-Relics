package com.hijackster99.core;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ARPacketHandler {

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
	    new ResourceLocation(References.MODID, "main"),
	    () -> PROTOCOL_VERSION,
	    PROTOCOL_VERSION::equals,
	    PROTOCOL_VERSION::equals
	);
	
	public static void register(){
		
		int id = 0;
		
		INSTANCE.registerMessage(id++, ItemMessage.class, ItemMessage::encode, ItemMessage::decode, ItemMessage::handle);
		
	}
	
}
