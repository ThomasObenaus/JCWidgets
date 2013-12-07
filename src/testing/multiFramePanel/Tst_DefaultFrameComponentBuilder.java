/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.multiFramePanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import thobe.widgets.action.AbstrAction;
import thobe.widgets.multiFramePanel.DefaultFrameComponentBuilder;
import thobe.widgets.multiFramePanel.FrameComponent;
import thobe.widgets.multiFramePanel.MultiFramePanel;
import thobe.widgets.multiFramePanel.actions.Action;
import thobe.widgets.multiFramePanel.actions.ActionComponent;
import thobe.widgets.multiFramePanel.actions.ActionGroup;

/**
 * @author Thomas Obenaus
 * @source Tst_DefaultFrameComponentBuilder.java
 * @date 10.05.2010
 */
@SuppressWarnings ( "serial")
public class Tst_DefaultFrameComponentBuilder extends JFrame
{
	private MultiFramePanel	container;

	public Tst_DefaultFrameComponentBuilder( )
	{

		buildGUI( );

		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setSize( new Dimension( 300, 200 ) );

	}

	private void buildGUI( )
	{
		this.setLayout( new BorderLayout( 0, 0 ) );
		this.container = new MultiFramePanel( );
		this.add( this.container, BorderLayout.CENTER );

		JButton bu_chlayout = new JButton( "ch" );
		this.add( bu_chlayout, BorderLayout.WEST );
		bu_chlayout.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				container.setDefaultFrameComponentBuilder( new MyDefaultFrameComponentBuilder( ) );
			}
		} );
	}

	public static void main( String args[] )
	{
		SwingUtilities.invokeLater( new Runnable( )
		{
			@Override
			public void run( )
			{
				Tst_DefaultFrameComponentBuilder fr = new Tst_DefaultFrameComponentBuilder( );
				fr.setVisible( true );
			}
		} );
	}

	private class MyDefaultFrameComponent extends FrameComponent
	{

		public MyDefaultFrameComponent( String title )
		{
			super( title, "Test Defautlcomponent" );
		}

		@Override
		public Component getContentComponent( )
		{
			JPanel pa_content = new JPanel( new BorderLayout( ) );

			pa_content.add( new JLabel( this.getTitle( ) ) );

			return pa_content;
		}

		@Override
		protected List<ActionComponent> getActionComponents( )
		{
			List<ActionComponent> result = new ArrayList<ActionComponent>( );
			ActionGroup agr = new ActionGroup( "a menu-entry", "TT of the menu-entry" );
			result.add( agr );
			Action act = new Action( new AbstrAction( "menu action" )
			{

				@Override
				public void actionPerformed( ActionEvent e )
				{
					System.out.println( "Tst_DiableManuBar.MyDefaultFrameComponent.getActionComponents().new AbstrAction() {...}.actionPerformed()" );
				}

				@Override
				public String getActionKey( )
				{
					return "ACT_TEST";
				}
			} );
			agr.addChild( act );

			return result;
		}

		@Override
		public int getMinHeight( )
		{
			return 50;
		}

		@Override
		public int getMinWidth( )
		{
			return 50;
		}

	}

	private class MyDefaultFrameComponentBuilder implements DefaultFrameComponentBuilder
	{
		private int	numFrames;

		public MyDefaultFrameComponentBuilder( )
		{
			this.numFrames = 0;
		}

		@Override
		public FrameComponent createDefaultFrameComponent( )
		{
			MyDefaultFrameComponent result = new MyDefaultFrameComponent( "test default comp-builder " + this.numFrames );
			this.numFrames++;
			return result;
		}

	}
}
