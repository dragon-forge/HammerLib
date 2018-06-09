package com.zeitheron.hammercore.mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.pengu.hammercore.common.utils.AnnotatedInstanceUtil;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;

public class ModuleLister
{
	public static <T extends ILoadModule> List<T> createModules(Class<T> type, String valtype, ASMDataTable table)
	{
		List<T> list = new ArrayList<>();
		for(ASMData data : table.getAll(ModuleLoader.class.getCanonicalName()))
		{
			try
			{
				Class<?> asmClass = Class.forName(data.getClassName());
				if(type.isAssignableFrom(asmClass))
				{
					Class<? extends T> sub = asmClass.asSubclass(type);
					ModuleLoader ml = sub.getAnnotation(ModuleLoader.class);
					if(ml != null && Loader.instance().isModLoaded(ml.requiredModid()) && (valtype == null || Objects.equals(ml.value(), valtype)))
						list.add(sub.getDeclaredConstructor().newInstance());
				}
			} catch(Throwable e)
			{
			}
		}
		return list;
	}
}