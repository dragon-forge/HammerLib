package org.zeith.hammerlib.util.java;

import com.google.common.collect.Table;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TableHelper
{
	/**
	 * <a href="https://github.com/google/guava/issues/2170#issuecomment-1856586860">Tables.computeIfAbsent #2170</a>
	 * <p>
	 * If the specified {@code (rowKey, columnKey)} pair is not already associated with a value in the specified table
	 * (or is mapped to {@code null}), attempts to compute its value using the given mapping function
	 * and enters it into the table map unless {@code null}.
	 *
	 * @param rowKey
	 * 		row key that the computed value should be associated with
	 * @param columnKey
	 * 		column key that the computed value should be associated with
	 * @param mappingFunction
	 * 		the function to compute a value
	 *
	 * @return the current (existing or computed) value associated with
	 * the specified {@code (rowKey, columnKey)} pair, or {@code null} if the computed value is {@code null}
	 *
	 * @throws NullPointerException
	 * 		if the specified keys are null and
	 * 		the table does not support null keys, or the mappingFunction
	 * 		is null
	 * @throws IllegalArgumentException
	 * 		if the specified keys do not satisfy some constraint
	 * 		imposed by the the table implementation (e.g. {@link com.google.common.collect.ArrayTable#put})
	 * 		(<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
	 * @see java.util.Map#computeIfAbsent(Object, Function)
	 */
	public static <R, C, V> V computeIfAbsent(Table<R, C, V> table, R rowKey, C columnKey, BiFunction<R, C, V> mappingFunction)
	{
		Objects.requireNonNull(mappingFunction);
		V v;
		if((v = table.get(rowKey, columnKey)) == null)
		{
			V newValue;
			if((newValue = mappingFunction.apply(rowKey, columnKey)) != null)
			{
				table.put(rowKey, columnKey, newValue);
				return newValue;
			}
		}
		return v;
	}
}