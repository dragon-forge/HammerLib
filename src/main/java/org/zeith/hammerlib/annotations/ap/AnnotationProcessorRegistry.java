package org.zeith.hammerlib.annotations.ap;

import net.minecraft.Util;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.jetbrains.annotations.*;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.util.java.*;
import org.zeith.hammerlib.util.mcf.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Additional processors for fields inside classes with @{@link org.zeith.hammerlib.annotations.SimplyRegister} annotation.
 */
public class AnnotationProcessorRegistry
{
	private static final Map<Class<? extends Annotation>, List<IAnnotationProcessor<?>>> AP = new HashMap<>();
	private static final Map<Class<? extends Annotation>, IAnnotationObtainer<?, ?>> OBTAINERS = new HashMap<>();
	
	static
	{
		boolean forced = ModHelper.isClient();
		
		for(var data : ScanDataHelper.lookupAnnotatedObjects(RegisterAP.class))
		{
			var clientOnly = (boolean) data.getProperty("clientOnly").orElse(false);
			var type = (Type) data.getProperty("value").orElse(null);
			
			if(type == null) continue;
			if(!forced && clientOnly) continue;
			
			Class<? extends Annotation> t = ReflectionUtil.fetchClass(type);
			try
			{
				register(t, IAnnotationProcessor.class.cast(data.getOwnerClass().getDeclaredConstructor().newInstance()));
				HammerLib.LOG.info("Register AP {} for {}.", data.getOwnerClass(), t);
			} catch(ReflectiveOperationException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
	
	public static <T extends Annotation> void register(Class<T> annotation, IAnnotationProcessor<T> ap)
	{
		var ret = annotation.getAnnotation(Retention.class);
		if(ret != null && ret.value() == RetentionPolicy.SOURCE)
			throw new IllegalArgumentException("Annotation type " + annotation.getName() + " has source retention and thus can not be processed by HammerLib as it's not registered at all.");
		AP.computeIfAbsent(annotation, v -> new ArrayList<>()).add(ap);
		OBTAINERS.computeIfAbsent(annotation, AnnotationProcessorRegistry::retrieverOf);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void scan(IAPContext ctx, Field f, Object value)
	{
		if(AP.isEmpty()) return;
		for(Annotation annotation : f.getDeclaredAnnotations())
		{
			List<IAnnotationProcessor<?>> aps = AP.get(annotation.annotationType());
			if(aps == null) continue;
			for(IAnnotationProcessor ap : aps)
				ap.onScanned(ctx, annotation, f, value);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void scanReg(IAPContext ctx, Field f, Object value, boolean postReg)
	{
		if(AP.isEmpty()) return;
		
		var an = allAnnotationsFor(f);
		Set<Type> unvisited = new HashSet<>(an.keySet());
		
		for(Annotation annotation : f.getDeclaredAnnotations())
		{
			unvisited.remove(Type.getType(annotation.annotationType()));
			List<IAnnotationProcessor<?>> aps = AP.get(annotation.annotationType());
			if(aps == null) continue;
			for(IAnnotationProcessor ap : aps)
			{
				if(postReg)
					ap.onPostRegistered(ctx, annotation, f, value);
				else
					ap.onPreRegistered(ctx, annotation, f, value);
			}
		}
		
		for(Type type : unvisited)
		{
			Class<? extends Annotation> t = ReflectionUtil.fetchClass(type);
			List<IAnnotationProcessor<?>> aps = AP.get(t);
			if(aps == null) continue;
			IAnnotationObtainer data = OBTAINERS.computeIfAbsent(t, AnnotationProcessorRegistry::retrieverOf);
			Annotation annotation = data.obtain(f);
			for(IAnnotationProcessor ap : aps)
			{
				if(postReg)
					ap.onPostRegistered(ctx, annotation, f, value);
				else
					ap.onPreRegistered(ctx, annotation, f, value);
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void scan(IAPContext ctx, Method m)
	{
		if(AP.isEmpty()) return;
		for(Annotation annotation : m.getDeclaredAnnotations())
		{
			List<IAnnotationProcessor<?>> aps = AP.get(annotation.annotationType());
			if(aps == null) continue;
			for(IAnnotationProcessor ap : aps)
				ap.onScanned(ctx, annotation, m);
		}
	}
	
	private static final Map<ElementPath, Map<Type, ModFileScanData.AnnotationData>> SCANNED = Util.make(new HashMap<>(), m ->
	{
		for(ModFileScanData dat : ModList.get().getAllScanData())
		{
			for(ModFileScanData.AnnotationData ad : dat.getAnnotations())
			{
				var ads = m.computeIfAbsent(new ElementPath(ad.clazz(), ad.memberName()), __ -> new HashMap<>());
				ads.put(ad.annotationType(), ad);
			}
		}
	});
	
	@NotNull
	public static <M extends AccessibleObject & Member> Map<Type, ModFileScanData.AnnotationData> allAnnotationsFor(M member)
	{
		ElementPath path = new ElementPath(Type.getType(member.getDeclaringClass()), member.getName());
		return SCANNED.getOrDefault(path, Map.of());
	}
	
	public static <T extends Annotation> IAnnotationObtainer<T, ?> retrieverOf(Class<T> type)
	{
		var ret = type.getAnnotation(Retention.class);
		
		if(ret != null && ret.value() == RetentionPolicy.RUNTIME) // direct
			return el -> el.getDeclaredAnnotation(type);
		
		var at = Type.getType(type);
		
		return el -> // through mod scans
		{
			var ad = allAnnotationsFor(el).getOrDefault(at, null);
			if(ad == null) return null;
			return AnnotationFactory.annotation(type, ad.annotationData());
		};
	}
	
	private record ElementPath(Type cls, String member) {}
	
	public interface IAnnotationObtainer<T extends Annotation, M extends AccessibleObject & Member>
	{
		@Nullable
		T obtain(M element);
	}
}