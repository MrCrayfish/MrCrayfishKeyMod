package com.mrcrayfish.key.gui;

import com.mrcrayfish.key.items.KeyItems;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotKeyRack extends Slot {

	public SlotKeyRack(IInventory inventoryIn, int index, int xPosition, int yPosition) 
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) 
	{
		return (stack.getItem() == KeyItems.item_key && stack.hasDisplayName()) | stack.getItem() == KeyItems.item_key_ring;
	}
}
