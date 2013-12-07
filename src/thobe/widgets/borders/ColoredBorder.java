/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.borders;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.border.Border;

/**
 * @author Thomas Obenaus
 * @source ColoredBorder.java
 * @date 09.02.2012
 */
public class ColoredBorder extends MouseAdapter implements Border
{
	private boolean	showBorder;
	private Color	borderHighlighted;
	private Color	border;

	public ColoredBorder( Color border, Color borderHighlighted )
	{
		this.showBorder = false;
		this.border = border;
		this.borderHighlighted = borderHighlighted;
	}

	public ColoredBorder( )
	{
		this( Color.LIGHT_GRAY, Color.DARK_GRAY );
	}

	public void setBorder( Color border )
	{
		this.border = border;
	}

	public void setBorderHighlighted( Color borderHighlighted )
	{
		this.borderHighlighted = borderHighlighted;
	}

	public Color getBorder( )
	{
		return border;
	}

	public Color getBorderHighlighted( )
	{
		return borderHighlighted;
	}

	@Override
	public void paintBorder( Component c, Graphics g, int x, int y, int width, int height )
	{
		g.setColor( border );
		g.drawRect( x + 1, y + 1, width - 3, height - 3 );
		if ( this.showBorder )
		{
			g.setColor( borderHighlighted );
			g.drawRect( x + 2, y + 2, width - 5, height - 5 );
		}
	}

	@Override
	public Insets getBorderInsets( Component c )
	{
		return new Insets( 0, 0, 0, 0 );
	}

	@Override
	public boolean isBorderOpaque( )
	{
		return false;
	}

	@Override
	public void mouseEntered( MouseEvent e )
	{
		super.mouseEntered( e );
		this.showBorder = true;
	}

	@Override
	public void mouseExited( MouseEvent e )
	{
		super.mouseExited( e );
		this.showBorder = false;
	}

}
