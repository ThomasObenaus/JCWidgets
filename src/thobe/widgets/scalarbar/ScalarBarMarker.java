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
import java.awt.Graphics;
import java.awt.Point;

/**
 * @author Thomas Obenaus
 * @source ScalarBarMarker.java
 * @date 19.01.2009
 */
public class ScalarBarMarker
{
	private static final Color	deSelectedBorderColor	= new Color( 0, 0, 0 );
	private static final Color	deSelectedColor			= new Color( 130, 130, 130 );

	private Point				center;
	private int					width;
	private boolean				selected;
	private boolean				mouseOver;
	private Point[]				corners;
	private boolean				movable;

	private ColorValuePair		colorValuePair;

	/**
	 * Ctor
	 * @param center - the position where the marker will be drawn
	 * @param width - the width of the marker
	 * @param colorValue - the value that should be represented by the marker.
	 */
	public ScalarBarMarker( Point center, int width, ColorValuePair colorValue )
	{
		this.center = center;
		this.movable = true;
		this.width = width;
		this.colorValuePair = colorValue;
		this.selected = false;
		this.mouseOver = false;
		this.updateCorners( );
	}

	/**
	 * Enable/ Disable the markers property to be able to move.
	 * @param moveable
	 */
	public void setMoveable( boolean moveable )
	{
		this.movable = moveable;
	}

	/**
	 * Returns the markers color-value-pair
	 * @return
	 */
	public ColorValuePair getColorValue( )
	{
		return this.colorValuePair;
	}

	/**
	 * Returns the markers color.
	 * @return
	 */
	public Color getColor( )
	{
		return this.colorValuePair.getColor( );
	}

	/**
	 * Returns true if the marker is move-able and false otherwise.
	 * @return
	 */
	public boolean isMoveable( )
	{
		return this.movable;
	}

	/**
	 * Returns the markers center-position.
	 * @return
	 */
	public Point getCenter( )
	{
		return this.center;
	}

	/**
	 * Sets the markers value.
	 * @param value
	 */
	public void setValue( double value )
	{
		this.colorValuePair.setValue( value );
	}

	/**
	 * Sets the markers color.
	 * @param color
	 */
	public void setColor( Color color )
	{
		this.colorValuePair.setColor( color );
	}

	/**
	 * Marks the marker as selected/ deselected
	 * @param select
	 */
	public void setSelected( boolean select )
	{
		this.selected = select;
	}

	/**
	 * Marks the marker to have/ have not a mousover
	 * @param over
	 */
	public void setMouseOver( boolean over )
	{
		this.mouseOver = over;
	}

	/**
	 * Returns the markers value.
	 * @return
	 */
	public double getValue( )
	{
		return this.colorValuePair.getValue( );
	}

	/**
	 * Returns true if the given point is inside the marker.
	 * @param pnt
	 * @return
	 */
	public boolean intersected( Point pnt )
	{
		if ( pnt.x < this.corners[0].x )
			return false;
		if ( pnt.x > this.corners[1].x )
			return false;
		if ( pnt.y < this.corners[0].y )
			return false;
		if ( pnt.y > this.corners[2].y )
			return false;

		return true;
	}

	private void updateCorners( )
	{
		int halfWidth = width / 2;
		this.corners = new Point[3];
		Point one = new Point( );
		Point two = new Point( );
		Point three = new Point( );
		one.x = center.x - halfWidth;
		one.y = center.y - halfWidth;
		two.x = center.x + halfWidth;
		two.y = center.y;
		three.x = one.x;
		three.y = center.y + halfWidth;

		this.corners[0] = one;
		this.corners[1] = two;
		this.corners[2] = three;
	}

	/**
	 * set the markers position.
	 * @param center
	 */
	public void setCenter( Point center )
	{
		this.center = center;
		this.updateCorners( );
	}

	/**
	 * Paints the marker.
	 * @param g
	 */
	public void paint( Graphics g )
	{
		int[] x = new int[3];
		int[] y = new int[3];
		x[0] = this.corners[0].x;
		x[1] = this.corners[1].x;
		x[2] = this.corners[2].x;
		y[0] = this.corners[0].y;
		y[1] = this.corners[1].y;
		y[2] = this.corners[2].y;
		Color color = this.colorValuePair.getColor( );

		if ( this.selected || this.mouseOver )
			g.setColor( color );
		else g.setColor( deSelectedColor );
		g.fillPolygon( x, y, 3 );

		if ( this.selected || this.mouseOver )
			g.setColor( color );
		else g.setColor( deSelectedBorderColor );
		g.drawPolygon( x, y, 3 );
	}

	@Override
	public String toString( )
	{
		String result = "Marker: Color=" + this.colorValuePair.getColor( ) + " Value=" + this.colorValuePair.getValue( ) + "\n";
		result += "Pos=(" + this.center.x + "," + this.center.y + ") isSelected " + this.selected + " mouseOver " + this.mouseOver + "\n";
		result += "moveable " + this.movable + "\n";

		return result;
	}
}
