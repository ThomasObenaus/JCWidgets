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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import thobe.widgets.textfield.MessageType;
import thobe.widgets.textfield.RestrictedTextFieldDouble;
import thobe.widgets.textfield.RestrictedTextFieldMessageBuilder;
import thobe.widgets.textfield.RestrictedTextfieldListener;

/**
 * @author Thomas Obenaus
 * @source Tst_RestrictedTextfieldRange.java
 * @date 10.01.2009
 */
@SuppressWarnings ( "serial")
public class Tst_RestrictedTextfieldRange extends JFrame
{

	private RestrictedTextFieldDouble	rtf_test;

	public Tst_RestrictedTextfieldRange( )
	{
		buildGUI( );
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

	}

	private void buildGUI( )
	{
		this.getContentPane( ).setLayout( new BorderLayout( 5, 5 ) );

		JPanel pa_txt = new JPanel( );
		this.getContentPane( ).add( pa_txt, BorderLayout.NORTH );
		this.rtf_test = new RestrictedTextFieldDouble( 10, true );
		this.rtf_test.setMessageBuilder( new RestrictedTextFieldMessageBuilder( )
		{
			@Override
			public String createRangeExceptionMsg( boolean lowerBoundExeeded, String exeededBound )
			{
				String result = "The given value is ";
				if ( lowerBoundExeeded )
					result += "smaller";
				else result += "greater";
				result += " than the given bound [" + exeededBound + "]";
				return result;
			}

			@Override
			public String createConvertExceptionMsg( String valueToConvert, String targetType )
			{
				return "Can't make " + valueToConvert + " to " + targetType;
			}

			@Override
			public String createInvalidValueExceptionMsg( String valueToConvert, String errorMessage )
			{
				return "The value " + valueToConvert + " is not allowed (" + errorMessage + ")";
			}
		} );

		this.rtf_test.setValue( 2d );
		this.rtf_test.setFormatString( "%.5e" );
		this.rtf_test.setRange( 0.0, 144.6 );
		this.rtf_test.setToolTipText( "tooooooooooooltip" );
		this.rtf_test.setMessage( "Das ist ein Test", MessageType.INFO );
		rtf_test.addListener( new RestrictedTextfieldListener( )
		{
			@Override
			public void valueChangeCommitted( )
			{
				System.out.println( "The value was corrected!" );
			}

			@Override
			public void valueChanged( )
			{
				handleValueChanged( );
			}

			@Override
			public void valueInvalid( String value )
			{
				System.err.println( "The value " + value + " is not valid!" );

			}

			@Override
			public void valueOutOfBounds( String value )
			{
				System.err.println( "The value " + value + " is out of bounds!" );

			}

		} );
		pa_txt.add( rtf_test );

		RestrictedTextFieldDouble rtfd_n = new RestrictedTextFieldDouble( 10, false );
		rtfd_n.setValue( 12d );
		rtfd_n.setRange( -11d, 11d );
		pa_txt.add( rtfd_n );

		JButton bu = new JButton( "m" );
		bu.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				rtf_test.clearMessage( );
				rtf_test.setValue( 2222d );
			}
		} );
		this.getContentPane( ).add( bu, BorderLayout.CENTER );

		JButton bu_clear = new JButton( "clear" );
		bu_clear.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				rtf_test.clear( );

			}
		} );
		this.getContentPane( ).add( bu_clear, BorderLayout.SOUTH );
		this.setSize( 300, 400 );
	}

	private void handleValueChanged( )
	{
		//this.l_test.setText( this.rtf_test.getText( ) );
	}

	public static void main( String args[] )
	{
		SwingUtilities.invokeLater( new Runnable( )
		{
			@Override
			public void run( )
			{
				Tst_RestrictedTextfieldRange fr = new Tst_RestrictedTextfieldRange( );
				fr.setVisible( true );
			}
		} );
	}
}
