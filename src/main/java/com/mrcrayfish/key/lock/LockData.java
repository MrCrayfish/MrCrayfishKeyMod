package com.mrcrayfish.key.lock;

import com.mrcrayfish.key.items.KeyItems;
import com.mrcrayfish.key.util.NBTHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.LockCode;

public class LockData {

	private BlockPos pos;
	private LockCode code;

    public LockData()
    {
        this.code = LockCode.EMPTY_CODE;
    }
    
    public LockData(BlockPos pos)
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
    
    public BlockPos getPos()
    {
    	return pos;
    }
}
