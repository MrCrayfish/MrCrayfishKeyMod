package com.mrcrayfish.key.items;

import java.util.List;

import com.mrcrayfish.key.MrCrayfishKeyMod;
import com.mrcrayfish.key.gui.GuiKeys;
import com.mrcrayfish.key.gui.InventoryKeys;
import com.mrcrayfish.key.objects.LockedDoor;
import com.mrcrayfish.key.objects.LockedDoorData;
import com.mrcrayfish.key.util.NBTHelper;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;

public class ItemKeys extends Item 
{
	public ItemKeys() 
	{
		this.setMaxStackSize(1);
		this.setHasSubtypes(true);
	}
	
	@Override
	public boolean getShareTag() 
	{
		return true;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) 
	{
		NBTTagCompound container = NBTHelper.getCompoundTag(stack, "KeyRing");
		if(container.hasKey("Keys"))
		{
			NBTTagList keys = (NBTTagList) container.getTag("Keys");
			if(keys.tagCount() > 0)
			{
				tooltip.add(EnumChatFormatting.GOLD.toString() + "Keys:");
				for(int i = 0; i < keys.tagCount(); i++)
				{
					ItemStack key = ItemStack.loadItemStackFromNBT(keys.getCompoundTagAt(i));
					tooltip.add("- " + key.getDisplayName());
				}
			}			
		}
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		if(!worldIn.isRemote)
		{
			if(playerIn.isSneaking())
			{
				EntityPlayerMP playerMp = (EntityPlayerMP) playerIn;
				TileEntity tileEntity = worldIn.getTileEntity(pos);
				if(tileEntity instanceof TileEntityLockable)
				{
					TileEntityLockable tileEntityLockable = (TileEntityLockable) tileEntity;
					if(tileEntityLockable.isLocked())
					{	
						boolean hasCorrectKey = false;
						NBTTagList keys = (NBTTagList) NBTHelper.getCompoundTag(stack, "KeyRing").getTag("Keys");
						if(keys != null)
						{
							for(int i = 0; i < keys.tagCount(); i++)
							{
								ItemStack key = ItemStack.loadItemStackFromNBT(keys.getCompoundTagAt(i));
								if(tileEntityLockable.getLockCode().getLock().equals(key.getDisplayName()))
								{
									hasCorrectKey = true;
									break;
								}
							}
						}
						if(hasCorrectKey)
						{
							tileEntityLockable.setLockCode(LockCode.EMPTY_CODE);
							playerMp.playerNetServerHandler.sendPacket(new S02PacketChat((new ChatComponentText(EnumChatFormatting.GREEN + "Succesfully unlocked this block.")), (byte)2));
						}
						else
						{
							playerMp.playerNetServerHandler.sendPacket(new S02PacketChat((new ChatComponentText(EnumChatFormatting.YELLOW + "You need to have correct key to unlock this block.")), (byte)2));
						}
					}
					return true;
				}
				else if(worldIn.getBlockState(pos).getBlock() instanceof BlockDoor && worldIn.getBlockState(pos).getBlock() != Blocks.iron_door)
				{
					BlockDoor blockDoor = (BlockDoor) worldIn.getBlockState(pos).getBlock();
					IBlockState state = worldIn.getBlockState(pos);
					if(((BlockDoor.EnumDoorHalf)state.getValue(BlockDoor.HALF)) == BlockDoor.EnumDoorHalf.UPPER)
					{
						pos = pos.down();
					}
					
					LockedDoorData lockedDoorData = LockedDoorData.get(worldIn);
					LockedDoor lockedDoor = lockedDoorData.getDoor(pos);
					if(lockedDoor != null)
					{
						if(lockedDoor.isLocked())
						{
							boolean hasCorrectKey = false;
							NBTTagList keys = (NBTTagList) NBTHelper.getCompoundTag(stack, "KeyRing").getTag("Keys");
							if(keys != null)
							{
								for(int i = 0; i < keys.tagCount(); i++)
								{
									ItemStack key = ItemStack.loadItemStackFromNBT(keys.getCompoundTagAt(i));
									if(lockedDoor.getLockCode().getLock().equals(key.getDisplayName()))
									{
										hasCorrectKey = true;
									}
								}
							}
							if(hasCorrectKey)
							{
								lockedDoor.setLockCode(LockCode.EMPTY_CODE);
								playerMp.playerNetServerHandler.sendPacket(new S02PacketChat((new ChatComponentText(EnumChatFormatting.GREEN + "Succesfully unlocked this block.")), (byte)2));
								lockedDoorData.markDirty();
							}
							else
							{
								playerMp.playerNetServerHandler.sendPacket(new S02PacketChat((new ChatComponentText(EnumChatFormatting.YELLOW + "You need to have correct key to unlock this block.")), (byte)2));
							}
						}
					}
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		if (!worldIn.isRemote)
		{
			playerIn.openGui(MrCrayfishKeyMod.instance, GuiKeys.ID, worldIn, 0, 0, 0);
		}
		return stack;
	}
	
	public static IInventory getInv(EntityPlayer playerIn)
	{
		ItemStack keys = playerIn.getCurrentEquippedItem();
		if (keys != null && keys.getItem() instanceof ItemKeys)
		{
			return new InventoryKeys(playerIn, keys);
		}
		return null;
	}
	
	public String getUnlocalizedName(ItemStack stack)
    {
		return super.getUnlocalizedName() + "_" + getMetadata(stack);
    }

	@Override
	public int getMetadata(ItemStack stack)
    {
		NBTTagList tagList = (NBTTagList) NBTHelper.getCompoundTag(stack, "KeyRing").getTag("Keys");
		if(tagList == null) return 0;
		return (tagList.tagCount() + 1) / 2;
    }
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) 
	{
		NBTTagList tagList = (NBTTagList) NBTHelper.getCompoundTag(stack, "KeyRing").getTag("Keys");
		if(tagList != null)
		{
			int keys = tagList.tagCount();
			if(keys > 0)
			{
				return super.getItemStackDisplayName(stack) + EnumChatFormatting.YELLOW + " (" + keys + " " + (keys > 1 ? "Keys" : "Key") + ")";
			}
		}
		return super.getItemStackDisplayName(stack);
	}
}
