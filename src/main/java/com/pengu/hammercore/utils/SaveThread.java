package com.pengu.hammercore.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveThread extends Thread
{
	private final List<String> files = new ArrayList<>();
	private final Map<String, byte[]> toSave = new HashMap<>();
	
	public void addSaveFile(String out, byte[] data)
	{
		toSave.put(out, data);
		files.add(out);
		synchronized(toSave)
		{
			toSave.notifyAll();
		}
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			synchronized(toSave)
			{
				try
				{
					toSave.wait();
				} catch(Throwable err)
				{
				}
			}
			
			File classesBase = new File("classes");
			if(!classesBase.isDirectory())
				classesBase.mkdirs();
			
			while(!files.isEmpty())
			{
				String file = files.remove(0);
				byte[] data = toSave.remove(file);
				File f = new File(classesBase, file);
				try
				{
					FileOutputStream fos = new FileOutputStream(f);
					fos.write(data);
					fos.close();
				} catch(Throwable err)
				{
				}
			}
		}
	}
}