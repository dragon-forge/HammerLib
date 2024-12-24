package org.zeith.hammerlib.client.flowgui.reader;

import org.zeith.hammerlib.annotations.ide.Namespace;
import org.zeith.hammerlib.proxy.HLConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Namespace(HLConstants.MOD_ID)
@Target(ElementType.TYPE)
public @interface FlowguiReader
{
	String value();
}