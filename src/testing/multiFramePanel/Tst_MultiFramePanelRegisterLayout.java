/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.multiFramePanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import thobe.widgets.multiFramePanel.MultiFrameLayout;
import thobe.widgets.multiFramePanel.MultiFramePanel;
import thobe.widgets.multiFramePanel.layouts.Layout_3SideBySide;

/**
 * @author Thomas Obenaus
 * @source Tst_MultiFramePanel2.java
 * @date 10.05.2010
 */
@SuppressWarnings ( "serial")
public class Tst_MultiFramePanelRegisterLayout extends JFrame
{
	private MultiFramePanel		container;
	private MultiFrameLayout	layout;
	private boolean				registered;

	public Tst_MultiFramePanelRegisterLayout( )
	{
		this.registered = false;
		this.layout = new Layout_3SideBySide( );
		buildGUI( );

		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setSize( new Dimension( 300, 200 ) );

	}

	private void buildGUI( )
	{
		this.setLayout( new BorderLayout( 0, 0 ) );
		this.container = new MultiFramePanel( );
		this.add( this.container, BorderLayout.CENTER );

		JButton bu_chlayout = new JButton( "ch" );
		this.add( bu_chlayout, BorderLayout.WEST );
		bu_chlayout.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				if ( !registered )
					container.registerLayout( layout );
				else container.unregisterLayout( layout );

				registered = !registered;
			}
		} );
	}

	public static void main( String args[] )
	{
		SwingUtilities.invokeLater( new Runnable( )
		{
			@Override
			public void run( )
			{
				Tst_MultiFramePanelRegisterLayout fr = new Tst_MultiFramePanelRegisterLayout( );
				fr.setVisible( true );
			}
		} );
	}
}
