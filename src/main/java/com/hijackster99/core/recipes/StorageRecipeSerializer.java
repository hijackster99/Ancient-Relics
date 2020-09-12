package com.hijackster99.core.recipes;

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

public class StorageRecipeSerializer<T extends StorageRecipes> extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {

	@ObjectHolder(References.MODID + ":storage_serializer")
	public static IRecipeSerializer<StorageRecipes> storageSer;
	
	private final StorageRecipeSerializer.IFactory<T> factory;
	public StorageRecipeSerializer(StorageRecipeSerializer.IFactory<T> factory) {
		this.factory = factory;
        this.setRegistryName(References.MODID, "storage_recipes");
	}
	
	@Override
	public T read(ResourceLocation recipeId, JsonObject json) {
		String group = JSONUtils.getString(json, "group", "");
		
		JsonElement ingr = (JsonElement)(JSONUtils.isJsonArray(json, "main") ?
                JSONUtils.getJsonArray(json, "main") :
                JSONUtils.getJsonObject(json, "main"));
		Ingredient mainI = Ingredient.deserialize(ingr);
	
		int energy = JSONUtils.getInt(json, "energy");
		
		if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
		
		ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
		
		return this.factory.create(recipeId, group,  mainI, energy, result);
	}

	@Override
	public T read(ResourceLocation recipeId, PacketBuffer buffer) {
		String group = buffer.readString(32767);
        Ingredient main = Ingredient.read(buffer);
        int energy = buffer.readVarInt();
        ItemStack result = buffer.readItemStack();

        return this.factory.create(recipeId, group, main, energy, result);
	}

	@Override
	public void write(PacketBuffer buffer, T recipe) {
		buffer.writeString(recipe.group);
        recipe.ingr.write(buffer);
        buffer.writeVarInt(recipe.voidEnergy);
        buffer.writeItemStack(recipe.result);
	}
	
	public interface IFactory<T extends StorageRecipes> {
        T create(ResourceLocation resourceLocation, String group, Ingredient ingr, int voidEnergy, ItemStack result);
    }

}
