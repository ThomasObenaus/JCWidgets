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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

/**
 * @author Thomas Obenaus
 * @source ColorLookupTable.java
 * @date 19.01.2009
 */
public class ColorLookupTable implements Cloneable
{
	public static final String		XML_TAG	= "ColorLookupTable";

	private RampMethod				rampMethod;
	private double					range;
	private double					first;

	private TreeSet<ColorValuePair>	colorsAndValues;

	/**
	 * Ctor <b>Attention: The the color and the value vector must have the same amount of
	 * entrys.</b>
	 * @param colors - the colors that should be used to color the given values
	 * @param values - the values that should be colored by the colors
	 */
	public ColorLookupTable( ArrayList<Color> colors, ArrayList<Double> values )
	{
		if ( colors.size( ) != values.size( ) )
		{
			System.err.println( "size of vector colors and values is different can't create the ColorLookupTable" );
			return;
		}

		this.colorsAndValues = new TreeSet<ColorValuePair>( );
		this.buildTreeSet( values, colors );
		this.updateRange( );

		this.rampMethod = RampMethod.RAMP_SCURVE;
	}

	/**
	 * DefCtor Creates a default CLUT initialized with a color-range from red over green to blue.
	 * And a value-range from 1 over 0 to -1.
	 */
	public ColorLookupTable( )
	{
		this( getDefaultColors( ), getDefaultValues( ) );
	}

	protected static ArrayList<Double> getDefaultValues( )
	{
		ArrayList<Double> values = new ArrayList<Double>( );
		values.add( 1d );
		values.add( 0d );
		values.add( -1d );
		return values;
	}

	protected static ArrayList<Color> getDefaultColors( )
	{
		ArrayList<Color> colors = new ArrayList<Color>( );
		colors.add( Color.red );
		colors.add( Color.green );
		colors.add( Color.blue );
		return colors;
	}

	/**
	 * Sets a new range for the clut. During this call the colors of the clut won't be removed.
	 * @param min
	 * @param max
	 */
	public void setRange( double min, double max )
	{
		if ( max <= min )
		{
			System.err.println( "Wrong range specified min must be smaller than max, but min=" + min + " max=" + max );
			return;
		}

		//		/* store the colors */
		//		ArrayList<Color> tempColors = this.getColors( );
		//
		//		/* determine the number of entries already in the list */
		//		int numEntries = tempColors.size( );
		//
		//		/* create the new values */
		//		ArrayList<Double> newValues = new ArrayList<Double>( );
		//		
		//		/* create the new colors */
		//		ArrayList<Double> newValues = new ArrayList<Double>( );
		//		
		//		double currentRange[] = this.getRangeOfValues( );
		//		/* min is smaller than the old min, we have to insert a new color-value-pair */
		//		if( min < currentRange[0] )
		//			newValues.add( e )

		//		/* calculate the stepsize */
		//		double stepsize = ( max - min ) / ( ( double ) numEntries - 1d );
		//
		//		/* insert the values */
		//		for ( int i = 0; i < numEntries; i++ )
		//			newValues.add( min + ( stepsize * i ) );

		//		/* rebuild the clut */
		//		this.buildTreeSet( newValues, tempColors );

		ColorValuePair minCVP = this.colorsAndValues.first( );
		ColorValuePair maxCVP = this.colorsAndValues.last( );

		TreeSet<ColorValuePair> cvpCopy = new TreeSet<ColorValuePair>( this.colorsAndValues );

		double currentRange[] = this.getRangeOfValues( );
		/* if min is smaller than the old min, we have to insert a new color-value-pair */
		if ( min < currentRange[0] )
		{
			/* use the color of the first (min) ColorValuePair */
			cvpCopy.add( new ColorValuePair( min, minCVP.getColor( ) ) );
		}
		else if ( min > currentRange[0] )
		{
			/* insert the new min-value using the color of the last min value */
			cvpCopy.add( new ColorValuePair( min, minCVP.getColor( ) ) );

			/* remove all color-value-pairs that are smaller than the min */
			Iterator<ColorValuePair> cvpIt = this.colorsAndValues.iterator( );
			while ( cvpIt.hasNext( ) )
			{
				ColorValuePair cvp = cvpIt.next( );
				if ( cvp.getValue( ) < min )
					cvpCopy.remove( cvp );
			}
		}

		/* if max is bigger than the old max, we have to insert a new color-value-pair */
		if ( max > currentRange[1] )
		{
			/* use the color of the first (max) ColorValuePair */
			cvpCopy.add( new ColorValuePair( max, maxCVP.getColor( ) ) );
		}
		else if ( max < currentRange[1] )
		{
			/* insert the new max-value using the color of the last max value */
			cvpCopy.add( new ColorValuePair( max, maxCVP.getColor( ) ) );

			/* remove all color-value-pairs that are bigger than the max */
			Iterator<ColorValuePair> cvpIt = this.colorsAndValues.iterator( );
			while ( cvpIt.hasNext( ) )
			{
				ColorValuePair cvp = cvpIt.next( );
				if ( cvp.getValue( ) > max )
					cvpCopy.remove( cvp );
			}
		}
		this.colorsAndValues = cvpCopy;

		this.updateRange( );
	}

