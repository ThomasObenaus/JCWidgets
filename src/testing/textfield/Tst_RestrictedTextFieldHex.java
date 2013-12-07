/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.textfield;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import thobe.widgets.textfield.RestrictedTextField32HexInteger;
import thobe.widgets.textfield.RestrictedTextfieldListener;

/**
 * @author Thomas Obenaus
 * @source Tst_RestrictedTextFieldHex.java
 * @date 24.03.2010
 */
@SuppressWarnings ( "serial")
public class Tst_RestrictedTextFieldHex extends JFrame
{

	private RestrictedTextField32HexInteger	rtf_hex;
	private JLabel							l_test;

	public Tst_RestrictedTextFieldHex( )
	{
		buildGUI( );

		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}

	private void buildGUI( )
	{
		this.getContentPane( ).setLayout( new BorderLayout( 5, 5 ) );

		this.rtf_hex = new RestrictedTextField32HexInteger( true );
		this.rtf_hex.setRange( 0L, ( Integer.MAX_VALUE ) * 2L + 1L );
		this.rtf_hex.addListener( new RestrictedTextfieldListener( )
		{
			@Override
			public void valueChangeCommitted( )
			{}

			@Override
			public void valueChanged( )
			{
				handleValueChanged( );
			}

			@Override
			public void valueInvalid( String value )
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void valueOutOfBounds( String value )
			{
				// TODO Auto-generated method stub

			}
		} );
		this.getContentPane( ).add( this.rtf_hex, BorderLayout.NORTH );
		this.l_test = new JLabel( );
		this.getContentPane( ).add( this.l_test, BorderLayout.SOUTH );
		this.setSize( 300, 400 );
		this.setMinimumSize( new Dimension( 10, 10 ) );
	}

	private void handleValueChanged( )
	{
		this.l_test.setText( "0x" + Long.toHexString( this.rtf_hex.getValue( ) ) );
	}

	public static void main( String args[] )
	{
		SwingUtilities.invokeLater( new Runnable( )
		{
			@Override
			public void run( )
			{
				Tst_RestrictedTextFieldHex fr = new Tst_RestrictedTextFieldHex( );
				fr.setVisible( true );
			}
		} );
	}
}
