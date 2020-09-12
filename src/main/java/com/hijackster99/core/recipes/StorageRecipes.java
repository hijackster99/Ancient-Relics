package com.hijackster99.core.recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class StorageRecipes implements IRecipe<IInventory>{

	public static final IRecipeType<StorageRecipes> STORAGE_RECIPES = IRecipeType.register("storage_recipes");
	
	protected final ResourceLocation loc;
	protected final String group;
	protected final Ingredient ingr;
	protected final int voidEnergy;
	protected final ItemStack result;
	
	public StorageRecipes(ResourceLocation loc, String group, Ingredient ingr, int voidEnergy, ItemStack result) {
		this.loc = loc;
		this.group = group;
		this.ingr = ingr;
		this.voidEnergy = voidEnergy;
		this.result = result;
	}
	
	@Override
	public boolean matches(IInventory inv, World worldIn) {
		for(ItemStack stack : ingr.getMatchingStacks()) {
			if(ItemStack.areItemStacksEqual(stack, inv.getStackInSlot(0)))
				return true;
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(IInventory inv) {
		return result.copy();
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return result;
	}

	@Override
	public ResourceLocation getId() {
		return loc;
	}

	public ResourceLocation getLoc() {
		return loc;
	}

	public String getGroup() {
		return group;
	}

	public Ingredient getIngr() {
		return ingr;
	}

	public int getVoidEnergy() {
		return voidEnergy;
	}

	public ItemStack getResult() {
		return result;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return IRecipeSerializer.register("storage_recipes", StorageRecipeSerializer.storageSer);
	}

	@Override
	public IRecipeType<?> getType() {
		return STORAGE_RECIPES;
	}

}
