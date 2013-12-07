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
 * @source Layout_TopBottom_SideBySide.java
 * @date 07.05.2010
 */
public class Layout_4SideBySide extends AbstractMultiFrameLayout
{
	private FrameTreeNode	root;

	@Override
	public void buildFrameTree( List<Frame> frames )
	{
		FrameContainer left = new FrameContainer( new Divider( DividerType.HORIZONTAL, 0.5 ), frames.get( 0 ), frames.get( 2 ) );
		FrameContainer right = new FrameContainer( new Divider( DividerType.HORIZONTAL, 0.5 ), frames.get( 1 ), frames.get( 3 ) );

		this.root = new FrameContainer( new Divider( DividerType.VERTICAL, 0.5 ), left, right );
	}

	@Override
	public FrameTreeNode getFrameTreeRoot( )
	{
		return this.root;
	}

	@Override
	public int numberOfFramesLayedOut( )
	{
		return 4;
	}

	@Override
	public String getDescription( )
	{
		return "<html>2 Frames side by side.<br>Each of them manges two panes.<br>Totally there will be four panes.</html>";
	}

	@Override
	public String getName( )
	{
		return "Four panes side by side";
	}

	@Override
	public int getID( )
	{
		return 6;
	}
}
