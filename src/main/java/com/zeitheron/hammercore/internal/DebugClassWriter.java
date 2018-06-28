package com.zeitheron.hammercore.internal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class DebugClassWriter
{
	private static final Pattern anyPattern = Pattern.compile(".*");
	private static final Pattern classPattern = Pattern.compile(".*.class");
	
	public static void output(OutputStream output) throws IOException
	{
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output));
		
		for(Class<?> c : getClasses(anyPattern))
		{
			bw.write("> " + c);
			bw.newLine();
			for(Field f : c.getDeclaredFields())
			{
				bw.write("    " + f);
				bw.newLine();
			}
			for(Method m : c.getDeclaredMethods())
			{
				bw.write("    " + m);
				bw.newLine();
			}
			bw.flush();
		}
	}
	
	private static ArrayList<Class<?>> getClasses(Pattern patternPath)
	{
		ArrayList<Class<?>> cls = new ArrayList<>();
		patternPath = Pattern.compile(patternPath.pattern() + ".class");
		for(String cl : getResources(patternPath))
		{
			char[] table = cl.substring(0, cl.length() - 6).toCharArray();
			for(int i = 0; i < table.length; ++i)
				if(table[i] == '/' || table[i] == '\\')
					table[i] = '.';
			cl = new String(table);
			try
			{
				cls.add(Class.forName(cl, false, ClassLoader.getSystemClassLoader()));
			} catch(Throwable err)
			{
			} // Failed to load, skip.
		}
		return cls;
	}
	
	private static ArrayList<String> getResources(Pattern pattern)
	{
		ArrayList<String> retval = new ArrayList<String>();
		String classPath = System.getProperty("java.class.path", ".");
		String[] classPathElements = classPath.split(System.getProperty("path.separator"));
		for(String element : classPathElements)
			retval.addAll(getResources(element, pattern));
		return retval;
	}
	
	private static ArrayList<String> getResources(String element, Pattern pattern)
	{
		ArrayList<String> retval = new ArrayList<String>();
		File file = new File(element);
		if(file.isDirectory())
			retval.addAll(getResourcesFromDirectory(file, pattern, file.getAbsolutePath() + "/"));
		else
			retval.addAll(getResourcesFromJarFile(file, pattern));
		return retval;
	}
	
	private static ArrayList<String> getResourcesFromJarFile(File file, Pattern pattern)
	{
		ArrayList<String> retval = new ArrayList<String>();
		ZipFile zf;
		try
		{
			zf = new ZipFile(file);
		} catch(ZipException e)
		{
			throw new Error(e);
		} catch(FileNotFoundException e)
		{
			return retval;
		} catch(IOException e)
		{
			throw new Error(e);
		}
		Enumeration<?> e = zf.entries();
		while(e.hasMoreElements())
		{
			ZipEntry ze = (ZipEntry) e.nextElement();
			String fileName = ze.getName();
			boolean accept = pattern.matcher(fileName).matches();
			if(accept)
				retval.add(fileName);
		}
		try
		{
			zf.close();
		} catch(IOException e1)
		{
			throw new Error(e1);
		}
		return retval;
	}
	
	private static ArrayList<String> getResourcesFromDirectory(File directory, Pattern pattern, String begin)
	{
		ArrayList<String> retval = new ArrayList<String>();
		File[] fileList = directory.listFiles();
		for(File file : fileList)
		{
			if(file.isDirectory())
			{
				retval.addAll(getResourcesFromDirectory(file, pattern, begin));
			} else
			{
				try
				{
					String fileName = file.getCanonicalPath();
					char[] cs = fileName.toCharArray();
					for(int i = 0; i < cs.length; ++i)
						if(cs[i] == File.separatorChar)
							cs[i] = '/';
					fileName = new String(cs);
					boolean accept = pattern.matcher(fileName).matches();
					if(accept)
						retval.add(fileName.substring(begin.length()));
				} catch(IOException e)
				{
					throw new Error(e);
				}
			}
		}
		return retval;
	}
}