package org.zeith.hammerlib.util.mcf;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.neoforged.fml.ModList;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforgespi.language.*;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.util.java.*;
import org.zeith.hammerlib.util.java.functions.Function3;

import java.lang.annotation.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ScanDataHelper
{
	public static <T> List<Class<? extends T>> lookupAnnotatedTypes(Class<? extends Annotation> annotation, Class<T> baseType)
	{
		return lookupAnnotatedTypes(annotation, baseType, Predicates.alwaysTrue());
	}
	
	/**
	 * matcher can be an instance of {@link ScanProperties}.
	 * Use {@link #properties(Object...)} to easily generate a matcher.
	 */
	public static <T> List<Class<? extends T>> lookupAnnotatedTypes(Class<? extends Annotation> annotation, Class<T> baseType, Predicate<ModAwareAnnotationData> matcher)
	{
		List<Class<? extends T>> classes = new ArrayList<>();
		lookupAnnotatedObjects(annotation,
				ad -> ad.parent.targetType() == ElementType.TYPE &&
						baseType.isAssignableFrom(ReflectionUtil.fetchClass(ad.parent.clazz())) && matcher.test(ad)
		) //
				.forEach(ad -> classes.add(ReflectionUtil.fetchClass(ad.parent.clazz())));
		return classes;
	}
	
	public static Collection<ModAwareAnnotationData> lookupAnnotatedObjects(Class<? extends Annotation> annotation)
	{
		return lookupAnnotatedObjects(annotation, Predicates.alwaysTrue());
	}
	
	public static Collection<ModAwareAnnotationData> lookupAnnotatedObjects(Class<? extends Annotation> annotation, Predicate<ModAwareAnnotationData> matcher)
	{
		List<ModAwareAnnotationData> data = new ArrayList<>();
		Type annotationType = Type.getType(annotation);
		ModList.get().getAllScanData().stream()
				.flatMap(d -> d.getAnnotations().stream().map(ad -> new ModAwareAnnotationData(ad, d)))
				.filter(ad -> annotationType.equals(ad.parent.annotationType()) && matcher.test(ad))
				.forEach(data::add);
		return data;
	}
	
	public static Collection<ModAwareAnnotationData> lookupAnnotatedObjects(ModFileScanData data, Class<? extends Annotation> annotation)
	{
		return lookupAnnotatedObjects(data, annotation, Predicates.alwaysTrue());
	}
	
	public static Collection<ModAwareAnnotationData> lookupAnnotatedObjects(ModFileScanData data, Class<? extends Annotation> annotation, Predicate<ModAwareAnnotationData> matcher)
	{
		List<ModAwareAnnotationData> lst = new ArrayList<>();
		Type annotationType = Type.getType(annotation);
		Stream.of(data)
				.flatMap(d -> d.getAnnotations().stream().map(ad -> new ModAwareAnnotationData(ad, d)))
				.filter(ad -> annotationType.equals(ad.parent.annotationType()) && matcher.test(ad))
				.forEach(lst::add);
		return lst;
	}
	
	/**
	 * Create a predicate for matching {@link ModFileScanData.AnnotationData} with passed
	 * key-values! <br>
	 * Example: properties("modid", "hammerlib", "value", 35)
	 */
	public static ScanProperties properties(Object... keyValuePairs)
	{
		return new ScanProperties(keyValuePairs);
	}
	
	public static <T, A extends Annotation> Map<ScanTarget, T> gatherScans(Class<A> annotation, ElementType target, Function3<ScanTarget, FMLModContainer, ModAwareAnnotationData, T> result)
	{
		return Util.make(ImmutableMap.<ScanTarget, T>builder(), map ->
		{
			for(var data : lookupAnnotatedObjects(annotation))
			{
				if(data.getTargetType() == target)
				{
					var mod = data.getOwnerMod().orElse(null);
					if(mod == null) continue;
					var tg = new ScanTarget(data.clazz(), data.getMemberName());
					map.put(tg, result.apply(tg, mod, data));
				}
			}
		}).build();
	}
	
	public static class ScanProperties
			implements Predicate<ModFileScanData.AnnotationData>
	{
		private final Map<String, Object> entries;
		
		public ScanProperties(Object... keyValuePairs)
		{
			if(keyValuePairs.length % 2 != 0)
				throw new IllegalArgumentException("Odd amount of key & value pairs!");
			ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
			for(int i = 0; i < keyValuePairs.length; i += 2)
			{
				String key = Cast.cast(keyValuePairs[i], String.class);
				Object value = keyValuePairs[i + 1];
				if(key == null)
					throw new NullPointerException(
							"Key argument @" + i + " is " + (keyValuePairs[i] == null ? "null" : "not a string") + "!");
				builder.put(key, value);
			}
			this.entries = builder.build();
		}
		
		public boolean matches(Map<String, Object> data)
		{
			if(entries.size() > data.size())
				return false;
			for(String key : entries.keySet())
				if(!data.containsKey(key))
					return false;
				else if(!Objects.equals(entries.get(key), data.get(key)))
					return false;
			return true;
		}
		
		@Override
		public boolean test(ModFileScanData.AnnotationData t)
		{
			return matches(t.annotationData());
		}
	}
	
	public static class ModAwareAnnotationData
	{
		private final ModFileScanData modFile;
		private Fetcher<Optional<FMLModContainer>> ownerMod;
		
		public final ModFileScanData.AnnotationData parent;
		
		public ModAwareAnnotationData(ModFileScanData.AnnotationData parent, ModFileScanData modFile)
		{
			this.parent = parent;
			this.modFile = modFile;
		}
		
		public Optional<FMLModContainer> getOwnerMod()
		{
			if(ownerMod == null)
			{
				ownerMod = Fetcher.fetchOnce(() -> modFile.getIModInfoData().stream()
						.flatMap(inf -> inf.getMods().stream())
						.map(IModInfo::getModId)
						.map(ModList.get()::getModContainerById)
						.map(modopt -> modopt.orElse(null))
						.filter(Predicates.instanceOf(FMLModContainer.class))
						.findFirst()
						.map(FMLModContainer.class::cast));
			}
			
			return ownerMod.get();
		}
		
		public ModFileScanData getModFile()
		{
			return modFile;
		}
		
		public Class<?> getOwnerClass()
		{
			return ReflectionUtil.fetchClass(parent.clazz());
		}
		
		public Type clazz()
		{
			return parent.clazz();
		}
		
		public Optional<Object> getProperty(String key)
		{
			Map<String, Object> map = parent.annotationData();
			return map.containsKey(key) ? Optional.ofNullable(map.get(key)) : Optional.empty();
		}
		
		public ElementType getTargetType()
		{
			return parent.targetType();
		}
		
		public String getMemberName()
		{
			return parent.memberName();
		}
	}
	
	public record ScanTarget(Type owner, String member)
	{
		public static ScanTarget of(Class<?> cls, String member)
		{
			return new ScanTarget(Type.getType(cls), member);
		}
	}
}