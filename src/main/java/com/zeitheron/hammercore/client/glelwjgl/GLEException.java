/**
 * $Id: GLEException.java,v 1.1 1998/05/02 12:08:32 descarte Exp descarte $
 *
 * Copyright (c)1998 Arcane Technologies Ltd. <http://www.arcana.co.uk>
 *
 * $Log: GLEException.java,v $ Revision 1.1 1998/05/02 12:08:32 descarte Initial
 * revision
 *
 * This software is Copyright (c)1998 Arcane Technologies Ltd. and is released
 * under the ``Artistic'' licence which is available in the source distribution.
 * If this license is not present, you have an unofficial release of this
 * software. The official release may be downloaded from Arcane Technologies
 * Ltd. WWW site at:
 *
 * http://www.arcana.co.uk/products/shapeshifter
 *
 */

package com.zeitheron.hammercore.client.glelwjgl;

/**
 * Exception object thrown if execution of a GLE function fails.
 *
 * @version $Id: GLEException.java,v 1.1 1998/05/02 12:08:32 descarte Exp
 *          descarte $
 * @author Alligator Descartes
 *         &lt;<A HREF="http://www.arcana.co.uk">http://www.arcana.co.uk</A>&gt;
 */
public class GLEException extends RuntimeException
{
	
	/** Revision of this class */
	private static final String VERSION = new String("$Revision: 1.1 $");
	
	/** Standard blank constructor */
	public GLEException()
	{
		super();
	}
	
	/**
	 * Constructor with an argument!
	 * 
	 * @param message
	 *            The message
	 */
	public GLEException(String message)
	{
		super(message);
	}
}
