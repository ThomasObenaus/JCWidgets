/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.multiFramePanel;

/**
 * @author Thomas Obenaus
 * @source BoundingBox.java
 * @date 23.04.2010
 */
public class BoundingBox
{
	private int	x;
	private int	y;
	private int	width;
	private int	height;

	public BoundingBox( )
	{
		this( 0, 0, 0, 0 );
	}

	public BoundingBox( int x, int y, int width, int height )
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void setBounds( int x, int y, int width, int height )
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getHeight( )
	{
		return height;
	}

	public int getWidth( )
	{
		return width;
	}

	public int getX( )
	{
		return x;
	}

	public int getY( )
	{
		return y;
	}

	/**
	 * Returns true if the given point at (x,y) is inside of the {@link BoundingBox}.
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isInside( int x, int y )
	{
		if ( x < this.x )
			return false;
		if ( x > ( this.x + this.width ) )
			return false;
		if ( y < this.y )
			return false;
		if ( y > ( this.y + this.height ) )
			return false;

		return true;
	}

	@Override
	public String toString( )
	{
		return "[" + this.x + "," + this.y + "," + this.width + "," + this.height + "]";
	}
}
