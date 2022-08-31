package org.zeith.hammerlib.client.screen;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.api.inv.ComplexProgressHandler;
import org.zeith.hammerlib.api.inv.ComplexProgressManager;

public abstract class MenuWithProgressBars extends AbstractContainerMenu
{
	public final ComplexProgressHandler progressHandler;
	public final ComplexProgressManager progressManager;
	
	public MenuWithProgressBars(@Nullable MenuType<?> type, int windowId, ComplexProgressHandler handler)
	{
		super(type, windowId);
		this.progressHandler = handler;
		this.progressManager = handler.create();
	}
	
	public void containerTick()
	{
		progressHandler.containerTick(progressManager);
	}
	
	@Override
	public void broadcastChanges()
	{
		if(synchronizer != null)
		{
			progressHandler.update(progressManager);
			progressManager.detectAndSendChanges(this);
		}
		super.broadcastChanges();
	}
	
	@Override
	public void setData(int id, int data)
	{
		progressManager.updateChange(id, data);
	}
}