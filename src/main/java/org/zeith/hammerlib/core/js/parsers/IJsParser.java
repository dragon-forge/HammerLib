package org.zeith.hammerlib.core.js.parsers;

import org.zeith.hammerlib.core.js.converters.IJsConverter;

import javax.script.ScriptEngine;
import java.util.List;

public interface IJsParser
{
	List<IJsConverter> getConverters();
	
	ScriptEngine create();
}