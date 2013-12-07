/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.splitPanel;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Thomas Obenaus
 * @source SPMenubar.java
 * @date 17.02.2012
 */
@SuppressWarnings ( "serial")
public class SampleToolbar extends JPanel
{
	public SampleToolbar( )
	{
		this.setLayout( new BorderLayout( 0, 0 ) );
		this.add( new JLabel( "SPMenubar" ) );
	}
}
