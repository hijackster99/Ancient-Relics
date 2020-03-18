package com.hijackster99.items;

import com.hijackster99.core.ARBase;
import com.hijackster99.core.References;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class ARItems extends Item {

	public ARItems(Properties properties, String registryName) {
		super(properties);
		setRegistryName(References.MODID, registryName);
		
	}
	
	public ARItems(String registryName, int maxStack, ItemGroup tab) {
		super(new Item.Properties().maxStackSize(maxStack).group(tab));
		setRegistryName(registryName);
	}
	
	public static ARItems RUBY = new Ruby("ruby", 64, ARBase.ARGroup);
	public static ARItems SAPPHIRE = new Sapphire("sapphire", 64, ARBase.ARGroup);
	public static ARItems PERIDOT = new Peridot("peridot", 64, ARBase.ARGroup);
	public static ARItems RUBY_RELIC = new RubyRelic("ruby_relic", 64, ARBase.ARGroup);
	public static ARItems SAPPHIRE_RELIC = new SapphireRelic("sapphire_relic", 64, ARBase.ARGroup);
	public static ARItems PERIDOT_RELIC = new PeridotRelic("peridot_relic", 64, ARBase.ARGroup);
	public static ARItems BUILD_STAFF = new BuildStaff("build_staff", 1, ARBase.ARGroup);
	public static ARItems INFO_STAFF = new InfoStaff("info_staff", 1, ARBase.ARGroup);
	public static ARItems LINK_STAFF = new LinkStaff("link_staff", 1, ARBase.ARGroup);
	public static ARItems RUBY_RELIC_1 = new ARItems("ruby_relic_1", 64, ARBase.ARGroup);
	public static ARItems SAPPHIRE_RELIC_1 = new ARItems("sapphire_relic_1", 64, ARBase.ARGroup);
	public static ARItems PERIDOT_RELIC_1 = new PeridotRelic1("peridot_relic_1", 64, ARBase.ARGroup);
	public static ARItems VOID_COAL = new VoidCoal("void_coal", 64, ARBase.ARGroup);
	public static ARItems VOID_CHARCOAL = new VoidCharcoal("void_charcoal", 64, ARBase.ARGroup);

}
