/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.testEnv;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButton;
import javax.swing.UIManager;

import thobe.widgets.utils.LookAndFeel;
import thobe.widgets.utils.Utilities;

/**
 * @author Thomas Obenaus
 * @source TestFrame.java
 * @date 24.02.2012
 */
@SuppressWarnings ( "serial")
public class TestFrame extends JFrame
{
	public TestFrame( )
	{
		this.buildGUI( );
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		this.pack( );
	}

	private void buildGUI( )
	{
		JMenuBar mb = new JMenuBar( );
		JMenu mu_plaf = new JMenu( "PLAF" );
		mb.add( mu_plaf );
		ButtonGroup bg = new ButtonGroup( );

		String currentPLAF = UIManager.getLookAndFeel( ).getName( );

		for ( LookAndFeel plaf : LookAndFeel.values( ) )
		{
			JRadioButton mi_plaf = new JRadioButton( plaf.toString( ) );
			mu_plaf.add( mi_plaf );
			bg.add( mi_plaf );

			if ( plaf.getClassName( ).contains( currentPLAF.toLowerCase( ) ) )
				mi_plaf.setSelected( true );

			mi_plaf.addActionListener( new PlafMenuActionListener( this, plaf ) );
		}

		this.setJMenuBar( mb );

	}

	class PlafMenuActionListener implements ActionListener
	{
		private LookAndFeel	plaf;
		private Component	comp;

		public PlafMenuActionListener( Component comp, LookAndFeel plaf )
		{
			this.plaf = plaf;
			this.comp = comp;
		}

		@Override
		public void actionPerformed( ActionEvent e )
		{
			Utilities.setLookAndFeel( comp, this.plaf );
		}

	}

	public static void main( String[] args )
	{
		TestFrame tf = new TestFrame( );
		tf.setVisible( true );
	}
}
