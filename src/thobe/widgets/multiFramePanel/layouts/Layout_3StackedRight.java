/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.multiFramePanel.layouts;

import java.util.List;

import thobe.widgets.multiFramePanel.AbstractMultiFrameLayout;
import thobe.widgets.multiFramePanel.Divider;
import thobe.widgets.multiFramePanel.DividerType;
import thobe.widgets.multiFramePanel.Frame;
import thobe.widgets.multiFramePanel.FrameContainer;
import thobe.widgets.multiFramePanel.FrameTreeNode;

/**
 * @author Thomas Obenaus
 * @source Layout_StackedRight.java
 * @date 25.04.2010
 */
public class Layout_3StackedRight extends AbstractMultiFrameLayout
{
	private FrameTreeNode	root;

	@Override
	public void buildFrameTree( List<Frame> frames )
	{
		FrameContainer right = new FrameContainer( new Divider( DividerType.HORIZONTAL, 0.5 ), frames.get( 1 ), frames.get( 2 ) );

		this.root = new FrameContainer( new Divider( DividerType.VERTICAL, 0.5 ), frames.get( 0 ), right );
	}

	@Override
	public FrameTreeNode getFrameTreeRoot( )
	{
		return this.root;
	}

	@Override
	public int numberOfFramesLayedOut( )
	{
		return 3;
	}

	@Override
	public String getDescription( )
	{
		return "3 panes where two of them are stacked at the right side.";
	}

	@Override
	public String getName( )
	{
		return "Three stacked right";
	}

	@Override
	public int getID( )
	{
		return 5;
	}
}
