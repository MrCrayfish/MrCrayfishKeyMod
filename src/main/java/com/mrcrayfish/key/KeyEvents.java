package com.mrcrayfish.key;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class KeyEvents 
{
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(event.action == Action.RIGHT_CLICK_BLOCK)
		{
			World world = event.world;
			BlockPos pos = event.pos;
			
			if(world.isRemote)
				return;
			
			TileEntity tileEntity = world.getTileEntity(pos);
			if(tileEntity instanceof TileEntityLockable)
			{
				TileEntityLockable tileEntityLockable = (TileEntityLockable) tileEntity;
				
				EntityPlayerMP player = (EntityPlayerMP) event.entityPlayer;
				
				ItemStack current = player.getCurrentEquippedItem();
				if(tileEntityLockable.isLocked())
				{	
					if(current != null && current.getItem() == KeyItems.item_key)
					{
						if(!tileEntityLockable.getLockCode().getLock().equals(current.getDisplayName()))
						{
							world.playSoundAtEntity(player, "fire.ignite", 1.0F, 1.0F);
							player.playerNetServerHandler.sendPacket(new S02PacketChat((new ChatComponentText(EnumChatFormatting.YELLOW + "This key does not fit the lock.")), (byte)2));
							event.setCanceled(true);
						}
					}
					else
					{
						world.playSoundAtEntity(player, "random.wood_click", 1.0F, 1.0F);
						player.playerNetServerHandler.sendPacket(new S02PacketChat((new ChatComponentText(EnumChatFormatting.YELLOW + "This block is locked with a key.")), (byte)2));
						event.setCanceled(true);
					}
				}
				else
				{
					if(current != null && current.getItem() == KeyItems.item_key)
					{
						if(!current.getDisplayName().equals(StatCollector.translateToLocal(current.getItem().getUnlocalizedName() +".name")))
						{
							tileEntityLockable.setLockCode(new LockCode(current.getDisplayName()));
							world.playSoundAtEntity(player, "random.click", 1.0F, 1.0F);
							player.playerNetServerHandler.sendPacket(new S02PacketChat((new ChatComponentText(EnumChatFormatting.GREEN + "Successfully locked the block with the key: " + EnumChatFormatting.RESET + current.getDisplayName())), (byte)2));
						}
						else
						{
							player.playerNetServerHandler.sendPacket(new S02PacketChat((new ChatComponentText(EnumChatFormatting.YELLOW + "The key needs to be renamed before you can lock this block.")), (byte)2));
						}
						event.setCanceled(true);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onBreakBlock(BreakEvent event)
	{
		if(event.world.isRemote)
			return;
		
		EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();
		
		TileEntity tileEntity = event.world.getTileEntity(event.pos);
		if(tileEntity instanceof TileEntityLockable)
		{
			TileEntityLockable tileEntityLockable = (TileEntityLockable) tileEntity;
			if(tileEntityLockable.isLocked())
			{
				if(!hasRequiredKey(event.getPlayer(), tileEntityLockable))
				{
					player.playerNetServerHandler.sendPacket(new S02PacketChat((new ChatComponentText(EnumChatFormatting.YELLOW + "You need to have correct key in your inventory to destroy this block.")), (byte)2));
					event.setCanceled(true);
				}
			}
		}
	}
	
	public boolean hasRequiredKey(EntityPlayer player, TileEntityLockable tileEntityLockable)
	{
		for(ItemStack stack : player.inventory.mainInventory)
		{
			if(stack != null && stack.getItem() == KeyItems.item_key)
			{
				if(tileEntityLockable.getLockCode().getLock().equals(stack.getDisplayName()))
				{
					return true;
				}
			}
		}
		return false;
	}
}
