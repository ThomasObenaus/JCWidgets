/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.utils;

import javax.swing.UIManager;

/**
 * @author Thomas Obenaus
 * @source LookAndFeel.java
 * @date 24.02.2012
 */
public enum LookAndFeel
{
	SYSTEM( UIManager.getSystemLookAndFeelClassName( ) ), CROSSPLATTFORM( UIManager.getCrossPlatformLookAndFeelClassName( ) ), NIMBUS( "javax.swing.plaf.nimbus.NimbusLookAndFeel" ), MOTIV( "com.sun.java.swing.plaf.motif.MotifLookAndFeel" ), GTK( "com.sun.java.swing.plaf.gtk.GTKLookAndFeel" ), WINDOWSCLASSIC( "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel" ), METAL( "javax.swing.plaf.metal.MetalLookAndFeel" );

	private String	className;

	private LookAndFeel( String className )
	{
		this.className = className;
	}

	public String getClassName( )
	{
		return className;
	}
}
