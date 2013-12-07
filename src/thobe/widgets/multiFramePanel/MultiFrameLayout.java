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

import java.util.List;

/**
 * @author Thomas Obenaus
 * @source MultiFrameLayout.java
 * @date 16.04.2010
 */
public interface MultiFrameLayout
{
	/**
	 * Returns the name of the layout.
	 * @return
	 */
	public String getName( );

	/**
	 * Returns a description of the layout => may be used as tool-tip.
	 * @return
	 */
	public String getDescription( );

	/**
	 * Returns the number of {@link Frame}'s that can be layed out using this {@link MultiFrameLayout}
	 * @return
	 */
	public int numberOfFramesLayedOut( );

	/**
	 * This method builds up an internal hierarchical representation of the {@link Frame}'s that
	 * should be layed out.
	 * @param frames
	 */
	public void buildFrameTree( List<Frame> frames );

	/**
	 * Returns the root of the layout specific internal hierarchical representation of this {@link MultiFrameLayout}.
	 * @return
	 */
	public FrameTreeNode getFrameTreeRoot( );

	/**
	 * Adjusts the sizes/ bounding-boxes of the {@link Frame}'s that are registered.
	 * @param boundingBox - the spatial limits wherein the registered {@link Frame}'s have to fit.
	 */
	public void doLayout( BoundingBox boundingBox );

	/**
	 * Returns the {@link FrameContainer} whose {@link Divider} is under the cursor (or the position
	 * specified by x and y).
	 * @param node - The {@link FrameTreeNode} where the lookup should start. This {@link FrameTreeNode} will be traversed until the
	 *            according {@link FrameContainer} is
	 *            found or until no more {@link FrameTreeNode}'s are untouched.
	 * @param x
	 * @param y
	 * @return - The {@link FrameContainer} whose {@link Divider} is under Cursor or null if none
	 *         were found.
	 */
	public FrameContainer getFrameContainerWithDividerUnderCursor( FrameTreeNode node, int x, int y );

	/**
	 * Respond on mouse-move events.
	 * @param x
	 * @param y
	 */
	public void onMouseMoved( int x, int y );

	/**
	 * Respond on mouse-dragged events.
	 * @param x
	 * @param y
	 */
	public void onMouseDragged( int x, int y );

	/**
	 * Respond on mouse-pressed events.
	 * @param x
	 * @param y
	 */
	public void onMousePressed( int x, int y );

	/**
	 * Respond on mouse-released events.
	 * @param x
	 * @param y
	 */
	public void onMouseReleased( int x, int y );

	public void onMouseExited( );

	public abstract int getID( );
}
