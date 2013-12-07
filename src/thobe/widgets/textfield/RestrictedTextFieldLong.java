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
 * @source RestrictedTextFieldLong.java
 * @date 24.03.2010
 */
@SuppressWarnings ( "serial")
public class RestrictedTextFieldLong extends RestrictedTextfield<Long>
{
	public RestrictedTextFieldLong( boolean correctErrorOnCommit )
	{
		super( correctErrorOnCommit );
	}

	@Override
	protected Long getDefaultValue( )
	{
		return 1L;
	}

	@Override
	protected Long convertStringToType( String stringToConvert ) throws ConvertException, InvalidValueExeption
	{
		try
		{
			return Long.parseLong( stringToConvert );
		}
		catch ( NumberFormatException e )
		{
			throw new ConvertException( stringToConvert, "Long" );
		}
	}

	@Override
	protected String convertTypeToString( Long typeToConvert )
	{
		return "" + typeToConvert;
	}

	@Override
	protected int compareValues( Long val1, Long val2 )
	{
		if ( val1 > val2 )
			return 1;
		if ( val1 < val2 )
			return -1;
		return 0;
	}

}
