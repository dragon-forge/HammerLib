package org.zeith.hammerlib.proxy;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.api.LanguageHelper.LangMap;
import org.zeith.hammerlib.api.lighting.ColoredLight;
import org.zeith.hammerlib.api.proxy.IProxy;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class HLCommonProxy
		implements IProxy
{
	protected List<QueuedTask> serverTickTasks = new ArrayList<>();
	
	public HLCommonProxy()
	{
		NeoForge.EVENT_BUS.addListener(this::serverTick);
		NeoForge.EVENT_BUS.addListener(this::serverStopping);
	}
	
	public void queueTask(Level level, int delay, Runnable task)
	{
		if(!level.isClientSide)
			serverTickTasks.add(new QueuedTask(delay, task));
	}
	
	private void serverTick(TickEvent.ServerTickEvent e)
	{
		if(e.phase == TickEvent.Phase.START) return;
		for(int i = 0; i < serverTickTasks.size(); i++)
		{
			if(serverTickTasks.get(i).shouldRemove())
			{
				serverTickTasks.remove(i);
				--i;
			}
		}
	}
	
	private void serverStopping(ServerStoppingEvent e)
	{
		serverTickTasks.clear();
	}
	
	public void construct(IEventBus modBus)
	{
	}
	
	public void commonSetup()
	{
	}
	
	public void clientSetup()
	{
	}
	
	public void applyLang(LangMap map)
	{
	}
	
	public String getLanguage()
	{
		return "en_us";
	}
	
	public String getLanguage(Player player)
	{
		if(player instanceof ServerPlayer ent)
			return ent.getLanguage();
		return getLanguage();
	}
	
	@Override
	public Player getClientPlayer()
	{
		return null;
	}
	
	public ReloadableResourceManager getResourceManager()
	{
		MinecraftServer serv = ServerLifecycleHooks.getCurrentServer();
		if(serv != null) return (ReloadableResourceManager) serv.getServerResources().resourceManager();
		return null;
	}
	
	public Stream<ColoredLight> getGlowingParticles(float partialTicks)
	{
		return Stream.empty();
	}
	
	private boolean finishedLoading;
	
	public void finishLoading()
	{
		finishedLoading = true;
	}
	
	public boolean hasFinishedLoading()
	{
		return finishedLoading;
	}
	
	public Consumer<FMLClientSetupEvent> addTESR(Type owner, String member, Type tesr)
	{
		return null;
	}
	
	public Consumer<RegisterParticleProvidersEvent> addParticleTypeProvider(Type owner, String member, Type tesr)
	{
		return null;
	}
	
	protected static final class QueuedTask
	{
		private int ticksBeforeExecution;
		private Runnable task;
		
		public QueuedTask(int ticksBeforeExecution, Runnable task)
		{
			this.ticksBeforeExecution = ticksBeforeExecution;
			this.task = task;
		}
		
		public boolean shouldRemove()
		{
			if(--ticksBeforeExecution <= 0)
			{
				if(task != null) task.run();
				task = null;
				return true;
			}
			
			return false;
		}
	}
}