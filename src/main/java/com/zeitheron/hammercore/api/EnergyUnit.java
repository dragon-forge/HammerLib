package com.zeitheron.hammercore.api;

import java.util.HashMap;
import java.util.Map;

/**
 * This class helps convert energy values across different power systems
 */
public class EnergyUnit
{
	private static final Map<String, EnergyUnit> UNITS = new HashMap<>();
	
	public static final EnergyUnit RF = getUnit("rf", 1);
	public static final EnergyUnit FE = getUnit("fe", 1);
	public static final EnergyUnit TESLA = getUnit("tesla", 1);
	public static final EnergyUnit EU = getUnit("eu", 4);
	
	/**
	 * This value was calculated using 16000 / 128. <br>
	 * Let me explain how we got this value: <br>
	 * Average generators produce 16000 RF from single coal. One coal costs 128
	 * EMC, thus diving 16000 by 128 will give us the amount of RF that single
	 * EMC costs.
	 */
	public static final EnergyUnit EMC = getUnit("emc", 125);
	
	/**
	 * Gets the unit, or registers one, if not found.
	 * 
	 * @param id
	 *            the ID of the unit. Ex: Forge Energy would be "fe"
	 * @param toRF
	 *            the amount of RF to get 1 unit of this energy
	 * @return the unit - may be already registered, or newly created
	 */
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
	
	/**
	 * Gets the amount of RF converted from THIS energy unit.
	 * 
	 * @param x
	 *            the amount of THIS energy units to be converted to RF.
	 * @return the amount of RF converted from THIS energy units.
	 */
	public double getInRF(double x)
	{
		return x * toRF;
	}
	
	/**
	 * Gets THIS amount of energy from REDSTONE FLUX system.
	 * 
	 * @param rf
	 *            the amount of RF that we should convert to THIS unit
	 * @return the amount of THIS energy units converted from RF
	 */
	public double getFromRF(double rf)
	{
		return rf / toRF;
	}
	
	/**
	 * Converts the given amount of THIS energy unit to TARGET unit.
	 * 
	 * @param x
	 *            the amount of energy from this energy type
	 * @param targetUnit
	 *            the target energy type we should convert to
	 * @return the amount of energy in TARGET energy unit
	 */
	public double convertTo(double x, EnergyUnit targetUnit)
	{
		return targetUnit.getFromRF(getInRF(x));
	}
	
	/**
	 * Converts the given amount of SOURCE energy unit to THIS unit.
	 * 
	 * @param x
	 *            the amount of energy from source energy type
	 * @param sourceUnit
	 *            the source energy type we should convert from
	 * @return the amount of energy in THIS energy unit
	 */
	public double convertFrom(double x, EnergyUnit sourceUnit)
	{
		return sourceUnit.convertTo(x, this);
	}
}