	/**
	 * Combines this CLUT with the given one.
	 * @param clut
	 */
	public void combine( ColorLookupTable clut )
	{
		Iterator<ColorValuePair> cvpIT = clut.getColorsAndValues( ).iterator( );
		/* insert the missing color-value-pairs */
		while ( cvpIT.hasNext( ) )
			this.insertEntry( cvpIT.next( ) );
	}

	/**
	 * Returns a copy of this ColorlookupTable
	 */
	public ColorLookupTable clone( )
	{
		ColorLookupTable cloned = new ColorLookupTable( this.getColors( ), this.getValues( ) );
		cloned.rampMethod = this.rampMethod;
		return cloned;
	}

	/**
	 * Function that returns the range (the minimum and the maximum) of values of this CLUT.
	 * @return - an array of two doubles, the first entry represents the minimum-value and the
	 *         second one the maximum-value of the CLUT.
	 */
	public double[] getRangeOfValues( )
	{
		double result[] = new double[2];
		result[0] = this.colorsAndValues.first( ).getValue( );
		result[1] = this.colorsAndValues.last( ).getValue( );
		return result;
	}

	private void buildTreeSet( ArrayList<Double> values, ArrayList<Color> colors )
	{
		this.colorsAndValues.clear( );
		for ( int i = 0; i < values.size( ); i++ )
		{
			double value = values.get( i );
			Color color = colors.get( i );
			this.colorsAndValues.add( new ColorValuePair( value, color ) );
		}
	}

	/**
	 * Removes the given entry from the CLUT
	 * @param entry
	 */
	public void removeEntry( ColorValuePair entry )
	{
		this.colorsAndValues.remove( entry );
		this.updateOrder( );
	}

	/**
	 * Inserts a new value into the CLUT.
	 * @param value
	 */
	public void insertValue( double value )
	{
		this.colorsAndValues.add( new ColorValuePair( value, Color.white ) );
		this.updateRange( );
	}

	/**
	 * Inserts a new ColorValuePair into the CLUT.
	 * @param cvp
	 */
	public void insertEntry( ColorValuePair cvp )
	{
		//		if ( this.colorsAndValues.add( cvp ) ) System.err.println( cvp + " inserted " );
		this.colorsAndValues.add( cvp );
		this.updateRange( );

		//		System.err.println( "-------------------------------------------- " + this.colorsAndValues.size( ) );
		//		Iterator<ColorValuePair> cvIT = this.colorsAndValues.iterator( );
		//		while ( cvIT.hasNext( ) )
		//			System.err.println( cvIT.next( ) );
		//		System.err.println( );
	}

