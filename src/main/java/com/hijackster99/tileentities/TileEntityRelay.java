package com.hijackster99.tileentities;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.hijackster99.items.ARItems;
import com.hijackster99.core.IInteractable;
import com.hijackster99.core.INetwork;
import com.hijackster99.core.IVoid;
import com.hijackster99.core.Request;
import com.hijackster99.core.RequestType;
import com.hijackster99.core.References;
import com.hijackster99.core.RelayType;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityRelay extends TileEntity implements IInteractable, ITickableTileEntity, IVoid{

	@ObjectHolder(References.MODID + ":relay")
	public static TileEntityType<TileEntityRelay> tetRelay;
	
	public List<IVoid> inVoid;
	public List<IVoid> outVoid;
	
	private Iterator<IVoid> outIter;
	
	public int THROUGH = 200;
	
	public int MAX_VOID = 2000;
	public int voidEnergy = 0;
	
	boolean nextConnection = false;
	INetwork relay = null;
	
	CompoundNBT tag = null;
	
	public TileEntityRelay() {
		super(tetRelay);
		inVoid = new CopyOnWriteArrayList<IVoid>();
		outVoid = new CopyOnWriteArrayList<IVoid>();
		
		outIter = outVoid.iterator();
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
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if(player.getHeldItem(handIn).getItem().equals(ARItems.LINK_STAFF)) {
			if(player.getHeldItem(handIn).hasTag() && player.getHeldItem(handIn).getTag().contains("link")) {
				CompoundNBT nbt = (CompoundNBT) player.getHeldItem(handIn).getTag().get("link");
				if(nbt.getInt("dim") == player.dimension.getId()) {
					TileEntity te = world.getTileEntity(new BlockPos(nbt.getInt("coordX"), nbt.getInt("coordY"), nbt.getInt("coordZ")));
					if(te instanceof IVoid) {
						IVoid otherVoid = (IVoid) te;
						addInput(otherVoid);
						otherVoid.addOutput(this);
					}
				}
			}else {
				if(!player.getHeldItem(handIn).hasTag()) {
					player.getHeldItem(handIn).setTag(new CompoundNBT());
				}
				CompoundNBT nbt = new CompoundNBT();
				nbt.putInt("coordX", pos.getX());
				nbt.putInt("coordY", pos.getY());
				nbt.putInt("coordZ", pos.getZ());
				nbt.putInt("dim", player.dimension.getId());
				player.getHeldItem(handIn).getTag().put("link", nbt);
			}
			return true;
		}else {
			System.out.println("InVoid: " + inVoid);
			System.out.println("OutVoid: " + outVoid);
		}
		return false;
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("void", voidEnergy);
		int [] arrX = new int[inVoid.size()];
		int [] arrY = new int[inVoid.size()];
		int [] arrZ = new int[inVoid.size()];
		for(int i = 0; i < inVoid.size(); i++) {
			arrX[i] = inVoid.get(i).getTe().getPos().getX();
			arrY[i] = inVoid.get(i).getTe().getPos().getY();
			arrZ[i] = inVoid.get(i).getTe().getPos().getZ();
		}
		nbt.putIntArray("inX", arrX);
		nbt.putIntArray("inY", arrY);
		nbt.putIntArray("inZ", arrZ);
		arrX = new int[outVoid.size()];
		arrY = new int[outVoid.size()];
		arrZ = new int[outVoid.size()];
		for(int i = 0; i < outVoid.size(); i++) {
			arrX[i] = outVoid.get(i).getTe().getPos().getX();
			arrY[i] = outVoid.get(i).getTe().getPos().getY();
			arrZ[i] = outVoid.get(i).getTe().getPos().getZ();
		}
		nbt.putIntArray("outX", arrX);
		nbt.putIntArray("outY", arrY);
		nbt.putIntArray("outZ", arrZ);
		compound.put("arrelay", nbt);
		return super.write(compound);
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		tag = compound;
	}

	@Override
	public void addVoid(int voidEnergy) {
		this.voidEnergy += voidEnergy;
		if(voidEnergy > MAX_VOID)
			voidEnergy = MAX_VOID;
	}

	@Override
	public int addVoidWithOverflow(int voidEnergy) {
		int overflow = 0;
		this.voidEnergy += voidEnergy;
		if(voidEnergy > MAX_VOID) {
			overflow = voidEnergy - MAX_VOID;
			voidEnergy = MAX_VOID;
		}
		return overflow;
	}

	@Override
	public void tick() {
		if(!world.isRemote) {
			if(tag != null) {
				CompoundNBT nbt = tag.getCompound("arrelay");
				if(!nbt.isEmpty()) {
					voidEnergy = nbt.getInt("void");
					int[] arrX = nbt.getIntArray("inX");
					int[] arrY = nbt.getIntArray("inY");
					int[] arrZ = nbt.getIntArray("inZ");
					for(int i = 0; i < arrX.length; i++) {
						BlockPos bp = new BlockPos(arrX[i], arrY[i], arrZ[i]);
						TileEntity te = world.getTileEntity(bp);
						if(te instanceof IVoid) {
							if(!(te instanceof INetwork)) {
								IVoid n = (IVoid) te;
								inVoid.add(n);
							}
						}
					}
					arrX = nbt.getIntArray("outX");
					arrY = nbt.getIntArray("outY");
					arrZ = nbt.getIntArray("outZ");
					for(int i = 0; i < arrX.length; i++) {
						BlockPos bp = new BlockPos(arrX[i], arrY[i], arrZ[i]);
						TileEntity te = world.getTileEntity(bp);
						if(te instanceof IVoid) {
							if(!(te instanceof INetwork)) {
								IVoid n = (IVoid) te;
								outVoid.add(n);
							}
						}
					}
					outIter = outVoid.iterator();
				}
				tag = null;
			}
			if(voidEnergy > 0) {
				if(outIter.hasNext()) {
					IVoid iv = outIter.next();
					int amount = iv.getCapacity() - iv.getVoid() < voidEnergy ? iv.getCapacity() - iv.getVoid() : voidEnergy < THROUGH ? voidEnergy : THROUGH;
					removeVoid(amount);
					iv.addVoid(amount);
				}else {
					outIter = outVoid.iterator();
					if(outIter.hasNext()) {
						IVoid iv = outIter.next();
						int amount = iv.getCapacity() - iv.getVoid() < voidEnergy ? iv.getCapacity() - iv.getVoid() : voidEnergy < THROUGH ? voidEnergy : THROUGH;
						removeVoid(amount);
						iv.addVoid(amount);
					}
				}
			}
		}
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
	public void removeFromNetwork() {
		for(IVoid n : inVoid) {
			n.removeInput(this);
		}
		for(IVoid n : outVoid) {
			n.removeOutput(this);
		}
	}
	
	@Override
	public void remove() {
		removeFromNetwork();
		super.remove();
	}

	@Override
	public void addInput(IVoid iv) {
		if(!inVoid.contains(iv))
			inVoid.add(iv);
		else 
			removeInput(iv);
	}

	@Override
	public void removeInput(IVoid iv) {
		inVoid.remove(iv);
	}

	@Override
	public void addOutput(IVoid iv) {
		if(!outVoid.contains(iv))
			outVoid.add(iv);
		else
			removeOutput(iv);
	}

	@Override
	public void removeOutput(IVoid iv) {
		outVoid.remove(iv);
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
