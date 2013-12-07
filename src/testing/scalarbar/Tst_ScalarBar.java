/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.scalarbar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import thobe.widgets.scalarbar.ColorLookupTable;
import thobe.widgets.scalarbar.ScalarBar;
import thobe.widgets.scalarbar.ScalarBarListener;
import thobe.widgets.scalarbar.ScalarBarMarker;
import thobe.widgets.textfield.RestrictedTextFieldDouble;

/**
 * @author Thomas Obenaus
 * @source Tst_ScalarBar.java
 * @date 19.01.2009
 */
@SuppressWarnings ( "serial")
public class Tst_ScalarBar extends JFrame
{

	private ScalarBar					sclarBar;
	private JTextArea					ta_msg;
	private JButton						bu_nextLookuptable;
	private Iterator<ColorLookupTable>	clutIterator;
	private ArrayList<ColorLookupTable>	cluts;
	private RestrictedTextFieldDouble	rtfd_minRange;
	private RestrictedTextFieldDouble	rtfd_maxRange;

	public Tst_ScalarBar( )
	{
		this.cluts = this.createCluts( );
		this.clutIterator = this.cluts.iterator( );
		buildGUI( );
	}

	private void buildGUI( )
	{
		this.getContentPane( ).setLayout( new BorderLayout( 5, 5 ) );

		this.sclarBar = new ScalarBar( this.clutIterator.next( ), true );
		this.sclarBar.addListener( new ScalarBarListener( )
		{
			@Override
			public void markerAdded( ScalarBarMarker addedMarker )
			{
				handleValueChanged( "marker added", addedMarker );
			}

			@Override
			public void markerDeleted( ScalarBarMarker deletedMarker )
			{
				handleValueChanged( "marker deleted", deletedMarker );

			}

			@Override
			public void markerDeselected( )
			{
				handleValueChanged( "marker deselected", null );

			}

			@Override
			public void markerSelected( ScalarBarMarker selectedMarker )
			{
				handleValueChanged( "marker selected", selectedMarker );

			}

			@Override
			public void valueOfSelectedMarkerChanged( ScalarBarMarker selectedMarker )
			{
				handleValueChanged( "marker value changed", selectedMarker );

			}

		} );

		this.getContentPane( ).add( sclarBar, BorderLayout.WEST );

		this.ta_msg = new JTextArea( );
		this.ta_msg.setEditable( false );
		this.getContentPane( ).add( this.ta_msg, BorderLayout.CENTER );

		JPanel pa_south = new JPanel( );
		this.getContentPane( ).add( pa_south, BorderLayout.SOUTH );

		/* button next clut */
		this.bu_nextLookuptable = new JButton( "Next CLUT" );
		this.bu_nextLookuptable.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent arg0 )
			{
				handleNextCLUT( );
			}
		} );
		pa_south.add( this.bu_nextLookuptable );

		/* button set range */
		JButton bu_setRange = new JButton( "apply range" );
		bu_setRange.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent arg0 )
			{
				handleApplyRange( );
			}
		} );
		pa_south.add( bu_setRange );

		/* range min */
		this.rtfd_minRange = new RestrictedTextFieldDouble( true );
		this.rtfd_minRange.setValue( -0.5d );
		pa_south.add( this.rtfd_minRange );

		/* range max */
		this.rtfd_maxRange = new RestrictedTextFieldDouble( true );
		this.rtfd_maxRange.setValue( 0.5d );
		pa_south.add( this.rtfd_maxRange );

		/* combine */
		JButton bu_combine = new JButton( "combine" );
		bu_combine.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent arg0 )
			{
				handleCombine( );
			}
		} );
		pa_south.add( bu_combine );

		/* reset cluts */
		JButton bu_reset = new JButton( "reset" );
		bu_reset.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent arg0 )
			{
				handleReset( );
			}
		} );
		pa_south.add( bu_reset );

		this.setSize( 300, 400 );
	}

	private void handleReset( )
	{
		this.cluts = this.createCluts( );
		this.clutIterator = this.cluts.iterator( );
	}

	private void handleCombine( )
	{
		ArrayList<Color> colors = new ArrayList<Color>( );
		ArrayList<Double> values = new ArrayList<Double>( );
		colors.add( Color.cyan );
		values.add( -4.5 );
		colors.add( Color.white );
		values.add( -1.3 );
		colors.add( Color.orange );
		values.add( -1.0 );
		colors.add( Color.pink );
		values.add( -0.3 );
		colors.add( Color.red );
		values.add( 0.3 );
		colors.add( Color.green );
		values.add( 0.7 );
		colors.add( Color.yellow );
		values.add( 1.8 );
		colors.add( Color.gray );
		values.add( 2.1 );
		colors.add( Color.black );
		values.add( 3.6 );
		colors.add( Color.blue );
		values.add( 4.0 );
		colors.add( Color.magenta );
		values.add( 4.5 );
		this.sclarBar.mergeColorLookupTable( new ColorLookupTable( ) );

	}

	private void handleApplyRange( )
	{
		this.sclarBar.setRange( this.rtfd_minRange.getValue( ), this.rtfd_maxRange.getValue( ) );
	}

	private ArrayList<ColorLookupTable> createCluts( )
	{
		ArrayList<ColorLookupTable> cluts = new ArrayList<ColorLookupTable>( );

		/* 1.5, 0.5, -0.5, -1.5 */
		ArrayList<Color> colors = new ArrayList<Color>( );
		ArrayList<Double> values = new ArrayList<Double>( );
		colors.add( Color.orange );
		colors.add( Color.yellow );
		colors.add( Color.pink );
		colors.add( Color.cyan );
		values.add( 1.5 );
		values.add( 0.5 );
		values.add( -1.5 );
		values.add( -0.5 );
		cluts.add( new ColorLookupTable( colors, values ) );

		/* default clut */
		cluts.add( new ColorLookupTable( ) );

		/* -0.005 to 0.005 / black to white */
		colors = new ArrayList<Color>( );
		values = new ArrayList<Double>( );
		colors.add( Color.black );
		colors.add( Color.white );
		values.add( -0.005 );
		values.add( 0.005 );
		cluts.add( new ColorLookupTable( colors, values ) );

		/* 100 to 10000 yellow, grey, pink */
		colors = new ArrayList<Color>( );
		values = new ArrayList<Double>( );
		colors.add( Color.yellow );
		colors.add( Color.gray );
		colors.add( Color.pink );
		values.add( 100d );
		values.add( 505d );
		values.add( 1000d );
		cluts.add( new ColorLookupTable( colors, values ) );

		return cluts;
	}

	private void handleNextCLUT( )
	{
		if ( !this.clutIterator.hasNext( ) )
			this.clutIterator = this.cluts.iterator( );
		this.sclarBar.setLookupTable( this.clutIterator.next( ) );
	}

	private void handleValueChanged( String msg, ScalarBarMarker marker )
	{
		if ( marker != null )
			msg += "\n" + marker;
		this.ta_msg.setText( msg );
	}

	public static void main( String args[] )
	{
		SwingUtilities.invokeLater( new Runnable( )
		{
			@Override
			public void run( )
			{
				Tst_ScalarBar fr = new Tst_ScalarBar( );
				fr.setVisible( true );
			}
		} );
	}
}
