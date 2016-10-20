package com.mrcrayfish.key.lock.type;

import com.mrcrayfish.key.items.KeyItems;
import com.mrcrayfish.key.lock.ILock;
import com.mrcrayfish.key.lock.LockData;
import com.mrcrayfish.key.lock.WorldLockData;
import com.mrcrayfish.key.util.MessageUtil;
import com.mrcrayfish.key.util.NBTHelper;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;

public class LockDoor implements ILock 
{
	@Override
	public boolean handleInteract(EntityPlayer player, World world, BlockPos pos) 
	{
		System.out.println("Test");
		if(world.getBlockState(pos).getBlock() instanceof BlockDoor && world.getBlockState(pos).getBlock() != Blocks.iron_door)
		{
			BlockDoor blockDoor = (BlockDoor) world.getBlockState(pos).getBlock();
			IBlockState state = world.getBlockState(pos);
			if(((BlockDoor.EnumDoorHalf)state.getValue(BlockDoor.HALF)) == BlockDoor.EnumDoorHalf.UPPER)
			{
				pos = pos.down();
			}
			
			WorldLockData worldLockData = WorldLockData.get(world);
			LockData lockedDoor = worldLockData.getLock(pos);
			ItemStack current = player.getCurrentEquippedItem();
			
			if(lockedDoor != null)
			{
				if(lockedDoor.isLocked())
				{
					if(current != null && current.getItem() == KeyItems.item_key)
					{
						if(!lockedDoor.getLockCode().getLock().equals(current.getDisplayName()))
						{
							world.playSoundAtEntity(player, "fire.ignite", 1.0F, 1.0F);
							MessageUtil.sendSpecial(player, EnumChatFormatting.YELLOW + "This key does not fit the lock.");
							world.markBlockForUpdate(pos);
							return true;
						}
					}
					else if(current != null && current.getItem() == KeyItems.item_key_ring)
					{
						boolean hasCorrectKey = false;
						NBTTagList keys = (NBTTagList) NBTHelper.getCompoundTag(current, "KeyRing").getTag("Keys");
						if(keys != null)
						{
							for(int i = 0; i < keys.tagCount(); i++)
							{
								ItemStack key = ItemStack.loadItemStackFromNBT(keys.getCompoundTagAt(i));
								if(lockedDoor.getLockCode().getLock().equals(key.getDisplayName()))
								{
									hasCorrectKey = true;
									break;
								}
							}
						}
						if(!hasCorrectKey)
						{
							world.playSoundAtEntity(player, "fire.ignite", 1.0F, 1.0F);
							MessageUtil.sendSpecial(player, EnumChatFormatting.YELLOW + "None of the keys fit the lock.");
							world.markBlockForUpdate(pos);
							return true;
						}
					}
					else
					{
						world.playSoundAtEntity(player, "random.wood_click", 1.0F, 1.0F);
						MessageUtil.sendSpecial(player, EnumChatFormatting.YELLOW + "This door is locked with a key.");
						world.markBlockForUpdate(pos);
						return true;
					}
				}
				else
				{
					if(current != null && current.getItem() == KeyItems.item_key)
					{
						if(!current.getDisplayName().equals(StatCollector.translateToLocal(current.getItem().getUnlocalizedName() + ".name")))
						{
							lockedDoor.setLockCode(new LockCode(current.getDisplayName()));
							world.playSoundAtEntity(player, "random.click", 1.0F, 1.0F);
							MessageUtil.sendSpecial(player, EnumChatFormatting.GREEN + "Successfully locked the door with the key: " + EnumChatFormatting.RESET + current.getDisplayName());
							worldLockData.markDirty();
						}
						else
						{
							MessageUtil.sendSpecial(player, EnumChatFormatting.YELLOW + "The key needs to be renamed before you can lock this door.");
							world.markBlockForUpdate(pos);
						}
						if(!world.isRemote)
						{
							return true;
						}
					}
					else if(current != null && current.getItem() == KeyItems.item_key_ring)
					{
						MessageUtil.sendSpecial(player, EnumChatFormatting.YELLOW + "You can only lock a door with a single key.");
					}
					else
					{
						return false;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean handleBreak(EntityPlayer player, World world, BlockPos pos) 
	{
		return false;
	}

	@Override
	public BlockPos fix(World world, BlockPos pos) 
	{
		if(world.getBlockState(pos).getBlock() instanceof BlockDoor && world.getBlockState(pos).getBlock() != Blocks.iron_door)
		{
			IBlockState state = world.getBlockState(pos);
			if(state.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.UPPER)
			{
				return pos.down();
			}
		}
		return pos;
	}

	@Override
	public LockCode getLockCode(World world, BlockPos pos) 
	{
		WorldLockData worldLockData = WorldLockData.get(world);
		LockData lockData = worldLockData.getLock(pos);
		if(lockData != null)
		{
			return lockData.getLockCode();
		}
		return null;
	}
}
