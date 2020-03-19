package com.hijackster99.core;

import com.hijackster99.blocks.ARBlocks;
import com.hijackster99.blocks.containers.PedestalContainer;
import com.hijackster99.blocks.containers.PedestalScreen;
import com.hijackster99.core.recipes.EnergizeRecipeSerializer;
import com.hijackster99.core.recipes.EnergizeRecipes;
import com.hijackster99.core.worldgen.OreGenerator;
import com.hijackster99.items.ARItems;
import com.hijackster99.tileentities.TileEntityEnergizeStone;
import com.hijackster99.tileentities.TileEntityExtractStone;
import com.hijackster99.tileentities.TileEntityInfuseStone;
import com.hijackster99.tileentities.TileEntityPedestal;
import com.hijackster99.tileentities.TileEntityRelay;
import com.hijackster99.tileentities.renderer.PedestalTER;

import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(References.MODID)
@Mod.EventBusSubscriber(modid = References.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ARBase {
	
	public static ItemGroup ARGroup = new ItemGroup("ancientrelics") {

		@Override
		public ItemStack createIcon() {
			return new ItemStack(ARItems.RUBY_RELIC_1);
		}
		
	};
	
	public ARBase() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void commonSetup(FMLCommonSetupEvent event) {
		OreGenerator.generate();
		Ritual.register();
		ARPacketHandler.register();
	}
	
	private void clientSetup(FMLClientSetupEvent event) {
		ScreenManager.registerFactory(PedestalContainer.ctPedestal, PedestalScreen::new);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPedestal.class, new PedestalTER());
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(ARItems.RUBY,
										ARItems.SAPPHIRE,
										ARItems.PERIDOT,
										ARItems.RUBY_RELIC,
										ARItems.SAPPHIRE_RELIC,
										ARItems.PERIDOT_RELIC,
										ARItems.BUILD_STAFF,
										ARItems.INFO_STAFF,
										ARItems.LINK_STAFF,
										ARItems.RUBY_RELIC_1,
										ARItems.PERIDOT_RELIC_1,
										ARItems.SAPPHIRE_RELIC_1,
										ARItems.VOID_CHARCOAL,
										ARItems.VOID_COAL);
		
		event.getRegistry().registerAll(ARBlocks.PERIDOT_BLOCKITEM,
										ARBlocks.RUBY_BLOCKITEM,
										ARBlocks.SAPPHIRE_BLOCKITEM,
										ARBlocks.PERIDOT_RELIC_BLOCKITEM,
										ARBlocks.RUBY_RELIC_BLOCKITEM,
										ARBlocks.SAPPHIRE_RELIC_BLOCKITEM,
										ARBlocks.INFUSED_STONE_BLOCKITEM,
										ARBlocks.RITUAL_STONE_BLOCKITEM,
										ARBlocks.EXTRACT_STONE_BLOCKITEM,
										ARBlocks.RELAY_BLOCKITEM,
										ARBlocks.ENERGIZE_STONE_BLOCKITEM,
										ARBlocks.PERIDOT_RELIC_1_BLOCKITEM,
										ARBlocks.ORE_PERIDOT_BLOCKITEM,
										ARBlocks.ORE_RUBY_BLOCKITEM,
										ARBlocks.ORE_SAPPHIRE_BLOCKITEM,
										ARBlocks.RITUAL_STONE_1_BLOCKITEM,
										ARBlocks.INFUSE_STONE_BLOCKITEM,
										ARBlocks.PEDESTAL_BLOCKITEM);
										
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(ARBlocks.RUBY,
										ARBlocks.PERIDOT,
										ARBlocks.SAPPHIRE,
										ARBlocks.PERIDOT_RELIC,
										ARBlocks.RUBY_RELIC,
										ARBlocks.SAPPHIRE_RELIC,
										ARBlocks.INFUSED_STONE,
										ARBlocks.RITUAL_STONE,
										ARBlocks.EXTRACT_STONE,
										ARBlocks.RELAY,
										ARBlocks.ENERGIZE_STONE,
										ARBlocks.PERIDOT_RELIC_1,
										ARBlocks.ORE_PERIDOT,
										ARBlocks.ORE_RUBY,
										ARBlocks.ORE_SAPPHIRE,
										ARBlocks.RITUAL_STONE_1,
										ARBlocks.INFUSE_STONE,
										ARBlocks.PEDESTAL);
	}
	
	@SubscribeEvent
	public static void registerTE(RegistryEvent.Register<TileEntityType<?>> event) {
		TileEntityType<TileEntityExtractStone> extract = TileEntityType.Builder.create(TileEntityExtractStone::new, ARBlocks.EXTRACT_STONE).build(null);
		extract.setRegistryName(References.MODID, "extract_stone");
		event.getRegistry().register(extract);
		
		TileEntityType<TileEntityRelay> relay = TileEntityType.Builder.create(TileEntityRelay::new, ARBlocks.RELAY).build(null);
		relay.setRegistryName(References.MODID, "relay");
		event.getRegistry().register(relay);
		
		TileEntityType<TileEntityEnergizeStone> energize = TileEntityType.Builder.create(TileEntityEnergizeStone::new, ARBlocks.ENERGIZE_STONE).build(null);
		energize.setRegistryName(References.MODID, "energize_stone");
		event.getRegistry().register(energize);
		
		TileEntityType<TileEntityInfuseStone> infuse = TileEntityType.Builder.create(TileEntityInfuseStone::new, ARBlocks.INFUSE_STONE).build(null);
		infuse.setRegistryName(References.MODID, "infuse_stone");
		event.getRegistry().register(infuse);

		TileEntityType<TileEntityPedestal> pedestal = TileEntityType.Builder.create(TileEntityPedestal::new, ARBlocks.PEDESTAL).build(null);
		pedestal.setRegistryName(References.MODID, "pedestal");
		event.getRegistry().register(pedestal);
	}
	
	@SubscribeEvent
	public static void registerContainer(RegistryEvent.Register<ContainerType<?>> event) {
		ContainerType<PedestalContainer> pedestal = new ContainerType<PedestalContainer>(PedestalContainer::new);
		
		pedestal.setRegistryName(References.MODID, "pedestal");
		event.getRegistry().register(pedestal);
	}
	
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
		event.getRegistry().registerAll(new EnergizeRecipeSerializer<EnergizeRecipes>(EnergizeRecipes::new));
	}
	
}
