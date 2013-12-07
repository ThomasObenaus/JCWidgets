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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * @author Thomas Obenaus
 * @source CancelButton.java
 * @date 30.01.2009
 */
@SuppressWarnings ( "serial")
public class CancelButton extends JButton
{
	private final ImageIcon	cancelButton;
	private final ImageIcon	cancelButtonPressed;

	private boolean			pressed;

	/**
	 * Ctor
	 * @param text
	 */
	public CancelButton( )
	{
		super( );
		this.pressed = false;
		this.cancelButton = new ImageIcon( getClass( ).getResource( "/widgets/icons/cancelButton.png" ) );
		this.cancelButtonPressed = new ImageIcon( getClass( ).getResource( "/widgets/icons/cancelButton_pressed.png" ) );

		this.setIcon( cancelButton );
		this.setBorderPainted( false );
		this.setBorder( BorderFactory.createEmptyBorder( ) );
		this.setOpaque( false );
		this.setMaximumSize( new Dimension( this.cancelButton.getIconWidth( ), this.cancelButton.getIconHeight( ) ) );

		this.addMouseListener( new MouseAdapter( )
		{
			@Override
			public void mousePressed( MouseEvent e )
			{
				pressed = true;
			}

			@Override
			public void mouseReleased( MouseEvent e )
			{
				pressed = false;
			}
		} );
	}

	@Override
	public void paint( Graphics g )
	{
		ImageIcon img = null;
		if ( this.pressed )
			img = this.cancelButtonPressed;
		else img = this.cancelButton;

		int midX = ( this.getWidth( ) / 2 ) - ( img.getIconWidth( ) / 2 );
		int midY = ( this.getHeight( ) / 2 ) - ( img.getIconHeight( ) / 2 );
		g.drawImage( img.getImage( ), midX, midY, new ImageObserver( )
		{
			@Override
			public boolean imageUpdate( Image img, int infoflags, int x, int y, int width, int height )
			{
				// TODO Auto-generated method stub
				return false;
			}
		} );
	}

}
