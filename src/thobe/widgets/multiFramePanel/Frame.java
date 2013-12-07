/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.multiFramePanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import thobe.widgets.multiFramePanel.actions.ActionComponent;

/**
 * @author Thomas Obenaus
 * @source Frame.java
 * @date 16.04.2010
 */
public final class Frame extends Observable implements FrameTreeNode
{
	private FrameComponent			component;
	private BoundingBox				boundingBox;

	private boolean					maximized;
	private boolean					visible;

	private List<ActionComponent>	globalActions;

	/**
	 * Ctor
	 * @param mfComponent
	 */
	public Frame( FrameComponent mfComponent )
	{
		this.globalActions = new ArrayList<ActionComponent>( );
		this.maximized = false;
		this.visible = false;
		this.component = mfComponent;
		this.boundingBox = new BoundingBox( );
		this.component.rebuildGUI( );
	}

	/**
	 * Removes the given {@link ActionComponent} from the {@link Frame} and its {@link FrameComponent} (if one is attached). Thereafter the
	 * {@link FrameComponent} will be
	 * notified about this modification to be able to rebuild its MenuBar.
	 * @param action
	 */
	public void unRegisterGlobalAction( ActionComponent action )
	{
		this.globalActions.remove( action );
		this.updateGlobalActions( );
	}

	/**
	 * Adds a new {@link ActionComponent} to the {@link Frame} and its {@link FrameComponent} (if
	 * one is attached). Thereafter the {@link FrameComponent} will be notified about this
	 * modification to be able to rebuild its MenuBar.
	 * @param action
	 */
	public void registerGlobalAction( ActionComponent action )
	{
		this.globalActions.add( action );
		this.updateGlobalActions( );
	}

	private void updateGlobalActions( )
	{
		this.component.removeAllGlobalActions( );
		for ( ActionComponent act : this.globalActions )
			this.component.addGlobalAction( act );
		this.component.rebuildGUI( );
	}

	public void setEnabled( boolean enabled )
	{
		this.component.setEnabled( enabled );
	}

	/**
	 * Returns true if the {@link Frame} is visible, false otherwise.
	 * @return
	 */
	public boolean isFrameVisible( )
	{
		return this.visible;
	}

	/**
	 * Sets the {@link Frame}'s visibility state.
	 * @param visible
	 */
	public void setFrameVisible( boolean visible )
	{
		this.visible = visible;
		if ( this.component != null )
			this.component.setVisible( visible );
	}

	/**
	 * Sets the {@link Frame}'s maximized state.
	 * @param maximized
	 */
	public void setMaximized( boolean maximized )
	{
		if ( maximized )
			this.visible = true;
		this.maximized = maximized;
	}

	/**
	 * Returns true if the {@link Frame} is maximized.
	 * @return
	 */
	public boolean isMaximized( )
	{
		return maximized;
	}

	/**
	 * Replaces the current component
	 * @param component
	 * @return - returns the {@link FrameComponent} that will be replaced by the new {@link FrameComponent}. This method might return
	 *         <code>null</code>.
	 */
	public FrameComponent setComponent( FrameComponent component )
	{
		FrameComponent oldComponent = this.component;

		this.component = component;
		this.updateGlobalActions( );

		/* notify all observers about that a new component where set */
		this.setChanged( );
		this.notifyObservers( FrameNotification.COMPONENT_SET );
		this.clearChanged( );

		return oldComponent;
	}

	/**
	 * Show/ hide the menubar
	 * @param visible
	 */
	public void setMenuBarVisible( boolean visible )
	{
		this.component.setMenuBarVisible( visible );
	}

	/**
	 * Returns the {@link FrameComponent} that is managed and displayed by the {@link Frame}
	 * @return
	 */
	public FrameComponent getComponent( )
	{
		return this.component;
	}

	@Override
	public void setBounds( int x, int y, int width, int height )
	{
		this.boundingBox.setBounds( x, y, width, height );
		this.component.setBounds( x, y, width, height );
	}

	/**
	 * Rebuild the gui of the attached {@link FrameComponent}.
	 */
	void rebuildGUI( )
	{
		this.component.rebuildGUI( );
	}

	@Override
	public BoundingBox getBoundingBox( )
	{
		return this.boundingBox;
	}

	@Override
	public Frame getFrame( )
	{
		return this;
	}

	@Override
	public FrameContainer getFrameContainer( )
	{
		return null;
	}

	@Override
	public int getMinHeight( )
	{
		if ( this.component == null )
			return 0;
		return this.component.getMinHeight( );
	}

	@Override
	public int getMinWidth( )
	{
		if ( this.component == null )
			return 0;
		return this.component.getMinWidth( );
	}

	@Override
	public String toString( )
	{
		if ( this.component != null )
			return this.component.getTitle( );
		return super.toString( );
	}

	@Override
	public void doLayout( )
	{
		this.component.setBounds( this.boundingBox.getX( ), this.boundingBox.getY( ), this.boundingBox.getWidth( ), this.boundingBox.getHeight( ) );
		this.component.doLayout( );
	}

	/**
	 * Removes all global actions registered at the {@link Frame} and its {@link FrameComponent}.
	 */
	public void removeAllGlobalActions( )
	{
		this.globalActions.clear( );
		this.component.removeAllGlobalActions( );
		this.updateGlobalActions( );
	}
}
