package com.mrcrayfish.key.gui;

import java.util.UUID;

import com.mrcrayfish.key.items.KeyItems;
import com.mrcrayfish.key.util.NBTHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryKeys extends InventoryBasic 
{
	private EntityPlayer player;
	private ItemStack keys;
	private boolean reading = false;
	private String uniqueId = "";
	
	public InventoryKeys(EntityPlayer player, ItemStack keys) 
	{
		super("KeyRing", false, getInventorySize());
		this.player = player;
		this.keys = keys;
		if(!hasInventory())
		{
			uniqueId = UUID.randomUUID().toString();
			createInventory();
		}
		loadInventory();
	}
	
	@Override
	public void openInventory(EntityPlayer player)
	{
		loadInventory();
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
		saveInventory();
	}
	
	@Override
	public void markDirty()
	{
		super.markDirty();
		if (!reading)
		{
			saveInventory();
		}
	}
	
	@Override
	public int getInventoryStackLimit() 
	{
		return 1;
	}

	protected static int getInventorySize()
	{
		return 6;
	}
	
	protected boolean hasInventory()
	{
		return NBTHelper.getCompoundTag(keys, "KeyRing").hasKey("Keys");
	}
	
	protected void createInventory()
	{
		writeToNBT();
	}
	
	public void loadInventory()
	{
		readFromNBT();
	}

	public void saveInventory()
	{
		writeToNBT();
		setNBT();
	}
	
	protected void setNBT()
	{
		for (ItemStack itemStack : player.inventory.mainInventory)
		{
			if (itemStack != null && itemStack.getItem() == KeyItems.item_key_ring)
			{
				NBTTagCompound nbt = itemStack.getTagCompound();
				if (nbt != null)
				{
					if (nbt.getCompoundTag("KeyRing").getString("UniqueID").equals(uniqueId))
					{
						itemStack.setTagCompound(keys.getTagCompound());
						break;
					}
				}
			}
		}
	}
	
	protected void writeToNBT()
	{
		NBTTagList tagList = new NBTTagList();
		for (int slot = 0; slot < getSizeInventory(); slot++)
		{
			if (getStackInSlot(slot) != null)
			{
				NBTTagCompound tagCompound = new NBTTagCompound();
				tagCompound.setByte("Slot", (byte) slot);
				getStackInSlot(slot).writeToNBT(tagCompound);
				tagList.appendTag(tagCompound);
			}
		}
		NBTTagCompound inventory = new NBTTagCompound();
		inventory.setTag("Keys", tagList);
		inventory.setString("UniqueID", uniqueId);
		NBTHelper.setCompoundTag(keys, "KeyRing", inventory);
	}
	
	protected void readFromNBT()
	{
		reading = true;
		NBTTagCompound nbt = NBTHelper.getCompoundTag(keys, "KeyRing");
		if ("".equals(uniqueId))
		{
			this.uniqueId = nbt.getString("UniqueID");
			if ("".equals(uniqueId))
			{
				this.uniqueId = UUID.randomUUID().toString();
			}
		}
		NBTTagList tagList = (NBTTagList) NBTHelper.getCompoundTag(keys, "KeyRing").getTag("Keys");
		if(tagList != null)
		{
			for (int i = 0; i < tagList.tagCount(); i++)
			{
				NBTTagCompound tagCompound = (NBTTagCompound) tagList.getCompoundTagAt(i);
				int slot = tagCompound.getByte("Slot") & 0xff;
	
				if (slot >= 0 && slot < getSizeInventory())
				{
					setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(tagCompound));
				}
			}
		}
		reading = false;
	}
}
