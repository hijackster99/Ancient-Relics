package com.hijackster99.core;

import net.minecraft.tileentity.TileEntity;

public interface IVoid {

	public int getCapacity();
	public int getVoid();
	
	public void setVoid(int voidEnergy);
	public void addVoid(int voidEnergy);
	public int addVoidWithOverflow(int voidEnergy);
	
	public void removeVoid(int voidEnergy);
	
	public void addInput(IVoid iv);
	public void removeInput(IVoid iv);
	public void addOutput(IVoid iv);
	public void removeOutput(IVoid iv);
	
	public void removeFromNetwork();
	
	public int getThrough();
	public void setThrough(int through);
	
	public TileEntity getTe();
	
}
