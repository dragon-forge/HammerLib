package com.pengu.hammercore.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.utils.EnumSide;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * This class helps to gather all required scopes of needed object with
 * specified annotation
 */
public class AnnotatedInstanceUtil
{
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
}