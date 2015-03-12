package thobe.widgets;
/*
 *  Copyright (C) 2015, Thomas Obenaus. All rights reserved.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JCWidgets
 */

/**
 * @author Thomas Obenaus
 * @source Version.java
 * @date Mar 12, 2015
 */
public class Version
{
	private static final int	MAJOR_VERSION	= 1;
	private static final int	MINOR_VERSION	= 4;
	private static final int	BUGFIX_VERSION	= 2;

	public static String getVersion( )
	{
		return MAJOR_VERSION + "." + MINOR_VERSION + "." + BUGFIX_VERSION;
	}

	public static int getBugfixVersion( )
	{
		return BUGFIX_VERSION;
	}

	public static int getMajorVersion( )
	{
		return MAJOR_VERSION;
	}

	public static int getMinorVersion( )
	{
		return MINOR_VERSION;
	}
}
