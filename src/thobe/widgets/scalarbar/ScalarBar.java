/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.scalarbar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JPanel;

/**
 * @author Thomas Obenaus
 * @source ScalarBar.java
 * @date 19.01.2009
 */

/**
 * @author Thomas Obenaus
 * @source ScalarBar.java
 * @date 15.01.2009
 */

@SuppressWarnings ( "serial")
public class ScalarBar extends JPanel
{
	private static final float				barWidth		= 0.5f;
	private static final int				labelSpacing	= 10;
	private static final int				labelBorder		= 1;
	private static final Font				axisFont		= new Font( "Helvetica", Font.PLAIN, 10 );
	private static final int				widthEditorBar	= 16;
	private static final int				borderEditorBar	= 3;
	private static final int				borderBar		= 6;

	private ArrayList<ScalarBarListener>	listeners;
	private ColorLookupTable				lookupTable;
	private int								heightBar;
	private int								widthBar;

	private boolean							editable;
	private boolean							mouseOverSet;
	private ScalarBarMarker					selectedMarker;
	private Vector<ScalarBarMarker>			marker;
	private boolean							dragging;

	/**
	 * Ctor
	 * @param lookupTable - the ColorLookupTable that should be displayed.
	 * @param editable - If true there will be marks drawn for every entry in the lookuptable. These
	 *            marks can be moved with the mouse. This modification directly moves the value within
	 *            the ColorLookupTable. As a result the ColorLookupTable will be modified.
	 */
	public ScalarBar( ColorLookupTable lookupTable, boolean editable )
	{
		this.lookupTable = lookupTable;
		this.heightBar = 0;
		this.widthBar = 0;
		this.editable = editable;
		this.mouseOverSet = false;
		this.selectedMarker = null;
		this.dragging = false;
		this.setMinimumSize( new Dimension( 100, 100 ) );
		this.setPreferredSize( new Dimension( 100, 100 ) );
		this.listeners = new ArrayList<ScalarBarListener>( );

		if ( editable )
		{
			this.marker = new Vector<ScalarBarMarker>( );
			this.createMarkers( );

			this.addMouseListener( new MouseListener( )
			{
				@Override
				public void mouseClicked( MouseEvent e )
				{
					handleMouseClicked( e );
				}

				@Override
				public void mouseEntered( MouseEvent e )
				{
					handleMouseEntered( e );
				}

				@Override
				public void mouseExited( MouseEvent e )
				{
					handleMouseExited( );
				}

				@Override
				public void mousePressed( MouseEvent e )
				{}

				@Override
				public void mouseReleased( MouseEvent e )
				{
					selectedMarker = null;
					dragging = false;
				}

			} );

			this.addMouseMotionListener( new MouseMotionListener( )
			{
				@Override
				public void mouseDragged( MouseEvent e )
				{
					handleMouseDragged( e );
				}

				@Override
				public void mouseMoved( MouseEvent e )
				{
					handleMouseMoved( e );
				}
			} );

		}
	}

	/**
	 * Returns the scalarbars ColorLookupTable.
	 * @return
	 */
	public ColorLookupTable getColorLookupTable( )
	{
		return this.lookupTable;
	}

	private void createMarkers( )
	{
		/* create the markers */
		this.marker.clear( );
		Iterator<ColorValuePair> cVit = this.lookupTable.getColorsAndValues( ).iterator( );
		int w = widthEditorBar - ( borderEditorBar * 2 );

		while ( cVit.hasNext( ) )
		{
			ColorValuePair cvp = cVit.next( );
			ScalarBarMarker tri = new ScalarBarMarker( new Point( ( widthEditorBar - ( borderEditorBar * 2 ) ) / 2 + borderEditorBar, borderBar ), w, cvp );
			this.marker.add( tri );
		}
	}

	/**
	 * Merges the ColorLookupTable of the SclarBar with the given CLUT.
	 * @param clut
	 */
	public void mergeColorLookupTable( ColorLookupTable clut )
	{
		this.lookupTable.combine( clut );
		this.update( );
		this.createMarkers( );
		this.repaint( );
	}

	/**
	 * Replaces the old ColorLookupTable of the ScalarBar with the given ColorLookupTable.
	 * @param clut
	 */
	public void setLookupTable( ColorLookupTable clut )
	{
		this.lookupTable = clut;
		this.update( );
		this.createMarkers( );
		this.repaint( );
	}

	/**
	 * Add a ScalarBarListener.
	 * @param listener
	 */
	public void addListener( ScalarBarListener listener )
	{
		this.listeners.add( listener );
	}

	/**
	 * Remove the given SclarBarListener from the scalarbar.
	 * @param listener
	 */
	public void removeListener( ScalarBarListener listener )
	{
		this.listeners.remove( listener );
	}

	public void paint( Graphics g )
	{
		if ( this.lookupTable == null )
			return;
		this.paintColorBar( g );
		this.paintAxis( g );
	}

