package com.hijackster99.tileentities;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import com.hijackster99.blocks.ARBlocks;
import com.hijackster99.core.INetwork;
import com.hijackster99.core.IVoid;
import com.hijackster99.core.Ritual;
import com.hijackster99.core.recipes.StorageRecipes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class TileEntityStorageStone extends TileEntity implements ITickableTileEntity, IVoid {

	private int voidEnergy = 0;
	private final int MAX_VOID = 1000000;
	private int THROUGH = 200;
	Iterator<BlockPos> stone1Iter;
	Iterator<BlockPos> ritIter;
	
	CompoundNBT tag = null;
	
	public List<IVoid> inVoid;
	public List<IVoid> outVoid;
	
	private Iterator<IVoid> outIter;
	
	public TileEntityStorageStone() {
		super(Ritual.tetStorageStone);
		stone1Iter = Ritual.rituals.get("storage_ritual").getRelicStones1().iterator();
		ritIter = Ritual.rituals.get("storage_ritual").getRitualStones().iterator();
		inVoid = new CopyOnWriteArrayList<IVoid>();
		outVoid = new CopyOnWriteArrayList<IVoid>();
		
		outIter = outVoid.iterator();
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
	
	public boolean checkValid() {
		if(stone1Iter.hasNext()) {
			if(world.getBlockState(pos.add(stone1Iter.next())).getBlock().equals(ARBlocks.INFUSED_STONE))
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
		stone1Iter = Ritual.rituals.get("storage_ritual").getRelicStones1().iterator();
		ritIter = Ritual.rituals.get("storage_ritual").getRitualStones().iterator();
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
	public void removeFromNetwork() {
		for(IVoid iv : inVoid) {
			iv.removeOutput(this);
		}
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
			ItemEntity entity = getFirstValidRecipe();
			if(entity != null) {
				ItemStack stack = entity.getItem();
				System.out.println(stack);
				if(!stack.isEmpty()) {
					int energy = getEnergyCost(stack);
					Optional<ItemStack> result = getResult(stack);
					
					ItemStack res;
					try {
						res = result.get();
					}catch(NoSuchElementException e) {
						e.printStackTrace();
						res = ItemStack.EMPTY;
					}
					
					if(!res.isEmpty()) {
						if(voidEnergy >= energy) {
							stack.shrink(1);
							spawnItem(res, entity.getPositionVec());
							removeVoid(energy);
						}
					}
				}
			}
		}
	}
	
	private void spawnItem(ItemStack toSpawn, Vec3d pos) {
		ItemEntity e = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), toSpawn);
		e.setNoDespawn();
		world.addEntity(e);
	}
	
	private ItemEntity getFirstValidRecipe() {
		List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.add(-2, -1, -2), pos.add(3, 1, 3)));
		for(Entity e : entities) {
			if(e instanceof ItemEntity) {
				ItemEntity item = (ItemEntity) e;
				if(item.getAge() != -6000) {
					if(isInput(item.getItem())) {
						return item;
					}
				}
			}
		}
		return null;
	}
	
	private Optional<StorageRecipes> getRecipe(final IInventory inventory) {
		return world.getRecipeManager().getRecipe(StorageRecipes.STORAGE_RECIPES, inventory, world);
    }
	
	private short getEnergyCost(final ItemStack input) {
        return getRecipeForInput(input)
                .map(StorageRecipes::getVoidEnergy)
                .orElse(500)
                .shortValue();
    }
	
	private boolean isInput(final ItemStack stack) {
        if (stack.isEmpty())
            return false;
        return getRecipeForInput(stack).isPresent();
    }
	
	private Optional<ItemStack> getResult(final ItemStack input) {
        final Inventory inventory = new Inventory(input);
        return getRecipe(inventory).map(recipe -> recipe.getCraftingResult(inventory));
    }
	
	private Optional<StorageRecipes> getRecipeForInput(final ItemStack input) {
        return getRecipe(new Inventory(input));
    }

}
