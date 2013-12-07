/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.action;

import javax.swing.Icon;

/**
 * @author Thomas Obenaus
 * @source AbstrActionPropertyChangeListener.java
 * @date 07.05.2010
 */
public interface AbstrActionPropertyChangeListener
{
	public void onIconsChanged( Icon iconEnabled, Icon iconDisabled );

	public void onSelectionIconsChanged( Icon iconSelectEnabled, Icon iconSelectDisabled );
}
