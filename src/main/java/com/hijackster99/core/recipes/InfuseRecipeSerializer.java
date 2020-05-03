package com.hijackster99.core.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hijackster99.core.References;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ObjectHolder;

public class InfuseRecipeSerializer<T extends InfuseRecipes> extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {

	@ObjectHolder(References.MODID + ":infuse_serializer")
	public static IRecipeSerializer<EnergizeRecipes> infuseSer;
	
	private final InfuseRecipeSerializer.IFactory<T> factory;
	public InfuseRecipeSerializer(InfuseRecipeSerializer.IFactory<T> factory) {
		this.factory = factory;
        this.setRegistryName(References.MODID, "infuse_recipes");
	}
	
	@Override
	public T read(ResourceLocation recipeId, JsonObject json) {
		String group = JSONUtils.getString(json, "group", "");
		Ingredient[] ingrs;
		
		if(JSONUtils.isJsonArray(json, "ingredients")) {
            JsonArray arr = JSONUtils.getJsonArray(json, "ingredients");
            ingrs = new Ingredient[arr.size()];
            for(int i = 0; i < arr.size(); i++) {
            	ingrs[i] = Ingredient.deserialize(arr.get(i));
            }
		}else {
            JsonObject obj = JSONUtils.getJsonObject(json, "main");
            ingrs = new Ingredient[1];
            ingrs[0] = Ingredient.deserialize(obj);
		}
		
		JsonElement main = (JsonElement)(JSONUtils.isJsonArray(json, "main") ?
                JSONUtils.getJsonArray(json, "main") :
                JSONUtils.getJsonObject(json, "main"));
		Ingredient mainI = Ingredient.deserialize(main);
	
		int energy = JSONUtils.getInt(json, "energy");
		
		if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
		
		ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
		
		return this.factory.create(recipeId, group,  ingrs, mainI, energy, result);
	}

	@Override
	public T read(ResourceLocation recipeId, PacketBuffer buffer) {
		String group = buffer.readString(32767);
		int size = buffer.readInt();
        Ingredient[] ingrs = new Ingredient[size];
        for(int i = 0; i < size; i++) ingrs[i] = Ingredient.read(buffer);
        Ingredient main = Ingredient.read(buffer);
        int energy = buffer.readVarInt();
        ItemStack result = buffer.readItemStack();

        return this.factory.create(recipeId, group, ingrs, main, energy, result);
	}

	@Override
	public void write(PacketBuffer buffer, T recipe) {
		buffer.writeString(recipe.group);
		buffer.writeInt(recipe.ingrs.length);
		for(Ingredient i : recipe.ingrs) i.write(buffer);
		recipe.main.write(buffer);
        buffer.writeVarInt(recipe.voidEnergy);
        buffer.writeItemStack(recipe.result);
	}
	
	public interface IFactory<T extends InfuseRecipes> {
        T create(ResourceLocation resourceLocation, String group, Ingredient[] ingrs, Ingredient main, int voidEnergy, ItemStack result);
    }

}
