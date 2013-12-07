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

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 * @author Thomas Obenaus
 * @source AbstrAction.java
 * @date 26.08.2009
 */
@SuppressWarnings ( "serial")
public abstract class AbstrAction extends AbstractAction
{
	private String									txtEnabled;
	private String									txtDisabled;
	private Icon									iconEnabled;
	private Icon									iconDisabled;
	private String									tooltipEnabled;
	private String									tooltipDisabled;
	private Icon									iconSelectedEnabled;
	private Icon									iconSelectedDisabled;

	private List<AbstrActionPropertyChangeListener>	listeners;

	/**
	 * Ctor
	 * @param txtEnabled
	 * @param txtDisabled
	 * @param tooltipEnabled
	 * @param tooltipDisabled
	 * @param iconEnabled
	 * @param iconDisabled
	 */
	public AbstrAction( String txtEnabled, String txtDisabled, String tooltipEnabled, String tooltipDisabled, Icon iconEnabled, Icon iconDisabled )
	{
		this.listeners = new ArrayList<AbstrActionPropertyChangeListener>( );
		this.iconEnabled = iconEnabled;
		this.iconDisabled = iconDisabled;
		this.txtDisabled = txtDisabled;
		this.txtEnabled = txtEnabled;
		this.tooltipDisabled = tooltipDisabled;
		this.tooltipEnabled = tooltipEnabled;
		this.iconSelectedDisabled = iconDisabled;
		this.iconSelectedEnabled = iconEnabled;

		putValue( AbstrAction.NAME, this.txtEnabled );
		putValue( AbstrAction.SHORT_DESCRIPTION, this.tooltipEnabled );

		if ( this.iconEnabled != null )
			putValue( AbstrAction.SMALL_ICON, this.iconEnabled );
	}

	public void addListener( AbstrActionPropertyChangeListener listener )
	{
		this.listeners.add( listener );
	}

	public void setSelectedIcons( Icon iconSelectedEnabled, Icon iconSelectedDisabled )
	{
		this.iconSelectedDisabled = iconSelectedDisabled;
		this.iconSelectedEnabled = iconSelectedEnabled;
		this.fireSelectIconsChanged( );
	}

	public Icon getIconSelectedDisabled( )
	{
		return iconSelectedDisabled;
	}

	public Icon getIconSelectedEnabled( )
	{
		return iconSelectedEnabled;
	}

	public void setAccelerator( KeyStroke keyStroke )
	{
		this.putValue( AbstractAction.ACCELERATOR_KEY, keyStroke );
	}

	/**
	 * Sets the strings used to notify the user about the functionality of the action.
	 * @param txtEnabled
	 * @param txtDisabled
	 * @param tooltipEnabled
	 * @param tooltipDisabled
	 */
	public void setTitle( String txtEnabled, String txtDisabled, String tooltipEnabled, String tooltipDisabled )
	{
		this.txtDisabled = txtDisabled;
		this.txtEnabled = txtEnabled;
		this.tooltipDisabled = tooltipDisabled;
		this.tooltipEnabled = tooltipEnabled;

		if ( this.isEnabled( ) )
		{
			putValue( AbstrAction.NAME, this.txtEnabled );
			putValue( AbstrAction.SHORT_DESCRIPTION, this.tooltipEnabled );
		}
		else
		{
			putValue( AbstrAction.NAME, this.txtDisabled );
			putValue( AbstrAction.SHORT_DESCRIPTION, this.tooltipDisabled );
		}
	}

	public void setIcon( Icon iconEnabled, Icon iconDisabled )
	{
		this.iconDisabled = iconDisabled;
		this.iconEnabled = iconEnabled;
		if ( this.iconEnabled != null && this.isEnabled( ) )
			putValue( AbstrAction.SMALL_ICON, this.iconEnabled );
		if ( this.iconEnabled != null && !this.isEnabled( ) )
			putValue( AbstrAction.SMALL_ICON, this.iconDisabled );
		this.fireIconsChanged( );
	}

	private void fireIconsChanged( )
	{
		for ( AbstrActionPropertyChangeListener l : this.listeners )
			l.onIconsChanged( this.iconEnabled, this.iconDisabled );
	}

	private void fireSelectIconsChanged( )
	{
		for ( AbstrActionPropertyChangeListener l : this.listeners )
			l.onSelectionIconsChanged( this.iconSelectedEnabled, this.iconSelectedDisabled );
	}

	public AbstrAction( String title )
	{
		this( title, null, title, null, null, null );
	}

	public abstract String getActionKey( );

	public Icon getIconDisabled( )
	{
		return iconDisabled;
	}

	public Icon getIconEnabled( )
	{
		return iconEnabled;
	}

	public void setEnabled( boolean enable )
	{
		super.setEnabled( enable );

		/* the icon */
		if ( enable && ( this.iconEnabled != null ) )
			putValue( Action.SMALL_ICON, this.iconEnabled );
		if ( !enable && ( this.iconDisabled != null ) )
			putValue( Action.SMALL_ICON, this.iconDisabled );

		/* the tooltip */
		if ( enable && this.tooltipEnabled != null )
			putValue( Action.SHORT_DESCRIPTION, this.tooltipEnabled );
		if ( !enable && this.tooltipDisabled != null )
			putValue( Action.SHORT_DESCRIPTION, this.tooltipDisabled );

		/* the text */
		if ( enable && this.txtEnabled != null )
			putValue( Action.NAME, this.txtEnabled );
		if ( !enable && this.txtDisabled != null )
			putValue( Action.NAME, this.txtDisabled );
	}

}
