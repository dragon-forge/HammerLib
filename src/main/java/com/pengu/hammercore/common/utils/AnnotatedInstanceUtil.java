package com.pengu.hammercore.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.utils.EnumSide;
import com.zeitheron.hammercore.client.PerUserModule;
import com.zeitheron.hammercore.client.UserModule;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This class helps to gather all required scopes of needed object with
 * specified annotation
 */
public class AnnotatedInstanceUtil
{
	/**
	 * Gets all classes that extend (or implement) typeClass
	 * <strong>and</strong> have annotationClass annotation present
	 * 
	 * @param asmDataTable
	 *            the ASM table with all loaded classes (get it via
	 *            {@link FMLPreInitializationEvent#getAsmData()})
	 * @param annotationClass
	 *            the annotation that must be present in the class for it to be
	 *            added to the list
	 * @param typeClass
	 *            the class that object must extend in order for it to be added
	 *            to the list
	 */
	public static <T> List<Class<? extends T>> getTypes(@Nonnull ASMDataTable asmDataTable, Class annotationClass, Class<T> typeClass)
	{
		String annotationClassName = annotationClass.getCanonicalName();
		Set<ASMData> asmDatas = asmDataTable.getAll(annotationClassName);
		List<Class<? extends T>> instances = new ArrayList();
		for(ASMData asmData : asmDatas)
		{
			try
			{
				Class<?> asmClass = Class.forName(asmData.getClassName());
				if(typeClass.isAssignableFrom(asmClass))
					instances.add(asmClass.asSubclass(typeClass));
			} catch(Throwable e)
			{
			}
		}
		return instances;
	}
	
	/**
	 * Gets (well, creates) all scopes of classes that extend (or implement)
	 * instanceClass <strong>and</strong> have annotationClass annotation
	 * present
	 * 
	 * @param asmDataTable
	 *            the ASM table with all loaded classes (get it via
	 *            {@link FMLPreInitializationEvent#getAsmData()})
	 * @param annotationClass
	 *            the annotation that must be present in the class for it to be
	 *            added to the list
	 * @param instanceClass
	 *            the class that object must extend in order for it to be added
	 *            to the list
	 */
	public static <T> List<T> getInstances(@Nonnull ASMDataTable asmDataTable, Class annotationClass, Class<T> instanceClass)
	{
		String annotationClassName = annotationClass.getCanonicalName();
		Set<ASMData> asmDatas = asmDataTable.getAll(annotationClassName);
		List<T> instances = new ArrayList();
		for(ASMData asmData : asmDatas)
		{
			try
			{
				Class<?> asmClass = Class.forName(asmData.getClassName());
				if(!instanceClass.isAssignableFrom(asmClass))
					continue;
				Class<? extends T> asmInstanceClass = asmClass.asSubclass(instanceClass);
				T instance = asmInstanceClass.newInstance();
				instances.add(instance);
			} catch(Throwable e)
			{
			}
		}
		return instances;
	}
	
	public static <T> List<T> getMCFBInstances(@Nonnull ASMDataTable asmDataTable, EnumSide side, Class<T> instanceClass)
	{
		String annotationClassName = MCFBus.class.getCanonicalName();
		Set<ASMData> asmDatas = asmDataTable.getAll(annotationClassName);
		List<T> instances = new ArrayList();
		for(ASMData asmData : asmDatas)
		{
			try
			{
				Class<?> asmClass = Class.forName(asmData.getClassName());
				if(!asmClass.getAnnotation(MCFBus.class).side().sideEqual(side))
					continue;
				Class<? extends T> asmInstanceClass = asmClass.asSubclass(instanceClass);
				T instance = asmInstanceClass.newInstance();
				instances.add(instance);
			} catch(Throwable e)
			{
			}
		}
		return instances;
	}
	
	@SideOnly(Side.CLIENT)
	public static PerUserModule getUserModule(@Nonnull ASMDataTable asmDataTable)
	{
		String username = Minecraft.getMinecraft().getSession().getUsername();
		
		HammerCore.LOG.info("Guessing username.... You are " + username + ", aren't you?");
		
		String annotationClassName = UserModule.class.getCanonicalName();
		Set<ASMData> asmDatas = asmDataTable.getAll(annotationClassName);
		for(ASMData asmData : asmDatas)
		{
			try
			{
				Class<?> asmClass = Class.forName(asmData.getClassName());
				if(PerUserModule.class.isAssignableFrom(asmClass) && username.equals(asmClass.getAnnotation(UserModule.class).username()))
					return asmClass.asSubclass(PerUserModule.class).getDeclaredConstructor().newInstance();
			} catch(Throwable e)
			{
				e.printStackTrace();
			}
		}
		
		return new PerUserModule();
	}
}