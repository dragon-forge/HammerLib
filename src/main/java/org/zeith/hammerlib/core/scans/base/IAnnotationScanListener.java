package org.zeith.hammerlib.core.scans.base;

import org.objectweb.asm.Type;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;

public interface IAnnotationScanListener
{
	void acceptAnnotationData(ScanDataHelper.ModAwareAnnotationData data);
	
	static IScanListener forAnnotation(Class<? extends Annotation> annotation, IAnnotationScanListener handler)
	{
		final var anType = Type.getType(annotation);
		return data ->
				data.getAnnotations().stream()
						.filter(ad -> ad.annotationType().equals(anType))
						.map(ad -> new ScanDataHelper.ModAwareAnnotationData(ad, data))
						.forEach(handler::acceptAnnotationData);
	}
	
	static IScanListener forAnnotation(Class<? extends Annotation> annotation, ElementType type, IAnnotationScanListener handler)
	{
		final var anType = Type.getType(annotation);
		return data ->
				data.getAnnotations().stream()
						.filter(ad -> ad.annotationType().equals(anType) && type == ad.targetType())
						.map(ad -> new ScanDataHelper.ModAwareAnnotationData(ad, data))
						.forEach(handler::acceptAnnotationData);
	}
}