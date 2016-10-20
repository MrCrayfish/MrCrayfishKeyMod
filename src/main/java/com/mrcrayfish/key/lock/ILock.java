package com.mrcrayfish.key.lock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;

public interface ILock {
	
	public boolean handleInteract(EntityPlayer player, World world, BlockPos pos);
	
	public boolean handleBreak(EntityPlayer player, World world, BlockPos pos);
	
	public LockCode getLockCode(World world, BlockPos pos);
	
	public BlockPos fix(World world, BlockPos pos);
	
}
