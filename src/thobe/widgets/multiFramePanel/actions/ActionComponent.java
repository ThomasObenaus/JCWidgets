/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.multiFramePanel.actions;

/**
 * @author Thomas Obenaus
 * @source ActionComponent.java
 * @date 10.05.2010
 */
public abstract class ActionComponent
{
	public abstract String getName( );

	public abstract String getDescription( );

	public abstract Action getAction( );

	public abstract ActionGroup getActionGroup( );
}
