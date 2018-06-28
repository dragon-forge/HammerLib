package com.zeitheron.hammercore.lib.zlib.error;

/**
 * Thrown to indicate a problem with the JSON API. Such problems include:
 * <ul>
 * <li>Attempts to parse or construct malformed documents
 * <li>Use of null as a name
 * <li>Use of numeric types not available to JSON, such as {@link Double#isNaN()
 * NaNs} or {@link Double#isInfinite() infinities}.
 * <li>Lookups using an out of range index or nonexistent name
 * <li>Type mismatches on lookups
 * </ul>
 *
 * <p>
 * Although this is a checked exception, it is rarely recoverable. Most callers
 * should simply wrap this exception in an unchecked exception and rethrow.
 */
public class JSONException extends Exception
{
	private static final long serialVersionUID = -8223333936049224309L;

	public JSONException(String s)
	{
		super(s);
	}
}