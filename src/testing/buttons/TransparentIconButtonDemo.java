/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.buttons;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import thobe.widgets.buttons.IconButton;
import thobe.widgets.buttons.TransparentIconButton;
import thobe.widgets.icons_internal.IconLib_Internal;

/**
 * @author Thomas Obenaus
 * @source TransparentIconButtonDemo.java
 * @date 09.02.2012
 */
@SuppressWarnings ( "serial")
public class TransparentIconButtonDemo extends JPanel
{

	public TransparentIconButtonDemo( )
	{
		this.buildGUI( );
	}

	private void buildGUI( )
	{
		this.setLayout( new BorderLayout( ) );

		JPanel pa_top = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		pa_top.setBackground( Color.red );
		this.add( pa_top, BorderLayout.NORTH );

		final TransparentIconButton ibc1 = new TransparentIconButton( IconLib_Internal.get( ).getMaximizeE( ), IconLib_Internal.get( ).getMinimizeE( ) );
		pa_top.add( ibc1 );
		final TransparentIconButton ibc2 = new TransparentIconButton( IconLib_Internal.get( ).getMaximizeE( ), IconLib_Internal.get( ).getMinimizeE( ), IconLib_Internal.get( ).getMaximizeD( ), IconLib_Internal.get( ).getMaximizeSelE( ) );
		pa_top.add( ibc2 );

		final IconButton ib1 = new IconButton( IconLib_Internal.get( ).getMaximizeE( ), IconLib_Internal.get( ).getMinimizeE( ), IconLib_Internal.get( ).getMaximizeD( ), IconLib_Internal.get( ).getMaximizeSelE( ) );
		pa_top.add( ib1 );
		final IconButton ib2 = new IconButton( IconLib_Internal.get( ).getMaximizeE( ), IconLib_Internal.get( ).getMinimizeE( ) );
		pa_top.add( ib2 );

		ibc1.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				ibc1.setTransparent( !ibc1.isTransparent( ) );

			}
		} );

		this.add( new JTextArea( ), BorderLayout.CENTER );

		JButton bu = new JButton( "en/dis" );
		bu.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				ibc1.setEnabled( !ibc1.isEnabled( ) );
				ibc2.setEnabled( !ibc2.isEnabled( ) );
				ib2.setEnabled( !ib2.isEnabled( ) );
				ib1.setEnabled( !ib1.isEnabled( ) );
			}
		} );
		this.add( bu, BorderLayout.SOUTH );

	}

	public static void main( String[] args )
	{
		JFrame f = new JFrame( );
		TransparentIconButtonDemo ibd = new TransparentIconButtonDemo( );
		f.setLayout( new BorderLayout( ) );
		f.add( ibd, BorderLayout.CENTER );
		f.setSize( 300, 200 );

		f.addWindowListener( new WindowAdapter( )
		{
			@Override
			public void windowClosing( WindowEvent e )
			{
				System.exit( 0 );
			}
		} );

		f.setVisible( true );
	}

}
