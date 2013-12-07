/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.icons;

import javax.swing.ImageIcon;

/**
 * @author Thomas Obenaus
 * @source IconLib.java
 * @date 03.09.2009
 */
public class IconLib
{
	private static IconLib	instance	= null;
	public static ImageIcon	testIconE;
	public static ImageIcon	testIconD;
	public static ImageIcon	testIconSD;
	public static ImageIcon	testIconSE;

	private IconLib( )
	{

	}

	public static IconLib get( )
	{
		if ( instance == null )
			instance = new IconLib( );
		return instance;
	}

	public void createIcons( )
	{
		testIconE = new ImageIcon( getClass( ).getResource( "/testing/icons/expandedE.png" ) );
		testIconD = new ImageIcon( getClass( ).getResource( "/testing/icons/expandedD.png" ) );
		testIconSD = new ImageIcon( getClass( ).getResource( "/testing/icons/expandedSD.png" ) );
		testIconSE = new ImageIcon( getClass( ).getResource( "/testing/icons/expandedSE.png" ) );
	}
}
