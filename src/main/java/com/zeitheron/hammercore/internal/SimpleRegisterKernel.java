package com.zeitheron.hammercore.internal;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.annotations.*;
import com.zeitheron.hammercore.api.*;
import com.zeitheron.hammercore.api.blocks.*;
import com.zeitheron.hammercore.api.multipart.BlockMultipartProvider;
import com.zeitheron.hammercore.internal.blocks.IItemBlock;
import com.zeitheron.hammercore.internal.init.ItemsHC;
import com.zeitheron.hammercore.utils.*;
import com.zeitheron.hammercore.utils.base.Cast;
import com.zeitheron.hammercore.utils.forge.*;
import com.zeitheron.hammercore.utils.java.tuples.*;
import net.minecraft.block.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.*;

import java.lang.reflect.*;
import java.util.*;

public class SimpleRegisterKernel
{
	protected final String className;
	protected final ModContainer container;
	
	protected Map<Class<?>, List<Tuple2<IForgeRegistryEntry<?>, ResourceLocation>>> fields;
	protected List<Tuple2<ICustomRegistrar, ResourceLocation>> customRegistrars;
	
	protected CreativeTabs assignedTab;
	protected boolean registeredItems, registeredBlocks;
	
	public SimpleRegisterKernel(String className, ModContainer container)
	{
		this.className = className;
		this.container = container;
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public List<Tuple2<ICustomRegistrar, ResourceLocation>> getCustomRegistrars()
	{
		if(customRegistrars != null) return customRegistrars;
		customRegistrars = new ArrayList<>();
		
		try
		{
			Class<?> ownerCls = Class.forName(className);
			
			String prefix = "";
			
			SimplyRegister sr = ownerCls.getAnnotation(SimplyRegister.class);
			if(sr != null)
				prefix = sr.prefix();
			
			for(Field f : ownerCls.getDeclaredFields())
			{
				if(!ICustomRegistrar.class.isAssignableFrom(f.getType()) || !Modifier.isStatic(f.getModifiers()))
					continue;
				if(!SimpleRegistration.doRegister(f))
				{
					HammerCore.LOG.debug("Skipped {} since @RegisterIf returned false.", f);
					continue;
				}
				
				RegistryName key = f.getAnnotation(RegistryName.class);
				if(key == null) continue; // silence
				
				f.setAccessible(true);
				ICustomRegistrar ctr = Cast.cast(f.get(null), ICustomRegistrar.class);
				if(ctr == null) continue;
				
				ResourceLocation regKey = new ResourceLocation(container.getModId(), prefix + key.value());
				customRegistrars.add(Tuples.immutable(ctr, regKey));
			}
		} catch(Exception e)
		{
			HammerCore.LOG.error("Failed to register custom registrars from class {}", className, e);
		}
		
		return customRegistrars;
	}
	
	public Map<Class<?>, List<Tuple2<IForgeRegistryEntry<?>, ResourceLocation>>> getFields()
	{
		if(fields != null) return fields;
		fields = new HashMap<>();
		
		try
		{
			Class<?> ownerCls = Class.forName(className);
			
			String prefix = "";
			
			SimplyRegister sr = ownerCls.getAnnotation(SimplyRegister.class);
			if(sr != null)
			{
				prefix = sr.prefix();
				FieldReference tab = sr.creativeTab();
				assignedTab = Cast.cast(ReflectionUtil.getValue(tab.clazz(), tab.field()), CreativeTabs.class);
			}
			
			for(Field f : ownerCls.getDeclaredFields())
			{
				if(!IForgeRegistryEntry.class.isAssignableFrom(f.getType()) || !Modifier.isStatic(f.getModifiers()))
					continue;
				if(!SimpleRegistration.doRegister(f))
				{
					HammerCore.LOG.debug("Skipped {} since @RegisterIf returned false.", f);
					continue;
				}
				
				RegistryName key = f.getAnnotation(RegistryName.class);
				if(key == null) continue; // silence
				
				f.setAccessible(true);
				IForgeRegistryEntry<?> ctr = Cast.cast(f.get(null));
				if(ctr == null) continue;
				
				ResourceLocation regKey = new ResourceLocation(container.getModId(), prefix + key.value());
				fields.computeIfAbsent(ctr.getRegistryType(), ignore -> new ArrayList<>())
						.add(Tuples.immutable(ctr, regKey));
			}
		} catch(Exception e)
		{
			HammerCore.LOG.error("Failed to register read class {}", className, e);
		}
		
		return fields;
	}
	
	public void registerItems()
	{
		register(new RegistryEvent.Register<>(GameData.ITEMS, ForgeRegistries.ITEMS));
	}
	
	public void registerBlocks()
	{
		register(new RegistryEvent.Register<>(GameData.BLOCKS, ForgeRegistries.BLOCKS));
	}
	
	@SubscribeEvent
	protected void register(RegistryEvent.Register evt)
	{
		IForgeRegistry<?> reg = evt.getRegistry();
		Class<? extends IForgeRegistryEntry<?>> base = reg.getRegistrySuperType();
		List<Tuple2<IForgeRegistryEntry<?>, ResourceLocation>> toRegister = getFields().get(base);
		
		ModContainer old = Loader.instance().activeModContainer();
		Loader.instance().setActiveModContainer(container);
		evt.setModContainer(container);
		
		boolean blocks = base.equals(Block.class);
		boolean items = base.equals(Item.class);
		String modid = container.getModId();
		
		if(blocks && registeredBlocks) return;
		if(items && registeredItems) return;
		
		if(blocks) registeredBlocks = true;
		if(items) registeredItems = true;
		
		RegisterEvent evtHL = new RegisterEvent(evt);
		for(Tuple2<ICustomRegistrar, ResourceLocation> registrar : getCustomRegistrars())
			registrar.a().register(registrar.b(), evtHL);
		
		if(toRegister != null)
			try
			{
				for(Tuple2<IForgeRegistryEntry<?>, ResourceLocation> tup : toRegister)
				{
					IForgeRegistryEntry<?> ctr = tup.a();
					ResourceLocation regKey = tup.b();
					ctr.setRegistryName(regKey);
					
					if(items)
					{
						Cast.optionally(ctr, Item.class).ifPresent(it ->
						{
							it.setTranslationKey(regKey.getNamespace().concat(":").concat(regKey.getPath()));
							if(assignedTab != null) it.setCreativeTab(assignedTab);
							ItemsHC.items.add(it);
						});
					} else if(blocks)
					{
						Cast.optionally(ctr, Block.class).ifPresent(block ->
						{
							block.setTranslationKey(regKey.getNamespace().concat(":").concat(regKey.getPath()));
							if(assignedTab != null) block.setCreativeTab(assignedTab);
							
							// ItemBlockDefinition
							Item ib;
							
							if(block instanceof BlockMultipartProvider)
								ib = ((BlockMultipartProvider) block).createItem();
							else if(block instanceof IItemBlock)
								ib = ((IItemBlock) block).getItemBlock();
							else
								ib = new ItemBlock(block);
							
							if(!(block instanceof INoItemBlock))
							{
								if(ib != null)
								{
									ForgeRegistries.ITEMS.register(ib.setRegistryName(block.getRegistryName()));
									if(block instanceof IBlockItemRegisterListener)
										((IBlockItemRegisterListener) block).onItemBlockRegistered(ib);
								}
								if(ib instanceof IRegisterListener)
									((IRegisterListener) ib).onRegistered();
							}
							
							if(block instanceof INoBlockstate)
								HammerCore.renderProxy.noModel(block);
							
							if(block instanceof ITileBlock)
							{
								Class<? extends TileEntity> c = ((ITileBlock) block).getTileClass();
								
								// Better registration of tiles. Maybe this will fix tile disappearing?
								TileEntity.register(modid + ":" + c.getSimpleName().toLowerCase(), c);
							} else if(block instanceof ITileEntityProvider)
							{
								try
								{
									ITileEntityProvider te = (ITileEntityProvider) block;
									TileEntity t = te.createNewTileEntity(null, 0);
									if(t != null)
									{
										Class<? extends TileEntity> c = t.getClass();
										TileEntity.register(modid + ":" + c.getSimpleName().toLowerCase(), c);
									}
								} catch(Throwable e)
								{
									e.printStackTrace();
								}
							}
							
							if(!(block instanceof INoItemBlock))
							{
								Item i = Item.getItemFromBlock(block);
								if(i != Items.AIR)
								{
									ItemsHC.items.add(i);
									if(assignedTab != null) i.setCreativeTab(assignedTab);
								}
							}
						});
					}
					
					reg.register(Cast.cast(ctr));
					
					if(ctr instanceof IRegisterListener)
						((IRegisterListener) ctr).onRegistered();
					
					HammerCore.LOG.debug("Registered {}: {} ({})", base.getSimpleName(), ctr, regKey);
				}
			} catch(Exception e)
			{
				HammerCore.LOG.error("Failed to register {} from class {}", base.getSimpleName(), className, e);
			}
		
		Loader.instance().setActiveModContainer(old);
	}
	
	public static List<SimpleRegisterKernel> doScan(ASMDataTable table)
	{
		List<SimpleRegisterKernel> kernels = new ArrayList<>();
		for(ASMDataTable.ASMData data : table.getAll(SimplyRegister.class.getCanonicalName()))
		{
			ModContainer mod = data.getCandidate().getContainedMods().stream().findFirst().orElse(null);
			if(mod == null)
			{
				HammerCore.LOG.warn("Skipping @SimplyRegister-annotated class {} since it does not belong to any mod.", data.getClassName());
				continue;
			}
			
			kernels.add(new SimpleRegisterKernel(data.getClassName(), mod));
			HammerCore.LOG.info("Applied @SimplyRegister to {}, which belongs to {} ({})", data.getClassName(), mod.getModId(), mod.getName());
		}
		return kernels;
	}
	
	public boolean is(String modid)
	{
		return Objects.equals(container.getModId(), modid);
	}
}