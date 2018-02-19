package com.pengu.hammercore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pengu.hammercore.common.items.iTooltipInjector;
import com.pengu.hammercore.math.ExpressionEvaluator;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TooltipAPI
{
	private static final ThreadLocal<Map<String, String>> results = ThreadLocal.withInitial(() -> new HashMap<>());
	private static final ThreadLocal<Map<String, String>> currentVars = ThreadLocal.withInitial(() -> new HashMap<>());
	
	@SubscribeEvent
	public void tooltipEvt(ItemTooltipEvent evt)
	{
		try
		{
			List<String> tooltip = evt.getToolTip();
			
			Map<String, String> currentVars = TooltipAPI.currentVars.get();
			Map<String, String> results = TooltipAPI.results.get();
			
			currentVars.clear();
			
			currentVars.put("count", "" + evt.getItemStack().getCount());
			currentVars.put("damage", "" + evt.getItemStack().getItemDamage());
			currentVars.put("nbt", "" + evt.getItemStack().getTagCompound());
			
			if(evt.getItemStack().getItem() instanceof iTooltipInjector)
				((iTooltipInjector) evt.getItemStack().getItem()).injectVariables(evt.getItemStack(), currentVars);
			
			int i = 0;
			while(true)
			{
				String b = evt.getItemStack().getUnlocalizedName() + ".tooltip" + i;
				String l = I18n.format(b);
				if(b.equals(l))
					break;
				
				for(String var : currentVars.keySet())
					l = l.replaceAll("&" + var, currentVars.get(var));
				
				try
				{
					if(l.contains("parse[") && l.substring(l.indexOf("parse[")).contains("]"))
					{
						String sput = "";
						String toEat = l;
						int size = 0;
						while(toEat.contains("parse[") && toEat.substring(toEat.indexOf("parse[")).contains("]"))
						{
							int parseStart = toEat.indexOf("parse[");
							sput += toEat.substring(0, parseStart);
							String fullExpr = toEat.substring(parseStart);
							fullExpr = fullExpr.substring(0, fullExpr.indexOf("]") + 1);
							String expr = fullExpr.substring(6, fullExpr.length() - 1);
							
							if(results.containsKey(expr))
							{
								sput += results.get(expr);
							} else
							{
								try
								{
									String res = ExpressionEvaluator.evaluate(expr);
									sput += res;
									results.put(expr, res);
								} catch(Throwable err)
								{
									l = l.replaceAll(fullExpr, "ERROR: " + expr);
								}
							}
							
							int s = parseStart + fullExpr.length();
							size += s;
							toEat = toEat.substring(s);
						}
						l = sput;
					}
				} catch(Throwable err)
				{
				}
				
				tooltip.add(l);
				++i;
			}
		} catch(Throwable err)
		{
			// This happened once in LT for no reason
		}
	}
}