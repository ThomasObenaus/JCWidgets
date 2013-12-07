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
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import thobe.widgets.multiFramePanel.Frame;
import thobe.widgets.multiFramePanel.MultiFramePanel;

/**
 * @author Thomas Obenaus
 * @source Tst_MultiFramePanel.java
 * @date 20.04.2010
 */
@SuppressWarnings ( "serial")
public class Tst_MultiFramePanel extends JFrame
{
	private MultiFramePanel	container;
	private int				currentLayout	= 1;

	public Tst_MultiFramePanel( )
	{

		buildGUI( );

		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setSize( new Dimension( 300, 200 ) );

	}

	private void buildGUI( )
	{
		this.setLayout( new BorderLayout( 0, 0 ) );
		this.container = new MultiFramePanel( );
		this.container.setMinFrameCount( 4 );
		this.add( this.container, BorderLayout.CENTER );

		JButton bu_chlayout = new JButton( "ch" );
		this.add( bu_chlayout, BorderLayout.WEST );
		bu_chlayout.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				List<Frame> frames = container.getFrames( );
				for ( Frame f : frames )
					System.out.println( f + " " + f.isFrameVisible( ) );

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
				Tst_MultiFramePanel fr = new Tst_MultiFramePanel( );
				fr.setVisible( true );
			}
		} );
	}
}
