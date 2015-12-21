package com.mrcrayfish.key.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerKeys extends Container 
{
	private IInventory inventoryPlayer, inventoryKeys;
	
	public ContainerKeys(IInventory inventoryPlayer, IInventory inventoryKeys) 
	{
		this.inventoryKeys = inventoryKeys;
		inventoryKeys.openInventory(null);
		
		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 3; ++j)
			{
				this.addSlotToContainer(new SlotKey(inventoryKeys, j + i * 3, j * 18 + 62, i * 18 + 17));
			}
		}
		
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, j * 18 + 8, i * 18 + 84));
			}
		}

		for (int i = 0; i < 9; i++)
		{
			this.addSlotToContainer(new Slot(inventoryPlayer, i, i * 18 + 8, 142));
		}
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum)
	{
		ItemStack itemCopy = null;
		Slot slot = (Slot) this.inventorySlots.get(slotNum);

		if (slot != null && slot.getHasStack())
		{
			ItemStack item = slot.getStack();
			itemCopy = item.copy();

			if (slotNum < 6)
			{
				if (!this.mergeItemStack(item, 6, this.inventorySlots.size(), true))
				{
					return null;
				}
			}
			else if (!this.mergeItemStack(item, 0, 6, false))
			{
				return null;
			}

			if (item.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}
		}

		return itemCopy;
	}
	
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) 
	{
		return inventoryKeys.isUseableByPlayer(playerIn);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);
		inventoryKeys.closeInventory(player);
	}

}
