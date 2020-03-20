package com.hijackster99.tileentities.renderer;

import com.hijackster99.tileentities.TileEntityPedestal;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;

public class PedestalTER extends TileEntityRenderer<TileEntityPedestal> {

	ItemRenderer rei = null;

	public PedestalTER() {
		super();
		rei = new ItemRenderer(Minecraft.getInstance().getRenderManager(),
				Minecraft.getInstance().getItemRenderer()) {
			@Override
			public boolean shouldSpreadItems() {
				return false;
			}
			
			@Override
			public boolean shouldBob() {
				return false;
			}
			
		};
	}
	
	@Override
	public void render(TileEntityPedestal te, double x, double y, double z, float partialTicks, int destroyStage) {
		
		te.checkEntity();
		te.updateEntity(partialTicks);
		
		GlStateManager.pushMatrix();
		GlStateManager.pushLightingAttributes();
		rei.doRender(te.getPedestalRender().getEntity(), x + 0.5, y + 0.625f, z + 0.5, 0, partialTicks);
		GlStateManager.popAttributes();
		GlStateManager.popMatrix();

	}
	
}
