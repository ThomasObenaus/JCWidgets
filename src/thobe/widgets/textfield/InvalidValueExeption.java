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
 * @source InvalidValueExeption.java
 * @date 01.09.2010
 */
@SuppressWarnings ( "serial")
public class InvalidValueExeption extends Exception
{
	private String	valueToConvert;
	private String	message;

	public InvalidValueExeption( String valueToConvert, String message )
	{
		super( "The value " + valueToConvert + " is not allowed: " + message );
		this.message = message;
		this.valueToConvert = valueToConvert;
	}

	public String getValueToConvert( )
	{
		return valueToConvert;
	}

	public String getErrorMessage( )
	{
		return message;
	}
}
