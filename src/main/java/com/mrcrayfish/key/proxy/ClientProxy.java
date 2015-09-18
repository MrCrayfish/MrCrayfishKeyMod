package com.mrcrayfish.key.proxy;

import com.mrcrayfish.key.KeyItems;

public class ClientProxy extends CommonProxy
{
	@Override
	public void init()
	{
		KeyItems.registerRenders();
	}
}
