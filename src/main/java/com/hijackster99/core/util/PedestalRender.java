package com.hijackster99.core.util;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;

public class PedestalRender {
	
	private ItemEntity entity = new ItemEntity(null, 0, 0, 0, ItemStack.EMPTY);
	private float tick = 0;
	
	public ItemEntity getEntity() {
		return entity;
	}
	public void setEntity(ItemEntity entity) {
		this.entity = entity;
	}
	public float getTick() {
		return tick;
	}
	public void setTick(float tick) {
		this.tick = tick;
	}
	
	

}
