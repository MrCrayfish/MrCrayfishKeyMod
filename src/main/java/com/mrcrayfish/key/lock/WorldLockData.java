package com.mrcrayfish.key.lock;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class WorldLockData extends WorldSavedData 
{
	private static final String IDENTIFIER = "locked_data";
	private List<LockData> lockedData = new ArrayList<LockData>();
	
	public WorldLockData() 
	{
		super(IDENTIFIER);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		lockedData.clear();
		
		if(nbt.hasKey("blocks"))
		{
			NBTTagList tagList = (NBTTagList) nbt.getTag("blocks");
			
			for(int i = 0; i < tagList.tagCount(); i++)
			{
				NBTTagCompound lockTag = tagList.getCompoundTagAt(i);
				LockData lock = new LockData();
				lock.readFromNBT(lockTag);
				lockedData.add(lock);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		NBTTagList tagList = new NBTTagList();
		
		for(LockData lock : lockedData)
		{
			NBTTagCompound lockTag = new NBTTagCompound();
			lock.writeToNBT(lockTag);
			tagList.appendTag(lockTag);
		}
		
		nbt.setTag("blocks", tagList);
	}
	
	public static WorldLockData get(World world) 
	{
		WorldLockData data = (WorldLockData) world.loadItemData(WorldLockData.class, IDENTIFIER);
		if (data == null) 
		{
			data = new WorldLockData();
			world.setItemData(IDENTIFIER, data);
		}
		return data;
	}
	
	public void addLock(BlockPos pos)
	{
		markDirty();
		lockedData.add(new LockData(pos));
	}
	
	public LockData getLock(BlockPos pos)
	{
		for(LockData lock : lockedData)
		{
			if(lock.getPos().equals(pos))
			{
				return lock;
			}
		}
		return null;
	}
	
	public void removeLock(BlockPos pos)
	{
		for(LockData lock : lockedData)
		{
			if(lock.getPos().equals(pos))
			{
				lockedData.remove(lock);
				return;
			}
		}
	}
}