	/**
	 * Call this to update the ordering of the CLUT.
	 */
	public void updateOrder( )
	{
		Vector<ColorValuePair> values = new Vector<ColorValuePair>( );
		Iterator<ColorValuePair> it = this.colorsAndValues.iterator( );
		while ( it.hasNext( ) )
		{
			ColorValuePair cvp = it.next( );
			values.add( cvp );
		}

		this.colorsAndValues.clear( );
		this.colorsAndValues.add( values.get( 0 ) );
		this.colorsAndValues.add( values.get( values.size( ) - 1 ) );
		for ( int i = 1; i < values.size( ) - 1; i++ )
		{
			ColorValuePair cvp = values.get( i );
			while ( !this.colorsAndValues.add( cvp ) )
			{
				double cvpValue = cvp.getValue( );
				if ( cvpValue == this.first )
					cvp.setValue( cvp.getValue( ) + ( this.range * 0.06 ) );
				else cvp.setValue( cvp.getValue( ) - ( this.range * 0.04 ) );
			}
		}
		this.updateRange( );
	}

	private void updateRange( )
	{
		ColorValuePair first = this.colorsAndValues.first( );
		ColorValuePair last = this.colorsAndValues.last( );
		this.range = last.getValue( ) - first.getValue( );
		this.first = this.colorsAndValues.first( ).getValue( );
	}

	/**
	 * Sets the ramp-method used by the LUT.
	 * @param method
	 */
	public void setRampMethod( RampMethod method )
	{
		this.rampMethod = method;
	}

	/**
	 * Returns the ramp-method used by the LUT.
	 * @return
	 */
	public RampMethod getRampMethod( )
	{
		return this.rampMethod;
	}

	/**
	 * Returns the distance from the first to the last value stored in the CLUT.
	 * @return
	 */
	public double getValueRange( )
	{
		return this.range;
	}

	/**
	 * Returns the first value of the CLUT.
	 * @return
	 */
	public double getFirst( )
	{
		return this.first;
	}

	/**
	 * Returns the ColorValuePairs stored in the CLUT.
	 * @return
	 */
	public TreeSet<ColorValuePair> getColorsAndValues( )
	{
		return this.colorsAndValues;
	}

	/**
	 * Translates the given value into a color.
	 * @param value - the value that should be translated
	 * @param rgb - a 3-component array wherein the translated color will be stored.
	 */
	public void getColorForValue( double value, int rgb[] )
	{
		ColorValuePair search = new ColorValuePair( value );
		ColorValuePair lower = this.colorsAndValues.floor( search );
		ColorValuePair upper = this.colorsAndValues.ceiling( search );

		if ( ( lower == null ) && ( upper != null ) )
			lower = upper; /* there is no lower but an upper threshold */
		if ( ( upper == null ) && ( lower != null ) )
			upper = lower; /* there is no upper but an lower threshold */
		if ( ( lower == null ) && ( upper == null ) )
		{
			System.err.println( "no value found in TreeSet of ColorValuePairs in " + this.getClass( ) + "getColorForValue(" + value + ",..)" );
			return;
		}

		Color lowerColor = lower.getColor( );
		Color upperColor = upper.getColor( );

		double lowerValue = lower.getValue( );
		double upperValue = upper.getValue( );
		float alpha = ( float ) ( ( value - lowerValue ) / ( upperValue - lowerValue ) );
		if ( Float.isNaN( alpha ) )
			alpha = 0;
		if ( Float.isInfinite( alpha ) )
			alpha = 1;

		float lRed = ( float ) lowerColor.getRed( ) / 255.0f;
		float lGreen = ( float ) lowerColor.getGreen( ) / 255.0f;
		float lBlue = ( float ) lowerColor.getBlue( ) / 255.0f;

		float uRed = ( float ) upperColor.getRed( ) / 255.0f;
		float uGreen = ( float ) upperColor.getGreen( ) / 255.0f;
		float uBlue = ( float ) upperColor.getBlue( ) / 255.0f;

		double rF = this.interpolate( lRed, uRed, alpha );
		double gF = this.interpolate( lGreen, uGreen, alpha );
		double bF = this.interpolate( lBlue, uBlue, alpha );

		/*	if( (interval[0]==3) && (interval[1]==4) )
			{
			
				MessageServer.get( ).addDebug( "interv " + interval[0] + " " + interval[1] );
				MessageServer.get( ).addDebug( "lowerR " + lRed + " upperR " + uRed + " alpha " + alpha + " int " + rF );
				MessageServer.get( ).addDebug( "lowerG " + lGreen + " upperG " + uGreen + " alpha " + alpha + " int " + gF );
				MessageServer.get( ).addDebug( "lowerB " + lBlue + " upperB " + uBlue + " alpha " + alpha + " int " + bF );
			}*/

		/*if( rF < 1 ) rF *= -1;
		if( gF < 1 ) gF *= -1;
		if( bF < 1 ) bF *= -1;
		
		if( rF > 1 ) rF = 1;
		if( gF > 1 ) gF = 1;
		if( bF > 1 ) bF = 1;
		*/
		int r = 0;
		int g = 0;
		int b = 0;

		switch ( this.rampMethod )
		{
		case RAMP_LINEAR:
			r = ( int ) ( rF * 255.0 + 0.5f );
			g = ( int ) ( gF * 255.0 + 0.5f );
			b = ( int ) ( bF * 255.0 + 0.5f );
			break;
		case RAMP_SCURVE:
			r = ( int ) ( ( Math.cos( ( 1.0 - rF ) * Math.PI ) + 1.0 ) * 127.5 );
			g = ( int ) ( ( Math.cos( ( 1.0 - gF ) * Math.PI ) + 1.0 ) * 127.5 );
			b = ( int ) ( ( Math.cos( ( 1.0 - bF ) * Math.PI ) + 1.0 ) * 127.5 );
			break;
		case RAMP_SQRT:
			r = ( int ) ( Math.sqrt( rF ) * 255.0 + 0.5f );
			g = ( int ) ( Math.sqrt( gF ) * 255.0 + 0.5f );
			b = ( int ) ( Math.sqrt( bF ) * 255.0 + 0.5f );
			break;
		}
		rgb[0] = r;
		rgb[1] = g;
		rgb[2] = b;
	}

