package com.mrcrayfish.key.proxy;

import com.mrcrayfish.key.blocks.KeyBlocks;
import com.mrcrayfish.key.items.KeyItems;
import com.mrcrayfish.key.tileentity.TileEntityKeyRack;
import com.mrcrayfish.key.tileentity.render.KeyRackRenderer;

import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		KeyItems.registerRenders();
		KeyBlocks.registerRenders();
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityKeyRack.class, new KeyRackRenderer());
	}
}
