package org.zeith.hammerlib.annotations.ide;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface FileReference
{
	String[] regex();
	
	String[] value();
}