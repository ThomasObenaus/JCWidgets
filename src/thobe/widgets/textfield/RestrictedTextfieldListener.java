/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.textfield;

/**
 * @author Thomas Obenaus
 * @source RestrictedTextfieldListener.java
 * @date 20 Jun 2008
 */
public interface RestrictedTextfieldListener
{
	/**
	 * Called whenever the content of the textfield was modified and this modification was commited by pressing enter or by loosing the
	 * focus.
	 */
	public void valueChangeCommitted( );

	/**
	 * Called whenever the content of the textfield was modified during a keypress.
	 */
	public void valueChanged( );

	public void valueInvalid( String value );

	public void valueOutOfBounds( String value );
}
