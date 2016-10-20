package com.mrcrayfish.key.event;

import com.mrcrayfish.key.items.KeyItems;
import com.mrcrayfish.key.lock.LockData;
import com.mrcrayfish.key.lock.LockManager;
import com.mrcrayfish.key.lock.WorldLockData;
import com.mrcrayfish.key.util.NBTHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.NeighborNotifyEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class KeyEvents 
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		Block block = event.world.getBlockState(event.pos).getBlock();
		TileEntity tileEntity = event.world.getTileEntity(event.pos);
		if(event.action == Action.RIGHT_CLICK_BLOCK)
		{
			event.setCanceled(LockManager.onInteract(block, tileEntity, event.entityPlayer, event.world, event.pos));
		}
	}
	
	@SubscribeEvent
	public void onOpenContainer(PlayerOpenContainerEvent event)
	{
		ItemStack current = event.entityPlayer.getCurrentEquippedItem();
		if(current != null)
		{
			if(current.getItem() == KeyItems.item_key_ring) 
			{
				current.clearCustomName();
			}
		}
	}
	
	@SubscribeEvent
	public void onBreakBlock(BreakEvent event)
	{
		if(event.world.isRemote)
			return;

		BlockPos pos = LockManager.isLockAround(event.world, event.pos);
		if(pos != null)
		{
			Block block = event.world.getBlockState(pos).getBlock();
			event.setCanceled(!block.canPlaceBlockAt(event.world, pos));
		}
	}
	
	//TODO change to item
	/*@SubscribeEvent
	public void onPlaceBlock(PlaceEvent event)
	{
		if(event.world.isRemote)
			return;
		
		if(event.placedBlock.getBlock() instanceof BlockDoor && event.world.getBlockState(event.pos).getBlock() != Blocks.iron_door)
		{
			LockedDoorData lockedDoorData = LockedDoorData.get(event.world);
			lockedDoorData.addDoor(event.pos);
		}
	}*/
	
	@SubscribeEvent
	public void onNeighbourChange(NeighborNotifyEvent event)
	{
		BlockPos pos = LockManager.isLockAround(event.world, event.pos);
		if(pos != null)
		{
			Block block = event.world.getBlockState(pos).getBlock();
			TileEntity tileEntity = event.world.getTileEntity(pos);
			event.setCanceled(true);
		}
	}
}
