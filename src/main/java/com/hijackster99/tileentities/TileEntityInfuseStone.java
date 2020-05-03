package com.hijackster99.tileentities;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.common.collect.Lists;
import com.hijackster99.blocks.ARBlocks;
import com.hijackster99.core.IVoid;
import com.hijackster99.core.Ritual;
import com.hijackster99.core.recipes.InfuseRecipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class TileEntityInfuseStone extends TileEntity implements ITickableTileEntity, IVoid {
	
	private int voidEnergy = 0;
	private final int MAX_VOID = 10000;
	private int THROUGH = 200;
	Iterator<BlockPos> stoneIter;
	Iterator<BlockPos> ritIter;
	
	List<TileEntityPedestal> pedestals;
	
	CompoundNBT tag = null;
	
	public List<IVoid> inVoid;
	
	public TileEntityInfuseStone() {
		super(Ritual.tetInfuseStone);
		System.out.println(Ritual.rituals.get("infuse_ritual") == null);
		stoneIter = Ritual.rituals.get("infuse_ritual").getRelicStones().iterator();
		ritIter = Ritual.rituals.get("infuse_ritual").getRitualStones().iterator();
		inVoid = new CopyOnWriteArrayList<IVoid>();
		pedestals = new CopyOnWriteArrayList<TileEntityPedestal>();
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
			if(!checkValid()) world.setBlockState(pos, ARBlocks.RITUAL_STONE_1.getDefaultState());
			getPedestals();
			ItemStack[] inputs = getFirstValidRecipe();
			if(inputs.length > 0) {
				System.out.println("hello");
				int energy = getEnergyCost(inputs);
				Optional<InfuseRecipes> rec = getRecipeForInputs(inputs);
				Optional<ItemStack> res = getResult(inputs);
				
				InfuseRecipes recipe;
				try {
					recipe = rec.get();
				}catch(NoSuchElementException e) {
					e.printStackTrace();
					recipe = null;
				}
				
				ItemStack result;
				try {
					result = res.get();
				}catch(NoSuchElementException e) {
					e.printStackTrace();
					result = ItemStack.EMPTY;
				}
				
				if(!result.isEmpty() && recipe != null) {
					if(voidEnergy >= energy) {
						removeRecipeFromPedestals(recipe);
						createOutput(result);
						removeVoid(energy);
					}
				}
			}
		}
	}
	
	private void removeRecipeFromPedestals(InfuseRecipes recipe) {
		TileEntity te = world.getTileEntity(pos.add(new Vec3i(0, 1, 0)));
		if(te instanceof TileEntityPedestal) {
			TileEntityPedestal ped = (TileEntityPedestal) te;
			ped.getInventory().setStackInSlot(0, ItemStack.EMPTY);
		}
		List<Ingredient> ingredients = Lists.newArrayList(recipe.getIngrs());
		for(Ingredient i : ingredients) {
			ingredient: 
			for(ItemStack stack : i.getMatchingStacks()) {
				for(TileEntityPedestal ped : pedestals) {
					if(ItemStack.areItemStacksEqual(stack, ped.getInventory().getStackInSlot(0))) {
						ped.getInventory().setStackInSlot(0, ItemStack.EMPTY);
						break ingredient;
					}
				}
			}
		}
	}
	
	private void createOutput(ItemStack result) {
		TileEntity te = world.getTileEntity(pos.add(new Vec3i(0, 1, 0)));
		if(te instanceof TileEntityPedestal) {
			TileEntityPedestal ped = (TileEntityPedestal) te;
			ped.getInventory().setStackInSlot(0, result.copy());
		}
	}
	
	public void getPedestals() {
		for(int x = -3; x <= 3; x += 3) {
			for(int y = -3; y <= 3; y += 3) {
				TileEntity te = world.getTileEntity(pos.add(new Vec3i(x, 1, y)));
				if(x != 0 && y != 0 && te instanceof TileEntityPedestal) {
					TileEntityPedestal ped = (TileEntityPedestal) te;
					pedestals.add(ped);
				}
			}
		}
	}
	
	private ItemStack[] getFirstValidRecipe() {
		ItemStack[] inputs = new ItemStack[pedestals.size() + 1];
		TileEntity te = world.getTileEntity(pos.add(new Vec3i(0, 1, 0)));
		if(te instanceof TileEntityPedestal) {
			TileEntityPedestal ped = (TileEntityPedestal) te;
			inputs[0] = ped.getInventory().getStackInSlot(0);
			for(int i = 0; i < pedestals.size(); i++) {
				inputs[i + 1] = pedestals.get(i).getInventory().getStackInSlot(0);
				if(isInput(inputs))
					return inputs;
			}
		}
		return new ItemStack[] {};
	}
	
	private Optional<InfuseRecipes> getRecipe(final IInventory inventory) {
		return world.getRecipeManager().getRecipe(InfuseRecipes.INFUSE_RECIPES, inventory, world);
    }
	
	private short getEnergyCost(final ItemStack... inputs) {
        return getRecipeForInputs(inputs)
                .map(InfuseRecipes::getVoidEnergy)
                .orElse(500)
                .shortValue();
    }
	
	private boolean isInput(final ItemStack... stacks) {
        return getRecipeForInputs((ItemStack[])stacks).isPresent();
    }
	
	private Optional<ItemStack> getResult(final ItemStack... inputs) {
        final Inventory inventory = new Inventory(inputs);
        return getRecipe(inventory).map(recipe -> recipe.getCraftingResult(inventory));
    }
	
	private Optional<InfuseRecipes> getRecipeForInputs(final ItemStack... inputs) {
        return getRecipe(new Inventory((ItemStack[])inputs));
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
			if(world.getBlockState(pos.add(new Vec3i(0, 1, 0))).getBlock().equals(ARBlocks.PEDESTAL))
				return true;
			else return false;
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
