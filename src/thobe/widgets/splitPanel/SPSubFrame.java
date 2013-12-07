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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import thobe.widgets.decoratorPanel.DecoratorPanelException;

/**
 * A class that represents the leaf of the composite design-pattern. This special node hold a
 * reference to the subframe of the {@link SplitPanel} that will be visible. This type of node will
 * be used to be able to build up the tree that represents the hierarchy of the subframes of the {@link SplitPanel}.
 * @author Thomas Obenaus
 * @source SPTreeLeaf.java
 * @date 13.02.2012
 */
public class SPSubFrame extends SPFrame implements SPComponentDecoratorListener, SPContentSwitchListener
{
	/**
	 * The panel that is added to the {@link SplitPanel}s content-panel. Each one of the {@link SPComponentDecorator}s in the contentMap
	 * that is added to this panel will be
	 * potentially visible.
	 */
	private JPanel						contentPanel;

	/**
	 * Key of the {@link SPComponentDecorator} that is currently active/ visible on the
	 * content-panel
	 */
	private String						currentContentKey;

	/**
	 * A list of attached listeners
	 */
	private List<SPSubFrameListener>	listeners;
	/**
	 * A list of listeners that are pending to be removed from the list of listeners ( {@link SPSubFrame#listeners}).
	 */
	private List<SPSubFrameListener>	listenersToBeRemoved;

	/**
	 * A list of listeners that are pending to be added to the list of listeners ( {@link SPSubFrame#listeners}).
	 */
	private List<SPSubFrameListener>	listenersToBeAdded;

	/**
	 * Component/ Container that will be placed into the {@link SplitPanel}. This one is a panel
	 * decorated with a menubar, content-switch, etc ...
	 */
	private SPComponentDecorator		componentDecorator;

	/**
	 * Factory that is used to create the content
	 */
	private SPContentFactory			contentFactory;

	/**
	 * Currently active/visible content
	 */
	private SPContent					currentContent;

	public SPSubFrame( int id, boolean firstChild, SPContentFactory contentFactory ) throws SPTreeException
	{
		this( id, firstChild, contentFactory, null );
	}

	/**
	 * Ctor
	 * @param id - see {@link SPFrame}
	 * @param firstChild - see {@link SPFrame}
	 */
	public SPSubFrame( int id, boolean firstChild, SPContentFactory contentFactory, String contentKey ) throws SPTreeException
	{
		super( id, firstChild );
		this.currentContent = null;
		this.currentContentKey = null;
		this.contentFactory = contentFactory;
		this.listeners = new ArrayList<SPSubFrameListener>( );
		this.listenersToBeRemoved = new ArrayList<SPSubFrameListener>( );
		this.listenersToBeAdded = new ArrayList<SPSubFrameListener>( );
		this.currentContentKey = "";
		this.contentPanel = new JPanel( );
		this.contentPanel.setLayout( null );
		this.contentPanel.setName( "ContentPanel of SPSubFrame" );

		try
		{
			this.componentDecorator = new SPComponentDecorator( this.contentPanel );
			this.componentDecorator.addListener( this );
			this.componentDecorator.addContentSwitchListener( this );
		}
		catch ( DecoratorPanelException e )
		{
			e.printStackTrace( );
		}

		// create content requested by the given content-key
		// if the key is null, the default content will be created 
		this.createAndSwitchToContent( contentKey );
	}

	public SPContent getCurrentContent( )
	{
		return currentContent;
	}

	/**
	 * Switches the content that is currently displayed inside this {@link SPSubFrame} to the
	 * content represented by the given content-key. If the given content-key is null the default
	 * one will be used (achieved via {@link SPContentFactory#getDefaultContentKey()}).
	 * @param contentKey - might be null
	 * @throws SPTreeException - Thrown if the {@link SPSubFrame}'s {@link SPContentFactory} is not
	 *             able to create {@link SPContent} for the given content-key.
	 */
	private void createAndSwitchToContent( String contentKey ) throws SPTreeException
	{
		// use default content-key if the given one is null 
		if ( contentKey == null )
			contentKey = this.contentFactory.getDefaultContentKey( );

		/* requested content is already the active one --> do nothing */
		if ( this.currentContentKey.equals( contentKey ) )
			return;

		// get the currently active SPContent --> remove it from the panel
		if ( this.currentContent != null )
		{
			Component currentComponent = this.currentContent.getComponent( );
			this.contentPanel.remove( currentComponent );
		}

		// create new content based on the given content-key
		this.currentContent = this.contentFactory.createContentForKey( contentKey, this.getId( ) );
		if ( this.currentContent == null )
			throw new SPTreeException( "Unable to switch to content with key " + contentKey + " since the given SPContentFactory is not able to create such content." );

		// update the list of content-keys		
		SPComponentDecorator decorator = this.getComponentDecorator( );
		decorator.getContentSwitch( ).addContentKeys( this.contentFactory.getContentKeys( ) );
		// update the toolBar
		decorator.setToolbar( this.currentContent.getToolBar( ) );

		this.currentContentKey = contentKey;

		// add the contents component to the panel + update visibility and bounds
		Component newComponent = this.currentContent.getComponent( );
		this.contentPanel.add( newComponent );
		newComponent.setVisible( true );
		newComponent.setBounds( 0, 0, this.componentDecorator.getWidth( ), this.componentDecorator.getHeight( ) );

		// switch the string displayed in the content-switch so that it matches the content 
		this.componentDecorator.getContentSwitch( ).switchContentDisplay( contentKey );
	}