	private void paintAxis( Graphics g )
	{
		g.setColor( this.getBackground( ) );
		g.fillRect( this.widthBar + 1, 0, this.getWidth( ) - this.widthBar, this.heightBar + borderBar );

		g.setColor( Color.black );
		g.setFont( ScalarBar.axisFont );

		FontMetrics metrics = g.getFontMetrics( ScalarBar.axisFont );
		Rectangle2D rect = metrics.getStringBounds( "9", g );
		int textWidth = ( int ) rect.getWidth( );
		int textHeight = ( int ) rect.getHeight( );

		int numLabels = ( this.heightBar + borderBar + ScalarBar.labelSpacing ) / ( ScalarBar.labelSpacing + textHeight );
		int labelSpacingAdjusted = ( this.heightBar + borderBar - numLabels * textHeight ) / ( numLabels - 1 );
		String s = "";
		for ( int i = 0; i < numLabels; i++ )
		{
			float value = 0.0f;

			int currHeight = ( textHeight + labelSpacingAdjusted ) * i + textHeight;
			if ( i == 0 )
			{
				value = ( float ) this.heightToValue( this.heightBar );
			}
			else if ( i == ( numLabels - 1 ) )
			{
				value = ( float ) this.heightToValue( 0 );
			}
			else value = ( float ) this.heightToValue( this.heightBar - currHeight );

			s = String.format( "%.2E", value );
			rect = metrics.getStringBounds( s, g );
			textWidth = ( int ) rect.getWidth( );

			g.drawString( s, this.getWidth( ) - textWidth - ScalarBar.labelBorder, currHeight );
		}
	}

	private void paintMarks( Graphics g )
	{
		int x = ( widthEditorBar - ( borderEditorBar * 2 ) ) / 2 + borderEditorBar;
		g.setColor( this.getBackground( ) );
		g.fillRect( 0, 0, widthEditorBar, this.heightBar + ( 2 * borderBar ) );
		for ( int i = 0; i < this.marker.size( ); i++ )
		{
			ScalarBarMarker triangle = this.marker.get( i );
			Point p = new Point( );
			p.x = x;
			p.y = this.valueToHeight( triangle.getValue( ) ) + borderBar;
			triangle.setCenter( p );
			triangle.paint( g );
		}

	}

	private void paintColorBar( Graphics g )
	{
		this.heightBar = this.getHeight( ) - ( 2 * borderBar );
		if ( heightBar < 50 )
			this.heightBar = 50;
		this.widthBar = ( int ) ( ( float ) this.getWidth( ) * barWidth );

		if ( this.editable )
			this.paintMarks( g );

		for ( int i = 0; i < this.heightBar; i++ )
		{
			g.setColor( this.lookupTable.getColorForValue( this.heightToValue( this.heightBar - i ) ) );
			g.drawLine( widthEditorBar + 1, i + borderBar, this.widthBar - 1, i + borderBar );
		}

		g.setColor( Color.black );
		g.drawRect( widthEditorBar, borderBar, widthBar - widthEditorBar, heightBar );
	}

	private double heightToValue( int height )
	{
		float alpha = ( float ) height / ( float ) this.heightBar;
		return ( lookupTable.getValueRange( ) * alpha + this.lookupTable.getFirst( ) );
	}

	private int valueToHeight( double value )
	{
		float alpha = 1.0f - ( float ) ( ( value - lookupTable.getFirst( ) ) / lookupTable.getValueRange( ) );
		return ( int ) ( ( float ) this.heightBar * alpha );
	}

	private void selectClickedMark( MouseEvent arg0 )
	{
		Point p = arg0.getPoint( );
		int clickCount = arg0.getClickCount( );

		if ( ( p.y > this.heightBar + borderBar ) || ( p.y < borderBar ) || ( p.x > widthEditorBar ) || ( p.x < 0 ) )
		{
			this.killSelection( );
			return;
		}

		this.selectedMarker = null;
		boolean addMarker = true;
		for ( int i = 0; i < this.marker.size( ); i++ )
		{
			ScalarBarMarker tri = this.marker.get( i );
			tri.setMoveable( true );
			if ( tri.intersected( p ) )
			{
				tri.setSelected( true );
				this.selectedMarker = tri;
				addMarker = false;
			}
			else tri.setSelected( false );
		}

		if ( ( this.selectedMarker != null ) && ( ( this.valueToHeight( this.selectedMarker.getValue( ) ) == 0 ) || ( this.valueToHeight( this.selectedMarker.getValue( ) ) == this.heightBar ) ) )
		{
			this.selectedMarker.setMoveable( false );
		}
		else if ( clickCount == 2 )
		{
			if ( !addMarker )
				this.removeMarker( );
			else this.addMarker( this.heightToValue( this.heightBar - p.y ) );
			return;
		}

		if ( this.selectedMarker != null )
		{
			for ( int i = 0; i < this.listeners.size( ); i++ )
				this.listeners.get( i ).markerSelected( this.selectedMarker );
		}
		else
		{
			for ( int i = 0; i < this.listeners.size( ); i++ )
				this.listeners.get( i ).markerDeselected( );
		}
	}

