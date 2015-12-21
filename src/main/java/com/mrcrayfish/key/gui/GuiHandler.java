package com.mrcrayfish.key.gui;

import com.mrcrayfish.key.items.ItemKeys;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler 
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if(ID == GuiKeys.ID)
		{
			return new ContainerKeys(player.inventory, ItemKeys.getInv(player));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if(ID == GuiKeys.ID)
		{
			return new GuiKeys(player.inventory, ItemKeys.getInv(player), player);
		}
		return null;
	}
}
