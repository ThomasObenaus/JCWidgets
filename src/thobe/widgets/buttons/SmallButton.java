/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.buttons;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

import thobe.widgets.action.AbstrAction;
import thobe.widgets.action.AbstrActionPropertyChangeListener;
import thobe.widgets.layout.WidgetLayout;

/**
 * @author Thomas Obenaus
 * @source MySmallButton.java
 * @date 09.10.2006
 */
/**
 * Class representing an custom-style small button
 */
public class SmallButton extends JButton implements AbstrActionPropertyChangeListener
{
	private static final long	serialVersionUID		= 0;
	private Icon				selectedDisabledIcon	= null;
	private Icon				iconEnabled				= null;
	private boolean				mouseOver				= false;
	private boolean				pressed					= false;
	private boolean				paintBorder				= false;

	private void setupIcons( )
	{
		Action action = this.getAction( );
		if ( action != null && action instanceof AbstrAction )
		{

			/* try to find the selected-disabled icon */
			this.selectedDisabledIcon = ( ( AbstrAction ) action ).getIconSelectedDisabled( );

			/* try to find the selected-enabled icon */
			Icon selectedEnabledIcon = ( ( AbstrAction ) action ).getIconSelectedEnabled( );
			if ( selectedEnabledIcon != null )
				this.setSelectedIcon( selectedEnabledIcon );

			this.setDisabledIcon( ( ( AbstrAction ) action ).getIconDisabled( ) );

		}

		/* try to find the disabled icon */
		Icon disabledIcon = this.getDisabledIcon( );
		if ( disabledIcon != null )
		{
			this.setDisabledIcon( disabledIcon );
			this.setPressedIcon( disabledIcon );

		}

		Dimension size = new Dimension( 20, 20 );
		this.iconEnabled = this.getIcon( );
		if ( this.iconEnabled != null )
			size = new Dimension( this.iconEnabled.getIconWidth( ), this.iconEnabled.getIconHeight( ) );

		this.setPreferredSize( size );
		this.setMinimumSize( size );
		this.setSize( size );

	}

	private void init( )
	{
		this.setupIcons( );

		Action action = this.getAction( );
		if ( action != null && action instanceof AbstrAction )
		{
			( ( AbstrAction ) action ).addListener( this );
		}

		this.setMargin( new Insets( 0, 0, 0, 0 ) );
		this.setForeground( WidgetLayout.FLAT_WIDGET_FOREGROUND );
		this.setBackground( WidgetLayout.FLAT_WIDGET_BACKGROUND );
		this.setFocusPainted( false );
		this.setOpaque( false );
		this.setBorderPainted( true );

		this.addMouseListener( new MouseAdapter( )
		{
			@Override
			public void mouseEntered( MouseEvent e )
			{
				onMouseEntered( );
			}

			@Override
			public void mouseExited( MouseEvent e )
			{
				onMouseExited( );
			}

			@Override
			public void mousePressed( MouseEvent e )
			{
				onButtonPressed( );
			}

			@Override
			public void mouseReleased( MouseEvent e )
			{
				onButtonReleased( );
			}
		} );

	}

	public void setPaintBorder( boolean paintBorder )
	{
		this.paintBorder = paintBorder;
	}

	public SmallButton( String text )
	{
		super( text );
		this.init( );
	}

	public SmallButton( Icon icon )
	{
		super( icon );
		this.init( );
	}

	public SmallButton( Action a )
	{
		super( a );

		this.init( );
	}

	@Override
	public void paint( Graphics g )
	{
		Icon icon = null;

		if ( this.isEnabled( ) )
		{
			if ( this.isSelected( ) && this.getSelectedIcon( ) != null )
				icon = this.getSelectedIcon( );
			if ( !this.isSelected( ) || this.getSelectedIcon( ) == null )
				icon = this.getIcon( );
		}
		else
		{
			if ( this.isSelected( ) && this.selectedDisabledIcon != null )
				icon = this.selectedDisabledIcon;
			if ( !this.isSelected( ) || this.selectedDisabledIcon == null )
				icon = this.getDisabledIcon( );
		}

		if ( icon == null )
			icon = this.getIcon( );

		if ( icon != null )
		{
			int midX = ( this.getWidth( ) / 2 ) - ( icon.getIconWidth( ) / 2 );
			int midY = ( this.getHeight( ) / 2 ) - ( icon.getIconHeight( ) / 2 );
			icon.paintIcon( this, g, midX, midY );

			if ( !this.paintBorder )
				return;
			if ( this.mouseOver && !this.pressed )
			{
				g.setColor( new Color( 0, 0, 0, 255 ) );
				g.drawRect( 0, 0, this.getWidth( ) - 1, this.getHeight( ) - 1 );
				g.setColor( new Color( 170, 170, 170, 255 ) );
				g.drawRect( 1, 1, this.getWidth( ) - 3, this.getHeight( ) - 3 );
			}
			if ( this.pressed )
			{
				g.setColor( new Color( 170, 170, 170, 255 ) );
				g.drawRect( 0, 0, this.getWidth( ) - 1, this.getHeight( ) - 1 );
				g.setColor( new Color( 0, 0, 0, 255 ) );
				g.drawRect( 1, 1, this.getWidth( ) - 3, this.getHeight( ) - 3 );
			}
		}
		else
		{
			super.paint( g );
		}

	}

	@Override
	public void setIcon( Icon defaultIcon )
	{
		super.setIcon( defaultIcon );
		this.iconEnabled = defaultIcon;

		if ( this.iconEnabled != null )
		{
			Dimension size = new Dimension( this.iconEnabled.getIconWidth( ), this.iconEnabled.getIconHeight( ) );
			this.setPreferredSize( size );
			this.setMinimumSize( size );
		}
	}

	private void onButtonPressed( )
	{
		this.pressed = true;
		Icon iconPressed = this.getPressedIcon( );
		if ( iconPressed != null )
		{
			super.setIcon( iconPressed );
			this.repaint( );
		}
	}

	private void onButtonReleased( )
	{
		this.pressed = false;
		if ( this.iconEnabled != null && this.isEnabled( ) && !this.mouseOver )
		{
			super.setIcon( iconEnabled );
			this.repaint( );
		}
		if ( this.mouseOver )
		{
			this.onMouseEntered( );
		}
	}

	private void onMouseEntered( )
	{
		this.mouseOver = true;
		Icon iconMouseOver = this.getSelectedIcon( );
		if ( iconMouseOver != null )
		{
			super.setIcon( iconMouseOver );
			this.repaint( );
		}
	}

	private void onMouseExited( )
	{
		this.mouseOver = false;
		if ( this.iconEnabled != null && this.isEnabled( ) )
		{
			super.setIcon( iconEnabled );
			this.repaint( );
		}
	}

	@Override
	public void onIconsChanged( Icon iconEnabled, Icon iconDisabled )
	{
		this.setupIcons( );
	}

	@Override
	public void onSelectionIconsChanged( Icon iconSelectEnabled, Icon iconSelectDisabled )
	{
		this.setupIcons( );
	}

}
