package com.mrcrayfish.key;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class KeyItems {
	
	public static Item item_key;
	
	public static void register()
	{
		item_key = new Item().setUnlocalizedName("item_key").setCreativeTab(MrCrayfishKeyMod.tabKey);
	}
	
	public static void registerItems()
	{
		GameRegistry.registerItem(item_key, item_key.getUnlocalizedName().substring(5));
	}
	
	public static void registerRenders()
	{
		registerRender(item_key);
	}
	
	public static void registerRender(Item item)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(Reference.MOD_ID + ":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}
