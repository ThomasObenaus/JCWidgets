/*
 *  Copyright (C) 2014, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    LogFileViewer
 */

package thobe.widgets.icons;

/**
 * @author Thomas Obenaus
 * @source IconSize.java
 * @date Apr 6, 2015
 */
public enum IconSize
{
	S16x16, S32x32;

	public String toString( )
	{
		String result = "UNKNOWN";
		switch ( this )
		{
		case S16x16:
			result = "16x16";
			break;
		case S32x32:
			result = "32x32";
			break;
		default:
			break;
		}
		return result;
	}
}
