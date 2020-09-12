package com.hijackster99.core;

import java.util.ArrayList;
import java.util.HashMap;

import com.hijackster99.tileentities.TileEntityExtractStone;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;

public class Ritual {
	
	public static HashMap<String, Ritual> rituals = new HashMap<String, Ritual>();
	
	@ObjectHolder(References.MODID + ":extract_stone")
	public static TileEntityType<TileEntityExtractStone> tetExtractStone;
	@ObjectHolder(References.MODID + ":energize_stone")
	public static TileEntityType<TileEntityExtractStone> tetEnergizeStone;
	@ObjectHolder(References.MODID + ":infuse_stone")
	public static TileEntityType<TileEntityExtractStone> tetInfuseStone;
	@ObjectHolder(References.MODID + ":storage_stone")
	public static TileEntityType<TileEntityExtractStone> tetStorageStone;
	
	private ArrayList<BlockPos> relicStones = new ArrayList<BlockPos>();
	private ArrayList<BlockPos> relicStones1 = new ArrayList<BlockPos>();
	private ArrayList<BlockPos> ritualStones = new ArrayList<BlockPos>();
	
	public Ritual(String name) {
		Ritual.rituals.put(name, this);
	}
	
	public void addRelicStones(BlockPos... relicStones) {
		for (BlockPos b : relicStones) {
			this.relicStones.add(b);
		}
	}
	
	public void addRelicStones(Integer... relicStones) {
		for(int i = 0; i < relicStones.length; i++) {
			BlockPos b = new BlockPos(relicStones[i], relicStones[i + 1], relicStones[i + 2]);
			i++; i++;
			this.relicStones.add(b);
		}
	}
	
	public void addRelicStones1(BlockPos... relicStones) {
		for (BlockPos b : relicStones) {
			this.relicStones1.add(b);
		}
	}
	
	public void addRelicStones1(Integer... relicStones) {
		for(int i = 0; i < relicStones.length; i++) {
			BlockPos b = new BlockPos(relicStones[i], relicStones[i + 1], relicStones[i + 2]);
			i++; i++;
			this.relicStones1.add(b);
		}
	}
	
	public void addRitualStones(BlockPos... ritualStones) {
		for (BlockPos b : ritualStones) {
			this.ritualStones.add(b);
		}
	}
	
	public void addRitualStones(int... ritualStones) {
		for(int i = 0; i < ritualStones.length; i++) {
			BlockPos b = new BlockPos(ritualStones[i], ritualStones[i + 1], ritualStones[i + 2]);
			i++; i++;
			this.ritualStones.add(b);
		}
	}

	public ArrayList<BlockPos> getRelicStones() {
		return relicStones;
	}

	public ArrayList<BlockPos> getRelicStones1() {
		return relicStones1;
	}

	public ArrayList<BlockPos> getRitualStones() {
		return ritualStones;
	}
	
	public static void register() {
		//Extract Ritual
		Ritual extractRitual = new Ritual("extract_ritual");
		extractRitual.addRelicStones(1, 1, 1, 1, 1, -1, -1, 1, 1, -1, 1, -1, 2, 2, 2, 2, 2, -2, -2, 2, 2, -2, 2, -2);
		
		//Energize Ritual
		Ritual energizeRitual = new Ritual("energize_ritual");
		energizeRitual.addRelicStones(1, 1, 1, 1, 1, -1, -1, 1, 1, -1, 1, -1);
		
		//Infuse Ritual
		Ritual infuseRitual = new Ritual("infuse_ritual");
		infuseRitual.addRelicStones(1, 1, 1, 1, 1, -1, -1, 1, 1, -1, 1, -1, 1, 2, 1, 1, 2, -1, -1, 2, 1, -1, 2, -1);
		infuseRitual.addRitualStones(0, 3, 0);
		
		//Storage Ritual
		Ritual storageRitual = new Ritual("storage_ritual");
		storageRitual.addRelicStones1(1, 2, 0, -1, 2, 0, 0, 2, 1, 0, 2, -1, 2, 2, 0, -2, 2, 0, 0, 2, 2, 0, 2, -2, 3, 1, 0, -3, 1, 0, 0, 1, 3, 0, 1, -3, 1, -2, 0, -1, -2, 0, 0, -2, 1, 0, -2, -1, 2, -2, 0, -2, -2, 0, 0, -2, 2, 0, -2, -2, 3, -1, 0, -3, -1, 0, 0, -1, 3, 0, -1, -3, 1, 0, 3, 2, 0, 3, 3, 0, 2, 3, 0, 1, 3, 0, -1, 3, 0, -2, 2, 0, -3, 1, 0, -3, -1, 0, -3, -2, 0, -3, -3, 0, -2, -3, 0, -1, -3, 0, 1, -3, 0, 2, -2, 0, 3, -1, 0, 3);
		storageRitual.addRitualStones(0, 2, 0, 0, -2, 0, 0, 0, 3, 0, 0, -3, 3, 0, 0, -3, 0, 0);
	}
	
	public static enum RitualType{
		NULL,
		EXTRACT,
		ENERGIZE,
		INFUSE,
		STORAGE
	}
	
}
