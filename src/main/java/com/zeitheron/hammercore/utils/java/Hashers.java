package com.zeitheron.hammercore.utils.java;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Hashers
{
	public static final Hashers MD5 = new Hashers("MD5", 32);
	public static final Hashers SHA1 = new Hashers("SHA1", 40);
	public static final Hashers SHA256 = new Hashers("SHA-256", 64);
	
	public static long hashCodeL(Object... a)
	{
		if(a == null)
			return 0;
		long result = 1;
		for(Object element : a)
		{
			long add;
			
			if(element instanceof CharSequence)
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
		for(char el : a)
			result = 31L * result + Character.hashCode(el);
		return result;
	}
	
	final String algorithm;
	final int hexLength;
	
	public Hashers(String algorithm, int hexLength)
	{
		this.algorithm = algorithm;
		this.hexLength = hexLength;
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
	
	public byte[] hashifyRaw(byte[] data)
	{
		MessageDigest messageDigest = newDigest();
		messageDigest.reset();
		messageDigest.update(data);
		return messageDigest.digest();
	}
	
	public String hashifyHex(byte[] data)
	{
		byte[] digest = hashifyRaw(data);
		BigInteger bigInt = new BigInteger(1, digest);
		String hex = bigInt.toString(16);
		while(hex.length() < hexLength) hex = "0" + hex;
		return hex;
	}
	
	public String hashifyHex(String line)
	{
		return hashifyHex(line.getBytes());
	}

	public String encrypt(byte[] data)
	{
		MessageDigest messageDigest = null;
		byte[] digest = new byte[]{};
		try
		{
			messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.reset();
			messageDigest.update(data);
			digest = messageDigest.digest();
		} catch(NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		BigInteger bigInt = new BigInteger(1, digest);
		String md5Hex = bigInt.toString(16);
		while(md5Hex.length() < hexLength)
			md5Hex = "0" + md5Hex;
		return md5Hex;
	}

	public String encrypt(String line)
	{
		return encrypt(line.getBytes());
	}

	public String createFolderMD5(String prime)
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
				b.append(getMD5Checksum(k) + k.substring(prime.length()).replace(File.separatorChar, ' ') + ";");
			} catch(Throwable err)
			{
				err.printStackTrace(System.out);
			}
		}
		return encrypt(b.toString().getBytes());
	}

	public byte[] createChecksum(String filename) throws Exception
	{
		int numRead;
		if(!new File(filename).exists())
		{
			MessageDigest messageDigest;
			byte[] digest = new byte[]{};
			try
			{
				messageDigest = MessageDigest.getInstance(algorithm);
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

	public String getMD5Checksum(String filename)
	{
		byte[] b = null;
		try
		{
			b = createChecksum(filename);
		} catch(Exception e)
		{
			e.printStackTrace(System.out);
		}
		BigInteger bigInt = new BigInteger(1, b);
		String md5Hex = bigInt.toString(16);
		while(md5Hex.length() < 32) md5Hex = "0" + md5Hex;
		return md5Hex;
	}
}