package com.pengu.hammercore.hlang;

import java.util.Map;

public interface iHLScript
{
	Map<String, Class> getVariables();
	
	Map<String, Object> getVariableValues();
	
	iHLFunction getFunction();
}