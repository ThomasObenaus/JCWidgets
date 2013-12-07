/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.decoratorPanel;

import java.awt.Image;

/**
 * An interface whose instances of its implementing classes are able to offer an screenshot.
 * @author Thomas Obenaus
 * @source Screenshot.java
 * @date 27.01.2012
 */
public interface Screenshot
{
	/**
	 * Returns a screenshot as {@link Image}
	 * @return
	 */
	public Image getScreenshot( );
}