	private void removeMarker( )
	{
		this.lookupTable.removeEntry( this.selectedMarker.getColorValue( ) );
		this.marker.remove( this.selectedMarker );

		for ( int n = 0; n < this.listeners.size( ); n++ )
			this.listeners.get( n ).markerDeleted( this.selectedMarker );
		this.selectedMarker = null;
	}

	private void addMarker( double value )
	{
		ColorValuePair cvp = new ColorValuePair( value, Color.white );

		this.lookupTable.insertEntry( cvp );
		int w = widthEditorBar - ( borderEditorBar * 2 );

		this.selectedMarker = new ScalarBarMarker( new Point( ( widthEditorBar - ( borderEditorBar * 2 ) ) / 2 + borderEditorBar, borderBar ), w, cvp );
		this.marker.add( this.selectedMarker );

		for ( int n = 0; n < this.listeners.size( ); n++ )
			this.listeners.get( n ).markerAdded( this.selectedMarker );
	}

	private void handleMouseClicked( MouseEvent arg0 )
	{
		if ( dragging )
			return;
		this.selectClickedMark( arg0 );

		this.repaint( );
	}

	private void handleMouseMoved( MouseEvent arg0 )
	{
		if ( dragging )
			return;
		Point p = arg0.getPoint( );
		if ( ( p.y > this.heightBar + borderBar ) || ( p.y < borderBar ) )
		{
			this.killMouseOver( );
			return;
		}
		if ( ( p.x > widthEditorBar ) || ( p.x < 0 ) )
		{
			this.killMouseOver( );
			return;
		}

		for ( int i = 0; i < this.marker.size( ); i++ )
		{
			ScalarBarMarker tri = this.marker.get( i );
			if ( tri.intersected( p ) )
			{
				tri.setMouseOver( true );
				this.mouseOverSet = true;
			}
			else tri.setMouseOver( false );
		}
		this.repaint( );
	}

	private void handleMouseDragged( MouseEvent arg0 )
	{
		this.dragging = true;
		if ( this.selectedMarker == null )
			this.selectClickedMark( arg0 );

		if ( ( this.selectedMarker == null ) || !this.selectedMarker.isMoveable( ) )
			return;

		int h = this.heightBar - arg0.getPoint( ).y;
		if ( h < 0 )
			h = 0;
		if ( h > this.heightBar )
			h = this.heightBar;

		double value = this.heightToValue( h );
		this.selectedMarker.setValue( value );
		this.lookupTable.updateOrder( );

		for ( int i = 0; i < this.listeners.size( ); i++ )
			this.listeners.get( i ).valueOfSelectedMarkerChanged( selectedMarker );
		this.repaint( );
	}

	/**
	 * Used to update the Scalarbar and its LUT.
	 */
	public void update( )
	{
		this.lookupTable.updateOrder( );
		this.createMarkers( );
		this.repaint( );
	}

	/**
	 * Sets a new range for the ScalarBar and the corresponding ColorLookupTable. During this call
	 * the colors of the clut won't be removed.
	 * @param min
	 * @param max
	 */
	public void setRange( double min, double max )
	{
		this.lookupTable.setRange( min, max );
		this.update( );
	}

	private void handleMouseExited( )
	{
		if ( !this.dragging )
			this.killMouseOver( );
	}

	private void killMouseOver( )
	{
		if ( !this.mouseOverSet )
			return;
		this.mouseOverSet = false;
		for ( int i = 0; i < this.marker.size( ); i++ )
		{
			ScalarBarMarker tri = this.marker.get( i );
			tri.setMouseOver( false );
		}
		this.repaint( );
	}

	private void killSelection( )
	{
		if ( dragging )
			return;
		if ( this.selectedMarker == null )
			return;

		this.selectedMarker = null;
		for ( int i = 0; i < this.marker.size( ); i++ )
		{
			ScalarBarMarker tri = this.marker.get( i );
			tri.setMouseOver( false );
		}

		for ( int i = 0; i < this.listeners.size( ); i++ )
			this.listeners.get( i ).markerDeselected( );
		this.repaint( );
	}

	private void handleMouseEntered( MouseEvent arg0 )
	{}

	/**
	 * Sets the ramp-method that should be used by the scalar-bars LUT.
	 * @param rMethod
	 */
	public void setRampMethod( RampMethod rMethod )
	{
		this.lookupTable.setRampMethod( rMethod );
	}

	/**
	 * Returns the ramp-method used by the scalarbars LUT
	 * @return
	 */
	public RampMethod getRampMethod( )
	{
		return this.lookupTable.getRampMethod( );
	}
}
