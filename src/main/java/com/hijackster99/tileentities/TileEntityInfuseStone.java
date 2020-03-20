package com.hijackster99.tileentities;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.hijackster99.blocks.ARBlocks;
import com.hijackster99.core.IVoid;
import com.hijackster99.core.Ritual;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityInfuseStone extends TileEntity implements ITickableTileEntity, IVoid {
	
	private int voidEnergy = 0;
	private final int MAX_VOID = 10000;
	private int THROUGH = 200;
	Iterator<BlockPos> stoneIter;
	Iterator<BlockPos> ritIter;
	
	CompoundNBT tag = null;
	
	public List<IVoid> inVoid;
	
	public TileEntityInfuseStone() {
		super(Ritual.tetInfuseStone);
		stoneIter = Ritual.rituals.get("infuse_stone").getRelicStones().iterator();
		ritIter = Ritual.rituals.get("infuse_stone").getRitualStones().iterator();
		inVoid = new CopyOnWriteArrayList<IVoid>();
	}

	@Override
	public void tick() {
		if(!world.isRemote) {
			if(tag != null) {
				CompoundNBT nbt = tag.getCompound("void");
				if(!nbt.isEmpty()) {
					int[] arrX = nbt.getIntArray("inX");
					int[] arrY = nbt.getIntArray("inY");
					int[] arrZ = nbt.getIntArray("inZ");
					for(int i = 0; i < arrX.length; i++) {
						BlockPos bp = new BlockPos(arrX[i], arrY[i], arrZ[i]);
						TileEntity te = world.getTileEntity(bp);
						if(te instanceof IVoid) {
							IVoid n = (IVoid) te;
							inVoid.add(n);
						}
					}
				}
				tag = null;
			}
			checkValid();
		}
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		CompoundNBT nbt = compound.getCompound("void");
		
		voidEnergy = nbt.getInt("void_energy");
		tag = compound;
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		CompoundNBT nbt = new CompoundNBT();
		
		nbt.putInt("void_energy", voidEnergy);
		
		int[] arrX = new int[inVoid.size()];
		int[] arrY = new int[inVoid.size()];
		int[] arrZ = new int[inVoid.size()];
		for(int i = 0; i < inVoid.size(); i++) {
			arrX[i] = inVoid.get(i).getTe().getPos().getX();
			arrY[i] = inVoid.get(i).getTe().getPos().getY();
			arrZ[i] = inVoid.get(i).getTe().getPos().getZ();
		}
		nbt.putIntArray("inX", arrX);
		nbt.putIntArray("inY", arrY);
		nbt.putIntArray("inZ", arrZ);
		
		compound.put("void", nbt);
		
		return super.write(compound);
	}
	
	public boolean checkValid() {
		if(stoneIter.hasNext()) {
			if(world.getBlockState(pos.add(stoneIter.next())).getBlock().equals(ARBlocks.INFUSED_STONE))
				return true;
			else return false;
		}else if(ritIter.hasNext()) {
			if(world.getBlockState(pos.add(ritIter.next())).getBlock().equals(ARBlocks.RITUAL_STONE))
				return true;
			else return false;
		}else {
			resetIter();
			return true;
		}
	}
	
	public void resetIter() {
		stoneIter = Ritual.rituals.get("infuse_ritual").getRelicStones().iterator();
		ritIter = Ritual.rituals.get("infuse_ritual").getRitualStones().iterator();
	}

	@Override
	public int getCapacity() {
		return MAX_VOID;
	}

	@Override
	public int getVoid() {
		return voidEnergy;
	}

	@Override
	public void setVoid(int voidEnergy) {
		this.voidEnergy = voidEnergy;
	}

	@Override
	public void addVoid(int voidEnergy) {
		this.voidEnergy += voidEnergy;
		if(voidEnergy > MAX_VOID)
			voidEnergy = MAX_VOID;
	}

	@Override
	public int addVoidWithOverflow(int voidEnergy) {
		int left = 0;
		this.voidEnergy += voidEnergy;
		if(voidEnergy > MAX_VOID) {
			left = voidEnergy - MAX_VOID;
			voidEnergy = MAX_VOID;
		}
		return left;
	}

	@Override
	public void removeVoid(int voidEnergy) {
		this.voidEnergy -= voidEnergy;
	}

	@Override
	public TileEntity getTe() {
		return this;
	}

	@Override
	public void addInput(IVoid iv) {
		
	}

	@Override
	public void removeInput(IVoid iv) {
		
	}

	@Override
	public void addOutput(IVoid iv) {
		
	}

	@Override
	public void removeOutput(IVoid iv) {
		
	}

	@Override
	public void removeFromNetwork() {
		for(IVoid iv : inVoid) {
			iv.removeOutput(this);
		}
	}
	
	@Override
	public void remove() {
		removeFromNetwork();
		super.remove();
	}

	@Override
	public int getThrough() {
		return THROUGH;
	}

	@Override
	public void setThrough(int through) {
		this.THROUGH = through;
	}

}
