package org.zeith.hammerlib.util.configured.data;

import org.zeith.hammerlib.util.configured.struct.RangeInt;
import org.zeith.hammerlib.util.configured.struct.RangeLong;

import java.math.BigInteger;
import java.util.function.Predicate;

public class IntValueRange
		implements Predicate<Number>
{
	protected final BigInteger min, max;
	protected final boolean minInclusive, maxInclusive, eq;
	
	public IntValueRange(BigInteger min, BigInteger max, boolean minInclusive, boolean maxInclusive)
	{
		this.min = min;
		this.max = max;
		this.minInclusive = minInclusive;
		this.maxInclusive = maxInclusive;
		
		if(max != null && max.compareTo(min) < 0) throw new IllegalArgumentException("The min value of a range is greater than max value. Why?");
		
		var e = max != null && max.compareTo(min) == 0;
		this.eq = e && (minInclusive || maxInclusive);
		
		if(e && !test(min)) throw new IllegalArgumentException("The min value of a range is equal to max value, but is not included. Why?");
	}
	
	public static IntValueRange rangeClosed(BigInteger minInclusive, BigInteger maxInclusive)
	{
		return new IntValueRange(minInclusive, maxInclusive, true, true);
	}
	
	public static IntValueRange range(BigInteger minInclusive, BigInteger maxExclusive)
	{
		return new IntValueRange(minInclusive, maxExclusive, true, false);
	}
	
	public static IntValueRange min(BigInteger minExclusive)
	{
		return new IntValueRange(minExclusive, null, false, false);
	}
	
	public static IntValueRange minClosed(BigInteger minInclusive)
	{
		return new IntValueRange(minInclusive, null, true, false);
	}
	
	public static IntValueRange max(BigInteger maxExclusive)
	{
		return new IntValueRange(null, maxExclusive, false, false);
	}
	
	public static IntValueRange maxClosed(BigInteger maxInclusive)
	{
		return new IntValueRange(null, maxInclusive, false, true);
	}
	
	
	public static IntValueRange rangeClosed(long minInclusive, long maxInclusive)
	{
		return rangeClosed(BigInteger.valueOf(minInclusive), BigInteger.valueOf(maxInclusive));
	}
	
	public static IntValueRange range(long minInclusive, long maxExclusive)
	{
		return range(BigInteger.valueOf(minInclusive), BigInteger.valueOf(maxExclusive));
	}
	
	public static IntValueRange min(long minExclusive)
	{
		return min(BigInteger.valueOf(minExclusive));
	}
	
	public static IntValueRange minClosed(long minInclusive)
	{
		return minClosed(BigInteger.valueOf(minInclusive));
	}
	
	public static IntValueRange max(long maxExclusive)
	{
		return max(BigInteger.valueOf(maxExclusive));
	}
	
	public static IntValueRange maxClosed(long maxInclusive)
	{
		return maxClosed(BigInteger.valueOf(maxInclusive));
	}
	
	public static IntValueRange fromIntRange(RangeInt r)
	{
		if(r.min() == Integer.MIN_VALUE) return maxClosed(r.max());
		if(r.max() == Integer.MAX_VALUE) return minClosed(r.min());
		return rangeClosed(r.min(), r.max());
	}
	
	public static IntValueRange fromLongRange(RangeLong r)
	{
		if(r.min() == Long.MIN_VALUE) return maxClosed(r.max());
		if(r.max() == Long.MAX_VALUE) return minClosed(r.min());
		return rangeClosed(r.min(), r.max());
	}
	
	public BigInteger enclose(BigInteger origin)
	{
		if(origin == null) return null;
		if(min != null) origin = origin.max(minInclusive ? min : min.add(BigInteger.ONE));
		if(max != null) origin = origin.min(maxInclusive ? max : max.subtract(BigInteger.ONE));
		return origin;
	}
	
	@Override
	public boolean test(Number number)
	{
		BigInteger big;
		if(number instanceof BigInteger bint) big = bint;
		else big = BigInteger.valueOf(number.longValue());
		
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