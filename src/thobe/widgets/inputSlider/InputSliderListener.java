/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.inputSlider;

/**
 * Listener that listens for events caused by the change of the value the input-slider points to.
 * @author Thomas Obenaus
 * @source InputSliderListener.java
 * @date 18 Nov 2008
 */
public interface InputSliderListener
{
	/**
	 * Called whenever the position of the slider or the value typed in the text-field has changed.
	 */
	public void valueChanged( );
}