	/**
	 * Remove a listener immediately.
	 * @param l
	 */
	public void removeListener( SPSubFrameListener l )
	{
		this.listeners.remove( l );
	}

	/**
	 * Register a new listener to be added in a while.
	 * @param l
	 */
	void registerToAddListener( SPSubFrameListener l )
	{
		this.listenersToBeAdded.add( l );
	}

	/**
	 * Register a listener to be removed in a while.
	 * @param l
	 */
	void registerToRemoveListener( SPSubFrameListener l )
	{
		this.listenersToBeRemoved.add( l );
	}

	public void addListener( SPSubFrameListener l )
	{
		this.listeners.add( l );
	}

	/**
	 * See {@link SPComponentDecorator#setButtonEnabled(SPComponentDecoratorButtonType, boolean)}
	 * @param type
	 * @param enabled
	 */
	public void setButtonEnabled( SPComponentDecoratorButtonType type, boolean enabled )
	{
		this.componentDecorator.setButtonEnabled( type, enabled );
	}

	public SPComponentDecorator getComponentDecorator( )
	{
		return componentDecorator;
	}

	@Override
	public void setBounds( int x, int y, int width, int height )
	{
		this.componentDecorator.setBounds( x, y, width, height );
		this.currentContent.getComponent( ).setBounds( this.contentPanel.getBounds( ) );
	}

	@Override
	public Rectangle getBounds( )
	{
		return this.componentDecorator.getBounds( );
	}

	@Override
	public void doLayout( )
	{
		this.componentDecorator.doLayout( );
	}

	/**
	 * Returns a string representing the currently active/visible content.
	 * @return
	 */
	public String getCurrentContentKey( )
	{
		return currentContentKey;
	}

	@Override
	public SPSubFrame getSubFrame( )
	{
		return this;
	}

	@Override
	public Dimension getMinSize( )
	{
		return this.componentDecorator.getMinimumSize( );
	}

	@Override
	public void setVisible( boolean visible )
	{
		this.componentDecorator.setVisible( visible );
	}

	@Override
	public void setEnabled( boolean enabled )
	{
		this.componentDecorator.setEnabled( enabled );
	}

	@Override
	public void onVerticalSplit( )
	{
		this.fireOnVSplit( );
	}

	@Override
	public void onHorizontalSplit( )
	{
		this.fireOnHSplit( );
	}

	@Override
	public void onMaximize( )
	{
		this.fireOnMaximize( );
	}

	@Override
	public void onMinimize( )
	{
		this.fireOnMinimize( );
	}

	@Override
	public void onClose( )
	{
		this.fireOnClose( );
	}

	private void fireOnClose( )
	{
		for ( SPSubFrameListener l : this.listeners )
			l.onClose( this );
		this.updatePendingListeners( );
	}

	private void fireOnMaximize( )
	{
		for ( SPSubFrameListener l : this.listeners )
			l.onMaximize( this );
		this.updatePendingListeners( );
	}

	private void fireOnMinimize( )
	{
		for ( SPSubFrameListener l : this.listeners )
			l.onMinimize( this );
		this.updatePendingListeners( );
	}

	private void fireOnHSplit( )
	{
		for ( SPSubFrameListener l : this.listeners )
			l.onHorizontalSplit( this );
		this.updatePendingListeners( );
	}

	private void fireOnVSplit( )
	{
		for ( SPSubFrameListener l : this.listeners )
			l.onVerticalSplit( this );

		this.updatePendingListeners( );
	}

	private void fireContentSwitched( String oldContentKey, String newContentKey )
	{
		for ( SPSubFrameListener l : this.listeners )
			l.onContentSwitched( this, newContentKey );
		this.updatePendingListeners( );
	}

	private void updatePendingListeners( )
	{
		for ( SPSubFrameListener l : this.listenersToBeAdded )
			this.listeners.add( l );
		this.listenersToBeAdded.clear( );

		for ( SPSubFrameListener l : this.listenersToBeRemoved )
			this.listeners.remove( l );
		this.listenersToBeRemoved.clear( );
	}

	@Override
	public boolean isVisible( )
	{
		return this.componentDecorator.isVisible( );
	}

	@Override
	public void onContentSwitch( String oldContentKey, String newContentKey )
	{
		try
		{
			this.createAndSwitchToContent( newContentKey );
			this.fireContentSwitched( oldContentKey, newContentKey );
		}
		catch ( SPTreeException e )
		{
			e.printStackTrace( );
		}
	}
}
