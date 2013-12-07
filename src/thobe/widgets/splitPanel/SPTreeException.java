/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.splitPanel;

/**
 * An {@link Exception} thrown on problems occurring in connection with the tree of the {@link SplitPanel}.
 * @author Thomas Obenaus
 * @source SPTreeException.java
 * @date 13.02.2012
 */
@SuppressWarnings ( "serial")
public class SPTreeException extends Exception
{
	/**
	 * Ctor
	 * @param cause - reason for the exception
	 */
	public SPTreeException( String cause )
	{
		super( cause );
	}
}