	private float interpolate( float lowerValue, float upperValue, float alpha )
	{
		return ( ( upperValue - lowerValue ) * alpha ) + lowerValue;
	}

	/**
	 * Translates the given value into a color.
	 * @param value
	 * @return
	 */
	public Color getColorForValue( double value )
	{
		int[] rgb = new int[3];
		this.getColorForValue( value, rgb );
		return new Color( rgb[0], rgb[1], rgb[2] );
	}

	public String getXMLTag( )
	{
		return ColorLookupTable.XML_TAG;
	}

	/**
	 * Returns a vector holding all colors.
	 * @return
	 */
	public ArrayList<Color> getColors( )
	{
		ArrayList<Color> colors = new ArrayList<Color>( );
		Iterator<ColorValuePair> it = this.colorsAndValues.iterator( );
		while ( it.hasNext( ) )
			colors.add( it.next( ).getColor( ) );

		return colors;
	}

	/**
	 * Returns a vector holding all values.
	 * @return
	 */
	public ArrayList<Double> getValues( )
	{
		ArrayList<Double> values = new ArrayList<Double>( );
		Iterator<ColorValuePair> it = this.colorsAndValues.iterator( );
		while ( it.hasNext( ) )
			values.add( it.next( ).getValue( ) );
		return values;
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( obj == this )
			return true;
		if ( obj == null || obj.getClass( ) != this.getClass( ) )
			return false;

		ColorLookupTable clut = ( ColorLookupTable ) obj;
		if ( this.colorsAndValues.size( ) != clut.colorsAndValues.size( ) )
			return false;
		Iterator<ColorValuePair> cvPIT1 = this.colorsAndValues.iterator( );
		Iterator<ColorValuePair> cvPIT2 = clut.colorsAndValues.iterator( );
		while ( cvPIT1.hasNext( ) )
		{
			if ( !cvPIT1.next( ).equals( cvPIT2.next( ) ) )
				return false;

		}

		if ( this.rampMethod != clut.rampMethod )
			return false;

		return true;
	}

	@Override
	public int hashCode( )
	{
		int h = 7;
		h += this.first + this.range;
		h += ( null == this.rampMethod ? 0 : this.rampMethod.hashCode( ) );
		h += ( null == this.colorsAndValues ? 0 : this.colorsAndValues.hashCode( ) );
		h *= 31;
		return h;
	}

}
