/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.scalarbar;

import java.awt.Color;

/**
 * @author Thomas Obenaus
 * @source ColorValuePair.java
 * @date 19.01.2009
 */
public class ColorValuePair implements Comparable<ColorValuePair>
{
	private double	value;
	private Color	color;

	public ColorValuePair( double value, Color color )
	{

		this.value = value;
		this.color = color;
	}

	public ColorValuePair( double value )
	{
		this( value, Color.black );
	}

	@Override
	public int compareTo( ColorValuePair o )
	{
		if ( this.value > o.getValue( ) )
			return 1;
		if ( this.value == o.getValue( ) )
			return 0;
		return -1;
	}

	/**
	 * @return the value
	 */
	public double getValue( )
	{
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue( double value )
	{
		this.value = value;
	}

	/**
	 * @return the color
	 */
	public Color getColor( )
	{
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor( Color color )
	{
		this.color = color;
	}

	public String toString( )
	{
		return "ColorValuePair color: [" + this.color.getRed( ) + "," + this.color.getGreen( ) + "," + this.color.getBlue( ) + "] value: " + this.value;
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj )
			return true;
		if ( obj == null || obj.getClass( ) != this.getClass( ) )
			return false;

		ColorValuePair cvP = ( ColorValuePair ) obj;
		if ( !this.color.equals( cvP.color ) )
			return false;
		if ( this.value != cvP.value )
			return false;

		return true;
	}

	@Override
	public int hashCode( )
	{
		int h = 7;
		h += ( this.color == null ) ? 0 : this.color.hashCode( );
		h += this.value;
		h *= 31;
		return h;
	}

}
