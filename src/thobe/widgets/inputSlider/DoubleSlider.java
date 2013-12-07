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

import java.math.BigDecimal;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JLabel;

/**
 * Represents a widget that can be used to slide through double values.
 * @author Thomas Obenaus
 * @source DoubleSlider.java
 * @date 14 Nov 2008
 */
@SuppressWarnings ( "serial")
public class DoubleSlider extends Slider<Double>
{
	/**
	 * Class that can be used to specify the labeling of the slider.
	 * @author Thomas Obenaus
	 * @source DoubleLabelling.java
	 * @date 14 Nov 2008
	 */
	private static class DoubleLabelling extends Dictionary<Integer, JLabel>
	{
		private HashMap<Integer, JLabel>	valueTable	= new HashMap<Integer, JLabel>( );

		@Override
		public Enumeration<JLabel> elements( )
		{
			Vector<JLabel> elements = new Vector<JLabel>( );
			for ( Map.Entry<Integer, JLabel> entry : this.valueTable.entrySet( ) )
				elements.add( entry.getValue( ) );
			return elements.elements( );
		}

		@Override
		public JLabel get( Object key )
		{
			return this.valueTable.get( key );
		}

		@Override
		public boolean isEmpty( )
		{
			return this.valueTable.isEmpty( );
		}

		@Override
		public Enumeration<Integer> keys( )
		{
			Vector<Integer> keys = new Vector<Integer>( );
			for ( Integer key : this.valueTable.keySet( ) )
				keys.add( key );
			return keys.elements( );
		}

		@Override
		public JLabel put( Integer key, JLabel value )
		{
			return this.valueTable.put( key, value );
		}

		@Override
		public JLabel remove( Object key )
		{
			return this.valueTable.remove( key );
		}

		@Override
		public int size( )
		{
			return this.valueTable.size( );
		}
	};

	/**
	 * The exponent used to scale the values, to avoid rounding-errors due to the conversion from
	 * double to int.
	 */
	private int	usedExponent;

	private int	_minimumValue;
	private int	_maximumValue;
	private int	_tickSpacingValue;

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
	protected int convertTypeToInteger( Double valueToScale )
	{
		return ( int ) ( Math.round( valueToScale * Math.pow( 10d, Math.abs( this.usedExponent ) ) ) );
	}

	@Override
	public void setDefaultRange( )
	{
		this.setRange( 0d, 1d, 0.1d );
	}

	@Override
	public void setRange( Double minimum, Double maximum )
	{
		double tickspacing = ( double ) ( maximum - minimum ) / 10d;
		this.setRange( minimum, maximum, tickspacing );
	}

