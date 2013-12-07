/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.layout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

/**
 * @author Thomas Obenaus
 * @source MyLayout.java
 * @date 09.10.2006
 */
/**
 * Class holding some constants for the custom layout.
 */
public class WidgetLayout
{
	public static final Color		HEADER_BACKGROUND			= new Color( 70, 150, 70 );
	public static final Color		HEADER_FOREGROUND			= new Color( 255, 255, 255 );

	public static final Color		HEADER_BACKGROUND_INACTIVE	= new Color( 150, 150, 150 );
	public static final Color		HEADER_FOREGROUND_INACTIVE	= new Color( 255, 255, 255 );

	public static final Color		TABLE_HEADER_BACKGROUND		= new Color( 100, 100, 100 );
	public static final Color		TABLE_HEADER_FOREGROUND		= new Color( 255, 255, 255 );

	public static final Font		HEADER_FONT					= new Font( "Arial", Font.BOLD, 12 );

	public static final Color		FLAT_WIDGET_BACKGROUND		= new Color( 255, 255, 255 );
	public static final Color		FLAT_WIDGET_FOREGROUND		= new Color( 0, 0, 0 );
	public static final Color		FLAT_WIDGET_BORDER			= new Color( 0, 0, 0 );
	public static final Color		FLAT_WIDGET_PRESSED			= new Color( 184, 207, 229 );
	public static final Color		FLAT_WIDGET_ROLLOVER		= new Color( 90, 120, 150 );

	public static final Color		INFO_COLOR					= new Color( 0, 0, 255 );
	public static final Color		ERROR_COLOR					= new Color( 255, 0, 0 );
	public static final Color		WARNING_COLOR				= new Color( 100, 100, 150 );

	public static final Border		EMPTY_BORDER				= BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
	public static final Border		PANEL_BORDER				= BorderFactory.createLineBorder( Color.GRAY );

	public static final Dimension	PANEL_INSET_SIZE			= new Dimension( 348, 40 );

	private WidgetLayout( )
	{}
}
