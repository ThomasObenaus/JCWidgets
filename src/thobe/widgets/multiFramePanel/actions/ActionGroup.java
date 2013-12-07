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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Thomas Obenaus
 * @source ActionGroup.java
 * @date 10.05.2010
 */
public class ActionGroup extends ActionComponent
{
	private String					name;
	private String					tooltip;
	private List<ActionComponent>	children;

	public ActionGroup( String name, String tooltipText )
	{
		this.name = name;
		this.tooltip = tooltipText;
		this.children = new ArrayList<ActionComponent>( );
	}

	public void addChild( ActionComponent child )
	{
		this.children.add( child );
	}

	public void removeChild( ActionComponent child )
	{
		this.children.remove( child );
	}

	public int getNumberOfChildren( )
	{
		return this.children.size( );
	}

	public List<ActionComponent> getChildren( )
	{
		return this.children;
	}

	@Override
	public Action getAction( )
	{
		return null;
	}

	@Override
	public ActionGroup getActionGroup( )
	{
		return this;
	}

	@Override
	public String getDescription( )
	{
		return this.tooltip;
	}

	@Override
	public String getName( )
	{
		return this.name;
	}

}
