package com.hijackster99.tileentities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hijackster99.blocks.containers.RelicAnvilContainer;
import com.hijackster99.core.INetwork;
import com.hijackster99.core.IVoid;
import com.hijackster99.core.References;
import com.hijackster99.core.config.RelicListConfig;
import com.hijackster99.items.ARItems;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityRelicAnvil extends TileEntity implements INamedContainerProvider, ITickableTileEntity, IVoid {

	private int MAX_VOID = 5000;
	private int voidEnergy = 0;
	
	private int THROUGH = 200;
	List<IVoid> inVoid;
	
	CompoundNBT tag = null;
	
	boolean extracted = true;
	
	@ObjectHolder(References.MODID + ":anvil")
	public static TileEntityType<TileEntityRelay> tetAnvil;
	
	private final ItemStackHandler inventory = new ItemStackHandler(3) {
		
		protected void onContentsChanged(int slot) {
			super.onContentsChanged(slot);
			markDirty();
		}
		
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if(!world.isRemote && !simulate && !extracted) {
				if(slot == 2) {
					if(getStackInSlot(0) != ItemStack.EMPTY && getStackInSlot(0).getCount() > 1) {
						getStackInSlot(0).setCount(getStackInSlot(0).getCount() - 1);
					}else {
						setStackInSlot(0, ItemStack.EMPTY);
					}
					if(getStackInSlot(1) != ItemStack.EMPTY && getStackInSlot(1).getCount() > 1) {
						getStackInSlot(1).setCount(getStackInSlot(1).getCount() - 1);
					}else {
						setStackInSlot(1, ItemStack.EMPTY);
					}
					voidEnergy -= 1000;
					extracted = true;
				}
			}
			return super.extractItem(slot, amount, simulate);
		}
		
	};
	
	public TileEntityRelicAnvil() {
		super(tetAnvil);
		inVoid = new ArrayList<IVoid>();
	}

	public ItemStackHandler getInventory() {
		return inventory;
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		CompoundNBT nbt = new CompoundNBT();
		compound = inventory.serializeNBT();
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
		inventory.deserializeNBT(compound);
		CompoundNBT nbt = compound.getCompound("arenergy");
		if(!nbt.isEmpty()) {
			voidEnergy = nbt.getInt("void");
		}
		tag = compound;
	}

	@Override
	public Container createMenu(int id, PlayerInventory inv, PlayerEntity entity) {
		return new RelicAnvilContainer(id, inv, inventory);
	}

	@Override
	public ITextComponent getDisplayName() {
		return new StringTextComponent("Relic Anvil");
	}

	@Override
	public void tick() {
		if(!world.isRemote) {
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
			ItemStack stack = inventory.getStackInSlot(1);
			if(isRelic(stack) && voidEnergy >= 1000) {
				ItemStack stack2 = inventory.getStackInSlot(0);
				if((!stack2.hasTag() || !stack2.getTag().contains("hasAREnchant")) && RelicListConfig.bakedMap.containsKey(stack2.getItem().getRegistryName().toString())) {
					if(stack.hasTag() && stack.getTag().contains("ar_enchant_data") && stack.getTag().getCompound("ar_enchant_data").contains(RelicListConfig.bakedMap.get(stack2.getItem().getRegistryName().toString()))) {
						String type = RelicListConfig.bakedMap.get(stack2.getItem().getRegistryName().toString());
						String modifier = stack.getTag().getCompound("ar_enchant_data").getString(type);
						int tier = stack.getTag().getCompound("ar_enchant_data").getInt("tier");
						ItemStack stackOut = stack2.copy();
						stackOut.addAttributeModifier(modifier, new AttributeModifier(modifier, getValue(modifier, tier, type, stackOut), AttributeModifier.Operation.ADDITION), getSlot(type));
						stackOut.getTag().putBoolean("hasAREnchant", true);
						inventory.setStackInSlot(2, stackOut);
						extracted = false;
					} else {
						inventory.setStackInSlot(2, ItemStack.EMPTY);
					}
				} else {
					inventory.setStackInSlot(2, ItemStack.EMPTY);
				}
			} else {
				inventory.setStackInSlot(2, ItemStack.EMPTY);
			}
		}
	}
	
	public double getValue(String modifier, int tier, String type, ItemStack stack) {
		if(modifier.equals("generic.attackSpeed")) {
			double base = 0;
			Iterator<AttributeModifier> iter = stack.getAttributeModifiers(getSlot(type)).get(modifier).iterator();
			while(iter.hasNext()) {
				base += iter.next().getAmount();
			}
			double value = base + 0.6 * tier;
			return value;
		}else if(modifier.equals("generic.movementSpeed")) {
			return 0.05 * tier;
		}
		return 0;
	}
	
	public EquipmentSlotType getSlot(String type) {
		if(type.equals("SHIELD")) return EquipmentSlotType.OFFHAND;
		else if(type.equals("HELMET")) return EquipmentSlotType.HEAD;
		else if(type.equals("CHESTPLATE")) return EquipmentSlotType.CHEST;
		else if(type.equals("LEGGINGS")) return EquipmentSlotType.LEGS;
		else if(type.equals("BOOTS")) return EquipmentSlotType.FEET;
		else if(type.equals("BAUBLE")) return EquipmentSlotType.MAINHAND;
		else return EquipmentSlotType.MAINHAND;
	}
	
	private boolean isRelic(ItemStack stack) {
		return stack.getItem() == ARItems.PERIDOT_RELIC || stack.getItem() == ARItems.PERIDOT_RELIC_1 || stack.getItem() == ARItems.RUBY_RELIC || stack.getItem() == ARItems.RUBY_RELIC_1 || stack.getItem() == ARItems.SAPPHIRE_RELIC || stack.getItem() == ARItems.SAPPHIRE_RELIC_1 || stack.getItem() == ARItems.RUBY_RELIC_2 || stack.getItem() == ARItems.SAPPHIRE_RELIC_2 || stack.getItem() == ARItems.PERIDOT_RELIC_2;
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
