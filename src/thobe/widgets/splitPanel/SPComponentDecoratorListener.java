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
 * Interface representing a listener that can be used to listen for window-events (close, split, maximize) of the top menu-bar of the
 * {@link SPComponentDecorator}.
 * @author Thomas Obenaus
 * @source SPCWindowListener.java
 * @date 06.02.2012
 */
public interface SPComponentDecoratorListener
{

	/**
	 * Called whenever the vertical-split button of the {@link SPComponentDecorator} was clicked.
	 */
	public void onVerticalSplit( );

	/**
	 * Called whenever the horizontal-split button of the {@link SPComponentDecorator} was clicked.
	 */
	public void onHorizontalSplit( );

	/**
	 * Called whenever the maximize button of the {@link SPComponentDecorator} was clicked.
	 */
	public void onMaximize( );

	/**
	 * Called whenever the minimize-split button of the {@link SPComponentDecorator} was clicked.
	 */
	public void onMinimize( );

	/**
	 * Called whenever the close button of the {@link SPComponentDecorator} was clicked.
	 */
	public void onClose( );
}
