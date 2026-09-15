package org.zeith.hammerlib.client.flowgui.reader;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlFlowgui
{
	String value();
	
	boolean registerToJei() default true;
}