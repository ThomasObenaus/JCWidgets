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

import thobe.widgets.textfield.RestrictedTextFieldDouble;
import thobe.widgets.textfield.RestrictedTextfield;

/**
 * Represents a widget that can be used to slide trough a range of Doubles.
 * @author Thomas Obenaus
 * @source InputDoubleSlider.java
 * @date 14 Nov 2008
 */
@SuppressWarnings ( "serial")
public class InputDoubleSlider extends InputSlider<Double>
{
	/**
	 * Ctor
	 * @param name - The name of the slider
	 * @param minValue - start of the sliders range
	 * @param maxValue - end of the sliders range
	 */
	public InputDoubleSlider( String name, Double minValue, Double maxValue )
	{
		super( name, minValue, maxValue );
	}

	/**
	 * Ctor
	 * @param name - The name of the slider
	 */
	public InputDoubleSlider( String name )
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
	public InputDoubleSlider( String name, Double minValue, Double maxValue, Double tickSpacing )
	{
		super( name, minValue, maxValue, tickSpacing );
	}

	@Override
	protected Slider<Double> createSlider( )
	{
		return new DoubleSlider( );
	}

	@Override
	protected RestrictedTextfield<Double> createTextfield( )
	{
		return new RestrictedTextFieldDouble( true );
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

	@Override
	protected String renderValue( Double value )
	{
		return String.format( "%1$E", value );
	}
}
