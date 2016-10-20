package com.mrcrayfish.key.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageUtil 
{
	/* This only works on a server because it modifies the packet */
	public static void sendSpecial(EntityPlayer player, String message)
	{
		if(player.worldObj.isRemote)
			return;
		
		EntityPlayerMP playerMp = (EntityPlayerMP) player;
		playerMp.playerNetServerHandler.sendPacket(new S02PacketChat((new ChatComponentText(message)), (byte)2));
	}
}
