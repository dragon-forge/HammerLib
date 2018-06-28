package com.zeitheron.hammercore.utils.math.functions;

public abstract class ExpressionFunction
{
	public final String functionName;
	
	public ExpressionFunction(String funcName)
	{
		functionName = funcName;
	}
	
	public boolean accepts(String functionName, double x)
	{
		return this.functionName.equalsIgnoreCase(functionName);
	}
	
	public double apply(String functionName, double x)
	{
		return apply(x);
	}
	
	@Deprecated
	public double apply(double x)
	{
		return x;
	}
}