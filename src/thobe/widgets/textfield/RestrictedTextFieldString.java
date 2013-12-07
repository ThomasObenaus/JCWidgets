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
 * Represents an implementation of the RestrictedTextfield accepting all strings.
 * @author Thomas Obenaus
 * @source RestrictedTextFieldString.java
 * @date 7 Jul 2008
 */
@SuppressWarnings ( "serial")
public class RestrictedTextFieldString extends RestrictedTextfield<String>
{
	public RestrictedTextFieldString( boolean correctErrorOnCommit )
	{
		super( correctErrorOnCommit );
	}

	public RestrictedTextFieldString( int columns, boolean correctErrorOnCommit )
	{
		super( columns, correctErrorOnCommit );
	}

	@Override
	protected String convertStringToType( String stringToConvert ) throws ConvertException, InvalidValueExeption
	{
		return stringToConvert;
	}

	@Override
	protected String convertTypeToString( String typeToConvert )
	{
		return typeToConvert;
	}

	@Override
	protected int compareValues( String val1, String val2 )
	{
		if( val1.compareTo( val2 ) > 0 ) return 1;
		if( val1.compareTo( val2 ) < 0 ) return -1;
		return 0;
	}

	@Override
	protected String getDefaultValue( )
	{
		return "";
	}
}
