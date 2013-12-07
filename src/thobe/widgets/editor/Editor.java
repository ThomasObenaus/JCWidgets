/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import thobe.widgets.layout.WidgetLayout;
import thobe.widgets.messagePanel.MessagePanel;

/**
 * @author Thomas Obenaus
 * @source Editor.java
 * @date 05.10.2006
 */
/**
 * Baseclass for the used Editordialogs.
 */
@SuppressWarnings ( "serial")
public abstract class Editor extends JDialog
{

	private JScrollPane		scroll_pane;
	private JPanel			content_pane;

	private JLabel			l_title;
	private MessagePanel	messagePanel;
	protected Window		owner;
	private JPanel			pa_buttons;

	public Editor( Window owner, String title, ModalityType modal )
	{
		super( owner, "", modal );
		this.owner = owner;
		this.buildGUI( title );

		this.addWindowListener( new WindowAdapter( )
		{
			public void windowOpened( WindowEvent arg0 )
			{
				Window owner = getOwner( );
				if ( owner == null )
					return;
				Point p = owner.getLocation( );
				int ownerWidth = owner.getWidth( );
				int ownerHeight = owner.getHeight( );

				int pos_x = ( ownerWidth / 2 ) + p.x - ( getWidth( ) / 2 );
				int pos_y = ( ownerHeight / 2 ) + p.y - ( getHeight( ) / 2 );

				if ( pos_x < 20 )
					pos_x = 20;
				if ( pos_y < 20 )
					pos_y = 20;

				setLocation( pos_x, pos_y );
			}

			public void windowClosing( WindowEvent evnt )
			{
				handleWindowClosing_( );
			}
		} );

		/* close the dialog on pressing ESCAPE */
		this.getRootPane( ).registerKeyboardAction( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				l_title.setFocusable( true );
				l_title.requestFocus( );
				handleWindowClosing_( );
			}
		}, KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0, true ), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT );

		this.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
		this.setMinimumSize( this.getMinimumEditorSize( ) );
	}

	public MessagePanel getMessagePanel( )
	{
		return this.messagePanel;
	}

	private void buildGUI( String title )
	{
		/* main-panel */
		super.getContentPane( ).setLayout( new BorderLayout( ) );
		this.setResizable( true );

		JPanel pa_top = new JPanel( new BorderLayout( ) );
		/* titel-panel */
		JPanel pa_title = new JPanel( );
		pa_title.setBackground( WidgetLayout.HEADER_BACKGROUND );
		pa_title.setBorder( BorderFactory.createLineBorder( Color.black ) );
		l_title = new JLabel( title, JLabel.LEFT );
		l_title.setForeground( WidgetLayout.HEADER_FOREGROUND );
		l_title.setFont( WidgetLayout.HEADER_FONT );
		pa_title.add( l_title );

		/* messagelabel */
		this.messagePanel = new MessagePanel( );
		pa_top.add( this.messagePanel, BorderLayout.CENTER );
		super.getContentPane( ).add( pa_top, BorderLayout.NORTH );

		/* content-panel */
		this.content_pane = new JPanel( );
		this.scroll_pane = new JScrollPane( );
		this.scroll_pane.setBorder( BorderFactory.createLineBorder( Color.black ) );
		this.scroll_pane.getViewport( ).add( content_pane );
		super.getContentPane( ).add( scroll_pane, BorderLayout.CENTER );

		/* buttons */
		this.pa_buttons = this.createButtonPanel( );
		if ( pa_buttons != null )
			super.getContentPane( ).add( this.pa_buttons, BorderLayout.SOUTH );
	}

	public void setContentPaneBorder( Border border )
	{
		this.scroll_pane.setBorder( border );
	}

	/**
	 * Implement this function to return the minimum size of the editor.
	 * @return
	 */
	public abstract Dimension getMinimumEditorSize( );

	/**
	 * Called whenever the editor should be closed. This function can be overwritten to specify what to do when the dialog is closing.
	 */
	protected void handleWindowClosing_( )
	{
		this.close_dlg( );
	}

	public String getTitle( )
	{
		return this.l_title.getText( );
	}

	public void setTitle( String title )
	{
		this.l_title.setText( title );
	}

	@Override
	public void setDefaultCloseOperation( int operation )
	{
		super.setDefaultCloseOperation( JDialog.DO_NOTHING_ON_CLOSE );
	}

	@Override
	public Container getContentPane( )
	{
		return this.content_pane;
	}

	public void setBorder( Border border )
	{
		this.content_pane.setBorder( border );
	}

	protected abstract JPanel createButtonPanel( );

	/**
	 * Hide the Editor.
	 */
	protected void close_dlg( )
	{
		this.setVisible( false );
	}

}
