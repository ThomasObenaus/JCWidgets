/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.floatingPanel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * @author Thomas Obenaus
 * @source FloatingPanelContainer.java
 * @date 24.07.2009
 */
@SuppressWarnings ( "serial")
public class FloatingPanelContainer extends JPanel
{

	private static final int	dividerSize			= 1;
	private static final int	bigDividerFactor	= 6;

	private List<FloatingPanel>	panels;
	private boolean				dragging;
	private int					currentDividerUnderMouse;
	private List<Divider>		dividerList;

	private static final Cursor	resizeCursor		= new Cursor( Cursor.N_RESIZE_CURSOR );
	private static final Cursor	defaultCursor		= new Cursor( Cursor.DEFAULT_CURSOR );

	public FloatingPanelContainer( )
	{
		this.currentDividerUnderMouse = -1;
		this.dividerList = new ArrayList<Divider>( );

		this.panels = new ArrayList<FloatingPanel>( );
		this.dragging = false;

		this.addMouseMotionListener( new MouseMotionAdapter( )
		{

			@Override
			public void mouseMoved( MouseEvent e )
			{
				if ( !isEnabled( ) )
					updateDivider( -1, -1 );
				else updateDivider( e.getY( ), e.getX( ) );
			}

			@Override
			public void mouseDragged( MouseEvent e )
			{
				if ( isEnabled( ) )
					resizePanels( e.getY( ), currentDividerUnderMouse );
			}
		} );

		this.addFocusListener( new FocusAdapter( )
		{
			@Override
			public void focusLost( FocusEvent e )
			{
				currentDividerUnderMouse = -1;
				updateDivider( -1, -1 );

			}
		} );

		this.addMouseListener( new MouseAdapter( )
		{

			public void mouseExited( MouseEvent arg0 )
			{
				if ( !dragging )
				{
					currentDividerUnderMouse = -1;
					updateDivider( -1, -1 );
				}

			}

			public void mousePressed( MouseEvent arg0 )
			{
				dragging = true;
				currentDividerUnderMouse = getDividerUnderCursor( arg0.getY( ) );
			}

			public void mouseReleased( MouseEvent arg0 )
			{
				dragging = false;
				if ( !dragging )
				{
					currentDividerUnderMouse = -1;
					if ( !isEnabled( ) )
						updateDivider( -1, -1 );
					else updateDivider( arg0.getY( ), arg0.getX( ) );
				}
			}
		} );

	}

	@Override
	public void removeAll( )
	{
		super.removeAll( );
		this.panels.clear( );
		this.dividerList.clear( );
		this.doLayout( );
	}

	@Override
	public void setEnabled( boolean enabled )
	{
		super.setEnabled( enabled );
		for ( FloatingPanel fp : this.panels )
			fp.setEnabled( enabled );
	}

	private int getDividerUnderCursor( int y )
	{
		for ( int i = 0; i < this.dividerList.size( ); i++ )
		{
			if ( this.dividerList.get( i ).isInside( y ) )
				return i;
		}
		return -1;
	}

	private boolean isInContainer( int x )
	{
		int w = this.getSize( ).width;
		int posX = this.getX( );
		if ( x >= posX && x <= ( w + posX ) )
			return true;
		return false;
	}

	private boolean isInVisibleRect( int y, int x )
	{
		Rectangle r = this.getVisibleRect( );
		if ( r.x > x )
			return false;
		if ( r.x + r.width < x )
			return false;
		if ( r.y > y )
			return false;
		if ( r.y + r.height < y )
			return false;
		return true;
	}

	private void updateDivider( int y, int x )
	{
		this.currentDividerUnderMouse = -1;
		//		this.resetCursor( );
		if ( this.getCursor( ).equals( resizeCursor ) )
			this.setCursor( defaultCursor );

		boolean needLayout = false;
		for ( int i = 0; i < this.dividerList.size( ); i++ )
		{
			Divider div = this.dividerList.get( i );
			FloatingPanel panelOverDivider = this.panels.get( i );

			if ( div.isInside( y ) && panelOverDivider.canGrow( ) && this.isInContainer( x ) && isInVisibleRect( y, x ) )
			{

				this.currentDividerUnderMouse = i;
				int newDivSize = dividerSize * bigDividerFactor;
				if ( div.getSize( ) != newDivSize )
				{
					div.setSize( dividerSize * bigDividerFactor );
					needLayout = true;
				}
				//								if ( this.lastCursor == null || !this.getCursor( ).equals( resizeCursor ) ) this.lastCursor = this.getCursor( );
				if ( this.getCursor( ).getType( ) == Cursor.DEFAULT_CURSOR )
					this.setCursor( resizeCursor );
			}
			else
			{

				if ( div.getSize( ) != dividerSize )
				{
					needLayout = true;
					div.setSize( dividerSize );
				}
			}
		}

		if ( needLayout )
		{
			this.doLayout( );
		}

	}

