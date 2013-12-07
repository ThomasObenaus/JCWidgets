/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.abstractAction;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import thobe.widgets.action.ActionRegistry;

/**
 * @author Thomas Obenaus
 * @source Tst_InMenu.java
 * @date 22.03.2010
 */
@SuppressWarnings ( "serial")
public class Tst_InMenu extends JFrame
{

	public Tst_InMenu( )
	{

		this.init( );

		this.buildGUI( );
		this.buildMenuBar( );

	}

	private void init( )
	{
		ActionRegistry.get( ).registerAction( new Act_Exit( this ) );
	}

	public void exit( )
	{
		System.exit( 0 );
	}

	private void buildMenuBar( )
	{
		JMenuBar mb = this.getJMenuBar( );
		if ( mb == null )
			mb = new JMenuBar( );

		JMenu mu_file = new JMenu( "File" );
		mb.add( mu_file );

		JMenuItem mi_exit = new JMenuItem( ActionRegistry.get( ).getAction( Act_Exit.ACT_EXIT ) );
		mu_file.add( mi_exit );

		this.setJMenuBar( mb );

	}

	private void buildGUI( )
	{
		this.getContentPane( ).setLayout( new BorderLayout( 5, 5 ) );

		this.add( new JLabel( "test abstr action" ) );

		this.setSize( 300, 400 );
	}

	public static void main( String args[] )
	{
		SwingUtilities.invokeLater( new Runnable( )
		{
			@Override
			public void run( )
			{
				Tst_InMenu fr = new Tst_InMenu( );
				fr.setVisible( true );
			}
		} );
	}
}
