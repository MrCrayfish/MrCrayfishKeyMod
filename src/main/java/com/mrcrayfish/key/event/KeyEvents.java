package com.mrcrayfish.key.event;

import com.mrcrayfish.key.items.KeyItems;
import com.mrcrayfish.key.objects.LockedDoor;
import com.mrcrayfish.key.objects.LockedDoorData;
import com.mrcrayfish.key.util.NBTHelper;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.StatCollector;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
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

			TileEntity tileEntity = world.getTileEntity(pos);
			if(tileEntity instanceof TileEntityLockable)
			{
				TileEntityLockable tileEntityLockable = (TileEntityLockable) tileEntity;
				EntityPlayer player = event.entityPlayer;
				ItemStack current = player.getCurrentEquippedItem();
				
				if(tileEntityLockable.isLocked())
				{	
					if(current != null && current.getItem() == KeyItems.item_key)
					{
						if(!tileEntityLockable.getLockCode().getLock().equals(current.getDisplayName()))
						{
							world.playSoundAtEntity(player, "fire.ignite", 1.0F, 1.0F);
							sendSpecialMessage(world, player, EnumChatFormatting.YELLOW + "This key does not fit the lock.");
							event.setCanceled(true);
						}
					}
					else if(current != null && current.getItem() == KeyItems.item_key_ring)
					{
						boolean hasPassword = false;
						NBTTagList passwords = NBTHelper.getTagList(current, "passwords");
						for(int i = 0; i < passwords.tagCount(); i++)
						{
							if(tileEntityLockable.getLockCode().getLock().equals(passwords.getStringTagAt(i)))
							{
								hasPassword = true;
								break;
							}
						}
						if(!hasPassword)
						{
							world.playSoundAtEntity(player, "fire.ignite", 1.0F, 1.0F);
							sendSpecialMessage(world, player, EnumChatFormatting.YELLOW + "None of the keys fit the lock.");
							event.setCanceled(true);
						}
					}
					else
					{
						if(!world.isRemote)
						{
							world.playSoundAtEntity(player, "random.wood_click", 1.0F, 1.0F);
							sendSpecialMessage(world, player, EnumChatFormatting.YELLOW + "This block is locked with a key.");
							event.setCanceled(true);
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
								sendSpecialMessage(world, player, EnumChatFormatting.GREEN + "Successfully locked the block with the key: " + EnumChatFormatting.RESET + current.getDisplayName());
								event.setCanceled(true);
							}
						}
						else
						{
							sendSpecialMessage(world, player, EnumChatFormatting.YELLOW + "The key needs to be renamed before you can lock this block.");
						}
					}
				}
			}
			else if(world.getBlockState(pos).getBlock() instanceof BlockDoor && world.getBlockState(pos).getBlock() != Blocks.iron_door)
			{
				BlockDoor blockDoor = (BlockDoor) world.getBlockState(pos).getBlock();
				IBlockState state = world.getBlockState(pos);
				if(((BlockDoor.EnumDoorHalf)state.getValue(BlockDoor.HALF)) == BlockDoor.EnumDoorHalf.UPPER)
				{
					pos = pos.down();
				}
				
				LockedDoorData lockedDoorData = LockedDoorData.get(world);
				LockedDoor lockedDoor = lockedDoorData.getDoor(pos);
				
				EntityPlayer player = event.entityPlayer;
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
								sendSpecialMessage(world, player, EnumChatFormatting.YELLOW + "This key does not fit the lock.");
								world.markBlockForUpdate(pos);
								event.setCanceled(true);
							}
						}
						else if(current != null && current.getItem() == KeyItems.item_key_ring)
						{
							boolean hasPassword = false;
							NBTTagList passwords = NBTHelper.getTagList(current, "passwords");
							for(int i = 0; i < passwords.tagCount(); i++)
							{
								if(lockedDoor.getLockCode().getLock().equals(passwords.getStringTagAt(i)))
								{
									hasPassword = true;
									break;
								}
							}
							if(!hasPassword)
							{
								world.playSoundAtEntity(player, "fire.ignite", 1.0F, 1.0F);
								sendSpecialMessage(world, player, EnumChatFormatting.YELLOW + "None of the keys fit the lock.");
								world.markBlockForUpdate(pos);
								event.setCanceled(true);
							}
						}
						else
						{
							world.playSoundAtEntity(player, "random.wood_click", 1.0F, 1.0F);
							sendSpecialMessage(world, player, EnumChatFormatting.YELLOW + "This door is locked with a key.");
							world.markBlockForUpdate(pos);
							event.setCanceled(true);
						}
					}
					else
					{
						if(current != null && current.getItem() == KeyItems.item_key)
						{
							if(!current.getDisplayName().equals(StatCollector.translateToLocal(current.getItem().getUnlocalizedName() +".name")))
							{
								lockedDoor.setLockCode(new LockCode(current.getDisplayName()));
								world.playSoundAtEntity(player, "random.click", 1.0F, 1.0F);
								sendSpecialMessage(world, player, EnumChatFormatting.GREEN + "Successfully locked the door with the key: " + EnumChatFormatting.RESET + current.getDisplayName());
								lockedDoorData.markDirty();
							}
							else
							{
								sendSpecialMessage(world, player, EnumChatFormatting.YELLOW + "The key needs to be renamed before you can lock this door.");
								world.markBlockForUpdate(pos);
							}
							if(!world.isRemote)event.setCanceled(true);
						}
						else if(current != null && current.getItem() == KeyItems.item_key_ring)
						{
							sendSpecialMessage(world, player, EnumChatFormatting.YELLOW + "You can only lock a door with a single key.");
						}
						else
						{
							event.setCanceled(false);
						}
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
		World world = event.world;
		BlockPos pos = event.pos;
		
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
		else if(world.getBlockState(pos).getBlock() instanceof BlockDoor && world.getBlockState(pos).getBlock() != Blocks.iron_door)
		{
			BlockDoor blockDoor = (BlockDoor) world.getBlockState(pos).getBlock();
			IBlockState state = world.getBlockState(pos);
			if(((BlockDoor.EnumDoorHalf)state.getValue(BlockDoor.HALF)) == BlockDoor.EnumDoorHalf.UPPER)
			{
				pos = pos.down();
			}
			LockedDoorData lockedDoorData = LockedDoorData.get(world);
			LockedDoor lockedDoor = lockedDoorData.getDoor(pos);
			ItemStack current = player.getCurrentEquippedItem();

			if(lockedDoor != null && lockedDoor.isLocked())
			{
				if(!lockedDoor.isCorrectKey(event.getPlayer()))
				{
					player.playerNetServerHandler.sendPacket(new S02PacketChat((new ChatComponentText(EnumChatFormatting.YELLOW + "You need to have correct key in your inventory to destroy this block.")), (byte)2));
					event.setCanceled(true);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlaceBlock(PlaceEvent event)
	{
		if(event.world.isRemote)
			return;
		
		if(event.placedBlock.getBlock() instanceof BlockDoor && event.world.getBlockState(event.pos).getBlock() != Blocks.iron_door)
		{
			LockedDoorData lockedDoorData = LockedDoorData.get(event.world);
			lockedDoorData.addDoor(event.pos);
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
			else if(stack != null && stack.getItem() == KeyItems.item_key_ring)
			{
				NBTTagList passwords = NBTHelper.getTagList(stack, "passwords");
				for(int i = 0; i < passwords.tagCount(); i++)
				{
					if(tileEntityLockable.getLockCode().getLock().equals(passwords.getStringTagAt(i)))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void sendSpecialMessage(World world, EntityPlayer player, String message)
	{
		if(world.isRemote)
			return;
		
		EntityPlayerMP playerMp = (EntityPlayerMP) player;
		playerMp.playerNetServerHandler.sendPacket(new S02PacketChat((new ChatComponentText(message)), (byte)2));
	}
}
