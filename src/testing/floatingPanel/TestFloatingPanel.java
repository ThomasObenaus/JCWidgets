/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.floatingPanel;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import thobe.widgets.floatingPanel.FloatingPanel;

/**
 * @author Thomas Obenaus
 * @source TestFloatingPanel.java
 * @date 01.09.2009
 */
@SuppressWarnings ( "serial")
public class TestFloatingPanel extends FloatingPanel
{
	public TestFloatingPanel( )
	{
		buildGUI( );
	}

	private void buildGUI( )
	{
		this.setLayout( new BorderLayout( ) );
		this.add( new JLabel( "skjksj" ) );
	}

	@Override
	public boolean canGrow( )
	{
		return false;
	}

}
