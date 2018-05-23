package com.pengu.hammercore.math.functions;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

/**
 * All functions of the past are contained in this small class
 */
public class FunctionMath extends ExpressionFunction
{
	// Create unrepeatable random. Cannot be the same.
	private static final SecureRandom rand = new SecureRandom(((System.currentTimeMillis() + System.nanoTime()) + "").getBytes());
	
	public static final FunctionMath inst = new FunctionMath();
	
	public FunctionMath()
	{
		super("Math");
	}
	
	private final Set<String> allowedFuncs = new HashSet<>();
	
	{
		for(Method m : Math.class.getMethods())
			if(Modifier.isStatic(m.getModifiers()) && Modifier.isPublic(m.getModifiers()) && m.getParameterTypes().length == 1 && (m.getParameterTypes()[0] == double.class || m.getParameterTypes()[0] == Double.class))
				allowedFuncs.add(m.getName());
	}
	
	@Override
	public boolean accepts(String functionName, double x)
	{
		functionName = functionName.toLowerCase();
		return allowedFuncs.contains(functionName) || functionName.equals("rand");
	}
	
	@Override
	public double apply(String functionName, double x)
	{
		functionName = functionName.toLowerCase();
		
		if(functionName.equals("rand"))
			return (((double) rand.nextInt(Integer.MAX_VALUE)) / ((double) Integer.MAX_VALUE)) * x;
		
		if(functionName.equals("sin") || functionName.equals("cos") || functionName.equals("tan"))
			x = Math.toRadians(x);
		
		try
		{
			return (Double) Math.class.getMethod(functionName, double.class).invoke(null, x);
		} catch(Throwable er)
		{
		}
		
		return x;
	}
}