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
import thobe.widgets.multiFramePanel.Frame;
import thobe.widgets.multiFramePanel.FrameTreeNode;

/**
 * @author Thomas Obenaus
 * @source Layout_Single.java
 * @date 25.04.2010
 */
public class Layout_1Single extends AbstractMultiFrameLayout
{
	private FrameTreeNode	root;

	public Layout_1Single( )
	{
		root = null;
	}

	@Override
	public void buildFrameTree( List<Frame> frames )
	{
		this.root = frames.get( 0 );
	}

	@Override
	public FrameTreeNode getFrameTreeRoot( )
	{
		return this.root;
	}

	@Override
	public int numberOfFramesLayedOut( )
	{
		return 1;
	}

	@Override
	public String getDescription( )
	{
		return "One pane will be visible";
	}

	@Override
	public String getName( )
	{
		return "One pane";
	}

	@Override
	public int getID( )
	{
		return 0;
	}
}
