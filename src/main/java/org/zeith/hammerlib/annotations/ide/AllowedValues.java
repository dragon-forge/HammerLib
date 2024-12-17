package org.zeith.hammerlib.annotations.ide;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A one or multiple allowed REGEX strings for the input field's validation.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedValues
{
	String BOOLEAN = "^true|false$";
	String POSITIVE_INTEGERS = "^0*[1-9]\\d*$";
	String ANY_INTEGERS = "^-?\\d+$";
	String NON_NEGATIVE_FLOAT = "^(0|[1-9]\\d*)(\\.\\d+)?$";
	String ANY_FLOAT = "^-?(0|[1-9]\\d*)(\\.\\d+)?$";
	String RESOURCE_LOCATION = "^([a-z0-9_.-]+:[a-z0-9_./-]+)|([a-z0-9_./-]+)$";
	
	String[] value();
}