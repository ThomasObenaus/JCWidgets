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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import thobe.widgets.multiFramePanel.Frame;
import thobe.widgets.multiFramePanel.FrameComponent;
import thobe.widgets.multiFramePanel.MultiFramePanel;
import thobe.widgets.multiFramePanel.actions.ActionComponent;

/**
 * @author Thomas Obenaus
 * @source Tst_ChageSetFrames.java
 * @date 10.05.2010
 */
@SuppressWarnings ( "serial")
public class Tst_ChageSetFrames extends JFrame
{
	private JComboBox				cb_fr1;
	private JComboBox				cb_fr2;
	private JComboBox				cb_fr3;
	private JComboBox				cb_fr4;

	private MultiFramePanel			container;

	private final MyFrameComponent	myFrameComponents[]	= new MyFrameComponent[]
														{ new MyFrameComponent( "Frame 1" ), new MyFrameComponent( "Frame 2" ), new MyFrameComponent( "Frame 3" ), new MyFrameComponent( "Frame 4" ), new MyFrameComponent( "Frame 5" ) };

	private List<Frame>				frames;

	public Tst_ChageSetFrames( )
	{

		buildGUI( );

		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setSize( new Dimension( 300, 200 ) );

	}

	private void buildGUI( )
	{
		this.setLayout( new BorderLayout( 0, 0 ) );
		this.container = new MultiFramePanel( );
		this.container.setMinFrameCount( 4 );
		this.frames = this.container.getFrames( );

		this.add( this.container, BorderLayout.CENTER );

		JPanel pa_frameSelection = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		this.add( pa_frameSelection, BorderLayout.SOUTH );

		this.cb_fr1 = new JComboBox( myFrameComponents );
		pa_frameSelection.add( this.cb_fr1 );
		this.cb_fr1.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				//				MyFrameComponent mfc = ( ( MyFrameComponent ) cb_fr1.getSelectedItem( ) );
				//				mfc = ( MyFrameComponent ) mfc.getClone( );
				//				mfc.test( );
				//				frames.get( 0 ).setComponent( mfc );

				FrameComponent comp = frames.get( 0 ).setComponent( ( ( MyFrameComponent ) cb_fr1.getSelectedItem( ) ).getClone( ) );
				comp.dispose( );
			}
		} );

		this.cb_fr2 = new JComboBox( myFrameComponents );
		pa_frameSelection.add( this.cb_fr2 );
		this.cb_fr2.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				FrameComponent comp = frames.get( 1 ).setComponent( ( ( MyFrameComponent ) cb_fr2.getSelectedItem( ) ).getClone( ) );
				comp.dispose( );
			}
		} );

		this.cb_fr3 = new JComboBox( myFrameComponents );
		pa_frameSelection.add( this.cb_fr3 );
		this.cb_fr3.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				FrameComponent comp = frames.get( 2 ).setComponent( ( ( MyFrameComponent ) cb_fr3.getSelectedItem( ) ).getClone( ) );
				comp.dispose( );
			}
		} );

		this.cb_fr4 = new JComboBox( myFrameComponents );
		pa_frameSelection.add( this.cb_fr4 );
		this.cb_fr4.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				FrameComponent comp = frames.get( 3 ).setComponent( ( ( MyFrameComponent ) cb_fr4.getSelectedItem( ) ).getClone( ) );
				comp.dispose( );
			}
		} );
	}

	private void setFrameComponentAt( int index, FrameComponent component )
	{
		this.container.setComponent( component, index );
	}

	public static void main( String args[] )
	{
		SwingUtilities.invokeLater( new Runnable( )
		{
			@Override
			public void run( )
			{
				try
				{
					UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName( ) );//"com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel" );
				}
				catch ( Exception e )
				{}

				Tst_ChageSetFrames fr = new Tst_ChageSetFrames( );
				fr.setVisible( true );
			}
		} );
	}

	private class MyFrameComponent extends FrameComponent
	{

		public MyFrameComponent( String title )
		{
			super( title, "Frame [" + title + "]" );
		}

		//		public void test( )
		//		{
		//			this.muList.clear( );
		//
		//			for ( int i = 0; i < 3; i++ )
		//				this.muList.add( new TestMU( ) );
		//			this.buildMenuBar( );
		//		}

		@Override
		public Component getContentComponent( )
		{
			JPanel pa_content = new JPanel( new BorderLayout( ) );

			pa_content.add( new JLabel( this.getTitle( ) ), BorderLayout.NORTH );
			return pa_content;
		}

		@Override
		protected List<ActionComponent> getActionComponents( )
		{
			// TODO Auto-generated method stub
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

		public FrameComponent getClone( )
		{
			return new MyFrameComponent( this.getTitle( ) );
		}

	}

	//	private static int	dbcount	= 0;
	//
	//	private class TestMU extends JMenu
	//	{
	//
	//		public TestMU()
	//		{
	//			super( "test" );
	//			dbcount++;
	//			System.out.println( "Tst_ChageSetFrames.TestMU.TestMU() " + dbcount );
	//
	//		}
	//
	//		@Override
	//		protected void finalize( ) throws Throwable
	//		{
	//			super.finalize( );
	//			dbcount--;
	//			System.out.println( "Tst_ChageSetFrames.TestMU.finalize() " + dbcount );
	//		}
	//	}

}
