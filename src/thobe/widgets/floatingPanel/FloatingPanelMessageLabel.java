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

import javax.swing.JLabel;

import thobe.widgets.layout.WidgetLayout;
import thobe.widgets.utils.Utilities;

/**
 * @author Thomas Obenaus
 * @source FloatingPanelMessageLabel.java
 * @date 24.07.2009
 */
public class FloatingPanelMessageLabel extends JLabel
{
	public static final int		WARNING				= 1;
	public static final int		ERROR				= 2;
	public static final int		INFO				= 3;
	private static final long	serialVersionUID	= 0;

	private int					type;
	private String				text;

	/**
	 * Ctor
	 * @param text
	 * @param type
	 */
	public FloatingPanelMessageLabel( String text, int type )
	{
		this.text = text;
		this.type = type;
		this.setText( text, type );

	}

	public void updateTextSize( )
	{
		this.setText( this.text, this.type );
	}

	public void updateGUI( int width )
	{
		this.setSize( width, this.getHeight( ) );
		this.setText( this.text, this.type );
	}

	/**
	 * Set the labeltext.
	 * @param text
	 * @param type
	 */
	public void setText( String text, int type )
	{
		this.text = text;
		this.type = type;

		String post_msg = "";
		switch ( type )
		{
		case INFO:
			post_msg = "Info";
			this.setForeground( WidgetLayout.INFO_COLOR );
			break;
		case WARNING:
			post_msg = "Warning";
			this.setForeground( WidgetLayout.WARNING_COLOR );
			break;
		case ERROR:
			post_msg = "Error";
			this.setForeground( WidgetLayout.ERROR_COLOR );
			break;
		}

		text = post_msg + " " + text;
		text = Utilities.resizeStringToMaxWidthHTML( this.getFontMetrics( this.getFont( ) ), text, this.getWidth( ) );
		this.setText( text );
	}

}
