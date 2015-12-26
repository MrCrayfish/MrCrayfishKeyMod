package com.mrcrayfish.key;

import com.mrcrayfish.key.items.KeyItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class KeyTab extends CreativeTabs 
{
	public KeyTab(String label) 
	{
		super(label);
	}

	@Override
	public Item getTabIconItem() 
	{
		return KeyItems.item_key;
	}
}
