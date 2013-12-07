/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.textfield;

/**
 * @author Thomas Obenaus
 * @source RangeException.java
 * @date 10.01.2009
 */
@SuppressWarnings ( "serial")
public class RangeException extends Exception
{
	private boolean	lowerBoundError;
	private String	exeededBound;

	public RangeException( String message, boolean lowerBoundError, String exeededBound )
	{
		super( message );
		this.lowerBoundError = lowerBoundError;
		this.exeededBound = exeededBound;
	}

	public boolean isLowerBoundError( )
	{
		return this.lowerBoundError;
	}

	public String getExeededBound( )
	{
		return exeededBound;
	}
}
