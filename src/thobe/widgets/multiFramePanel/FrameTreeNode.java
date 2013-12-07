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
 * @source FrameTreeNode.java
 * @date 23.04.2010
 */
public interface FrameTreeNode
{
	/**
	 * Lays out its subcomponents. This method is called whenever the parent-component was resized
	 * or layed out.
	 */
	public void doLayout( );

	/**
	 * Adjust the bounds of this component.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void setBounds( int x, int y, int width, int height );

	/**
	 * Returns <i>this</i> if this object is an {@link FrameContainer} and null otherwise.
	 * @return
	 */
	public FrameContainer getFrameContainer( );

	/**
	 * Returns <i>this</i> if this object is an {@link Frame} and null otherwise.
	 * @return
	 */
	public Frame getFrame( );

	/**
	 * Returns the components {@link BoundingBox}.
	 * @return
	 */
	public BoundingBox getBoundingBox( );

	/**
	 * Returns the minimum height of this component
	 * @return
	 */
	public int getMinWidth( );

	/**
	 * Returns the minimum width of this component
	 * @return
	 */
	public int getMinHeight( );

}
