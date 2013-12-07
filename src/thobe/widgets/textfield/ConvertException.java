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
 * @source ConvertException.java
 * @date 23.11.2008
 */
@SuppressWarnings ( "serial")
public class ConvertException extends Exception
{
	private String	valueToConvert;
	private String	targetType;

	public ConvertException( String valueToConvert, String targetType )
	{
		super( "Can't convert " + valueToConvert + " to " + targetType );
		this.targetType = targetType;
		this.valueToConvert = valueToConvert;
	}

	public String getValueToConvert( )
	{
		return valueToConvert;
	}

	public String getTargetType( )
	{
		return targetType;
	}
}
