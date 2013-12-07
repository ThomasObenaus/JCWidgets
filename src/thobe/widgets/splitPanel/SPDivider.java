/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.splitPanel;

import java.awt.Rectangle;

/**
 * A class representing the divider that separates two subframes of the {@link SplitPanel} from each
 * other.
 * @author Thomas Obenaus
 * @source SPDivider.java
 * @date 13.02.2012
 */
public class SPDivider
{
	private static final int	thickness	= 4;

	/**
	 * Position of the divider (a value between 0 and 1 .. a percentage)
	 */
	private double				dividerPosition;

	/**
	 * Type of the divider (VERTICAL,HORIZONTAL)
	 */
	private SPDividerType		type;

	/**
	 * Boundingbox
	 */
	private Rectangle			boundingBox;

	/**
	 * Is the mouse-cursor above the divider?
	 */
	private boolean				underCursor;

	/**
	 * Are we in dragging mode?
	 */
	private boolean				dragging;

	/**
	 * Ctor with a initial divider position of 1 (100%)
	 * @param type - Type of the divider (VERTICAL,HORIZONTAL)
	 */
	public SPDivider( SPDividerType type )
	{
		this( type, 1.0 );
	}

	/**
	 * Ctor
	 * @param type - Type of the divider (VERTICAL,HORIZONTAL)
	 * @param pos - Position of the divider (a value between 0 and 1 .. a percentage)
	 */
	public SPDivider( SPDividerType type, double pos )
	{
		this.boundingBox = new Rectangle( );
		this.dividerPosition = pos;
		this.type = type;
		this.dragging = false;
		this.underCursor = false;
	}

	/**
	 * Enter/ leave dragging-mode
	 * @param dragging
	 */
	public void setDragging( boolean dragging )
	{
		this.dragging = dragging;
	}

	/**
	 * Set divider is/ not under cursor
	 * @param underCursor
	 */
	public void setUnderCursor( boolean underCursor )
	{
		this.underCursor = underCursor;
	}

	public boolean isDragging( )
	{
		return dragging;
	}

	public boolean isUnderCursor( )
	{
		return underCursor;
	}

	public double getDividerPosition( )
	{
		return dividerPosition;
	}

	public int getThickness( )
	{
		return thickness;
	}

	public SPDividerType getType( )
	{
		return type;
	}

	/**
	 * Set the divider (a value between 0 and 1 .. a percentage)
	 * @param dividerPosition
	 */
	public void setPosition( double dividerPosition )
	{
		if ( dividerPosition < 0 )
			dividerPosition = 0;
		if ( dividerPosition > 1.0 )
			dividerPosition = 1.0;
		this.dividerPosition = dividerPosition;
	}

	public void setBounds( int x, int y, int width, int height )
	{
		this.boundingBox.setBounds( x, y, width, height );
	}

	public Rectangle getBoundingBox( )
	{
		return boundingBox;
	}

	@Override
	public String toString( )
	{
		return "Divider " + this.type;
	}

	@Override
	protected Object clone( ) throws CloneNotSupportedException
	{
		SPDivider cloned = new SPDivider( this.getType( ), this.getDividerPosition( ) );
		cloned.boundingBox = new Rectangle( this.boundingBox.x, this.boundingBox.y, this.boundingBox.width, this.boundingBox.height );
		cloned.dragging = this.dragging;
		cloned.underCursor = this.underCursor;
		return cloned;
	}

}
