/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.buttons;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import testing.icons.IconLib;
import thobe.widgets.action.AbstrAction;
import thobe.widgets.buttons.SmallButton;

/**
 * @author Thomas Obenaus
 * @source Tst_SmallButton.java
 * @date 23.10.2009
 */
@SuppressWarnings ( "serial")
public class Tst_SmallButton extends JFrame
{

	private SmallButton	cbu_cancel;
	private SmallButton	cbu_txt;
	private JCheckBox	cb_enable;
	private JLabel		l_buttonState;

	public Tst_SmallButton( )
	{
		IconLib.get( ).createIcons( );
		buildGUI( );
	}

	private void buildGUI( )
	{
		this.getContentPane( ).setLayout( new BorderLayout( 5, 5 ) );

		AbstrAction action = new AbstrAction( "", "", "", "", null, null )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				l_buttonState.setText( "Pressed at: " + System.currentTimeMillis( ) );
			}

			@Override
			public String getActionKey( )
			{
				return "ACT_TEST_SMALL_BUTTON";
			}

		};
		action.setSelectedIcons( IconLib.testIconSE, IconLib.testIconSD );

		JPanel pa_buttons = new JPanel( );
		this.getContentPane( ).add( pa_buttons, BorderLayout.NORTH );
		this.cbu_cancel = new SmallButton( action );
		pa_buttons.add( this.cbu_cancel );
		this.cbu_cancel.setIcon( IconLib.testIconE );
		this.cbu_cancel.setDisabledIcon( IconLib.testIconD );
		this.cbu_cancel.setPressedIcon( IconLib.testIconD );

		SmallButton bu_withoutBorder = new SmallButton( action );
		pa_buttons.add( bu_withoutBorder );
		bu_withoutBorder.setPaintBorder( true );
		bu_withoutBorder.setIcon( IconLib.testIconE );
		bu_withoutBorder.setDisabledIcon( IconLib.testIconD );
		bu_withoutBorder.setPressedIcon( IconLib.testIconD );

		this.cbu_txt = new SmallButton( "+" );
		pa_buttons.add( this.cbu_txt );
		this.cbu_txt.addActionListener( new ActionListener( )
		{

			@Override
			public void actionPerformed( ActionEvent e )
			{
				if ( cbu_txt.getText( ).trim( ).equals( "+" ) )
					cbu_txt.setText( "-" );
				else cbu_txt.setText( "+" );

			}
		} );

		/* south */
		JPanel pa_south = new JPanel( new BorderLayout( ) );
		this.getContentPane( ).add( pa_south, BorderLayout.SOUTH );
		this.l_buttonState = new JLabel( "" );
		pa_south.add( this.l_buttonState, BorderLayout.CENTER );

		this.cb_enable = new JCheckBox( new AbstrAction( "enable", "disable", null, null, null, null )
		{

			@Override
			public void actionPerformed( ActionEvent e )
			{
				cbu_cancel.setEnabled( cb_enable.isSelected( ) );
			}

			@Override
			public String getActionKey( )
			{
				return "ACT_ENABLE";
			}
		} );
		this.cb_enable.setSelected( true );
		pa_south.add( this.cb_enable, BorderLayout.WEST );

		this.setSize( 300, 400 );
	}

	public static void main( String args[] )
	{
		SwingUtilities.invokeLater( new Runnable( )
		{
			@Override
			public void run( )
			{
				Tst_SmallButton fr = new Tst_SmallButton( );
				fr.setVisible( true );
			}
		} );
	}
}
