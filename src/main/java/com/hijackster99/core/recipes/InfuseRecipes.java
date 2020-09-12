package com.hijackster99.core.recipes;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class InfuseRecipes implements IRecipe<IInventory> {

	public static final IRecipeType<InfuseRecipes> INFUSE_RECIPES = IRecipeType.register("infuse_recipes");
	
	protected final ResourceLocation loc;
	protected final String group;
	protected final Ingredient[] ingrs;
	protected final Ingredient main;
	protected final int voidEnergy;
	protected final String restriction;

	protected final ItemStack result;
	
	public InfuseRecipes(ResourceLocation loc, String group, Ingredient[] ingrs, Ingredient main, int voidEnergy, String restriction, ItemStack result) {
		this.loc = loc;
		this.group = group;
		this.ingrs = ingrs;
		this.voidEnergy = voidEnergy;
		this.restriction = restriction;
		this.result = result;
		this.main = main;
	}
	
	@Override
	public boolean matches(IInventory inv, World worldIn) {
		ArrayList<Ingredient> ingrCopy = new ArrayList<Ingredient>();
		for(Ingredient i : ingrs) {
			ingrCopy.add(i);
		}
		for(int i = 1; i < inv.getSizeInventory(); i++) {
			Iterator<Ingredient> ingrIter = ingrCopy.iterator();
			ingredient: while(ingrIter.hasNext()) {
				Ingredient ingr = ingrIter.next();
				for(ItemStack stack : ingr.getMatchingStacks()) {
					if(inv.getStackInSlot(i) != null && stack != null && !stack.isEmpty() && ItemStack.areItemStacksEqual(stack, inv.getStackInSlot(i))){
						ingrIter.remove();
						break ingredient;
					}
				}
			}
		}
		for(ItemStack stack : main.getMatchingStacks()) {
			if(ItemStack.areItemStacksEqual(stack, inv.getStackInSlot(0)) && ingrCopy.isEmpty()) 
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
		return ingrs.length + 1 <= width * height;
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

	public Ingredient[] getIngrs() {
		return ingrs;
	}

	public int getVoidEnergy() {
		return voidEnergy;
	}
	
	public Ingredient getMain() {
		return main;
	}

	public ItemStack getResult() {
		return result;
	}
	
	public String getRestriction() {
		return restriction;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return IRecipeSerializer.register("infuse_recipes", InfuseRecipeSerializer.infuseSer);
	}

	@Override
	public IRecipeType<?> getType() {
		return INFUSE_RECIPES;
	}

}
