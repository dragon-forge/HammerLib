package org.zeith.hammerlib.util.java.reflection;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

public class DelegateCodegen
{
	public static String generateDelegate(Class<?> owner, String name, String ownerFieldName)
	{
		String ownerName = tts(owner);
		StringBuilder root = new StringBuilder();
		
		Set<Class<?>> imported = new HashSet<>();
		
		StringBuilder methods = new StringBuilder();
		
		for(Method m : owner.getMethods())
		{
			int mod = m.getModifiers();
			if(Modifier.isStatic(mod) || !Modifier.isPublic(mod) || m.getDeclaringClass().equals(Object.class)) continue;
			
			methods.append("\tpublic ")
					.append(tts(m.getReturnType()))
					.append(" ")
					.append(m.getName())
					.append("(")
					.append(Arrays.stream(m.getParameters()).map(par -> tts(par.getType()) + " " + par.getName()).collect(Collectors.joining(", ")))
					.append(")");
			
			methods.append("\n\t{\n\t\t");
			if(!m.getReturnType().equals(void.class)) methods.append("return ");
			methods.append(ownerFieldName)
					.append(".")
					.append(m.getName())
					.append("(")
					.append(Arrays.stream(m.getParameters()).map(Parameter::getName).collect(Collectors.joining(", ")))
					.append(")");
			methods.append(";\n\t}\n\n");
		}
		
		for(Class<?> c : imported) root.append("import ").append(c.getName()).append(";\n");
		
		root.append("\npublic class ").append(name).append("\n{\n");
		root.append("\tprivate final ").append(ownerName).append(" ").append(ownerFieldName).append(";");
		root.append("\n");
		root.append("\tpublic ").append(name).append("(").append(ownerName).append(" ").append(ownerFieldName).append(")");
		root.append("\n\t{");
		root.append("\n\t\tthis.").append(ownerFieldName).append(" = ").append(ownerFieldName).append(";");
		root.append("\n\t}\n\n");
		root.append(methods);
		root.append("}");
		
		return root.toString();
	}
	
	private static String tts(Class<?> type)
	{
		if(type.isArray()) return tts(type.componentType()) + "[]";
		return type.getName();
	}
}