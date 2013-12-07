/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.splitPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import thobe.widgets.decoratorPanel.DecoratorPanelException;
import thobe.widgets.splitPanel.SPComponentDecorator;
import thobe.widgets.splitPanel.SPComponentDecoratorListener;

/**
 * @author Thomas Obenaus
 * @source SplitPanelDemo.java
 * @date 06.02.2012
 */
@SuppressWarnings ( "serial")
public class SPComponentDemo extends JFrame implements SPComponentDecoratorListener
{
	private SPComponentDecorator	spc;

	public SPComponentDemo( )
	{
		this.buildGUI( );
		this.setSize( 300, 250 );
		this.addWindowListener( new WindowAdapter( )
		{
			@Override
			public void windowClosing( WindowEvent e )
			{
				System.exit( 0 );
			}
		} );
	}

	private void buildGUI( )
	{
		this.setLayout( new BorderLayout( ) );

		JPanel pa_toOverlay = new JPanel( );
		pa_toOverlay.setLayout( new BorderLayout( ) );
		pa_toOverlay.add( new JLabel( "ein Label" ), BorderLayout.NORTH );
		pa_toOverlay.add( new JTextArea( "Etwas Text .....\n....\nnoch mehr Text...." ), BorderLayout.CENTER );

		try
		{
			this.spc = new SPComponentDecorator( pa_toOverlay );
			this.spc.addListener( this );
			this.add( spc, BorderLayout.CENTER );

			JButton bu_out = new JButton( "out" );
			bu_out.addActionListener( new ActionListener( )
			{
				@Override
				public void actionPerformed( ActionEvent e )
				{
					System.out.println( "SplitPanelDemo.buildGUI().new ActionListener() {...}.actionPerformed()" );
				}
			} );
			pa_toOverlay.add( bu_out, BorderLayout.SOUTH );
		}
		catch ( DecoratorPanelException e )
		{
			System.err.println( "Error: " + e.getLocalizedMessage( ) );
		}
	}

	public static void main( String[] args )
	{
		SPComponentDemo olpd = new SPComponentDemo( );
		olpd.setVisible( true );
	}

	@Override
	public void onVerticalSplit( )
	{
		this.spc.setWaiting( !this.spc.isWaiting( ) );
		System.out.println( "SplitPanelDemo.onVerticalSplit()" );
	}

	@Override
	public void onHorizontalSplit( )
	{
		System.out.println( "SplitPanelDemo.onHorizontalSplit()" );
	}

	@Override
	public void onMaximize( )
	{
		System.out.println( "SplitPanelDemo.onMaximize()" );

	}

	@Override
	public void onClose( )
	{
		System.out.println( "SplitPanelDemo.onClose()" );

	}

	@Override
	public void onMinimize( )
	{
		System.out.println( "SplitPanelDemo.onMinimize()" );

	}

}