	@Override
	public void _setRange( Double minimum, Double maximum, Double tickSpacing )
	{
		/* check if the values are valid */
		if ( maximum <= minimum )
		{
			System.err.println( "Wrong range specified. Maximum [" + maximum + "] have to be greater than the minimum [" + minimum + "]" );
			return;
		}

		double tickSpacingRatio = Math.abs( tickSpacing / ( maximum - minimum ) );

		/*
		 * We want to display the incoming range of doubles. So we have to convert
		 * the double-values to int-values, because the slider only takes int-values.
		 * To reduce the rounding-error caused by the conversion from double to int, we have
		 * to scale the values (right-shift the comma). Therefore we have to calculate
		 * the maximum exponent.  
		 */
		int exponent = this.getExponent( minimum );
		this.usedExponent = this.getExponent( maximum );
		if ( ( exponent > this.usedExponent ) && ( exponent != 0 ) )
			this.usedExponent = exponent;
		exponent = this.getExponent( tickSpacing );
		if ( ( exponent > this.usedExponent ) && ( exponent != 0 ) )
			this.usedExponent = exponent;

		/*
		 * Scale the values using the exponent determined before. 
		 */
		this._maximumValue = ( int ) ( maximum * Math.pow( 10d, this.usedExponent ) );
		this._minimumValue = ( int ) ( minimum * Math.pow( 10d, this.usedExponent ) );
		this._tickSpacingValue = ( int ) ( ( this._maximumValue - this._minimumValue ) * tickSpacingRatio );

		/*
		 * Reduce the exponent if the given exponent caused an integer-over-/ underflow.
		 */
		while ( ( this._maximumValue == Integer.MAX_VALUE ) || ( this._minimumValue == Integer.MIN_VALUE ) )
		{
			this.usedExponent--;
			this._maximumValue = ( int ) ( maximum * Math.pow( 10d, this.usedExponent ) );
			this._minimumValue = ( int ) ( minimum * Math.pow( 10d, this.usedExponent ) );
			this._tickSpacingValue = ( int ) ( ( this._maximumValue - this._minimumValue ) * tickSpacingRatio );
		}

		/* guarantee that the tickspacing is not 0 */
		if ( this._tickSpacingValue == 0 )
			this._tickSpacingValue = 1;

		/*
		 * Set the values
		 */
		this.setMajorTickSpacing( this._tickSpacingValue );
		this.setMaximum( this._maximumValue );
		this.setMinimum( this._minimumValue );

		/* calculate the number of thicks */
		int numTicks = ( ( this._maximumValue - this._minimumValue ) / this._tickSpacingValue ) + 1;
		if ( numTicks % 2 == 0 )
			numTicks++;

		/* create the labelling */
		DoubleLabelling labelling = new DoubleLabelling( );
		for ( int i = 0; i < numTicks; i++ )
		{

			int key = this._minimumValue + ( this._tickSpacingValue * i );

			/* avoid an overflow */
			if ( key > this._maximumValue )
				key = this._maximumValue;
			/* guarantee that the last tick-mark points to the last value of the slider */
			if ( i == ( numTicks - 1 ) )
				key = this._maximumValue;

			BigDecimal bd = new BigDecimal( key / Math.pow( 10d, Math.abs( this.usedExponent ) ) );
			labelling.put( key, new JLabel( String.format( "%1$.2E", bd.doubleValue( ) ) ) );
		}

		this.setLabelTable( labelling );

	}

	@Override
	protected Integer getNextRoundedKey( int value )
	{

		int numValues = ( int ) Math.round( ( value - this._minimumValue ) / ( double ) this._tickSpacingValue );
		value = ( numValues * this._tickSpacingValue ) + this._minimumValue;
		if ( value < this._minimumValue )
			value = this._minimumValue;
		if ( value > this._maximumValue )
			value = this._maximumValue;

		return value;
	}

	@Override
	protected Double convertIntegerToType( int integerToConvert )
	{
		return ( integerToConvert / Math.pow( 10d, Math.abs( this.usedExponent ) ) );
	}

	/**
	 * Calculates the exponent of the given value. The function tries to find a exponent that can be
	 * used to scale the given double value, whereby the rounding-error caused by an conversion of
	 * the scaled double value to an int will be minimal.
	 * @param value
	 * @return
	 */
	private int getExponent( double value )
	{
		int exponent = 0;
		if ( value == 0.0 )
			return 0;

		double dbl_val = value * Math.pow( 10d, ( double ) exponent );
		long int_val = ( long ) dbl_val;
		/* increment the exponent until the rounding-error is gone */
		while ( Math.abs( int_val - dbl_val ) > 0.0000001 )
		{
			exponent++;
			dbl_val = value * Math.pow( 10d, ( double ) exponent );
			int_val = ( long ) dbl_val;
		}
		return exponent;
	}

	@Override
	public Double getIncreasedValue( )
	{

		double value = this.getCurrentValue( ) + this.getTickSpacingValue( );

		if ( value > this.getMaximumValue( ) )
			value = this.getMaximumValue( );
		if ( value < this.getMinimumValue( ) )
			value = this.getMinimumValue( );

		return value;
	}

	@Override
	public Double getDecreasedValue( )
	{
		double value = this.getCurrentValue( ) - this.getTickSpacingValue( );
		if ( value > this.getMaximumValue( ) )
			value = this.getMaximumValue( );
		if ( value < this.getMinimumValue( ) )
			value = this.getMinimumValue( );

		return value;
	}

}
