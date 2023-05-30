package org.zeith.hammerlib.util.java;

import java.io.*;
import java.util.Locale;

/**
 * The OSArch class provides utility methods for retrieving information about the operating system architecture.
 * It determines the architecture type, instruction set, and operating system distribution.
 */
public class OSArch
{
	private static final String OS_NAME = System.getProperty("os.name");
	private static final String PROCESSOR_IDENTIFIER;
	private static final String OS_ARCH = System.getProperty("os.arch");
	private static final ArchDistro ARCHITECTURE;
	private static final InstructionSet INSTRUCTIONS;
	
	// Static initialization block to determine the architecture and instruction set
	static
	{
		String osn = OS_NAME.toLowerCase(Locale.ROOT);
		
		ArchDistro distro = ArchDistro.UNKNOWN;
		InstructionSet insn = InstructionSet.X86;
		
		boolean is64 = OS_ARCH.contains("64");
		
		if(OS_ARCH.equals("amd64") || (OS_ARCH.contains("x86") && is64))
			insn = InstructionSet.X86_64;
		
		String details = "Unknown";
		if(osn.contains("windows"))
		{
			details = System.getenv("PROCESSOR_IDENTIFIER");
			distro = is64 ? ArchDistro.WINDOWS_X64 : ArchDistro.WINDOWS_X86;
		} else if(osn.contains("mac os"))
		{
			ProcessBuilder pb = new ProcessBuilder("sysctl", "-n", "machdep.cpu.brand_string");
			try
			{
				Process p = pb.start();
				try(BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream())))
				{
					details = br.readLine();
					p.waitFor();
				}
			} catch(InterruptedException | IOException x)
			{
				x.printStackTrace();
			}
			
			if(details.toLowerCase(Locale.ROOT).contains("apple"))
			{
				distro = ArchDistro.MACOS_APPLE;
				insn = is64 ? InstructionSet.ARM_64 : InstructionSet.ARM_32;
			} else
			{
				distro = ArchDistro.MACOS_INTEL;
				insn = is64 ? InstructionSet.X86_64 : InstructionSet.X86;
			}
		} else if(isUnix())
		{
			distro = ArchDistro.GNU_LINUX;
			insn = OS_ARCH.contains("arm") //
					? (is64 ? InstructionSet.ARM_64 : InstructionSet.ARM_32) //
					: (is64 ? InstructionSet.X86_64 : InstructionSet.X86);
		}
		
		PROCESSOR_IDENTIFIER = details;
		INSTRUCTIONS = insn;
		ARCHITECTURE = distro;
	}
	
	/**
	 * Retrieves the architecture distribution of the operating system.
	 *
	 * @return the architecture distribution
	 */
	public static ArchDistro getArchitecture()
	{
		return ARCHITECTURE;
	}
	
	/**
	 * Retrieves the instruction set of the operating system.
	 *
	 * @return the instruction set
	 */
	public static InstructionSet getInstructions()
	{
		return INSTRUCTIONS;
	}
	
	/**
	 * Checks whether the operating system is Unix-based.
	 *
	 * @return {@code true} if the operating system is Unix-based, {@code false} otherwise
	 */
	public static boolean isUnix()
	{
		return OS_NAME.contains("nix") || OS_NAME.contains("nux") || OS_NAME.contains("aix");
	}
	
	/**
	 * Retrieves the processor identifier of the system.
	 *
	 * @return the processor identifier
	 */
	public static String getProcessorIdentifier()
	{
		return PROCESSOR_IDENTIFIER;
	}
	
	/**
	 * Enumeration of the instruction sets.
	 */
	public enum InstructionSet
	{
		X86,
		X86_64,
		ARM_32,
		ARM_64
	}
	
	/**
	 * Enumeration of the architecture distributions.
	 */
	public enum ArchDistro
	{
		WINDOWS_X86(OSType.WINDOWS),
		WINDOWS_X64(OSType.WINDOWS),
		GNU_LINUX(OSType.UNIX),
		MACOS_INTEL(OSType.MACOS),
		MACOS_APPLE(OSType.MACOS),
		UNKNOWN(OSType.UNKNOWN);
		
		private final OSType type;
		
		ArchDistro(OSType type)
		{
			this.type = type;
		}
		
		/**
		 * Retrieves the operating system type associated with the architecture distribution.
		 *
		 * @return the operating system type
		 */
		public OSType getType()
		{
			return type;
		}
	}
	
	/**
	 * Enumeration of the operating system types.
	 */
	public enum OSType
	{
		WINDOWS,
		MACOS,
		UNIX,
		UNKNOWN;
	}
}