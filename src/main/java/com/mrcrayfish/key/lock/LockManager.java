package com.mrcrayfish.key.lock;

import java.util.HashMap;
import java.util.Map;

import com.mrcrayfish.key.items.KeyItems;
import com.mrcrayfish.key.lock.type.LockContainer;
import com.mrcrayfish.key.lock.type.LockDoor;
import com.mrcrayfish.key.util.NBTHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.BlockPos;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;

public class LockManager 
{
	private static Map<Object, ILock> lockTypes = new HashMap<Object, ILock>();
	
	public static void registerTypes() 
	{
		lockTypes.put(TileEntityLockable.class, new LockContainer());
		lockTypes.put(BlockDoor.class, new LockDoor());
	}

	public static boolean onInteract(Block block, TileEntity tileEntity, EntityPlayer player, World world, BlockPos pos) 
	{
		for(Object object : lockTypes.keySet())
		{
			Class clazz = (Class) object;
			if(clazz.isInstance(block) | clazz.isInstance(tileEntity))
			{
				return lockTypes.get(object).handleInteract(player, world, pos);
			}
		}
		return false;
	}
	
	public static boolean onBreak(Block block, TileEntity tileEntity, EntityPlayer player, World world, BlockPos pos) 
	{
		for(Object object : lockTypes.keySet())
		{
			Class clazz = (Class) object;
			if(clazz.isInstance(block) | clazz.isInstance(tileEntity))
			{
				return lockTypes.get(object).handleBreak(player, world, pos);
			}
		}
		return false;
	}
	
	public static BlockPos fix(World world, BlockPos pos) 
	{
		Block block = world.getBlockState(pos).getBlock();
		TileEntity tileEntity = world.getTileEntity(pos);
		for(Object object : lockTypes.keySet())
		{
			Class clazz = (Class) object;
			if(clazz.isInstance(block) | clazz.isInstance(tileEntity))
			{
				return lockTypes.get(object).fix(world, pos);
			}
		}
		return pos;
	}
	
	public static boolean isKeyInInvetory(EntityPlayer player, LockCode code)
	{
		for(ItemStack stack : player.inventory.mainInventory)
		{
			if(stack != null && stack.getItem() == KeyItems.item_key)
			{
				if(code.getLock().equals(stack.getDisplayName()))
				{
					return true;
				}
			}
			else if(stack != null && stack.getItem() == KeyItems.item_key_ring)
			{
				NBTTagList keys = (NBTTagList) NBTHelper.getCompoundTag(stack, "KeyRing").getTag("Keys");
				if(keys != null)
				{
					for(int i = 0; i < keys.tagCount(); i++)
					{
						ItemStack key = ItemStack.loadItemStackFromNBT(keys.getCompoundTagAt(i));
						if(code.getLock().equals(key.getDisplayName()))
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public static BlockPos isLockAround(World world, BlockPos pos)
	{
		BlockPos checkPos;
		WorldLockData worldLockData = WorldLockData.get(world);
		for(int x = pos.getX() - 1; x <= pos.getX() + 1; x++)
		{
			for(int y = pos.getY() - 1; y <= pos.getY() + 1; y++)
			{
				for(int z = pos.getZ() - 1; z <= pos.getZ() + 1; z++)
				{
					checkPos = new BlockPos(x, y, z);
					if(world.getTileEntity(checkPos) instanceof TileEntityLockable)
					{
						TileEntityLockable lockable = (TileEntityLockable) world.getTileEntity(checkPos);
						if(lockable.isLocked())
						{
							return checkPos;
						}
					}
					else
					{
						LockData lock = worldLockData.getLock(checkPos);
						if(lock != null)
						{
							checkPos = fix(world, checkPos);
							return checkPos;
						}
					}
				}
			}
		}
		return null;
	}
	
	private static boolean hasPower(World world, BlockPos pos)
	{
		BlockPos checkPos;
		for(int x = pos.getX() - 1; x <= pos.getX() + 1; x++)
		{
			for(int y = pos.getY() - 1; y <= pos.getY() + 1; y++)
			{
				for(int z = pos.getZ() - 1; z <= pos.getZ() + 1; z++)
				{
					checkPos = new BlockPos(x, y, z);
					if(world.isBlockPowered(checkPos))
					{
						return true;
					}
				}
			}
		}
		return false;
	}	
}
