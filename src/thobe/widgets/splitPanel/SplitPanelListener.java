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
 * @author Thomas Obenaus
 * @source SplitPanelListener.java
 * @date 27.08.2012
 */
public interface SplitPanelListener
{
	/**
	 * Called whenever a specific {@link SPSubFrame} was clicked.
	 * @param event - the {@link SPSubFrameEvent}
	 */
	public void onSubFrameClicked( SPSubFrameEvent event );

	/**
	 * Called whenever the mouse has entered a specific {@link SPSubFrame}
	 * @param event - the {@link SPSubFrameEvent}
	 */
	public void onSubFrameEntered( SPSubFrameEvent event );

	/**
	 * Called whenever the mouse has left/exited a specific {@link SPSubFrame}
	 * @param event - the {@link SPSubFrameEvent}
	 */
	public void onSubFrameExited( SPSubFrameEvent event );

	public void onSubFrameRemoved( SPSubFrameEvent event );

	public void onSubFrameContentSwitched( SPSubFrameEvent event );
}
