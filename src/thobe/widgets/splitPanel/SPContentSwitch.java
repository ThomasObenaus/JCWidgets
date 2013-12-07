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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * @author Thomas Obenaus
 * @source SPMenubar.java
 * @date 16.02.2012
 */
@SuppressWarnings ( "serial")
public class SPContentSwitch extends JPanel
{
	private List<SPContentSwitchListener>	listeners;
	private Set<String>						contentKeys;
	private String							oldKey;

	private JComboBox<String>				cb_contentSwitch;
	private ActionListener					acl_contentSwicth;

	public SPContentSwitch( )
	{
		this.listeners = new ArrayList<SPContentSwitchListener>( );
		this.contentKeys = new HashSet<String>( );
		this.oldKey = "";
		this.buildGUI( );
	}

	private void buildGUI( )
	{
		this.setLayout( new BorderLayout( ) );

		this.cb_contentSwitch = new JComboBox<String>( );
		this.acl_contentSwicth = new ActionListener( )
		{

			@SuppressWarnings ( "unchecked")
			@Override
			public void actionPerformed( ActionEvent e )
			{
				fireContentSwitch( ( String ) ( ( JComboBox<String> ) e.getSource( ) ).getSelectedItem( ) );
			}
		};
		this.add( this.cb_contentSwitch, BorderLayout.WEST );
		this.updateContentList( );
	}

	/**
	 * This method only changes the string displayed in the content-switch. No events about a
	 * content switch etc. will be thrown.
	 * @param contentKey
	 */
	public void switchContentDisplay( String contentKey )
	{
		this.cb_contentSwitch.removeActionListener( this.acl_contentSwicth );
		this.cb_contentSwitch.setSelectedItem( contentKey );
		this.cb_contentSwitch.addActionListener( this.acl_contentSwicth );
	}

	public void addContentSwitchListener( SPContentSwitchListener l )
	{
		this.listeners.add( l );
	}

	private void updateContentList( )
	{
		this.cb_contentSwitch.removeActionListener( this.acl_contentSwicth );
		this.oldKey = ( String ) this.cb_contentSwitch.getSelectedItem( );

		this.cb_contentSwitch.removeAllItems( );
		for ( String item : this.contentKeys )
			this.cb_contentSwitch.addItem( item );

		if ( ( this.oldKey == null || this.oldKey.trim( ).equals( "" ) ) && this.contentKeys.size( ) > 0 )
			oldKey = this.contentKeys.iterator( ).next( );
		this.cb_contentSwitch.setSelectedItem( this.oldKey );

		this.cb_contentSwitch.addActionListener( this.acl_contentSwicth );
	}

	public void addContentKey( String contentKey )
	{
		this.contentKeys.add( contentKey );
		this.updateContentList( );
	}

	public void removeContentKey( String contentKey )
	{
		this.contentKeys.remove( contentKey );
		this.updateContentList( );
	}

	public void addContentKeys( List<String> contentKeys )
	{
		for ( String contentKey : contentKeys )
			this.contentKeys.add( contentKey );
		this.updateContentList( );
	}

	public void removeContentKeys( List<String> contentKeys )
	{
		for ( String contentKey : contentKeys )
			this.contentKeys.remove( contentKey );
		this.updateContentList( );
	}

	public void clearContentKeys( )
	{
		this.contentKeys.clear( );
		this.updateContentList( );
	}

	private void fireContentSwitch( String newKey )
	{
		for ( SPContentSwitchListener l : this.listeners )
			l.onContentSwitch( this.oldKey, newKey );
		this.oldKey = newKey;
	}

	@Override
	public String getName( )
	{
		return "SPMenubar";
	}

}
