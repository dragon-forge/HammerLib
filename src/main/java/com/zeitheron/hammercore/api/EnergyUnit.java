package com.zeitheron.hammercore.api;

import java.util.HashMap;
import java.util.Map;

public class EnergyUnit
{
	public static final EnergyUnit RF = getUnit("rf", 1);
	public static final EnergyUnit FE = getUnit("fe", 1);
	public static final EnergyUnit TESLA = getUnit("tesla", 1);
	public static final EnergyUnit EU = getUnit("eu", 4);
	
	/**
	 * This value was calculated using 16000 / 128. Let me explain: Average
	 * generators produce 16000 RF from single coal. One coal costs 128 EMC,
	 * thus diving 16000 by 128 will give us the amount of RF that single EMC
	 * costs.
	 */
	public static final EnergyUnit EMC = getUnit("emc", 125);
	
	private static final Map<String, EnergyUnit> UNITS = new HashMap<>();
	
	public static EnergyUnit getUnit(String id, double toRF)
	{
		if(UNITS.containsKey(id))
			return UNITS.get(id);
		return new EnergyUnit(id, toRF);
	}
	
	public final double toRF;
	
	public EnergyUnit(String id, double toRF)
	{
		UNITS.put(id, this);
		this.toRF = toRF;
	}
	
	public double getInRF(double x)
	{
		return x * toRF;
	}
	
	public double getFromRF(double rf)
	{
		return rf / toRF;
	}
	
	/**
	 * Converts the given amount of THIS energy unit to TARGET unit.
	 */
	public double convertTo(double x, EnergyUnit targetUnit)
	{
		return targetUnit.getFromRF(getInRF(x));
	}
}