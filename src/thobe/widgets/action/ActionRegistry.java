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

import java.util.HashMap;

/**
 * @author Thomas Obenaus
 * @source ActionRegistry.java
 * @date 26.08.2009
 */
public class ActionRegistry
{
	private HashMap<String, AbstrAction>	registeredActions;
	private static ActionRegistry			instance	= null;

	private ActionRegistry( )
	{
		this.registeredActions = new HashMap<String, AbstrAction>( );
	}

	public static ActionRegistry get( )
	{
		if ( instance == null )
			instance = new ActionRegistry( );
		return instance;
	}

	public AbstrAction getAction( String key )
	{
		return this.registeredActions.get( key );
	}

	public void registerAction( AbstrAction action )
	{
		this.registeredActions.put( action.getActionKey( ), action );
	}
}
