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
 * @source RestrictedTextFieldHexInteger.java
 * @date 24.03.2010
 */
@SuppressWarnings ( "serial")
public class RestrictedTextField32HexInteger extends RestrictedTextFieldLong
{
	public RestrictedTextField32HexInteger( boolean correctErrorOnCommit )
	{
		super( correctErrorOnCommit );
	}

	@Override
	protected Long convertStringToType( String stringToConvert ) throws ConvertException, InvalidValueExeption
	{
		if ( stringToConvert == null || stringToConvert.trim( ).equals( "" ) )
			return 0L;

		stringToConvert = stringToConvert.trim( );

		if ( ( stringToConvert.startsWith( "0x" ) || stringToConvert.startsWith( "0X" ) ) && stringToConvert.length( ) > 2 )
		{
			try
			{
				String str = stringToConvert.substring( 2 );
				long result = Long.parseLong( str, 16 );
				return result;
			}
			catch ( NumberFormatException e )
			{
				throw new ConvertException( stringToConvert, "Long" );
			}
		}

		return super.convertStringToType( stringToConvert );
	}

	@Override
	protected String convertTypeToString( Long typeToConvert )
	{
		return "0x" + Long.toHexString( typeToConvert ).toUpperCase( );
	}
}
