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
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import thobe.widgets.borders.ColoredBorder;
import thobe.widgets.layout.WidgetLayout;

/**
 * Class representing an button having an Icon and no text, no margin etc.
 * @author Thomas Obenaus
 * @source IconButton.java
 * @date 09.02.2012
 */
@SuppressWarnings ( "serial")
public class IconButton extends JButton
{
	private ColoredBorder	buttonBorder;

	/**
	 * Icon that will be displayed if the Button is in normal state (enabled, no mouse over, not
	 * pressed)
	 */
	protected ImageIcon		enabled;

	/**
	 * Icon that will be displayed if the Button is in pressed state (pressed, enabled)
	 */
	protected ImageIcon		pressed;

	/**
	 * Icon that will be displayed if the Button is in disabled state (disabled, no mouse over, not
	 * pressed)
	 */
	protected ImageIcon		disabled;

	/**
	 * Icon that will be displayed if the Button is in normal and mouse over state (enabled, mouse
	 * over, not pressed)
	 */
	protected ImageIcon		mouseOver;

	/**
	 * Ctor
	 * @param enabled - Icon that will be displayed if the Button is in normal state (enabled, no
	 *            mouse over, not pressed)
	 * @param pressed - Icon that will be displayed if the Button is in pressed state (pressed,
	 *            enabled)
	 */
	public IconButton( ImageIcon enabled, ImageIcon pressed )
	{
		this( enabled, pressed, null, null );
	}

	/**
	 * Ctor
	 * @param enabled - Icon that will be displayed if the Button is in normal state (enabled, no
	 *            mouse over, not pressed)
	 * @param pressed - Icon that will be displayed if the Button is in pressed state (pressed,
	 *            enabled)
	 * @param disabled - Icon that will be displayed if the Button is in disabled state (disabled,
	 *            no mouse over, not pressed)
	 * @param mouseOver - Icon that will be displayed if the Button is in normal and mouse over
	 *            state (enabled, mouse over, not pressed)
	 */
	public IconButton( ImageIcon enabled, ImageIcon pressed, ImageIcon disabled, ImageIcon mouseOver )
	{
		this.enabled = enabled;
		this.pressed = pressed;

		this.disabled = disabled;
		this.mouseOver = mouseOver;

		this.setUpIcons( );
		this.setForeground( WidgetLayout.FLAT_WIDGET_FOREGROUND );
		this.setBackground( WidgetLayout.FLAT_WIDGET_BACKGROUND );
		this.setMargin( new Insets( 0, 0, 0, 0 ) );

		this.setFocusPainted( false );
		this.setOpaque( false );
		this.setBorderPainted( true );

		this.buttonBorder = new ColoredBorder( new Color( 100, 100, 100 ), new Color( 61, 126, 47 ) );
		this.addMouseListener( buttonBorder );
		this.setBorder( buttonBorder );

		this.addMouseListener( new MouseAdapter( )
		{
			@Override
			public void mouseEntered( MouseEvent e )
			{
				super.mouseEntered( e );
				setIcon( IconButton.this.mouseOver );
			}

			@Override
			public void mouseExited( MouseEvent e )
			{
				super.mouseExited( e );
				setIcon( IconButton.this.enabled );
			}
		} );
	}

	@Override
	public void paint( Graphics g )
	{
		// TODO Auto-generated method stub
		super.paint( g );
	}

	@Override
	protected void paintComponent( Graphics g )
	{
		// TODO Auto-generated method stub
		super.paintComponent( g );
	}

	@Override
	public Insets getInsets( )
	{
		return new Insets( 2, 2, 2, 2 );
	}

	public void setIconPressed( ImageIcon pressed )
	{
		this.pressed = pressed;
		this.setUpIcons( );
	}

	public void setIconDisabled( ImageIcon disabled )
	{
		this.disabled = disabled;
		this.setUpIcons( );
	}

	public void setIconEnabled( ImageIcon enabled )
	{
		this.enabled = enabled;
		this.setUpIcons( );
	}

	public void setIconMouseOver( ImageIcon mouseOver )
	{
		this.mouseOver = mouseOver;
		this.setUpIcons( );
	}

	protected ColoredBorder getButtonBorder( )
	{
		return buttonBorder;
	}

	protected void setButtonBorder( ColoredBorder buttonBorder )
	{
		this.buttonBorder = buttonBorder;
	}

	private void setUpIcons( )
	{
		if ( this.disabled == null )
			this.disabled = new ImageIcon( GrayFilter.createDisabledImage( this.enabled.getImage( ) ) );
		if ( this.mouseOver == null )
			this.mouseOver = enabled;

		this.setPressedIcon( this.pressed );
		this.setIcon( this.enabled );
		this.setDisabledIcon( this.disabled );

	}

}