	private void resizePanels( int y, int dividerUnderCursor )
	{
		/* no divider under cursor => do nothing */
		if ( dividerUnderCursor == -1 )
			return;

		/* get the panel under cursor */
		FloatingPanel panel = this.panels.get( dividerUnderCursor );

		/* calculate the new height of the panel that is over the selected divider */
		int newHeight = y;
		int heightOfPanelsAbove = 0;
		for ( int i = 0; i < dividerUnderCursor; i++ )
		{
			heightOfPanelsAbove += this.panels.get( i ).getPanelHeight( );
			heightOfPanelsAbove += dividerSize;
		}

		if ( newHeight < heightOfPanelsAbove + panel.getMinimumHeight( ) + ( dividerSize * 8 ) )
			panel.setPanelSize( panel.getPanelWidth( ), panel.getMinimumHeight( ) + ( dividerSize * 8 ) );
		else panel.setPanelSize( panel.getPanelWidth( ), newHeight - heightOfPanelsAbove );

		this.doLayout( );
	}

	public void addPanel( FloatingPanel panel )
	{
		panel.setContainer( this );
		this.panels.add( panel );
		this.dividerList.clear( );

		for ( int i = 0; i < this.panels.size( ); i++ )
			this.dividerList.add( new Divider( 0, dividerSize ) );

		this.add( panel );

		this.doLayout( );
	}

	public void doLayout( )
	{
		super.doLayout( );

		Dimension panelSize = this.getSize( );
		int x = 0;
		int y = 0;
		boolean dividerVisible = false;
		for ( int i = 0; i < this.panels.size( ); i++ )
		{
			FloatingPanel panel = this.panels.get( i );

			/* update the panels position on the container-panel */
			panel.setBounds( x, y, ( int ) panelSize.getWidth( ), panel.getPanelHeight( ) );

			/* calculate the y-position for the next panel */
			y += panel.getHeight( );
			Divider currentDivider = this.dividerList.get( i );
			currentDivider.setYPos( y );
			y += currentDivider.getSize( );

			if ( currentDivider.getSize( ) > dividerSize ) /* divider is bigger => divider is visible */
				dividerVisible = true;
		}

		if ( !dividerVisible )
			y += dividerSize * bigDividerFactor;

		/* change the size of view-port if the content is too big for the current size of the panel */
		//				if ( y >= this.getVisibleRect( ).height )
		this.setPreferredSize( new Dimension( 0, y ) );
		//				else
		//			this.setPreferredSize( new Dimension( 0, 0 ) );

		this.revalidate( );
		this.repaint( );

	}

	@Override
	public void paint( Graphics g )
	{
		super.paint( g );
		if ( this.panels.size( ) <= 0 )
			return;

		this.drawDivider( g );

		if ( this.currentDividerUnderMouse == -1 )
			return;
		this.drawSelectedDivider( g );

	}

	private void drawDivider( Graphics g )
	{
		/* draw the divider that is under cursor */
		int newHeight = -dividerSize;

		for ( int i = 0; i < this.panels.size( ); i++ )
		{
			newHeight += this.panels.get( i ).getPanelHeight( );
			newHeight += this.dividerList.get( i ).getSize( );

			if ( this.panels.get( i ).canGrow( ) )
			{
				g.setColor( Color.lightGray );
				g.fillRect( 0, newHeight, ( int ) this.getSize( ).getWidth( ), dividerSize );
			}
		}

	}

	private void drawSelectedDivider( Graphics g )
	{
		/* draw the divider that is under cursor */
		int newHeight = this.panels.get( 0 ).getPanelHeight( );

		for ( int i = 1; i < this.currentDividerUnderMouse + 1; i++ )
		{
			newHeight += this.panels.get( i ).getPanelHeight( );
			newHeight += dividerSize;
		}

		int oneFifth = ( dividerSize * bigDividerFactor ) / 5;

		g.setColor( new Color( 220, 220, 220 ) );
		g.fillRect( 0, newHeight, ( int ) this.getSize( ).getWidth( ), oneFifth );

		g.setColor( Color.lightGray );
		g.fillRect( 0, newHeight + oneFifth, ( int ) this.getSize( ).getWidth( ), oneFifth * 3 );

		g.setColor( new Color( 150, 150, 150 ) );
		g.fillRect( 0, newHeight + ( 4 * oneFifth ), ( int ) this.getSize( ).getWidth( ), ( dividerSize * bigDividerFactor ) - ( 4 * oneFifth ) );

	}

}
