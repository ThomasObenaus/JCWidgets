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
 * Represents an implementation of the RestrictedTextfield accepting only integer-values.
 * @author Thomas Obenaus
 * @source RestrictedTextFieldInteger.java
 * @date 12 Jun 2008
 */
@SuppressWarnings ( "serial")
public class RestrictedTextFieldInteger extends RestrictedTextfield<Integer>
{

	public RestrictedTextFieldInteger( boolean correctErrorOnCommit )
	{
		super( correctErrorOnCommit );
	}

	@Override
	protected Integer getDefaultValue( )
	{
		return 1;
	}

	@Override
	protected Integer convertStringToType( String stringToConvert ) throws ConvertException, InvalidValueExeption
	{
		try
		{
			return Integer.parseInt( stringToConvert );
		}
		catch ( NumberFormatException e )
		{
			throw new ConvertException( stringToConvert, "Integer" );
		}
	}

	@Override
	protected String convertTypeToString( Integer typeToConvert )
	{
		return "" + typeToConvert;
	}

	@Override
	protected int compareValues( Integer val1, Integer val2 )
	{
		if ( val1 > val2 )
			return 1;
		if ( val1 < val2 )
			return -1;
		return 0;
	}

}
