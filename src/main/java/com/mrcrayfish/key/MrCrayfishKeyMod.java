package com.mrcrayfish.key;

import com.mrcrayfish.key.proxy.CommonProxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
		
		GameRegistry.addRecipe(new ItemStack(KeyItems.item_key), "NNI", 'I', Items.gold_ingot, 'N', Items.gold_nugget);
	}
}
