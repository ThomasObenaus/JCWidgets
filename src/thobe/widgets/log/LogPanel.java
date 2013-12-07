/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.log;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * @author Thomas Obenaus
 * @source LogPanel.java
 * @date 14.06.2009
 */
@SuppressWarnings ( "serial")
public class LogPanel extends JPanel
{
	private JTextPane		log;
	private JCheckBox		cb_supressDebugMessages;
	private JButton			bu_clear;
	private boolean			showSupressDebugMessagesModifier;
	private boolean			supressDebugMessages;
	private List<String>	debugMessages;

	private ActionListener	acl_supressDebugMessages;

	private int				maxLines	= 500;
	private int				numLines	= 0;

	public LogPanel( )
	{
		this( false, true );
	}

	public LogPanel( boolean showSupressDebugMessagesModifier, boolean supressDebugMessages )
	{
		this.showSupressDebugMessagesModifier = showSupressDebugMessagesModifier;
		this.supressDebugMessages = supressDebugMessages;
		this.debugMessages = new ArrayList<String>( );
		this.buildGUI( );
	}

	private void buildGUI( )
	{
		this.setLayout( new BorderLayout( 5, 5 ) );

		/* log */
		this.log = new JTextPane( );
		this.log.setEditable( false );
		JScrollPane scrpa_log = new JScrollPane( this.log );
		this.add( scrpa_log, BorderLayout.CENTER );

		/* modifiers panel */
		JPanel pa_modifiers = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		this.add( pa_modifiers, BorderLayout.SOUTH );

		if ( this.showSupressDebugMessagesModifier )
		{
			this.cb_supressDebugMessages = new JCheckBox( "supress debug-messages" );
			this.cb_supressDebugMessages.setSelected( this.supressDebugMessages );
			pa_modifiers.add( this.cb_supressDebugMessages );
			this.acl_supressDebugMessages = new ActionListener( )
			{
				@Override
				public void actionPerformed( ActionEvent e )
				{
					JCheckBox source = ( JCheckBox ) e.getSource( );
					setSupressDebugMessages( source.isSelected( ) );
				}
			};
			this.cb_supressDebugMessages.addActionListener( this.acl_supressDebugMessages );
		}

		/* clear button */
		this.bu_clear = new JButton( "clear log" );
		pa_modifiers.add( this.bu_clear );
		this.bu_clear.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				log.setText( "" );
				log.setCaretPosition( log.getDocument( ).getLength( ) );
				debugMessages.clear( );
				numLines = 0;
			}
		} );
	}

	public void setClearButtonText( String text )
	{
		this.bu_clear.setText( text );
		this.bu_clear.setToolTipText( text );
	}

	public void setShowupressDebugMessagesModifierText( String text )
	{
		if ( this.showSupressDebugMessagesModifier )
		{
			this.cb_supressDebugMessages.setText( text );
			this.cb_supressDebugMessages.setToolTipText( text );
		}
	}

	public void setSupressDebugMessages( boolean supress )
	{
		this.supressDebugMessages = supress;
		if ( !this.supressDebugMessages )
		{
			for ( String dbg : this.debugMessages )
				this.addDebug( dbg );
			this.debugMessages.clear( );
		}

		/* modify the checkbox-state */
		if ( this.showSupressDebugMessagesModifier )
		{
			this.cb_supressDebugMessages.removeActionListener( this.acl_supressDebugMessages );
			this.cb_supressDebugMessages.setSelected( supress );
			this.cb_supressDebugMessages.addActionListener( this.acl_supressDebugMessages );
		}
	}

	private int coutLines( String str )
	{
		if ( str == null )
			return 0;
		int pos = 0;
		int lines = 0;
		String logTxt = this.log.getText( );
		if ( logTxt == null )
			return 0;
		while ( ( ( pos = logTxt.indexOf( "\n", pos + 1 ) ) >= 0 ) )
			lines++;
		return lines;
	}

	public void setMaxLines( int maxLines )
	{
		this.maxLines = maxLines;
	}

	public void addLine( String line )
	{
		if ( this.numLines + this.coutLines( line ) >= maxLines )
		{
			int removeLines = ( int ) ( maxLines * 0.05 );
			if ( removeLines == 0 )
				removeLines = 1;
			int pos = 0;
			int linesRemoved = 0;
			while ( ( ( pos = this.log.getText( ).indexOf( "\n", pos + 1 ) ) >= 0 ) && linesRemoved < removeLines )
				linesRemoved++;

			String str = this.log.getText( ).substring( pos + 1 );
			this.log.setText( str + line + "\n" );
		}
		else this.log.setText( this.log.getText( ) + line + "\n" );
		this.log.setCaretPosition( this.log.getDocument( ).getLength( ) );

		this.numLines = this.coutLines( this.log.getText( ) );

	}

	public void addDebug( String dbg )
	{
		if ( this.supressDebugMessages )
			this.debugMessages.add( dbg );
		else this.addLine( "DEBUG: " + dbg );
	}

	public void addError( String err )
	{
		this.addLine( "ERROR: " + err );
	}

}
