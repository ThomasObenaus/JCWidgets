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

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import thobe.widgets.multiFramePanel.actions.ActionComponent;

/**
 * @author Thomas Obenaus
 * @source EmptyVizComponent.java
 * @date 16.04.2010
 */
@SuppressWarnings ( "serial")
public class DefaultFrameComponent extends FrameComponent
{

	public DefaultFrameComponent( )
	{
		super( "Default component", "An empty VizComponent used as example" );
	}

	public Component getContentComponent( )
	{
		JPanel pa_content = new JPanel( new BorderLayout( ) );
		pa_content.add( new JLabel( "DefaultFrameComponent" ) );
		return pa_content;
	}

	@Override
	protected List<ActionComponent> getActionComponents( )
	{
		return null;
	}

	@Override
	public int getMinHeight( )
	{
		return 30;
	}

	@Override
	public int getMinWidth( )
	{
		return 30;
	}
}
