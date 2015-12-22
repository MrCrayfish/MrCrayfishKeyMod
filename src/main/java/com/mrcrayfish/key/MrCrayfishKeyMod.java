package com.mrcrayfish.key;

import com.mrcrayfish.key.blocks.KeyBlocks;
import com.mrcrayfish.key.event.KeyEvents;
import com.mrcrayfish.key.gui.GuiHandler;
import com.mrcrayfish.key.items.KeyItems;
import com.mrcrayfish.key.proxy.CommonProxy;
import com.mrcrayfish.key.tileentity.TileEntityKeyRack;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
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
		/** Initialize and Register Blocks */
		KeyBlocks.register();
		KeyBlocks.registerBlocks();
		
		/** Initialize and Register Items */
		KeyItems.register();
		KeyItems.registerItems();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init();
		
		MinecraftForge.EVENT_BUS.register(new KeyEvents());
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		
		GameRegistry.addRecipe(new ItemStack(KeyItems.item_key), "NNI", 'I', Items.gold_ingot, 'N', Items.gold_nugget);
		GameRegistry.addRecipe(new ItemStack(KeyItems.item_iron_nugget, 9), "I", 'I', Items.iron_ingot);
		GameRegistry.addRecipe(new ItemStack(KeyItems.item_key_ring), "NNN", "N N", "NNN", 'N', KeyItems.item_iron_nugget);
		GameRegistry.addRecipe(new ItemStack(Items.iron_ingot), "NNN", "NNN", "NNN", 'N', KeyItems.item_iron_nugget);
		GameRegistry.addRecipe(new ItemStack(KeyBlocks.block_key_rack), "WWW", "NNN", 'W', new ItemStack(Blocks.log2, 1, 1), 'N', KeyItems.item_iron_nugget);
		
		GameRegistry.registerTileEntity(TileEntityKeyRack.class, "ckmKeyRack");
		
	}
}
