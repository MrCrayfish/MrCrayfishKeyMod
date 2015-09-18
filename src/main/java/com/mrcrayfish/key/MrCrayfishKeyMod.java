package com.mrcrayfish.key;

import com.mrcrayfish.key.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class MrCrayfishKeyMod
{
	@Instance(Reference.MOD_ID)
	public static MrCrayfishKeyMod instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	public static CreativeTabs tabKey = new KeyTab("tabKey");
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		KeyItems.register();
		KeyItems.registerItems();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init();
		
		MinecraftForge.EVENT_BUS.register(new KeyEvents());
	}
}
