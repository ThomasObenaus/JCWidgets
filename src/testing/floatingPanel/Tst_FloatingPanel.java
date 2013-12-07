/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.floatingPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import thobe.widgets.floatingPanel.FloatingPanelContainer;
import thobe.widgets.floatingPanel.FloatingPanelMessageLabel;
import thobe.widgets.inputSlider.InputDoubleSlider;

/**
 * @author Thomas Obenaus
 * @source Tst_FloatingPanel.java
 * @date 24.07.2009
 */
@SuppressWarnings ( "serial")
public class Tst_FloatingPanel extends JFrame
{

	private InputDoubleSlider		ipds_test;
	private JLabel					l_test;
	private FloatingPanelContainer	fpac;

	public Tst_FloatingPanel( )
	{
		buildGUI( );
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}

	private void buildGUI( )
	{
		this.getContentPane( ).setLayout( new BorderLayout( ) );
		fpac = new FloatingPanelContainer( );
		JScrollPane scrpa = new JScrollPane( fpac );

		TestExpandableFloatingPanel f1 = new TestExpandableFloatingPanel( "TestPanel 1wwwwwwwwwwwwww  wwwwwwwwwwwwwwwwwwwwwww", fpac );
		f1.setMessageLabel( FloatingPanelMessageLabel.ERROR, "ein sehr sehr langer text der umbegrochen werden muesste" );
		fpac.addPanel( f1 );
		fpac.addPanel( new TestFloatingPanel( ) );
		fpac.addPanel( new TestExpandableFloatingPanel( "TestPanel 2", fpac ) );

		this.getContentPane( ).add( scrpa, BorderLayout.CENTER );
		this.setSize( 300, 400 );

		JButton bu_en = new JButton( "en/dis" );
		bu_en.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{

				fpac.setEnabled( !fpac.isEnabled( ) );
			}
		} );
		this.getContentPane( ).add( bu_en, BorderLayout.NORTH );
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
					try
					{
						UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
					}
					catch ( Exception e1 )
					{
						System.err.println( "Error setting the look and feel. " + e.getLocalizedMessage( ) );
					}
				}

				Tst_FloatingPanel fr = new Tst_FloatingPanel( );
				fr.setVisible( true );
			}
		} );
	}

}
