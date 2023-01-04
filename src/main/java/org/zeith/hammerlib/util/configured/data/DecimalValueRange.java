package org.zeith.hammerlib.util.configured.data;

import org.zeith.hammerlib.util.configured.struct.RangeDouble;
import org.zeith.hammerlib.util.configured.struct.RangeFloat;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Predicate;

public class DecimalValueRange
		implements Predicate<Number>
{
	private static final BigDecimal MIN_FLOAT_SHIFT = BigDecimal.valueOf(0.000000000000000000000000000000000000000000001F);
	
	protected final BigDecimal min, max;
	protected final boolean minInclusive, maxInclusive, eq;
	
	public DecimalValueRange(BigDecimal min, BigDecimal max, boolean minInclusive, boolean maxInclusive)
	{
		this.min = min;
		this.max = max;
		this.minInclusive = minInclusive;
		this.maxInclusive = maxInclusive;
		
		if(max != null && max.compareTo(min) < 0) throw new IllegalArgumentException("The min value of a range is greater than max value. Why?");
		
		boolean e = max != null && max.compareTo(min) == 0;
		this.eq = e && (minInclusive || maxInclusive);
		
		if(e && !test(min)) throw new IllegalArgumentException("The min value of a range is equal to max value, but is not included. Why?");
	}
	
	public static DecimalValueRange rangeClosed(BigDecimal minInclusive, BigDecimal maxInclusive)
	{
		return new DecimalValueRange(minInclusive, maxInclusive, true, true);
	}
	
	public static DecimalValueRange range(BigDecimal minInclusive, BigDecimal maxExclusive)
	{
		return new DecimalValueRange(minInclusive, maxExclusive, true, false);
	}
	
	public static DecimalValueRange min(BigDecimal minExclusive)
	{
		return new DecimalValueRange(minExclusive, null, false, false);
	}
	
	public static DecimalValueRange minClosed(BigDecimal minInclusive)
	{
		return new DecimalValueRange(minInclusive, null, true, false);
	}
	
	public static DecimalValueRange max(BigDecimal maxExclusive)
	{
		return new DecimalValueRange(null, maxExclusive, false, false);
	}
	
	public static DecimalValueRange maxClosed(BigDecimal maxInclusive)
	{
		return new DecimalValueRange(null, maxInclusive, false, true);
	}
	
	
	public static DecimalValueRange rangeClosed(double minInclusive, double maxInclusive)
	{
		return rangeClosed(BigDecimal.valueOf(minInclusive), BigDecimal.valueOf(maxInclusive));
	}
	
	public static DecimalValueRange range(double minInclusive, double maxExclusive)
	{
		return range(BigDecimal.valueOf(minInclusive), BigDecimal.valueOf(maxExclusive));
	}
	
	public static DecimalValueRange min(double minExclusive)
	{
		return min(BigDecimal.valueOf(minExclusive));
	}
	
	public static DecimalValueRange minClosed(double minInclusive)
	{
		return minClosed(BigDecimal.valueOf(minInclusive));
	}
	
	public static DecimalValueRange max(double maxExclusive)
	{
		return max(BigDecimal.valueOf(maxExclusive));
	}
	
	public static DecimalValueRange maxClosed(double maxInclusive)
	{
		return maxClosed(BigDecimal.valueOf(maxInclusive));
	}
	
	public static DecimalValueRange fromFloatRange(RangeFloat r)
	{
		if(r.min() == Float.MIN_VALUE) return maxClosed(r.max());
		if(r.max() == Float.MAX_VALUE) return minClosed(r.min());
		return rangeClosed(r.min(), r.max());
	}
	
	public static DecimalValueRange fromDoubleRange(RangeDouble r)
	{
		if(r.min() == Double.MIN_VALUE) return maxClosed(r.max());
		if(r.max() == Double.MAX_VALUE) return minClosed(r.min());
		return rangeClosed(r.min(), r.max());
	}
	
	public BigDecimal enclose(BigDecimal origin)
	{
		if(origin == null) return null;
		if(min != null) origin = origin.max(minInclusive ? min : min.add(MIN_FLOAT_SHIFT));
		if(max != null) origin = origin.min(maxInclusive ? max : max.subtract(MIN_FLOAT_SHIFT));
		return origin;
	}
	
	@Override
	public boolean test(Number number)
	{
		BigDecimal big;
		if(number instanceof BigDecimal) big = (BigDecimal) number;
		else if(number instanceof BigInteger) big = new BigDecimal((BigInteger) number);
		else big = BigDecimal.valueOf(number.longValue());
		
		if(eq && big.equals(min))
			return true;
		
		if(min != null && big.compareTo(min) < (minInclusive ? 0 : 1))
			return false;
		
		return max == null || big.compareTo(max) <= (maxInclusive ? 0 : 1);
	}
	
	@Override
	public String toString()
	{
		return (minInclusive && min != null ? "[" : "(") + (min != null ? min : "-") + "; " + (max != null ? max : "+") + (maxInclusive && max != null ? "]" : ")");
	}
}