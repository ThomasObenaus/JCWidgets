/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.floatingPanel;

/**
 * @author Thomas Obenaus
 * @source Divider.java
 * @date 27.07.2009
 */
public class Divider
{
	private int	y1;
	private int	size;

	public Divider( int y, int size )
	{
		this.y1 = y;
		this.size = size;
	}

	public void setYPos( int ypos )
	{
		this.y1 = ypos;
	}

	public void setSize( int size )
	{
		this.size = size;
	}

	public boolean isInside( int y )
	{
		//				System.err.println( this.y1 + "<=" + y + "<=" + ( this.y1 + this.size ) );
		return ( this.y1 <= y ) && ( y <= this.y1 + this.size );
	}

	public int getSize( )
	{
		return this.size;
	}
}
