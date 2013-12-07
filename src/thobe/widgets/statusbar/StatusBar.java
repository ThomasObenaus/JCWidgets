/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.statusbar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import sun.swing.DefaultLookup;
import thobe.widgets.action.AbstrAction;
import thobe.widgets.buttons.SmallButton;
import thobe.widgets.icons.IconLib;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Thomas Obenaus
 * @source StatusBar.java
 * @date 14.04.2010
 */
@SuppressWarnings ( "serial")
public class StatusBar extends JPanel
{

	private static StatusBar	instance	= null;

	private JPanel				pa_right;
	private JPanel				pa_rightContent;
	private JPanel				pa_left;
	private JLabel				l_info;
	private Color				defaultBG;
	private SmallButton			bu_help;
	private String				helpMessage;
	private Timer				timer;
	private JFrame				owner;

	private StatusBar( JFrame owner )
	{
		this.owner = owner;
		this.timer = new Timer( );

		try
		{
			this.defaultBG = DefaultLookup.getColor( this, ui, "Label.background" );

		}
		catch ( NoSuchMethodError e )
		{
			this.defaultBG = new Color( 224, 223, 227 );
		}

		this.pa_rightContent = null;
		this.buildGUI( );
	}

	/**
	 * Creates the new statusbar.
	 * @param owner
	 * @throws Exception
	 */
	public static void createStatusBar( JFrame owner ) throws Exception
	{
		if ( instance != null )
			new RuntimeException( "Don't call StatusBar.createStatusBar twice in one run. The StatusBar already has been created" );
		instance = new StatusBar( owner );
	}

	public static StatusBar get( )
	{
		if ( instance == null )
			System.err.println( "Error requesting the StatusBar. You have to call createStatusBar at first." );
		return instance;
	}

	/**
	 * Sets the message that should be displayed in the statusbar.
	 * @param message - the String that represents the message
	 * @param type - the type of the message (info, warning, error,...)
	 */
	public void setMessage( String message, StatusBarMessageType type )
	{
		this.setMessage( message, type, null );
	}

	/**
	 * Sets the message that should be displayed in the statusbar.
	 * @param message - the String that represents the message
	 * @param type - the type of the message (info, warning, error,...)
	 * @param help - a short text that will be provided to the user as help
	 */
	public void setMessage( String message, StatusBarMessageType type, String help )
	{
		this.l_info.setText( message );
		this.bu_help.setVisible( true );
		if ( help != null )
		{
			this.l_info.setToolTipText( help );
			this.bu_help.setVisible( true );
			this.helpMessage = help;
		}
		else this.bu_help.setVisible( false );
		this.pa_left.setBackground( type.getColor( ) );
		this.timer.cancel( );
		this.timer = new Timer( );
		this.timer.schedule( new ColorAnim( this.pa_left ), 1000 );
	}

	/**
	 * Adds a user-defined panel to the statusbar that can be used to provide additional
	 * informations (e.g. progress).
	 * @param panel - the panel
	 */
	public void setMiscPanel( JPanel panel )
	{
		this.pa_rightContent = panel;
		this.pa_right.removeAll( );

		if ( this.pa_rightContent != null )
			this.pa_right.add( this.pa_rightContent, BorderLayout.CENTER );

		this.pa_right.revalidate( );
		this.pa_right.repaint( );
	}

	/**
	 * Collapses the StatusBar and unregisters the StatusBarPanel that were registered previously by
	 * using the method setMiscPanel.
	 */
	public void clear( )
	{
		this.setMiscPanel( null );
		this.bu_help.setVisible( false );
		this.l_info.setText( "" );
	}

	private void buildGUI( )
	{
		FormLayout fla_main = new FormLayout( "1dlu,fill:MIN(pref;100dlu):grow(0.5),0dlu,fill:MIN(pref;100dlu):grow(0.5),0dlu", "0dlu,fill:MAX(pref;20dlu),0dlu" );
		CellConstraints cc_main = new CellConstraints( );
		this.setLayout( fla_main );
		this.setBorder( BorderFactory.createLineBorder( Color.gray ) );

		/* info area */
		FormLayout fla_left = new FormLayout( "2dlu,pref,4dlu,fill:pref:grow,2dlu", "2dlu,center:pref:grow,2dlu" );
		CellConstraints cc_left = new CellConstraints( );
		this.pa_left = new JPanel( fla_left );
		this.add( this.pa_left, cc_main.xy( 2, 2 ) );

		this.l_info = new JLabel( "" );
		pa_left.add( this.l_info, cc_left.xy( 4, 2 ) );

		this.bu_help = new SmallButton( new Act_Help( ) );
		pa_left.add( this.bu_help, cc_left.xy( 2, 2 ) );
		this.bu_help.setVisible( false );

		/* misc area */
		this.pa_right = new JPanel( new BorderLayout( 5, 5 ) );
		this.add( this.pa_right, cc_main.xy( 4, 2 ) );
	}

	private class Act_Help extends AbstrAction
	{
		public static final String	ACT_KEY	= "ACT_HELP";

		public Act_Help( )
		{
			super( "Hilfe", "Hilfe", "Hilfe", "Hilfe", IconLib.get( ).getHelpE( ), IconLib.get( ).getHelpD( ) );
		}

		public void actionPerformed( ActionEvent e )
		{
			JOptionPane.showMessageDialog( owner, helpMessage );
		}

		@Override
		public String getActionKey( )
		{
			return ACT_KEY;
		}

	}

	private class ColorAnim extends TimerTask
	{
		private JPanel	panel;

		public ColorAnim( JPanel panel )
		{
			this.panel = panel;
		}

		@Override
		public void run( )
		{
			this.panel.setBackground( defaultBG );
		}

	}

}
