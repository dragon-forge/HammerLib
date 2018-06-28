package com.zeitheron.hammercore.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandTimeToTicks extends CommandBase
{
	@Override
	public String getName()
	{
		return "hc_timetoticks";
	}
	
	@Override
	public String getUsage(ICommandSender sender)
	{
		return "Use /hc_timetoticks <time:string> (time example: 1t1s1m1h1d1M1y - 1 tick and 1 second and 1 minute and 1 hour and 1 day and 1 month and 1 year)";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		sender.sendMessage(new TextComponentString("In ticks: " + fancyFormat(formatTimeToTicksWithCommandException(args[0]))));
	}
	
	public static long formatTimeToTicksWithCommandException(String time) throws CommandException
	{
		try
		{
			return formatTimeToTicks(time);
		} catch(RuntimeException e)
		{
			throw new CommandException(e.getMessage());
		}
	}
	
	public static String fancyFormat(long num)
	{
		String n0 = num + "";
		String n = "";
		
		char splitter = '	';
		
		for(int i = n0.length() - 1; i >= 0; --i)
		{
			n = n0.charAt(i) + n;
			if((n0.length() - i) % 3 == 0)
				n = splitter + n;
		}
		
		while(n.startsWith(splitter + ""))
			n = n.substring(1);
		
		while(n.endsWith(splitter + ""))
			n = n.substring(0, n.length() - 1);
		
		return n;
	}
	
	public static long formatTimeToTicks(String time)
	{
		long ticksElapsed = 0L;
		
		int last = 0;
		char[] arr = time.toCharArray();
		
		for(int i = 0; i < arr.length; ++i)
		{
			if(arr[i] >= '0' && arr[i] <= '9' || arr[i] == '-')
				continue;
			
			if(arr[i] == 't')
			{
				try
				{
					ticksElapsed += Integer.parseInt(new String(arr, last, i - last));
				} catch(Throwable err)
				{
				}
				last = i + 1;
			} else if(arr[i] == 's')
			{
				try
				{
					ticksElapsed += Integer.parseInt(new String(arr, last, i - last)) * 20L;
				} catch(Throwable err)
				{
				}
				last = i + 1;
			} else if(arr[i] == 'm')
			{
				try
				{
					ticksElapsed += Integer.parseInt(new String(arr, last, i - last)) * 20L * 60L;
				} catch(Throwable err)
				{
				}
				last = i + 1;
			} else if(arr[i] == 'h')
			{
				try
				{
					ticksElapsed += Integer.parseInt(new String(arr, last, i - last)) * 20L * 60L * 60L;
				} catch(Throwable err)
				{
				}
				last = i + 1;
			} else if(arr[i] == 'd')
			{
				try
				{
					ticksElapsed += Integer.parseInt(new String(arr, last, i - last)) * 20L * 60L * 60L * 24L;
				} catch(Throwable err)
				{
				}
				last = i + 1;
			} else if(arr[i] == 'M')
			{
				try
				{
					ticksElapsed += Integer.parseInt(new String(arr, last, i - last)) * 20L * 60L * 60L * 24L * 30L;
				} catch(Throwable err)
				{
				}
				last = i + 1;
			} else if(arr[i] == 'y')
			{
				try
				{
					ticksElapsed += Integer.parseInt(new String(arr, last, i - last)) * 20L * 60L * 60L * 24L * 30L * 365L;
				} catch(Throwable err)
				{
				}
				last = i + 1;
			} else
				throw new RuntimeException("Undefined time unit: " + arr[i]);
		}
		
		return ticksElapsed;
	}
}