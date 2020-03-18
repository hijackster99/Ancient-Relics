package com.hijackster99.tileentities;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import com.hijackster99.core.INetwork;
import com.hijackster99.core.IVoid;
import com.hijackster99.core.Ritual;
import com.hijackster99.core.recipes.EnergizeRecipes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityEnergizeStone extends TileEntity implements ITickableTileEntity, IVoid {
	
	private int MAX_VOID = 10000;
	private int voidEnergy = 0;
	
	private int THROUGH = 200;
	
	List<IVoid> inVoid;
	
	CompoundNBT tag = null;
	
	public TileEntityEnergizeStone() {
		super(Ritual.tetEnergizeStone);
		inVoid = new CopyOnWriteArrayList<IVoid>();
	}

	@Override
	public void tick() {
		if(!world.isRemote()) {
			if(tag != null) {
				CompoundNBT nbt = tag.getCompound("arenergy");
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
				tag = null;
			}
			ItemStack stack = getFirstValidRecipe();
			if(!stack.isEmpty()) {
				int energy = getEnergyCost(stack);
				System.out.println(energy);
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
						spawnItem(res);
						removeVoid(energy);
					}
				}
			}
			
		}
	}
	
	private void spawnItem(ItemStack toSpawn) {
		ItemEntity e = new ItemEntity(world, pos.getX(), pos.getY() + 0.75, pos.getZ(), toSpawn);
		e.setNoDespawn();
		world.addEntity(e);
	}
	
	private ItemStack getFirstValidRecipe() {
		List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.add(0, 1, 0)));
		for(Entity e : entities) {
			if(e instanceof ItemEntity) {
				ItemEntity item = (ItemEntity) e;
				if(item.getAge() != -6000) {
					if(isInput(item.getItem())) {
						return item.getItem();
					}
				}
			}
		}
		return ItemStack.EMPTY;
	}
	
	private Optional<EnergizeRecipes> getRecipe(final IInventory inventory) {
		return world.getRecipeManager().getRecipe(EnergizeRecipes.ENERGIZE_RECIPES, inventory, world);
    }
	
	private short getEnergyCost(final ItemStack input) {
        return getRecipeForInput(input)
                .map(EnergizeRecipes::getVoidEnergy)
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
	
	private Optional<EnergizeRecipes> getRecipeForInput(final ItemStack input) {
        return getRecipe(new Inventory(input));
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
		compound.put("arenergy", nbt);
		return super.write(compound);
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		CompoundNBT nbt = compound.getCompound("arenergy");
		if(!nbt.isEmpty()) {
			voidEnergy = nbt.getInt("void");
		}
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
		this.voidEnergy = voidEnergy;
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

}
