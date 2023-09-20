package org.zeith.hammerlib.util.java;

import net.minecraft.resources.*;
import net.minecraft.util.StringRepresentable;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Hashers
{
	public static final Hashers MD5 = new Hashers("MD5");
	public static final Hashers SHA1 = new Hashers("SHA1");
	public static final Hashers SHA256 = new Hashers("SHA256");
	
	public static long hashCodeL(Object... a)
	{
		if(a == null)
			return 0;
		long result = 1;
		for(Object element : a)
		{
			long add;
			
			if(element instanceof StringRepresentable sr)
				add = hashCodeL4Chars(sr.getSerializedName().toCharArray());
			else if(element instanceof CharSequence || element instanceof ResourceLocation || element instanceof ResourceKey<?>)
				add = hashCodeL4Chars(element.toString().toCharArray());
			else
				add = element == null ? 0 : element.hashCode();
			
			result = 31L * result + add;
		}
		return result;
	}
	
	public static long hashCodeL4Chars(char... a)
	{
		if(a == null)
			return 0;
		long result = 1;
		for(var el : a)
			result = 31L * result + Character.hashCode(el);
		return result;
	}
	
	final String algorithm;
	
	public Hashers(String algorithm)
	{
		this.algorithm = algorithm;
	}
	
	public Digestion digestion()
	{
		return new Digestion(this);
	}
	
	protected MessageDigest newDigest()
	{
		try
		{
			return MessageDigest.getInstance(algorithm);
		} catch(NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public String hashify(byte[] data)
	{
		MessageDigest messageDigest = newDigest();
		messageDigest.reset();
		messageDigest.update(data);
		byte[] digest = messageDigest.digest();
		
		BigInteger bigInt = new BigInteger(1, digest);
		String md5Hex = bigInt.toString(16);
		while(md5Hex.length() < 32)
		{
			md5Hex = "0" + md5Hex;
		}
		return md5Hex;
	}
	
	public String hashify(String line)
	{
		return hashify(line.getBytes());
	}
	
	public String genFolderHash(File prime)
	{
		if(!prime.exists())
			return "ERROR";
		ArrayList<File> paths = new ArrayList<File>();
		ArrayList<File> files = new ArrayList<File>();
		paths.add(prime);
		int lastSize = 0;
		while(paths.size() != lastSize)
		{
			lastSize = paths.size();
			int i = 0;
			while(i < paths.size())
			{
				File f = paths.get(i);
				if(f.isDirectory())
				{
					File[] arrfile = f.listFiles();
					int n = arrfile.length;
					int n2 = 0;
					while(n2 < n)
					{
						File r = arrfile[n2];
						if(r.isDirectory() && !paths.contains(r))
						{
							paths.add(r);
						} else if(r.isFile() && !files.contains(r))
						{
							files.add(r);
						}
						++n2;
					}
				} else if(f.isFile() && !files.contains(f))
				{
					files.add(f);
				}
				++i;
			}
		}
		StringBuilder b = new StringBuilder();
		for(File k : files)
		{
			if(k.equals(prime))
				continue;
			try
			{
				b.append(genHash(k) + k.getAbsolutePath().substring(prime.getAbsolutePath().length())
						.replace(File.separatorChar, ' ') + ";");
			} catch(Throwable err)
			{
				err.printStackTrace(System.out);
			}
		}
		return hashify(b.toString().getBytes());
	}
	
	public String genHash(File file)
	{
		if(file.isDirectory())
			return genFolderHash(file);
		byte[] b = null;
		try
		{
			b = createChecksum(file);
		} catch(Exception e)
		{
			e.printStackTrace(System.out);
		}
		BigInteger bigInt = new BigInteger(1, b);
		String md5Hex = bigInt.toString(16);
		while(md5Hex.length() < 32)
			md5Hex = "0" + md5Hex;
		return md5Hex;
	}
	
	private byte[] createChecksum(File file)
			throws Exception
	{
		int numRead;
		if(!file.exists())
		{
			MessageDigest messageDigest = newDigest();
			messageDigest.reset();
			messageDigest.update("0".getBytes());
			return messageDigest.digest();
		}
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance(algorithm);
		do
		{
			if((numRead = fis.read(buffer)) <= 0)
				continue;
			complete.update(buffer, 0, numRead);
		} while(numRead != -1);
		fis.close();
		return complete.digest();
	}
	
	public static class Digestion
	{
		final Hashers hasher;
		MessageDigest digest;
		
		public Digestion(Hashers hasher)
		{
			this.hasher = hasher;
		}
		
		public Digestion start()
		{
			digest = hasher.newDigest();
			return this;
		}
		
		public Digestion feed(byte[] input)
		{
			digest.update(input);
			return this;
		}
		
		public Digestion feed(byte[] input, int off, int len)
		{
			digest.update(input, off, len);
			return this;
		}
		
		public byte[] digestRaw()
		{
			byte[] r = digest.digest();
			digest.reset();
			return r;
		}
		
		public String digestHex()
		{
			BigInteger bigInt = new BigInteger(1, digestRaw());
			String hex = bigInt.toString(16);
			while(hex.length() < 32)
				hex = "0" + hex;
			return hex;
		}
	}
}