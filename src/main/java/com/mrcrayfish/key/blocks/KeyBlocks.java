package com.mrcrayfish.key.blocks;

import com.mrcrayfish.key.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class KeyBlocks 
{
	public static Block block_key_rack;
	
	public static void register() 
	{
		block_key_rack = new BlockKeyRack(Material.wood).setUnlocalizedName("block_key_rack");
	}
	
	public static void registerBlocks() 
	{
		GameRegistry.registerBlock(block_key_rack, block_key_rack.getUnlocalizedName().substring(5));
	}
	
	public static void registerRenders() 
	{
		registerRender(block_key_rack);
	}
	
	public static void registerRender(Block block)
	{
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Reference.MOD_ID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
}
