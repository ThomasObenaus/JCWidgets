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
 * @source Layout_TopBottom.java
 * @date 25.04.2010
 */
public class Layout_2Stacked extends AbstractMultiFrameLayout
{
	private FrameTreeNode	root;

	@Override
	public void buildFrameTree( List<Frame> frames )
	{
		this.root = new FrameContainer( new Divider( DividerType.HORIZONTAL, 0.5 ), frames.get( 0 ), frames.get( 1 ) );
	}

	@Override
	public FrameTreeNode getFrameTreeRoot( )
	{
		return this.root;
	}

	@Override
	public int numberOfFramesLayedOut( )
	{
		return 2;
	}

	@Override
	public String getDescription( )
	{
		return "Two panes stacked over another.";
	}

	@Override
	public String getName( )
	{
		return "Two panes stacked";
	}

	@Override
	public int getID( )
	{
		return 2;
	}
}
