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
 * @source Divider.java
 * @date 20.04.2010
 */
public class Divider
{
	private double		dividerPosition;
	private DividerType	type;

	private int			thickness	= 4;

	private BoundingBox	boundingBox;
	private boolean		underCursor;
	private boolean		dragging;

	public Divider( DividerType type, double pos )
	{
		this.boundingBox = new BoundingBox( );
		this.dividerPosition = pos;
		this.type = type;
		this.dragging = false;
		this.underCursor = false;
	}

	public void setDragging( boolean dragging )
	{
		this.dragging = dragging;
	}

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

	public DividerType getType( )
	{
		return type;
	}

	public void setDividerPosition( double dividerPosition )
	{
		this.dividerPosition = dividerPosition;
	}

	public void setBounds( int x, int y, int width, int height )
	{
		this.boundingBox.setBounds( x, y, width, height );
	}

	public BoundingBox getBoundingBox( )
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
		Divider cloned = new Divider( this.getType( ), this.getDividerPosition( ) );
		cloned.boundingBox = new BoundingBox( this.boundingBox.getX( ), this.boundingBox.getY( ), this.boundingBox.getWidth( ), this.boundingBox.getHeight( ) );
		cloned.dragging = this.dragging;
		cloned.thickness = this.thickness;
		cloned.underCursor = this.underCursor;
		return cloned;
	}

	/**
	 * Set the dividers thickness in pixel.
	 * @param thickness
	 */
	public void setThickness( int thickness )
	{
		this.thickness = thickness;
	}
}
