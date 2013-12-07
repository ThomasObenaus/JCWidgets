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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyListener;

import javax.swing.JSlider;

/**
 * Abstract class that represents a slider of an arbitrary type.
 * 
 * @author Thomas Obenaus
 * @source Slider.java
 * @date 18 Nov 2008
 */
@SuppressWarnings ( "serial")
public abstract class Slider<T> extends JSlider
{
	private final Color	freezedSliderColor	= new Color( 180, 180, 120, 128 );

	protected T			minimumValue;
	protected T			maximumValue;
	protected T			tickSpacingValue;
	private boolean		contiounsMode;
	private boolean		freezeSliderPos;
	private int			freezedPos;

	/**
	 * DefCtor
	 */
	public Slider( )
	{
		super( );
		this.contiounsMode = false;
		this.freezeSliderPos = false;
		this.freezedPos = -1;
		this.setDefaultRange( );

	}

	public void addKeyListener( KeyListener keyListener )
	{
		super.addKeyListener( keyListener );
	}

	public T getTickSpacingValue( )
	{
		return tickSpacingValue;
	}

	/**
	 * Within this method the initial range of the slider have to be set, using setRange(..).
	 */
	public abstract void setDefaultRange( );

	protected abstract void _setRange( T minimum, T maximum, T tickSpacing );

	/**
	 * Returns the next value (<= max-range), computed by adding the tickSpacing to the current
	 * value.
	 * @return
	 */
	public abstract T getIncreasedValue( );

	/**
	 * Returns the next value (>= min-range), computed by subtracting the tickSpacing from the
	 * current value.
	 * @return
	 */
	public abstract T getDecreasedValue( );

	/**
	 * Set the range of the slider. The tickspacing used will be calculated with the following
	 * equation: tickspacing = (maximum-minimum)/10
	 * @param minimum - lower bound of the sliders range
	 * @param maximum - upper bound of the sliders range
	 */
	public abstract void setRange( T minimum, T maximum );

	/**
	 * Set the range of the slider.
	 * @param minimum - lower bound of the sliders range
	 * @param maximum - upper bound of the sliders range
	 * @param tickSpacing - the spacing between the tick-marks
	 */
	public void setRange( T minimum, T maximum, T tickSpacing )
	{
		this.minimumValue = minimum;
		this.maximumValue = maximum;
		this.tickSpacingValue = tickSpacing;
		this._setRange( minimum, maximum, tickSpacing );
	}

	/**
	 * Enable/ disable the continuous slider mode. Not enabled: values that does not match a value
	 * that is a multiple of tickspacing will be rounded to the nearest value that matches this
	 * restriction. Enabled: all values are allowed.
	 * @param enable
	 */
	public void setEnableContinuousMode( boolean enable )
	{
		this.contiounsMode = enable;
	}

	/**
	 * Pushes the slider-pointer to the sliders maximum value.
	 */
	public void setToMax( )
	{
		this.setValue( this.getMaximum( ) );
	}

	/**
	 * Pushes the slider-pointer to the sliders minimum value.
	 */
	public void setToMin( )
	{
		this.setValue( this.getMinimum( ) );
	}

	/**
	 * Converts the value of the given type to an Integer.
	 * @param typeToConvert
	 * @return
	 */
	protected abstract int convertTypeToInteger( T typeToConvert );

	/**
	 * Converts the Integer value to the given type.
	 * @param integerToConvert
	 * @return
	 */
	protected abstract T convertIntegerToType( int integerToConvert );

	/**
	 * The function returns 0 if the values are equal, 1 if the first value is greater than the
	 * second one and -1 if the first one is smaller than the second one.
	 * @param val1
	 * @param val2
	 * @return
	 */
	protected abstract int compareValues( T val1, T val2 );

	/**
	 * Sets the slider-pointer to the specified value.
	 * @param value
	 */
	public void setNewValue( T value )
	{
		if ( this.compareValues( value, this.getMaximumValue( ) ) == 1 )
			value = this.getMaximumValue( );
		if ( this.compareValues( value, this.getMinimumValue( ) ) == -1 )
			value = this.getMinimumValue( );

		if ( this.contiounsMode )
			this.setValue( ( int ) this.convertTypeToInteger( value ) );
		else this.setValue( this.getNextRoundedKey( ( int ) this.convertTypeToInteger( value ) ) );

	}

	protected abstract Integer getNextRoundedKey( int value );

	/**
	 * Returns the value the slider-pointer points to.
	 * @return
	 */
	public T getCurrentValue( )
	{
		if ( this.contiounsMode )
			return this.convertIntegerToType( this.getValue( ) );
		else return this.convertIntegerToType( this.getNextRoundedKey( this.getValue( ) ) );
	}

	/**
	 * Returns the sliders maximum-value.
	 * @return
	 */
	public T getMaximumValue( )
	{
		return this.convertIntegerToType( this.getMaximum( ) );
	}

	/**
	 * Returns the sliders minimum-value.
	 * @return
	 */
	public T getMinimumValue( )
	{
		return this.convertIntegerToType( this.getMinimum( ) );
	}

	@Override
	public void paint( Graphics g )
	{
		super.paint( g );
		if ( this.freezeSliderPos && ( this.freezedPos != -1 ) )
		{
			g.setColor( freezedSliderColor );
			g.fillRect( this.freezedPos, 0, 10, this.getHeight( ) );
		}
	}

	/**
	 * Draws a not fully opaque rectangle to visualize the last (freezed) position of the slider.
	 * @param freeze - true to enable the drawing of the freezed slider position, false to disable.
	 */
	public void setFreezeSliderPos( boolean freeze )
	{
		int x = this.getValue( );
		int range = this.getMaximum( ) - this.getMinimum( );
		float ppos = ( x + Math.abs( this.getMinimum( ) ) ) / ( float ) range;
		x = ( int ) ( ( this.getWidth( ) - 11 ) * ppos );

		this.freezedPos = x;
		this.freezeSliderPos = freeze;

		this.repaint( );
	}
}
