/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.abstractAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import thobe.widgets.action.AbstrAction;

/**
 * @author Thomas Obenaus
 * @source Act_Exit.java
 * @date 22.03.2010
 */
@SuppressWarnings ( "serial")
public class Act_Exit extends AbstrAction
{
	public static final String	ACT_EXIT	= "ACT_EXIT";

	private Tst_InMenu			frame;

	public Act_Exit( Tst_InMenu frame )
	{
		super( "Exit" );
		this.frame = frame;
		this.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_E, ActionEvent.CTRL_MASK ) );
	}

	@Override
	public String getActionKey( )
	{
		return ACT_EXIT;
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		this.frame.exit( );
	}

}
