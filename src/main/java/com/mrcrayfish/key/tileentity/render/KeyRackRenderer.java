package com.mrcrayfish.key.tileentity.render;

import com.mrcrayfish.key.blocks.BlockKeyRack;
import com.mrcrayfish.key.items.KeyItems;
import com.mrcrayfish.key.tileentity.TileEntityKeyRack;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class KeyRackRenderer extends TileEntitySpecialRenderer {
	private EntityItem keyEntity = new EntityItem(Minecraft.getMinecraft().theWorld, 0D, 0D, 0D);

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double posX, double posY, double posZ, float p_180535_8_, int p_180535_9_) 
	{
		Block block = tileEntity.getBlockType();
		if (!(block instanceof BlockKeyRack))
			return;

		if (tileEntity.getWorld().isAirBlock(tileEntity.getPos()))
			return;

		int metadata = block.getMetaFromState(tileEntity.getWorld().getBlockState(tileEntity.getPos()));

		TileEntityKeyRack keyRack = (TileEntityKeyRack) tileEntity;

		GlStateManager.pushMatrix();
		{
			GlStateManager.translate((float) posX + 0.5F, (float) posY + 0.5F, (float) posZ + 0.5F);
			GlStateManager.rotate(metadata * -90F, 0, 1, 0);
			GlStateManager.translate(0.065F, -0.13F, 0.4F);
			this.keyEntity.hoverStart = 0.0F;

			for (int i = 0; i < keyRack.getSizeInventory(); i++) 
			{
				ItemStack key = keyRack.getStackInSlot(i);
				if (key != null) {
					GlStateManager.pushMatrix();
					{
						keyEntity.setEntityItemStack(key);
						GlStateManager.scale(0.75F, 0.75F, 0.75F);
						GlStateManager.rotate(90F, 0, 0, 1);
						GlStateManager.rotate(180F, 1, 0, 0);

						if (key.getItem() == KeyItems.item_key_ring) 
						{
							GlStateManager.rotate(-90F, 0, 0, 1);
							GlStateManager.translate(-0.35F, -0.375F, 0F);
							GlStateManager.rotate(5F, 0, 1, 0);
						} 
						else 
						{
							GlStateManager.translate(0F, 0F, -0.025F);
							GlStateManager.rotate(5F, 1, 0, 0);
						}

						Minecraft.getMinecraft().getRenderManager().renderEntityWithPosYaw(keyEntity, 0, 0, 0, 0, 0);
					}
					GlStateManager.popMatrix();
				}
				GlStateManager.translate(-0.2175F, 0F, 0F);
			}
		}
		GlStateManager.popMatrix();
	}
}
