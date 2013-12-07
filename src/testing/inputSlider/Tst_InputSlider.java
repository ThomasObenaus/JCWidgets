/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.inputSlider;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import thobe.widgets.inputSlider.InputDoubleSlider;
import thobe.widgets.inputSlider.InputSliderListener;

/**
 * @author Thomas Obenaus
 * @source Tst_InputSlider.java
 * @date 11.01.2009
 */
@SuppressWarnings ( "serial")
public class Tst_InputSlider extends JFrame
{

	private InputDoubleSlider	ipds_test;
	private JLabel				l_test;

	public Tst_InputSlider( )
	{
		buildGUI( );
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}

	private void buildGUI( )
	{
		this.getContentPane( ).setLayout( new BorderLayout( 5, 5 ) );

		this.ipds_test = new InputDoubleSlider( "ee", -10d, 1.0, 0.2 );
		this.ipds_test.setNoCommitValueChangeWhenMousePressed( true );
		this.ipds_test.setCommitOnFocusLost( false );
		this.ipds_test.setToolTipText( "BLA " );
		this.ipds_test.setFormatString( "%.5g" );
		this.ipds_test.setEnabled( true );
		ipds_test.addListener( new InputSliderListener( )
		{
			@Override
			public void valueChanged( )
			{
				handleValueChanged( );
			}
		} );

		this.getContentPane( ).add( ipds_test, BorderLayout.NORTH );

		this.l_test = new JLabel( );
		this.getContentPane( ).add( this.l_test, BorderLayout.SOUTH );

		JSpinner sp = new JSpinner( new SpinnerNumberModel( 0.2, 0.1, 1.2, 0.0025 ) );
		sp.addChangeListener( new ChangeListener( )
		{
			@Override
			public void stateChanged( ChangeEvent e )
			{
				JSpinner spi = ( JSpinner ) e.getSource( );
				l_test.setText( "spi " + spi.getValue( ) );

			}
		} );
		this.getContentPane( ).add( sp, BorderLayout.CENTER );

		this.setSize( 300, 400 );
	}

	private void handleValueChanged( )
	{
		this.l_test.setText( this.ipds_test.getValue( ) + "" );
		System.out.println( "Tst_InputSlider.handleValueChanged()" );
	}

	public static void main( String args[] )
	{
		SwingUtilities.invokeLater( new Runnable( )
		{
			@Override
			public void run( )
			{
				try
				{
					UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName( ) );//"com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel" );
				}
				catch ( Exception e )
				{
					e.getStackTrace( );
				}
				Tst_InputSlider fr = new Tst_InputSlider( );
				fr.setVisible( true );
			}
		} );
	}
}
