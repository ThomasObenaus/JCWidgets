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

/**
 * Represents a widget that can be used to slide trough Integer values.
 * @author Thomas Obenaus
 * @source IntegerSlider.java
 * @date 18 Nov 2008
 */
@SuppressWarnings ( "serial")
public class IntegerSlider extends Slider<Integer>
{

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
	protected int convertTypeToInteger( Integer valueToScale )
	{
		return valueToScale;
	}

	@Override
	public void setDefaultRange( )
	{
		this.setRange( 0, 10, 1 );
	}

	@Override
	public void setRange( Integer minimum, Integer maximum )
	{
		int range = maximum - minimum;
		int tickSpacing = range / 10;
		if ( tickSpacing == 0 )
			tickSpacing = 1;
		this.setRange( minimum, maximum, tickSpacing );
	}

	@Override
	public void _setRange( Integer minimum, Integer maximum, Integer tickSpacing )
	{
		/*
		 * Set the values
		 */
		this.setMajorTickSpacing( tickSpacing );
		this.setMaximum( maximum );
		this.setMinimum( minimum );
	}

	@Override
	protected Integer getNextRoundedKey( int value )
	{
		return value;
	}

	@Override
	protected Integer convertIntegerToType( int valueToUnScale )
	{
		return valueToUnScale;
	}

	@Override
	public Integer getIncreasedValue( )
	{
		int value = this.getCurrentValue( ) + this.getTickSpacingValue( );
		if ( value > this.getMaximumValue( ) )
			value = this.getMaximumValue( );
		if ( value < this.getMinimumValue( ) )
			value = this.getMinimumValue( );

		return value;
	}

	@Override
	public Integer getDecreasedValue( )
	{
		int value = this.getCurrentValue( ) - this.getTickSpacingValue( );
		if ( value > this.getMaximumValue( ) )
			value = this.getMaximumValue( );
		if ( value < this.getMinimumValue( ) )
			value = this.getMinimumValue( );

		return value;
	}
}
