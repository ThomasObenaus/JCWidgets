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

import thobe.widgets.textfield.RestrictedTextFieldString;
import thobe.widgets.textfield.RestrictedTextfieldListener;

/**
 * @author Thomas Obenaus
 * @source Tst_RestrictedTextfield.java
 * @date 1 Dec 2008
 */
@SuppressWarnings ( "serial")
public class Tst_RestrictedTextfield extends JFrame
{

	private RestrictedTextFieldString	rtf_test;
	private JLabel						l_test;

	public Tst_RestrictedTextfield( )
	{
		buildGUI( );

		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}

	private void buildGUI( )
	{
		this.getContentPane( ).setLayout( new BorderLayout( 5, 5 ) );

		this.rtf_test = new RestrictedTextFieldString( 10, true );
		rtf_test.addListener( new RestrictedTextfieldListener( )
		{
			@Override
			public void valueChangeCommitted( )
			{
				System.out.println( "Tst_RestrictedTextfield.buildGUI().new RestrictedTextfieldListener() {...}.valueChangeCommitted()" );
			}

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
		this.getContentPane( ).add( rtf_test, BorderLayout.NORTH );
		this.l_test = new JLabel( );
		this.getContentPane( ).add( this.l_test, BorderLayout.SOUTH );
		this.setSize( 300, 400 );
		this.setMinimumSize( new Dimension( 10, 10 ) );
	}

	private void handleValueChanged( )
	{
		this.l_test.setText( this.rtf_test.getText( ) );
	}

	public static void main( String args[] )
	{
		SwingUtilities.invokeLater( new Runnable( )
		{
			@Override
			public void run( )
			{
				Tst_RestrictedTextfield fr = new Tst_RestrictedTextfield( );
				fr.setVisible( true );
			}
		} );
	}
}
