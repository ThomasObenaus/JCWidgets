/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import thobe.widgets.editor.Editor;
import thobe.widgets.messagePanel.Message;
import thobe.widgets.messagePanel.MessageCategory;
import thobe.widgets.textfield.RestrictedTextFieldDouble;

/**
 * @author Thomas Obenaus
 * @source Tst_Editor.java
 * @date 12.08.2009
 */
@SuppressWarnings ( "serial")
public class Tst_Editor extends Editor
{
	private JButton						bu_close;
	private JButton						bu_show;
	private JButton						bu_clear;
	private RestrictedTextFieldDouble	rtfd_value;

	private static final Message		m1	= new Message( 1, "Error1", MessageCategory.ERROR );
	private static final Message		m2	= new Message( 2, "warn1", MessageCategory.WARNING );
	private static final Message		m3	= new Message( 3, "Error2", MessageCategory.ERROR );
	private static final Message		m4	= new Message( 4, "info1", MessageCategory.INFO );
	private static final Message		m5	= new Message( 5, "info2", MessageCategory.INFO );
	private static final Message		m6	= new Message( 6, "warn2", MessageCategory.WARNING );
	private static final Message		m7	= new Message( 7, "Error3", MessageCategory.ERROR );

	public Tst_Editor( )
	{
		super( new JFrame( ), "Editor-Test", ModalityType.MODELESS );
		buildGUI( );
	}

	private void buildGUI( )
	{
		this.setLayout( new BorderLayout( 5, 5 ) );

		this.rtfd_value = new RestrictedTextFieldDouble( false );
		this.rtfd_value.setRange( 0d, 10d );
		this.getContentPane( ).add( this.rtfd_value, BorderLayout.NORTH );

		this.setSize( 300, 400 );

	}

	@Override
	protected void handleWindowClosing_( )
	{
		super.handleWindowClosing_( );
		System.exit( 0 );
	}

	private void printHCs( )
	{
		this.getMessagePanel( ).registerMessage( this.getClass( ), m1 );
		this.getMessagePanel( ).registerMessage( this.getClass( ), m2 );
		this.getMessagePanel( ).registerMessage( this.getClass( ), m3 );
		this.getMessagePanel( ).registerMessage( this.getClass( ), m4 );
		this.getMessagePanel( ).registerMessage( this.getClass( ), m5 );
		this.getMessagePanel( ).registerMessage( this.getClass( ), m6 );
		this.getMessagePanel( ).registerMessage( this.getClass( ), m7 );
	}

	public static void main( String args[] )
	{
		SwingUtilities.invokeLater( new Runnable( )
		{
			@Override
			public void run( )
			{
				Tst_Editor fr = new Tst_Editor( );
				fr.setVisible( true );
			}
		} );
	}

	@Override
	protected JPanel createButtonPanel( )
	{
		JPanel pa_buttons = new JPanel( );
		this.bu_close = new JButton( "close" );
		this.bu_close.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				close_dlg( );
			}
		} );
		this.bu_show = new JButton( "show" );
		this.bu_show.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				printHCs( );
			}
		} );

		this.bu_clear = new JButton( "clear" );
		this.bu_clear.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				getMessagePanel( ).clearMessages( Tst_Editor.this.getClass( ) );
			}
		} );

		pa_buttons.add( this.bu_show );

		pa_buttons.add( this.bu_clear );

		pa_buttons.add( this.bu_close );

		return pa_buttons;
	}

	@Override
	public Dimension getMinimumEditorSize( )
	{
		// TODO Auto-generated method stub
		return null;
	}

}
