package com.zeitheron.hammercore.internal;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.annotations.*;
import com.zeitheron.hammercore.api.*;
import com.zeitheron.hammercore.api.blocks.*;
import com.zeitheron.hammercore.api.multipart.BlockMultipartProvider;
import com.zeitheron.hammercore.internal.ap.*;
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
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class SimpleRegisterKernel
{
	protected final String className;
	protected final SimpleRegisterKernelForMod container;
	
	protected Map<Class<?>, List<RegisterEntry>> fields;
	protected List<Tuple2<ICustomRegistrar, ResourceLocation>> customRegistrars;
	
	protected CreativeTabs assignedTab;
	protected boolean registeredItems, registeredBlocks;
	
	public SimpleRegisterKernel(String className, SimpleRegisterKernelForMod container)
	{
		this.className = className;
		this.container = container;
	}
	
	public String className()
	{
		return className;
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
				IAPContext.Builder ctxb = IAPContext.builder();
				
				boolean register = SimpleRegistration.doRegister(f);
				ctxb.shouldRegister(register);
				
				f.setAccessible(true);
				Object value = f.get(null);
				
				RegistryName key = f.getAnnotation(RegistryName.class);
				if(key != null)
				{
					ResourceLocation regKey = new ResourceLocation(container.getModId(), prefix + key.value());
					ctxb.id(regKey);
					ICustomRegistrar ctr = Cast.cast(value, ICustomRegistrar.class);
					if(register && ctr != null) customRegistrars.add(Tuples.immutable(ctr, regKey));
				}
				
				IAPContext ctx = ctxb.build();
				AnnotationProcessorRegistry.scan(ctx, f, value);
			}
			
			for(Method m : ownerCls.getDeclaredMethods())
				AnnotationProcessorRegistry.scan(IAPContext.DUMMY, m);
		} catch(Exception e)
		{
			HammerCore.LOG.error("Failed to register custom registrars from class {}", className, e);
		}
		
		return customRegistrars;
	}
	
	public Map<Class<?>, List<RegisterEntry>> getFields()
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
				
				boolean register = SimpleRegistration.doRegister(f);
				
				RegistryName key = f.getAnnotation(RegistryName.class);
				if(key == null) continue; // silence
				
				f.setAccessible(true);
				IForgeRegistryEntry<?> ctr = Cast.cast(f.get(null));
				if(ctr == null) continue;
				
				ResourceLocation regKey = new ResourceLocation(container.getModId(), prefix + key.value());
				fields.computeIfAbsent(ctr.getRegistryType(), ignore -> new ArrayList<>())
						.add(new RegisterEntry(f, ctr, regKey, register));
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
	
	protected void register(RegistryEvent.Register evt)
	{
		IForgeRegistry<?> reg = evt.getRegistry();
		Class<? extends IForgeRegistryEntry<?>> base = reg.getRegistrySuperType();
		List<RegisterEntry> toRegister = getFields().get(base);
		
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
				for(RegisterEntry tup : toRegister)
				{
					IForgeRegistryEntry<?> ctr = tup.entry;
					ResourceLocation regKey = tup.id;
					boolean doReg = tup.register;
					
					IAPContext ctx = IAPContext.builder().shouldRegister(doReg).id(regKey).build();
					
					AnnotationProcessorRegistry.scanReg(ctx, tup.field, ctr, false);
					
					if(!doReg) continue;
					
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
								{
									IRegisterListener rl = (IRegisterListener) ib;
									rl.onRegistered();
									RegisterHook.HookCollector.propagate(rl);
								}
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
					{
						IRegisterListener rl = (IRegisterListener) ctr;
						rl.onRegistered();
						RegisterHook.HookCollector.propagate(rl);
					}
					
					AnnotationProcessorRegistry.scanReg(ctx, tup.field, ctr, true);
					
					HammerCore.LOG.debug("Registered {}: {} ({})", base.getSimpleName(), ctr, regKey);
				}
			} catch(Exception e)
			{
				HammerCore.LOG.error("Failed to register {} from class {}", base.getSimpleName(), className, e);
			}
	}
	
	public static Map<String, SimpleRegisterKernelForMod> doScan(ASMDataTable table)
	{
		Map<String, SimpleRegisterKernelForMod> kernels = new HashMap<>();
		
		for(ASMDataTable.ASMData data : table.getAll(SimplyRegister.class.getCanonicalName()))
		{
			ModContainer mod = data.getCandidate().getContainedMods().stream().findFirst().orElse(null);
			if(mod == null)
			{
				HammerCore.LOG.warn("Skipping @SimplyRegister-annotated class {} since it does not belong to any mod.", data.getClassName());
				continue;
			}
			
			SimpleRegisterKernelForMod coll = kernels.computeIfAbsent(mod.getModId(), __ -> new SimpleRegisterKernelForMod(mod));
			coll.add(new SimpleRegisterKernel(data.getClassName(), coll));
			HammerCore.LOG.info("Applied @SimplyRegister to {}, which belongs to {} ({})", data.getClassName(), mod.getModId(), mod.getName());
		}
		
		for(SimpleRegisterKernelForMod kernelCollection : kernels.values())
			kernelCollection.sort(Comparator.comparing(SimpleRegisterKernel::className));
		
		return kernels;
	}
	
	public boolean is(String modid)
	{
		return Objects.equals(container.getModId(), modid);
	}
	
	public static class RegisterEntry
	{
		public final Field field;
		public final IForgeRegistryEntry<?> entry;
		public final ResourceLocation id;
		public final boolean register;
		
		public RegisterEntry(Field field, IForgeRegistryEntry<?> entry, ResourceLocation id, boolean register)
		{
			this.field = field;
			this.entry = entry;
			this.id = id;
			this.register = register;
		}
	}
}