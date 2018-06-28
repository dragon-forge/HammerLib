package com.zeitheron.hammercore.lib.zlib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MD5
{
	public static String encrypt(byte[] data)
	{
		MessageDigest messageDigest = null;
		byte[] digest = new byte[] {};
		try
		{
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(data);
			digest = messageDigest.digest();
		} catch(NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		BigInteger bigInt = new BigInteger(1, digest);
		String md5Hex = bigInt.toString(16);
		while(md5Hex.length() < 32)
		{
			md5Hex = "0" + md5Hex;
		}
		return md5Hex;
	}
	
	public static String encrypt(String line)
	{
		return encrypt(line.getBytes());
	}
	
	public static String createFolderMD5(String prime)
	{
		if(!new File(prime).exists())
			return "ERROR";
		ArrayList<String> paths = new ArrayList<String>();
		ArrayList<String> files = new ArrayList<String>();
		paths.add(prime);
		int lastSize = 0;
		while(paths.size() != lastSize)
		{
			lastSize = paths.size();
			int i = 0;
			while(i < paths.size())
			{
				String p = (String) paths.get(i);
				File f = new File(p);
				if(f.isDirectory())
				{
					File[] arrfile = f.listFiles();
					int n = arrfile.length;
					int n2 = 0;
					while(n2 < n)
					{
						File r = arrfile[n2];
						if(r.isDirectory() && !paths.contains(r.getAbsolutePath()))
						{
							paths.add(r.getAbsolutePath());
						} else if(r.isFile() && !files.contains(r.getAbsolutePath()))
						{
							files.add(r.getAbsolutePath());
						}
						++n2;
					}
				} else if(f.isFile() && !files.contains(f.getAbsolutePath()))
				{
					files.add(f.getAbsolutePath());
				}
				++i;
			}
		}
		StringBuilder b = new StringBuilder();
		for(String k : files)
		{
			if(k.equals(prime))
				continue;
			try
			{
				b.append(String.valueOf(MD5.getMD5Checksum(k)) + k.substring(prime.length()).replace(File.separatorChar, ' ') + ";");
			} catch(Throwable err)
			{
				err.printStackTrace(System.out);
			}
		}
		return MD5.encrypt(b.toString().getBytes());
	}
	
	public static byte[] createChecksum(String filename) throws Exception
	{
		int numRead;
		if(!new File(filename).exists())
		{
			MessageDigest messageDigest = null;
			byte[] digest = new byte[] {};
			try
			{
				messageDigest = MessageDigest.getInstance("MD5");
				messageDigest.reset();
				messageDigest.update("0".getBytes());
				digest = messageDigest.digest();
			} catch(NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			}
			return digest;
		}
		FileInputStream fis = new FileInputStream(filename);
		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		do
		{
			if((numRead = fis.read(buffer)) <= 0)
				continue;
			complete.update(buffer, 0, numRead);
		} while(numRead != -1);
		fis.close();
		return complete.digest();
	}
	
	public static String getMD5Checksum(String filename)
	{
		byte[] b = null;
		try
		{
			b = MD5.createChecksum(filename);
		} catch(Exception e)
		{
			e.printStackTrace(System.out);
		}
		BigInteger bigInt = new BigInteger(1, b);
		String md5Hex = bigInt.toString(16);
		while(md5Hex.length() < 32)
		{
			md5Hex = "0" + md5Hex;
		}
		return md5Hex;
	}
}
