package com.mrcrayfish.key;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.LockCode;

public class LockedDoor {

	private BlockPos pos;
	private LockCode code;

    public LockedDoor()
    {
        this.code = LockCode.EMPTY_CODE;
    }
    
    public LockedDoor(BlockPos pos)
    {
    	this.pos = pos;
        this.code = LockCode.EMPTY_CODE;
    }
    
    public void readFromNBT(NBTTagCompound compound)
    {
    	this.pos = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
        this.code = LockCode.fromNBT(compound);
    }
    
    public void writeToNBT(NBTTagCompound compound)
    {
    	compound.setInteger("x", pos.getX());
    	compound.setInteger("y", pos.getY());
    	compound.setInteger("z", pos.getZ());
    	
        if (this.code != null)
        {
            this.code.toNBT(compound);
        }
    }

    public boolean isLocked()
    {
        return this.code != null && !this.code.isEmpty();
    }

    public LockCode getLockCode()
    {
        return this.code;
    }

    public void setLockCode(LockCode code)
    {
        this.code = code;
    }
    
    public boolean isCorrectKey(EntityPlayer player)
    {
    	for(ItemStack stack : player.inventory.mainInventory)
		{
			if(stack != null && stack.getItem() == KeyItems.item_key)
			{
				if(getLockCode().getLock().equals(stack.getDisplayName()))
				{
					return true;
				}
			}
		}
		return false;
    }
    
    public BlockPos getPos()
    {
    	return pos;
    }
}
