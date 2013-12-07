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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import thobe.widgets.buttons.SmallButton;
import thobe.widgets.multiFramePanel.actions.ActionComponent;
import thobe.widgets.multiFramePanel.actions.ActionGroup;

/**
 * @author Thomas Obenaus
 * @source FrameComponent.java
 * @date 16.04.2010
 */
@SuppressWarnings ( "serial")
public abstract class FrameComponent extends JPanel
{
	private static final Font		titleFont	= new Font( "Arial", Font.ITALIC, 11 );
	private static final Color		titleColor	= new Color( 110, 110, 110 );

	private List<ActionComponent>	globalActions;
	private JMenuBar				menuBar;
	private JMenuBar				globalMenuBar;
	private JPanel					pa_content;
	private JPanel					pa_menuBar;
	private JLabel					l_title;

	private String					title;
	private String					description;

	public FrameComponent( )
	{
		this( "", "" );
	}

	public FrameComponent( String title, String description )
	{
		this.title = title;
		this.description = description;

		this.globalActions = new ArrayList<ActionComponent>( );

		this.setLayout( new BorderLayout( 0, 0 ) );
	}

	@Override
	public void setEnabled( boolean enabled )
	{
		super.setEnabled( enabled );
		this.pa_menuBar.setEnabled( enabled );
		this.menuBar.setEnabled( enabled );

	}

	public void dispose( )
	{
		this.globalActions.clear( );
		this.menuBar.removeAll( );
		this.removeAll( );
	}

	void rebuildGUI( )
	{
		this.buildMenuBar( );
		this.buildGUI( );

		this.setToolTipText( this.description );
		this.setTitle( this.title );
	}

	public Rectangle getBoundsOfContentPanel( )
	{
		return this.pa_content.getBounds( );
	}

	/**
	 * Show/ hide the menubar
	 * @param visible
	 */
	public void setMenuBarVisible( boolean visible )
	{
		this.pa_menuBar.setVisible( visible );
	}

	protected void buildMenuBar( )
	{
		if ( this.pa_menuBar == null )
		{
			this.pa_menuBar = new JPanel( new BorderLayout( 0, 0 ) );
			this.add( this.pa_menuBar, BorderLayout.NORTH );
		}

		if ( this.menuBar == null )
		{
			this.menuBar = new JMenuBar( );
			this.pa_menuBar.add( this.menuBar, BorderLayout.CENTER );
		}
		if ( this.globalMenuBar == null )
		{
			this.globalMenuBar = new JMenuBar( );
			this.globalMenuBar.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
			this.pa_menuBar.add( this.globalMenuBar, BorderLayout.EAST );
		}

		this.menuBar.removeAll( );
		this.globalMenuBar.removeAll( );

		this.l_title = new JLabel( "" );
		this.l_title.setBorder( BorderFactory.createEmptyBorder( 0, 5, 0, 15 ) );
		this.l_title.setForeground( titleColor );
		this.l_title.setFont( titleFont );
		this.menuBar.add( this.l_title );

		/* add all actions to the frame-specific menu-bar */
		List<ActionComponent> actionComponents = this.getActionComponents( );
		if ( actionComponents != null )
		{
			for ( ActionComponent actionComp : actionComponents )
				this.addActionsToMenuBar( this.menuBar, actionComp );
		}

		/* add all global actions */
		for ( ActionComponent action : this.globalActions )
			this.addActionsToMenuBar( this.globalMenuBar, action );

		this.setTitle( this.title );
		this.setToolTipText( this.description );

		this.menuBar.repaint( );
		this.menuBar.revalidate( );
		this.globalMenuBar.repaint( );
		this.globalMenuBar.revalidate( );

	}

	private void buildGUI( )
	{
		if ( this.pa_content == null )
		{
			this.pa_content = new JPanel( new BorderLayout( 0, 0 ) );
			this.add( this.pa_content, BorderLayout.CENTER );
		}
		this.pa_content.removeAll( );
		this.pa_content.add( this.getContentComponent( ), BorderLayout.CENTER );
		this.pa_content.repaint( );
		this.pa_content.revalidate( );
	}

	/**
	 * add global actions to the Frame.
	 * @param action
	 */
	void addGlobalAction( ActionComponent action )
	{
		this.globalActions.add( action );
		this.buildMenuBar( );
	}

	/**
	 * Returns the minimum height of this {@link FrameComponent}
	 * @return
	 */
	protected abstract int getMinHeight( );

	/**
	 * Returns the minimum width of this {@link FrameComponent}
	 * @return
	 */
	protected abstract int getMinWidth( );

	/**
	 * Should return a List containing all {@link ActionComponent}'s that should be in the MenuBar
	 * of the {@link Frame}.
	 * @return
	 */
	protected abstract List<ActionComponent> getActionComponents( );

	/**
	 * Should return the main {@link Component} that should be displayed in the {@link Frame}.
	 * @return
	 */
	protected abstract Component getContentComponent( );

	private void addActionsToMenuBar( JMenuBar menuBar, ActionComponent action )
	{
		/* add single actions directly as a SmallButton to the menu-bar */
		if ( action.getAction( ) != null )
		{
			menuBar.add( new SmallButton( action.getAction( ).getAbstrAction( ) ) );
			return;
		}

		/* add each child of a ActionGroup to a JMenu */
		ActionGroup actionGroup = action.getActionGroup( );
		JMenu mu_actionGroup = new JMenu( actionGroup.getName( ) );
		mu_actionGroup.setToolTipText( actionGroup.getDescription( ) );
		menuBar.add( mu_actionGroup );
		for ( ActionComponent actionComponent : actionGroup.getChildren( ) )
			this.addActionsToMenu( mu_actionGroup, actionComponent );

	}

	private void addActionsToMenu( JMenu menu, ActionComponent action )
	{
		/* add single actions directly as a JMenuItem to the JMenu */
		if ( action.getAction( ) != null )
		{
			JMenuItem mi_action = new JMenuItem( action.getAction( ).getAbstrAction( ) );
			mi_action.setToolTipText( action.getDescription( ) );
			menu.add( new JMenuItem( action.getAction( ).getAbstrAction( ) ) );
			return;
		}

		/* add each child of a ActionGroup to a JMenu */
		ActionGroup actionGroup = action.getActionGroup( );
		JMenu mu_actionGroup = new JMenu( actionGroup.getName( ) );
		mu_actionGroup.setToolTipText( actionGroup.getDescription( ) );
		menu.add( mu_actionGroup );
		for ( ActionComponent actionComponent : actionGroup.getChildren( ) )
			this.addActionsToMenu( mu_actionGroup, actionComponent );
	}

	/**
	 * Set the {@link Frame}'s title.
	 * @param title
	 */
	public void setTitle( String title )
	{
		this.title = title;
		this.l_title.setText( this.title );
	}

	@Override
	public void setToolTipText( String text )
	{
		if ( text == null || text.trim( ).equals( "" ) )
			return;
		this.description = text;
		this.l_title.setToolTipText( text );
		this.menuBar.setToolTipText( text );
		this.globalMenuBar.setToolTipText( text );
	}

	public String getTitle( )
	{
		return title;
	}

	public String getDescription( )
	{
		return description;
	}

	/**
	 * Clears all global actions registered at the {@link FrameComponent}.
	 */
	void removeAllGlobalActions( )
	{
		this.globalActions.clear( );
		this.buildMenuBar( );
	}

	@Override
	public String toString( )
	{
		return this.title;
	}

}
