package com.mrcrayfish.key.gui;

import com.mrcrayfish.key.items.KeyItems;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiKeys extends GuiContainer {
	
	public static final int ID = 0;
	private static final ResourceLocation gui = new ResourceLocation("ckm:textures/gui/keys.png");
	private IInventory inventoryKeys;
	private EntityPlayer player;
	
	public GuiKeys(IInventory inventoryPlayer, IInventory inventoryKeys, EntityPlayer player) 
	{
		super(new ContainerKeys(inventoryPlayer, inventoryKeys));
		this.inventoryKeys = inventoryKeys;
		this.player = player;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		if (player.inventory.getCurrentItem() == null | (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() != KeyItems.item_key_ring))
		{
			this.mc.thePlayer.closeScreen();
		}
		
		this.fontRendererObj.drawString("Key Ring", xSize / 2 - 21, 5, 4210752);
		this.fontRendererObj.drawString("Inventory", 8, (ySize - 96) + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(gui);
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;
		this.drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
	}
	
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		this.inventoryKeys.closeInventory(null);
	}

}
