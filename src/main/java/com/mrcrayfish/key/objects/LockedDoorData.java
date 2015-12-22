package com.mrcrayfish.key.objects;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class LockedDoorData extends WorldSavedData {

	private static final String IDENTIFIER = "locked_doors";
	private List<LockedDoor> lockedDoors = new ArrayList<LockedDoor>();
	
	public LockedDoorData() 
	{
		super(IDENTIFIER);
	}
	
	public LockedDoorData(String identifier) 
	{
		super(identifier);
	}
	
	public void addDoor(BlockPos pos)
	{
		markDirty();
		lockedDoors.add(new LockedDoor(pos));
	}
	
	public LockedDoor getDoor(BlockPos pos)
	{
		for(LockedDoor door : lockedDoors)
		{
			if(door.getPos().equals(pos))
			{
				return door;
			}
		}
		return null;
	}
	
	public void removeDoor(BlockPos pos)
	{
		for(LockedDoor door : lockedDoors)
		{
			if(door.getPos().equals(pos))
			{
				lockedDoors.remove(door);
				return;
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		lockedDoors.clear();
		
		if(nbt.hasKey("lockedDoors"))
		{
			NBTTagList tagList = (NBTTagList) nbt.getTag("lockedDoors");
			
			for(int i = 0; i < tagList.tagCount(); i++)
			{
				NBTTagCompound doorNbt = tagList.getCompoundTagAt(i);
				LockedDoor door = new LockedDoor();
				door.readFromNBT(doorNbt);
				lockedDoors.add(door);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		NBTTagList tagList = new NBTTagList();
		
		for(LockedDoor door : lockedDoors)
		{
			NBTTagCompound doorNbt = new NBTTagCompound();
			door.writeToNBT(doorNbt);
			tagList.appendTag(doorNbt);
		}
		
		nbt.setTag("lockedDoors", tagList);
	}
	
	public static LockedDoorData get(World world) 
	{
		LockedDoorData data = (LockedDoorData)world.loadItemData(LockedDoorData.class, IDENTIFIER);
		if (data == null) 
		{
			data = new LockedDoorData();
			world.setItemData(IDENTIFIER, data);
		}
		return data;
	}
}
