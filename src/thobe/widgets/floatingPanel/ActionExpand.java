/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.floatingPanel;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

import thobe.widgets.action.AbstrAction;

/**
 * @author Thomas Obenaus
 * @source ActionExpand.java
 * @date 14.09.2009
 */
@SuppressWarnings ( "serial")
public class ActionExpand extends AbstrAction
{
	public static final String		ACT_KEY	= "ACT_EXPAND_PANEL";

	private ExpandableFloatingPanel	panel;

	private Icon					iconExpandedDisabled;
	private Icon					iconExpandedEnabled;
	private Icon					iconCollapsedDisabled;
	private Icon					iconCollapsedEnabled;

	Icon							cancelButton;

	public ActionExpand( ExpandableFloatingPanel panel )
	{
		super( "+", "+", "+", "+", null, null );
		this.panel = panel;

		this.iconExpandedEnabled = null;
		this.iconExpandedDisabled = null;
		this.iconCollapsedDisabled = null;
		this.iconCollapsedEnabled = null;

		String buttonTxt = "+";
		if ( this.panel.isExpanded( ) )
			buttonTxt = "-";
		this.setTitle( buttonTxt, buttonTxt, null, null );
	}

	@Override
	public String getActionKey( )
	{
		return ACT_KEY;
	}

	public void setIcons( Icon iconCollapsedEnabled, Icon iconCollapsedDisabled, Icon iconExpandedEnabled, Icon iconExpandedDisabled )
	{
		this.iconExpandedDisabled = iconExpandedDisabled;
		this.iconExpandedEnabled = iconExpandedEnabled;
		this.iconCollapsedEnabled = iconCollapsedEnabled;
		this.iconCollapsedDisabled = iconCollapsedDisabled;

		if ( this.panel.isExpanded( ) )
			this.setIcon( this.iconExpandedEnabled, this.iconExpandedDisabled );
		else this.setIcon( this.iconCollapsedEnabled, this.iconCollapsedDisabled );
	}

	@Override
	public void actionPerformed( ActionEvent e )
	{
		this.panel.setExpaned( !this.panel.isExpanded( ) );

		if ( this.panel.isExpanded( ) )
		{
			if ( this.iconExpandedDisabled != null && this.iconExpandedEnabled != null )
				this.setIcon( this.iconExpandedEnabled, this.iconExpandedDisabled );
			else this.setTitle( "-", "-", "-", "-" );
		}
		else
		{
			if ( this.iconCollapsedEnabled != null && this.iconCollapsedDisabled != null )
				this.setIcon( this.iconCollapsedEnabled, this.iconCollapsedDisabled );
			else this.setTitle( "+", "+", "+", "+" );
		}

	}

}
