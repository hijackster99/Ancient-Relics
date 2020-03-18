package com.hijackster99.core;

import java.util.ArrayList;
import java.util.HashMap;

import com.hijackster99.tileentities.TileEntityEnergizeStone;
import com.hijackster99.tileentities.TileEntityExtractStone;

import net.minecraft.tileentity.TileEntity;
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
	
	private ArrayList<BlockPos> relicStones = new ArrayList<BlockPos>();
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

	public ArrayList<BlockPos> getRitualStones() {
		return ritualStones;
	}
	
	public static void register() {
		//Extract Ritual Stone
		Ritual extractRitual = new Ritual("extract_ritual");
		extractRitual.addRelicStones(1, 1, 1, 1, 1, -1, -1, 1, 1, -1, 1, -1, 2, 2, 2, 2, 2, -2, -2, 2, 2, -2, 2, -2);
		
		//Energize Ritual Stone
		Ritual energizeRitual = new Ritual("energize_ritual");
		energizeRitual.addRelicStones(1, 1, 1, 1, 1, -1, -1, 1, 1, -1, 1, -1);
		
		//Infuse Ritual
		Ritual infuseRitual = new Ritual("infuse_ritual");
		infuseRitual.addRelicStones(1, 1, 1, 1, 1, -1, -1, 1, 1, -1, 1, -1, 1, 2, 1, 1, 2, -1, -1, 2, 1, -1, 2, -1);
		infuseRitual.addRitualStones(0, 3, 0);
	}
	
	public static enum RitualType{
		NULL,
		EXTRACT,
		ENERGIZE,
		INFUSE
	}
	
}
