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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import testing.testEnv.TestFrame;
import thobe.widgets.buttons.IconButton;
import thobe.widgets.icons_internal.IconLib_Internal;

/**
 * @author Thomas Obenaus
 * @source IconButtonDemo.java
 * @date 09.02.2012
 */
@SuppressWarnings ( "serial")
public class IconButtonDemo extends TestFrame
{

	public IconButtonDemo( )
	{
		this.buildGUI( );
	}

	private void buildGUI( )
	{

		this.setLayout( new BorderLayout( ) );

		JPanel pa_top = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		this.add( pa_top, BorderLayout.NORTH );

		final IconButton ib = new IconButton( IconLib_Internal.get( ).getHelpE( ), IconLib_Internal.get( ).getMinimizeE( ), IconLib_Internal.get( ).getMaximizeD( ), IconLib_Internal.get( ).getMaximizeSelE( ) );
		pa_top.add( ib );
		this.add( new JTextArea( ), BorderLayout.CENTER );

		JButton bu = new JButton( "en/dis" );
		bu.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				ib.setEnabled( !ib.isEnabled( ) );
			}
		} );
		this.add( bu, BorderLayout.SOUTH );

	}

	public static void main( String[] args )
	{
		IconButtonDemo ibd = new IconButtonDemo( );
		ibd.setSize( 300, 250 );
		ibd.setVisible( true );
	}

}
