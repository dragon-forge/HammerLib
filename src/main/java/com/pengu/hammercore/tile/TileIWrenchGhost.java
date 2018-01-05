package com.pengu.hammercore.tile;

import java.util.List;

import com.pengu.hammercore.tile.tooltip.ProgressBar;
import com.pengu.hammercore.tile.tooltip.eNumberFormat;
import com.pengu.hammercore.tile.tooltip.iTooltipTile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

public class TileIWrenchGhost extends TileSyncableTickable implements iTooltipTile
{
	public ProgressBar bar = new ProgressBar(100L);
	public int timeLeft = 100;
	public int timeTotal = 100;
	
	public TileIWrenchGhost(int lifetime)
	{
		timeLeft = timeTotal = lifetime;
	}
	
	public TileIWrenchGhost()
	{
	}
	
	@Override
	public void tick()
	{
		if(timeLeft > 0)
			--timeLeft;
		else
			getLocation().setAir();
		
		if(atTickRate(10))
			sendChangesToNearby();
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Left", timeLeft);
		nbt.setInteger("Max", timeTotal);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		timeLeft = nbt.getInteger("Left");
		timeTotal = nbt.getInteger("Max");
	}

	@Override
	public void getTextTooltip(List<String> list, EntityPlayer player)
	{
		
	}
	
	@Override
	public boolean hasProgressBars(EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public ProgressBar[] getProgressBars(EntityPlayer player)
	{
		bar.setMaxValue(timeTotal);
		bar.setProgress(timeLeft);
		
		bar.filledMainColor = 0xFFFFFFFF;
		bar.filledAlternateColor = 0xFFCCCCCC;
		bar.borderColor = 0xFF888888;
		bar.backgroundColor = 0xFF666666;
		bar.prefix = TextFormatting.DARK_GRAY + "Time Left: " + (timeLeft / 20) + " s.";
		bar.numberFormat = eNumberFormat.NONE;
		
		return new ProgressBar[] { bar };
	}
}