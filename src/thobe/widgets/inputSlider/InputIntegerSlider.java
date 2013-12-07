/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.inputSlider;

import thobe.widgets.textfield.RestrictedTextFieldInteger;
import thobe.widgets.textfield.RestrictedTextfield;

/**
 * Represents a widget that can be used to slide trough a range of Integers.
 * @author Thomas Obenaus
 * @source InputIntegerSlider.java
 * @date 18 Nov 2008
 */
@SuppressWarnings ( "serial")
public class InputIntegerSlider extends InputSlider<Integer>
{
	/**
	 * Ctor
	 * @param name - The name of the slider
	 * @param minValue - start of the sliders range
	 * @param maxValue - end of the sliders range
	 */
	public InputIntegerSlider( String name, Integer minValue, Integer maxValue )
	{
		super( name, minValue, maxValue );
	}

	/**
	 * Ctor
	 * @param name - The name of the slider
	 */
	public InputIntegerSlider( String name )
	{
		super( name );
	}

	/**
	 * Ctor
	 * @param name - The name of the slider
	 * @param minValue - start of the sliders range
	 * @param maxValue - end of the sliders range
	 * @param tickSpacing - spacing between the tickmarks (won't be painted by default, so use setPaintTicks())
	 */
	public InputIntegerSlider( String name, Integer minValue, Integer maxValue, Integer tickSpacing )
	{
		super( name, minValue, maxValue, tickSpacing );
	}

	@Override
	protected Slider<Integer> createSlider( )
	{
		return new IntegerSlider( );
	}

	@Override
	protected RestrictedTextfield<Integer> createTextfield( )
	{
		return new RestrictedTextFieldInteger( true );
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

	@Override
	protected String renderValue( Integer value )
	{
		return String.format( "%1$d", value );
	}

}
