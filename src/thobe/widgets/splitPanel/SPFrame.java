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
 * Abstract class representing the component of the composite design-pattern. Using this class a
 * tree can be build that represents the hierarchy of the subframes of a {@link SplitPanel}.
 * @author Thomas Obenaus
 * @source SPTreeNode.java
 * @date 13.02.2012
 */
public abstract class SPFrame
{
	private static final Dimension	minSize	= new Dimension( 10, 10 );

	/**
	 * Parent of this node. Might be null (node is root).
	 */
	private SPFrameContainer		parent;

	/**
	 * Indicates if this node is the first child of a {@link SPFrameContainer}. Note: Yes the root
	 * has no parent and is therefore not a child of any other {@link SPFrameContainer}, but for
	 * this special node this parameter is true.
	 */
	private boolean					firstChild;

	/**
	 * ID of this frame
	 */
	private int						id;

	/**
	 * Ctor
	 * @param firstChild - Indicates if this node is the first child of a {@link SPFrameContainer}.
	 *            Note: Yes the root has no parent and is therefore not a child of any other {@link SPFrameContainer}, but for this special
	 *            node this parameter should be true.
	 * @param id - id of this frame
	 */
	public SPFrame( int id, boolean firstChild )
	{
		this.id = id;
		this.parent = null;
		this.firstChild = firstChild;
	}

	/**
	 * Indicates if this node is the first child of a {@link SPFrameContainer}. Note: Yes the root
	 * has no parent and is therefore not a child of any other {@link SPFrameContainer}, but for
	 * this special node this parameter is true.
	 * @return
	 */
	public boolean isFirstChild( )
	{
		return firstChild;
	}

	/**
	 * @param firstChild - Indicates if this node is the first child of a {@link SPFrameContainer}.
	 *            Note: Yes the root has no parent and is therefore not a child of any other {@link SPFrameContainer}, but for this special
	 *            node this parameter is true.
	 */
	public void setFirstChild( boolean firstChild )
	{
		this.firstChild = firstChild;
	}

	public abstract void setVisible( boolean visible );

	public abstract void setEnabled( boolean enabled );

	public abstract void setBounds( int x, int y, int width, int height );

	public abstract Rectangle getBounds( );

	public abstract boolean isVisible( );

	public abstract void doLayout( );

	protected void setParent( SPFrameContainer parent )
	{
		this.parent = parent;
	}

	public SPFrameContainer getParent( )
	{
		return parent;
	}

	public Dimension getMinSize( )
	{
		return minSize;
	}

	/**
	 * Returns this instance if this is an {@link SPFrameContainer}, null otherwise.
	 * @return
	 */
	public SPFrameContainer getFrameContainer( )
	{
		return null;
	}

	/**
	 * Returns this instance if this is an {@link SPSubFrame}, null otherwise.
	 * @return
	 */
	public SPSubFrame getSubFrame( )
	{
		return null;
	}

	public int getId( )
	{
		return id;
	}
}
