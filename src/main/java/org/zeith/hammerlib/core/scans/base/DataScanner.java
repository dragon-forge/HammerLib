package org.zeith.hammerlib.core.scans.base;

import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class DataScanner
{
	private final List<IScanListener> listeners = new ArrayList<>();
	
	private DataScanner()
	{
	}
	
	public static DataScanner start()
	{
		return new DataScanner();
	}
	
	public void add(IScanListener listener)
	{
		listeners.add(listener);
	}
	
	public static void finish(DataScanner scanner)
	{
		List<IScanListener> lst = scanner.listeners;
		for(var d : ModList.get().getAllScanData())
			for(IScanListener l : lst)
				l.accept(d);
		lst.clear();
	}
}