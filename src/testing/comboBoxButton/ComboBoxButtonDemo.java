/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.comboBoxButton;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import testing.testEnv.TestFrame;
import thobe.widgets.comboBoxButton.ComboBoxButtonEntry;
import thobe.widgets.comboBoxButton.ComboboxButton;
import thobe.widgets.icons.IconLib;

/**
 * @author Thomas Obenaus
 * @source ContentSwitchDemo.java
 * @date 22.02.2012
 */
@SuppressWarnings ( "serial")
public class ComboBoxButtonDemo extends TestFrame
{
	public ComboBoxButtonDemo( )
	{
		this.buildGUI( );
	}

	private void buildGUI( )
	{
		this.setLayout( new BorderLayout( ) );

		JTextArea ta = new JTextArea( );
		this.add( ta );

		ComboBoxButtonEntry entryHelp = new ComboBoxButtonEntry( "Help", IconLib.get( ).getHelpE( ) );
		ComboBoxButtonEntry entryMax = new ComboBoxButtonEntry( "Maximize", IconLib.get( ).getMaximizeE( ) );
		ComboBoxButtonEntry entryMin = new ComboBoxButtonEntry( "Minimize", IconLib.get( ).getMinimizeE( ) );

		ComboboxButton cs = new ComboboxButton( new ComboBoxButtonEntry[]
		{ entryHelp, entryMax, entryMin } );

		JPanel pa_south = new JPanel( new FlowLayout( FlowLayout.LEADING ) );
		this.add( pa_south, BorderLayout.SOUTH );
		pa_south.add( cs );
	}

	public static void main( String[] args )
	{
		ComboBoxButtonDemo cbd = new ComboBoxButtonDemo( );
		cbd.setSize( 300, 250 );
		cbd.setVisible( true );

	}
}
