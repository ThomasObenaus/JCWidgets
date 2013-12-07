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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import thobe.widgets.multiFramePanel.FrameComponent;
import thobe.widgets.multiFramePanel.MultiFramePanel;
import thobe.widgets.multiFramePanel.actions.ActionComponent;

/**
 * @author Thomas Obenaus
 * @source Tst_MPFUpdateTitle.java
 * @date 02.06.2010
 */
@SuppressWarnings ( "serial")
public class Tst_MPFUpdateTitle extends JFrame
{
	private MultiFramePanel	container;
	private List<TestComp>	components;

	public Tst_MPFUpdateTitle( )
	{
		this.components = new ArrayList<TestComp>( );

		buildGUI( );

		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setSize( new Dimension( 300, 200 ) );

	}

	private void buildGUI( )
	{
		this.setLayout( new BorderLayout( 0, 0 ) );
		this.container = new MultiFramePanel( );

		for ( int i = 0; i < 4; i++ )
		{
			TestComp fc = new TestComp( "" );
			this.container.setComponent( fc, i );
			this.components.add( fc );
		}

		this.add( this.container, BorderLayout.CENTER );

		JTextField tf_title = new JTextField( );
		this.add( tf_title, BorderLayout.SOUTH );
		tf_title.addActionListener( new ActionListener( )
		{

			@Override
			public void actionPerformed( ActionEvent e )
			{
				JTextField tf = ( JTextField ) e.getSource( );
				String txt = tf.getText( );
				for ( TestComp comp : components )
				{
					comp.setTitle( txt );
				}

				//container.update( );
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
				Tst_MPFUpdateTitle fr = new Tst_MPFUpdateTitle( );
				fr.setVisible( true );
			}
		} );
	}

	private class TestComp extends FrameComponent
	{

		public TestComp( String title )
		{
			super( title, "TT " + title );
		}

		@Override
		public Component getContentComponent( )
		{
			return new JPanel( );
		}

		@Override
		protected List<ActionComponent> getActionComponents( )
		{
			return null;
		}

		@Override
		public int getMinHeight( )
		{
			return 20;
		}

		@Override
		public int getMinWidth( )
		{
			return 20;
		}

	}

}
