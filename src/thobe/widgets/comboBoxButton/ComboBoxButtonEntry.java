/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.comboBoxButton;

import javax.swing.Icon;

/**
 * @author Thomas Obenaus
 * @source ComboBoxButtonEntry.java
 * @date 22.02.2012
 */
public class ComboBoxButtonEntry
{
	private Object	entryValue;
	private Icon	entryIcon;

	public ComboBoxButtonEntry( Object entryValue, Icon entryIcon )
	{
		if ( entryValue == null )
			throw new NullPointerException( "Argument entryValue is null." );
		if ( entryIcon == null )
			throw new NullPointerException( "Argument entryIcon is null." );

		this.entryIcon = entryIcon;
		this.entryValue = entryValue;
	}

	public Icon getEntryIcon( )
	{
		return entryIcon;
	}

	public Object getEntryValue( )
	{
		return entryValue;
	}

	@Override
	public String toString( )
	{
		return this.entryValue.toString( );
	}
}
