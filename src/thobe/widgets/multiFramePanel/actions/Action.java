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

import thobe.widgets.action.AbstrAction;

/**
 * @author Thomas Obenaus
 * @source Action.java
 * @date 10.05.2010
 */
public class Action extends ActionComponent
{
	private AbstrAction	action;

	public Action( AbstrAction action )
	{
		this.action = action;
	}

	public AbstrAction getAbstrAction( )
	{
		return this.action;
	}

	@Override
	public String getDescription( )
	{
		return ( String ) this.action.getValue( AbstrAction.SHORT_DESCRIPTION );
	}

	@Override
	public Action getAction( )
	{
		return this;
	}

	@Override
	public ActionGroup getActionGroup( )
	{
		return null;
	}

	@Override
	public String getName( )
	{
		return ( String ) this.action.getValue( AbstrAction.NAME );
	}

}
