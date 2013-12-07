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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import thobe.widgets.buttons.CancelButton;

/**
 * @author Thomas Obenaus
 * @source Tst_CancelButton.java
 * @date 30.01.2009
 */
@SuppressWarnings ( "serial")
public class Tst_CancelButton extends JFrame
{

	private CancelButton	cbu_cancel;
	private JLabel			l_buttonState;

	public Tst_CancelButton( )
	{
		buildGUI( );
	}

	private void buildGUI( )
	{
		this.getContentPane( ).setLayout( new BorderLayout( 5, 5 ) );

		this.cbu_cancel = new CancelButton( );
		this.cbu_cancel.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				l_buttonState.setText( "Pressed at: " + System.currentTimeMillis( ) );
			}
		} );

		this.getContentPane( ).add( this.cbu_cancel, BorderLayout.NORTH );

		this.l_buttonState = new JLabel( "" );
		this.getContentPane( ).add( this.l_buttonState, BorderLayout.SOUTH );

		this.setSize( 300, 400 );
	}

	public static void main( String args[] )
	{
		SwingUtilities.invokeLater( new Runnable( )
		{
			@Override
			public void run( )
			{
				Tst_CancelButton fr = new Tst_CancelButton( );
				fr.setVisible( true );
			}
		} );
	}
}
