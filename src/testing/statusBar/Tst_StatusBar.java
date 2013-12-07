/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.statusBar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import thobe.widgets.statusbar.StatusBar;
import thobe.widgets.statusbar.StatusBarMessageType;

/**
 * @author Thomas Obenaus
 * @source Tst_StatusBar.java
 * @date 14.04.2010
 */
@SuppressWarnings ( "serial")
public class Tst_StatusBar extends JFrame
{
	public Tst_StatusBar( )
	{

		buildGUI( );
		this.setSize( new Dimension( 400, 200 ) );
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}

	private void buildGUI( )
	{
		this.setLayout( new BorderLayout( ) );

		try
		{
			StatusBar.createStatusBar( this );
		}
		catch ( Exception e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
			return;
		}

		this.add( StatusBar.get( ), BorderLayout.SOUTH );

		JPanel pa_misc = new JPanel( );
		pa_misc.add( new JLabel( "Misc Panel" ) );
		StatusBar.get( ).setMiscPanel( pa_misc );

		JPanel pa_buttons = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		this.add( pa_buttons, BorderLayout.NORTH );
		JButton bu_setInfo = new JButton( "Info" );
		pa_buttons.add( bu_setInfo );
		bu_setInfo.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				StatusBar.get( ).setMessage( "Information", StatusBarMessageType.INFO );
			}
		} );

		JButton bu_setWarning = new JButton( "Warning" );
		pa_buttons.add( bu_setWarning );
		bu_setWarning.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				StatusBar.get( ).setMessage( "Warning", StatusBarMessageType.WARNING );
			}
		} );

		JButton bu_setError = new JButton( "Error" );
		pa_buttons.add( bu_setError );
		bu_setError.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				StatusBar.get( ).setMessage( "Error", StatusBarMessageType.ERROR, "The Help for the error..." );
			}
		} );

		JButton bu_clear = new JButton( "Clear" );
		pa_buttons.add( bu_clear );
		bu_clear.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				StatusBar.get( ).clear( );
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
				try
				{
					UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName( ) );
				}
				catch ( Exception e )
				{
					try
					{
						UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
					}
					catch ( Exception e1 )
					{
						System.err.println( "Error setting the look and feel. " + e.getLocalizedMessage( ) );
					}
				}

				Tst_StatusBar fr = new Tst_StatusBar( );
				fr.setVisible( true );
			}
		} );
	}

}
