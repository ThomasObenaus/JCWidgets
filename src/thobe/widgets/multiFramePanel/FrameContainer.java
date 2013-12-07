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
 * @source FrameContainer.java
 * @date 24.04.2010
 */
public class FrameContainer implements FrameTreeNode
{
	private Divider			divider;
	private FrameTreeNode	firstChild;
	private FrameTreeNode	secondChild;
	private BoundingBox		boundingBox;

	/**
	 * Ctor
	 * @param divider
	 * @param firstChild
	 * @param secondChild
	 */
	public FrameContainer( Divider divider, FrameTreeNode firstChild, FrameTreeNode secondChild )
	{
		this.boundingBox = new BoundingBox( );
		this.firstChild = firstChild;
		this.secondChild = secondChild;
		this.divider = divider;

	}

	@Override
	public void doLayout( )
	{
		int x1 = this.boundingBox.getX( );
		int x2 = 0;
		int y1 = this.boundingBox.getY( );
		int y2 = 0;
		int width1 = 0;
		int width2 = 0;
		int height1 = 0;
		int height2 = 0;
		int halfThickness = ( int ) ( this.divider.getThickness( ) / 2d );
		int mid = 0;

		switch ( this.divider.getType( ) )
		{
		case HORIZONTAL:
			mid = ( int ) ( this.boundingBox.getHeight( ) * this.divider.getDividerPosition( ) );
			height1 = mid - halfThickness;
			width1 = this.boundingBox.getWidth( );
			x2 = x1;
			y2 = mid + halfThickness + y1;
			width2 = width1;
			height2 = this.boundingBox.getHeight( ) - height1 - ( halfThickness * 2 );

			/* update the dividers bbox */
			this.divider.setBounds( this.boundingBox.getX( ), mid - halfThickness + y1, this.boundingBox.getWidth( ), this.divider.getThickness( ) );
			break;
		case VERTICAL:
			mid = ( int ) ( this.boundingBox.getWidth( ) * this.divider.getDividerPosition( ) );
			width1 = mid - halfThickness;
			height1 = this.boundingBox.getHeight( );

			x2 = mid + halfThickness + x1;
			y2 = y1;
			width2 = this.boundingBox.getWidth( ) - width1 - ( halfThickness * 2 );
			height2 = this.boundingBox.getHeight( );

			/* update the dividers bbox */
			this.divider.setBounds( mid - halfThickness + x1, this.boundingBox.getY( ), this.divider.getThickness( ), this.boundingBox.getHeight( ) );
			break;
		}
		/* update the children's bbox */
		this.firstChild.setBounds( x1, y1, width1, height1 );
		this.secondChild.setBounds( x2, y2, width2, height2 );

		/* layout the children's */
		this.firstChild.doLayout( );
		this.secondChild.doLayout( );

		if ( this.firstChild.getFrame( ) != null )
			this.firstChild.getFrame( ).setFrameVisible( true );
		if ( this.secondChild.getFrame( ) != null )
			this.secondChild.getFrame( ).setFrameVisible( true );

	}

	public Divider getDivider( )
	{
		return divider;
	}

	@Override
	public Frame getFrame( )
	{
		return null;
	}

	@Override
	public FrameContainer getFrameContainer( )
	{
		return this;
	}

	@Override
	public void setBounds( int x, int y, int width, int height )
	{
		this.boundingBox.setBounds( x, y, width, height );

	}

	public FrameTreeNode getFirstChild( )
	{
		return firstChild;
	}

	public FrameTreeNode getSecondChild( )
	{
		return secondChild;
	}

	@Override
	public BoundingBox getBoundingBox( )
	{
		return this.boundingBox;
	}

	@Override
	public int getMinHeight( )
	{
		return this.firstChild.getMinHeight( ) + this.secondChild.getMinHeight( );
	}

	@Override
	public int getMinWidth( )
	{
		return this.firstChild.getMinWidth( ) + this.secondChild.getMinWidth( );
	}

}
