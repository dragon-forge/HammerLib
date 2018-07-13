package com.zeitheron.hammercore.client;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import com.zeitheron.hammercore.utils.ILoadable;

import net.minecraft.nbt.NBTTagCompound;

public class PerUserModule implements ILoadable
{
	public CTButton[] getSkinButtons()
	{
		return new CTButton[0];
	}
	
	public void loadClientOpts(NBTTagCompound nbt)
	{
		
	}
	
	public void saveClientOpts(NBTTagCompound nbt)
	{
		
	}
	
	public static interface CTButton
	{
		String getText();
		
		boolean isEnabled();
		
		void click();
		
		static CTButton create(Supplier<String> text, Runnable onClick)
		{
			return create(text, () -> true, onClick);
		}
		
		static CTButton create(Supplier<String> text, BooleanSupplier enabled, Runnable onClick)
		{
			return new CTButton()
			{
				@Override
				public String getText()
				{
					return text.get();
				}
				
				@Override
				public boolean isEnabled()
				{
					return enabled.getAsBoolean();
				}
				
				@Override
				public void click()
				{
					onClick.run();
				}
			};
		}
	}
}