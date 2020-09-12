package com.hijackster99.tileentities;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.hijackster99.items.ARItems;
import com.hijackster99.blocks.ARBlocks;
import com.hijackster99.core.IInteractable;
import com.hijackster99.core.IVoid;
import com.hijackster99.core.Ritual;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class TileEntityExtractStone extends TileEntity implements ITickableTileEntity, IInteractable, IVoid{

	private int burnTime = 0;
	private int voidEnergy = 0;
	private int vpt = 1;
	private final int MAX_VOID = 10000;
	private int THROUGH = 200;
	Iterator<BlockPos> iter;
	
	CompoundNBT tag = null;
	
	public List<IVoid> outVoid;

	private Iterator<IVoid> outIter;
	
	public TileEntityExtractStone() {
		super(Ritual.tetExtractStone);
		iter = Ritual.rituals.get("extract_ritual").getRelicStones().iterator();
		outVoid = new CopyOnWriteArrayList<IVoid>();
		outIter = outVoid.iterator();
	}

	@Override
	public void tick() {
		if(!world.isRemote) {
			if(tag != null) {
				CompoundNBT nbt = tag.getCompound("void");
				if(!nbt.isEmpty()) {
					int[] arrX = nbt.getIntArray("outX");
					int[] arrY = nbt.getIntArray("outY");
					int[] arrZ = nbt.getIntArray("outZ");
					for(int i = 0; i < arrX.length; i++) {
						BlockPos bp = new BlockPos(arrX[i], arrY[i], arrZ[i]);
						TileEntity te = world.getTileEntity(bp);
						if(te instanceof IVoid) {
							IVoid n = (IVoid) te;
							outVoid.add(n);
						}
					}
					outIter = outVoid.iterator();
				}
				tag = null;
			}
			if(!checkValid()) world.setBlockState(pos, ARBlocks.RITUAL_STONE.getDefaultState());
			if(burnTime == 0) {
				List<Entity> entities = getEntities();
				for(Entity entity : entities) {
					if(entity instanceof ItemEntity) {
						ItemEntity item = (ItemEntity) entity;
						if(isValidFuel(item.getItem())) {
							burnTime = getBurnTime(item.getItem());
							vpt = getVPT(item.getItem());
							removeItem(item, 1);
						}
					}

				}
			}else {
				if(voidEnergy <= MAX_VOID - vpt) {
					voidEnergy += vpt;
				}
				burnTime -= vpt;
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
	public void read(CompoundNBT compound) {
		super.read(compound);
		CompoundNBT nbt = compound.getCompound("void");
		
		voidEnergy = nbt.getInt("void_energy");
		burnTime = nbt.getInt("burn_time");
		tag = compound;
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		CompoundNBT nbt = new CompoundNBT();
		
		nbt.putInt("void_energy", voidEnergy);
		nbt.putInt("burn_time", burnTime);
		
		int[] arrX = new int[outVoid.size()];
		int[] arrY = new int[outVoid.size()];
		int[] arrZ = new int[outVoid.size()];
		for(int i = 0; i < outVoid.size(); i++) {
			arrX[i] = outVoid.get(i).getTe().getPos().getX();
			arrY[i] = outVoid.get(i).getTe().getPos().getY();
			arrZ[i] = outVoid.get(i).getTe().getPos().getZ();
		}
		nbt.putIntArray("outX", arrX);
		nbt.putIntArray("outY", arrY);
		nbt.putIntArray("outZ", arrZ);
		
		compound.put("void", nbt);
		
		return super.write(compound);
	}
	
	public boolean checkValid() {
		if(iter.hasNext()) {
			if(world.getBlockState(pos.add(iter.next())).getBlock().equals(ARBlocks.INFUSED_STONE))
				return true;
			else return false;
		}else {
			resetIter();
			return true;
		}
	}
	
	public void resetIter() {
		iter = Ritual.rituals.get("extract_ritual").getRelicStones().iterator();
	}
	
	public void removeItem(ItemEntity item, int count) {
		ItemStack itemstack = item.getItem().copy();
		itemstack.setCount(itemstack.getCount() - count);
		if(itemstack.getCount() <= 0) 
			item.remove();
		else
			item.setItem(itemstack);
	}
	
	public List<Entity> getEntities(){
		AxisAlignedBB bb = new AxisAlignedBB(pos.add(new BlockPos(0, 1, 0)));
		return getWorld().getEntitiesWithinAABB(Entity.class, bb);
	}
	
	public boolean isValidFuel(ItemStack stack) {
		return stack.getItem().getBurnTime(stack) == -1 ? FurnaceTileEntity.isFuel(stack) : stack.getItem().getBurnTime(stack) > 0;
	}
	
	public int getBurnTime(ItemStack stack) {
		return stack.getItem().getBurnTime(stack) == -1 ? FurnaceTileEntity.getBurnTimes().get(stack.getItem()) : stack.getItem().getBurnTime(stack);
	}
	
	public int getVPT(ItemStack stack) {
		if(stack.getItem() == ARItems.CHEAT_COAL) return 200;
		if(stack.getItem() == ARItems.VOID_CHARCOAL || stack.getItem() == ARItems.VOID_COAL)
			return 20;
		return 1;
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
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if(!worldIn.isRemote() && player.getHeldItem(handIn).getItem().equals(ARItems.INFO_STAFF)) {
			player.sendMessage(new StringTextComponent("Burn Time: " + burnTime));
			return false;
		}
		if(player.getHeldItem(handIn).getItem().equals(ARItems.LINK_STAFF))
			return false;
		return true;
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
	public void removeFromNetwork() {
		for(IVoid iv : outVoid) {
			iv.removeInput(this);
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
