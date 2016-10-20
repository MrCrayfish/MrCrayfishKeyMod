package com.mrcrayfish.key.lock.type;

import com.mrcrayfish.key.items.KeyItems;
import com.mrcrayfish.key.lock.ILock;
import com.mrcrayfish.key.lock.LockData;
import com.mrcrayfish.key.lock.LockManager;
import com.mrcrayfish.key.lock.WorldLockData;
import com.mrcrayfish.key.util.MessageUtil;
import com.mrcrayfish.key.util.NBTHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;

public class LockContainer implements ILock 
{
	@Override
	public boolean handleInteract(EntityPlayer player, World world, BlockPos pos) 
	{
		System.out.println("Handling");
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof TileEntityLockable)
		{
			TileEntityLockable tileEntityLockable = (TileEntityLockable) tileEntity;
			ItemStack current = player.getCurrentEquippedItem();
			
			if(tileEntityLockable.isLocked())
			{	
				if(current != null && current.getItem() == KeyItems.item_key)
				{
					if(!tileEntityLockable.getLockCode().getLock().equals(current.getDisplayName()))
					{
						world.playSoundAtEntity(player, "fire.ignite", 1.0F, 1.0F);
						MessageUtil.sendSpecial(player, EnumChatFormatting.YELLOW + "This key does not fit the lock.");
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
							if(tileEntityLockable.getLockCode().getLock().equals(key.getDisplayName()))
							{
								current.setStackDisplayName(key.getDisplayName());
								hasCorrectKey = true;
								break;
							}
						}
					}
					if(!hasCorrectKey)
					{
						world.playSoundAtEntity(player, "fire.ignite", 1.0F, 1.0F);
						MessageUtil.sendSpecial(player, EnumChatFormatting.YELLOW + "None of the keys fit the lock.");
						return true;
					}
				}
				else
				{
					if(!world.isRemote)
					{
						world.playSoundAtEntity(player, "random.wood_click", 1.0F, 1.0F);
						MessageUtil.sendSpecial(player, EnumChatFormatting.YELLOW + "This block is locked with a key.");
						return true;
					}
				}
			}
			else
			{
				if(current != null && current.getItem() == KeyItems.item_key)
				{
					if(!current.getDisplayName().equals(StatCollector.translateToLocal(current.getItem().getUnlocalizedName() +".name")))
					{
						if(!world.isRemote)
						{
							tileEntityLockable.setLockCode(new LockCode(current.getDisplayName()));
							world.playSoundAtEntity(player, "random.click", 1.0F, 1.0F);
							MessageUtil.sendSpecial(player, EnumChatFormatting.GREEN + "Successfully locked the block with the key: " + EnumChatFormatting.RESET + current.getDisplayName());
							return true;
						}
					}
					else
					{
						MessageUtil.sendSpecial(player, EnumChatFormatting.YELLOW + "The key needs to be renamed before you can lock this block.");
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean handleBreak(EntityPlayer player, World world, BlockPos pos) 
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof TileEntityLockable)
		{
			TileEntityLockable tileEntityLockable = (TileEntityLockable) tileEntity;
			if(tileEntityLockable.isLocked())
			{
				if(!LockManager.isKeyInInvetory(player, tileEntityLockable.getLockCode()))
				{
					MessageUtil.sendSpecial(player, EnumChatFormatting.YELLOW + "You need to have correct key in your inventory to destroy this block.");
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public BlockPos fix(World world, BlockPos pos)
	{
		return pos;
	}
	
	@Override
	public LockCode getLockCode(World world, BlockPos pos) 
	{
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity instanceof TileEntityLockable)
		{
			TileEntityLockable tileEntityLockable = (TileEntityLockable) tileEntity;
			if(tileEntityLockable.isLocked())
			{
				return tileEntityLockable.getLockCode();
			}
		}
		return null;
	}
}
