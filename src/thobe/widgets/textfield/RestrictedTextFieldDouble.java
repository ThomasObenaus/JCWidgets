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

import java.util.Locale;

/**
 * Represents an implementation of the RestrictedTextfield accepting only double-values.
 * @author Thomas Obenaus
 * @source RestrictedTextFieldDouble.java
 * @date 28 Aug 2007
 */
@SuppressWarnings ( "serial")
public class RestrictedTextFieldDouble extends RestrictedTextfield<Double>
{

	public RestrictedTextFieldDouble( boolean correctErrorOnCommit )
	{
		super( correctErrorOnCommit );

	}

	public RestrictedTextFieldDouble( int columns, boolean correctErrorOnCommit )
	{
		super( columns, correctErrorOnCommit );
	}

	@Override
	protected Double getDefaultValue( )
	{
		return 0d;
	}

	@Override
	protected Double convertStringToType( String stringToConvert ) throws ConvertException, InvalidValueExeption
	{
		try
		{
			return Double.parseDouble( stringToConvert );
		}
		catch ( NumberFormatException e )
		{
			throw new ConvertException( stringToConvert, "Double" );
		}
	}

	@Override
	protected String convertTypeToString( Double strToRender )
	{
		if ( this.formatString == null )
			this.formatString = defaultFormatString;
		return String.format( Locale.ENGLISH, this.formatString, strToRender );
	}

	@Override
	protected int compareValues( Double val1, Double val2 )
	{
		if ( val1 > val2 )
			return 1;
		if ( val1 < val2 )
			return -1;
		return 0;
	}

}
