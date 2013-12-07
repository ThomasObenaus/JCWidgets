/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.statusbar;

import java.awt.Color;

/**
 * @author Thomas Obenaus
 * @source StatusBarMessageType.java
 * @date 14.04.2010
 */
public enum StatusBarMessageType
{
	INFO( new Color( 180, 200, 255 ) ), WARNING( new Color( 255, 255, 180 ) ), ERROR( new Color( 255, 200, 180 ) );

	private Color	color;

	private StatusBarMessageType( Color color )
	{
		this.color = color;
	}

	public Color getColor( )
	{
		return color;
	}
}
