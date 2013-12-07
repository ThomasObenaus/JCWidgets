/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.floatingPanel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import thobe.widgets.action.AbstrAction;
import thobe.widgets.buttons.SmallButton;
import thobe.widgets.floatingPanel.ExpandableFloatingPanel;
import thobe.widgets.floatingPanel.FloatingPanelContainer;
import thobe.widgets.icons.IconLib;

/**
 * @author Thomas Obenaus
 * @source TestExpandableFloatingPanel.java
 * @date 01.09.2009
 */
@SuppressWarnings ( "serial")
public class TestExpandableFloatingPanel extends ExpandableFloatingPanel
{
	private JTable					ta_test;
	private FloatingPanelContainer	container;
	private AbstrAction				aa;

	public TestExpandableFloatingPanel( String title, FloatingPanelContainer container )
	{
		super( title, false );
		this.container = container;
		this.buildGUI( );
		this.setPanelSize( 300, 200 );
		this.setRestrictShrinking( true, 200, 200 );
	}

	private void buildGUI( )
	{
		this.setIconSet( IconLib.get( ).getCollapsed_E( ), IconLib.get( ).getCollapsed_EO( ), IconLib.get( ).getCollapsed_P( ), IconLib.get( ).getExpanded_E( ), IconLib.get( ).getExpanded_EO( ), IconLib.get( ).getExpanded_P( ) );

		this.setLayout( new BorderLayout( ) );
		this.add( new JLabel( "lskdjflksdjf" ), BorderLayout.WEST );
		ImageIcon iiE = new ImageIcon( getClass( ).getResource( "/widgets/icons/cancelButton.png" ) );
		ImageIcon iiD = new ImageIcon( getClass( ).getResource( "/widgets/icons/cancelButton_pressed.png" ) );

		aa = new AbstrAction( null, null, null, null, iiE, iiD )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{}

			@Override
			public String getActionKey( )
			{
				// TODO Auto-generated method stub
				return null;
			}
		};

		SmallButton bu_remove = new SmallButton( aa );

		this.add( bu_remove, BorderLayout.SOUTH );
		this.ta_test = new JTable( new DefaultTableModel( new String[][]
		{
		{ "1", "2" },
		{ "1", "2" },

		{ "3", "4" } }, new String[]
		{ "A", "b" } ) );
		this.ta_test.putClientProperty( "terminateEditOnFocusLost", Boolean.TRUE );

		JScrollPane sp = new JScrollPane( ta_test );
		this.add( sp, BorderLayout.CENTER );
		this.add( ta_test.getTableHeader( ), BorderLayout.NORTH );
		JButton bu_switch = new JButton( "sw" );
		bu_switch.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				aa.setEnabled( !aa.isEnabled( ) );
			}
		} );
		this.add( bu_switch, BorderLayout.EAST );
	}

	@Override
	public JPanel getAdditionalHeaderPanel( )
	{
		JPanel pa_additionalHeaderPanel = new JPanel( new BorderLayout( ) );
		pa_additionalHeaderPanel.add( new JComboBox( new String[]
		{ "Edit", "View" } ), BorderLayout.EAST );

		return pa_additionalHeaderPanel;
	}
}
