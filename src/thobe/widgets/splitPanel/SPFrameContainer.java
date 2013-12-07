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

import java.awt.Dimension;
import java.awt.Rectangle;

/**
 * A class that represents the composite of the composite design-pattern. This special node can be
 * used to combine two {@link SPFrame}s to be able to build up the tree that represents the
 * hierarchy of the subframes of the {@link SplitPanel}.
 * @author Thomas Obenaus
 * @source SPTreeContainer.java
 * @date 13.02.2012
 */
public class SPFrameContainer extends SPFrame
{
	/**
	 * Boundingbox of the container
	 */
	private Rectangle	bounds;

	/**
	 * The first child
	 */
	private SPFrame		firstChild;

	/**
	 * The second child
	 */
	private SPFrame		secondChild;

	/**
	 * The divider that separates the first from the second child.
	 */
	private SPDivider	divider;

	/**
	 * Ctor
	 * @param id - see {@link SPFrame}
	 * @param isFirstChild - see {@link SPFrame}
	 * @param firstChild - the first child
	 * @param secondChild - the second child
	 * @param divider - the divider that separates the first from the second child
	 */
	public SPFrameContainer( int id, boolean isFirstChild, SPFrame firstChild, SPFrame secondChild, SPDivider divider )
	{
		super( id, isFirstChild );
		this.bounds = new Rectangle( );
		this.firstChild = firstChild;
		this.secondChild = secondChild;
		this.divider = divider;

		this.firstChild.setParent( this );
		if ( this.secondChild != null )
			this.secondChild.setParent( this );
	}

	/**
	 * Ctor - with only one child
	 * @param id - see {@link SPFrame}
	 * @param isFirstChild - see {@link SPFrame}
	 * @param firstChild - the first child
	 */
	public SPFrameContainer( int id, boolean isFirstChild, SPFrame firstChild )
	{
		this( id, isFirstChild, firstChild, null, new SPDivider( SPDividerType.HORIZONTAL ) );
	}

	public SPDivider getDivider( )
	{
		return divider;
	}

	@Override
	public boolean isVisible( )
	{
		SPFrame fChild = this.getFirstChild( );
		if ( fChild != null && fChild.isVisible( ) )
			return true;

		SPFrame sChild = this.getSecondChild( );
		if ( sChild != null && sChild.isVisible( ) )
			return true;
		return false;
	}

	public SPFrame getFirstChild( )
	{
		return this.firstChild;
	}

	public SPFrame getSecondChild( )
	{
		return this.secondChild;
	}

	public void removeSecondChild( )
	{
		this.secondChild = null;
	}

	/**
	 * Set the first and the second child. The method itself is responsible to set this container as
	 * parent of the given childs.
	 * @param firstChild - the first child
	 * @param secondChild - the second child
	 */
	public void setChildren( SPFrame firstChild, SPFrame secondChild )
	{
		this.firstChild = firstChild;
		this.secondChild = secondChild;
		if ( this.firstChild != null )
			this.firstChild.setParent( this );
		if ( this.secondChild != null )
			this.secondChild.setParent( this );
	}

	public void setFirstChild( SPFrame fChild )
	{
		this.firstChild = fChild;
		if ( this.firstChild != null )
			this.firstChild.setParent( this );
	}

	@Override
	public SPFrameContainer getFrameContainer( )
	{
		return this;
	}

	@Override
	public void setBounds( int x, int y, int width, int height )
	{
		this.bounds.x = x;
		this.bounds.y = y;
		this.bounds.width = width;
		this.bounds.height = height;
	}

	@Override
	public void setVisible( boolean visible )
	{
		if ( this.firstChild != null )
			this.firstChild.setVisible( visible );
		if ( this.secondChild != null )
			this.secondChild.setVisible( visible );
	}

	@Override
	public void setEnabled( boolean enabled )
	{
		if ( this.firstChild != null )
			this.firstChild.setEnabled( enabled );
		if ( this.secondChild != null )
			this.secondChild.setEnabled( enabled );
	}

	@Override
	public Rectangle getBounds( )
	{
		return this.bounds;
	}

	@Override
	public Dimension getMinSize( )
	{
		return this.computeMinSize( this );
	}

	private Dimension computeMinSize( SPFrame node )
	{
		SPFrameContainer container = node.getFrameContainer( );
		if ( container != null )
		{
			int h = 0;
			int w = 0;
			SPFrame fChild = container.getFirstChild( );
			if ( fChild != null )
			{
				Dimension d = fChild.getMinSize( );
				h = d.height;
				w = d.width;
			}
			SPFrame sChild = container.getSecondChild( );
			if ( sChild != null )
			{
				Dimension d = sChild.getMinSize( );
				h = Math.max( d.height, h );
				w = Math.max( d.width, w );
			}
			return new Dimension( w, h );
		}

		SPSubFrame leaf = node.getSubFrame( );
		return leaf.getMinSize( );
	}

	/**
	 * Sets the size of the first child. The given size should be given as a value between 0 and 1
	 * as the relation of the first nodes size compared to the containers size. That is the
	 * percentage of space (offered by the container) the first node occupies. The remaining
	 * percentage (1-size) will be used for the second child.
	 * @param size
	 */
	public void setSize( double size )
	{
		if ( size < 0 )
			size = 0;
		if ( size > 1 )
			size = 1;
		this.divider.setPosition( size );
	}

	@Override
	public void doLayout( )
	{
		int x1 = this.bounds.x;
		int x2 = 0;
		int y1 = this.bounds.y;
		int y2 = 0;
		int width1 = 0;
		int width2 = 0;
		int height1 = 0;
		int height2 = 0;
		int halfThickness = ( int ) ( this.divider.getThickness( ) / 2d );
		if ( this.divider.getDividerPosition( ) == 1 )
			halfThickness = 0;
		int mid = 0;

		switch ( this.divider.getType( ) )
		{
		case HORIZONTAL:
			mid = ( int ) ( this.bounds.getHeight( ) * this.divider.getDividerPosition( ) );
			height1 = mid - halfThickness;
			width1 = this.bounds.width;
			x2 = x1;
			y2 = mid + halfThickness + y1;
			width2 = width1;
			height2 = this.bounds.height - height1 - ( halfThickness * 2 );

			/* update the dividers bbox */
			this.divider.setBounds( this.bounds.x, mid - halfThickness + y1, this.bounds.width, this.divider.getThickness( ) );
			break;
		case VERTICAL:
			mid = ( int ) ( this.bounds.getWidth( ) * this.divider.getDividerPosition( ) );
			width1 = mid - halfThickness;
			height1 = this.bounds.height;

			x2 = mid + halfThickness + x1;
			y2 = y1;
			width2 = this.bounds.width - width1 - ( halfThickness * 2 );
			height2 = this.bounds.height;

			/* update the dividers bbox */
			this.divider.setBounds( mid - halfThickness + x1, this.bounds.y, this.divider.getThickness( ), this.bounds.height );
			break;
		}
		/* update the children's bbox */
		if ( this.firstChild != null )
			this.firstChild.setBounds( x1, y1, width1, height1 );
		if ( this.secondChild != null )
			this.secondChild.setBounds( x2, y2, width2, height2 );

		/* layout the children's */
		if ( this.firstChild != null )
			this.firstChild.doLayout( );
		if ( this.secondChild != null )
			this.secondChild.doLayout( );
	}
}
