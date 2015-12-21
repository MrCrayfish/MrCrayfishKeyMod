package com.mrcrayfish.key.items;

import com.mrcrayfish.key.objects.LockedDoor;
import com.mrcrayfish.key.objects.LockedDoorData;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;

public class ItemKey extends Item 
{
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
						if(tileEntityLockable.getLockCode().getLock().equals(stack.getDisplayName()))
						{
							tileEntityLockable.setLockCode(LockCode.EMPTY_CODE);
							playerMp.playerNetServerHandler.sendPacket(new S02PacketChat((new ChatComponentText(EnumChatFormatting.GREEN + "Succesfully unlocked this block.")), (byte)2));
						}
						else
						{
							playerMp.playerNetServerHandler.sendPacket(new S02PacketChat((new ChatComponentText(EnumChatFormatting.YELLOW + "You need to have correct key to unlock this block.")), (byte)2));
						}
					}
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
							if(lockedDoor.getLockCode().getLock().equals(stack.getDisplayName()))
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
				}
			}
		}
		return true;
	}
}
