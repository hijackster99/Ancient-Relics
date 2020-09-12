package com.hijackster99.blocks.containers;

import com.hijackster99.core.References;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class PedestalScreen extends ContainerScreen<PedestalContainer> {

	public PedestalScreen(PedestalContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		minecraft.textureManager.bindTexture(new ResourceLocation(References.MODID, "textures/gui/pedestal.png"));
		blit(getGuiLeft(), getGuiTop(), 0, 0, getXSize(), getYSize());
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		renderHoveredToolTip(mouseX - getGuiLeft(), mouseY - getGuiTop());
	}

}
