/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.scalarbar;

/**
 * @author Thomas Obenaus
 * @source ScalarBarListener.java
 * @date 19.01.2009
 */
public interface ScalarBarListener
{
	/**
	 * Called whenever one of the markers was selected.
	 * @param selectedMarker
	 */
	public void markerSelected( ScalarBarMarker selectedMarker );

	/**
	 * Called whenever the currently selected marker was deselected.
	 */
	public void markerDeselected( );

	/**
	 * Called whenever the value of the currently selected marker was changed.
	 * @param selectedMarker
	 */
	public void valueOfSelectedMarkerChanged( ScalarBarMarker selectedMarker );

	/**
	 * Called whenever one marker was deleter.
	 * @param deletedMarker
	 */
	public void markerDeleted( ScalarBarMarker deletedMarker );

	/**
	 * Called whenever a marker added.
	 * @param addedMarker
	 */
	public void markerAdded( ScalarBarMarker addedMarker );
}
