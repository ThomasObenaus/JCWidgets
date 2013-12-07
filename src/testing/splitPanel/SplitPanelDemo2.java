/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.splitPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.xml.stream.XMLStreamException;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import testing.testEnv.TestFrame;
import thobe.widgets.splitPanel.SPContent;
import thobe.widgets.splitPanel.SPContentFactory;
import thobe.widgets.splitPanel.SPSubFrame;
import thobe.widgets.splitPanel.SPSubFrameEvent;
import thobe.widgets.splitPanel.SPTreeException;
import thobe.widgets.splitPanel.SplitPanel;
import thobe.widgets.splitPanel.SplitPanelListener;

/**
 * @author Thomas Obenaus
 * @source SplitPanelDemo2.java
 * @date 14.02.2012
 */
@SuppressWarnings ( "serial")
public class SplitPanelDemo2 extends TestFrame implements SplitPanelListener
{
	private SplitPanel	sp;

	public SplitPanelDemo2( )
	{
		super( );
		this.addWindowListener( new WindowAdapter( )
		{
			@Override
			public void windowClosing( WindowEvent e )
			{
				System.exit( 0 );
			}
		} );

		this.setSize( 300, 250 );

		MySPContentFactory cf = new MySPContentFactory( );
		try
		{
			sp = new SplitPanel( cf );
			sp.addListener( this );
			this.setLayout( new BorderLayout( ) );
			this.add( sp );
		}
		catch ( SPTreeException e1 )
		{
			// TODO Auto-generated catch block
			e1.printStackTrace( );
		}

		JMenuBar mBar = this.getJMenuBar( );
		JMenu mu_xmlStructure = new JMenu( "XML-Structure" );
		mBar.add( mu_xmlStructure );

		JMenuItem mi_writeXML = new JMenuItem( "Write to file" );
		mu_xmlStructure.add( mi_writeXML );
		mi_writeXML.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				writeXML( );
			}
		} );

		JMenuItem mi_loadXML = new JMenuItem( "Load from file" );
		mu_xmlStructure.add( mi_loadXML );
		mi_loadXML.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				loadXML( );
			}
		} );
	}

	private void writeXML( )
	{
		File file = new File( "test.splitPanel" );
		System.out.println( "Writing to " + file.getAbsolutePath( ) + "..." );
		try
		{
			this.sp.asXMLToStream( new FileWriter( file ) );
		}
		catch ( XMLStreamException | IOException e )
		{
			System.err.println( "Error writing xml structure: " + e.getLocalizedMessage( ) );
			return;
		}
		System.out.println( "done" );
	}

	private void loadXML( )
	{
		File file = new File( "test.splitPanel" );
		if ( !file.exists( ) || !file.canRead( ) )
		{
			System.err.println( file.getAbsolutePath( ) + " is not readable" );
			return;
		}
		System.out.println( "Loading from " + file.getAbsolutePath( ) + "..." );

		try
		{
			sp.fromXMLStream( new FileReader( file ) );
		}
		catch ( FileNotFoundException | XMLStreamException | SPTreeException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}

		System.out.println( "done" );
	}

	public static void main( String[] args )
	{

		SplitPanelDemo2 demo = new SplitPanelDemo2( );
		demo.setVisible( true );
	}

	private class MySPContentFactory implements SPContentFactory
	{
		private final String	DEFAULT_KEY	= "DEFAULT";
		private final String	SIMPLE_KEY	= "SIMPLE";
		private List<String>	contentKeys;

		public MySPContentFactory( )
		{
			this.contentKeys = new ArrayList<String>( );
			this.contentKeys.add( DEFAULT_KEY );
			this.contentKeys.add( SIMPLE_KEY );
		}

		public SPContent createContentForKey( String key, int frameID )
		{
			if ( key.equals( DEFAULT_KEY ) )
				return this.createDefaultContent( );
			if ( key.equals( SIMPLE_KEY ) )
				return this.createSimpleContent( );
			throw new NotImplementedException( );
		}

		private SPContent createSimpleContent( )
		{
			JPanel pa_comp = new JPanel( new BorderLayout( ) );
			JEditorPane ta = new JEditorPane( );

			ta.setText( "SIMPLE" );
			pa_comp.add( ta, BorderLayout.CENTER );

			SPContent content = new SPContent( SIMPLE_KEY, pa_comp, new SampleToolbar( ) );
			return content;
		}

		private SPContent createDefaultContent( )
		{
			JPanel pa_comp = new JPanel( new BorderLayout( ) );
			pa_comp.add( new JLabel( "DEFAULT" ), BorderLayout.NORTH );
			JEditorPane ta = new JEditorPane( );
			ta.setText( "" );
			ta.setEditable( true );
			ta.setEnabled( true );

			pa_comp.add( ta, BorderLayout.CENTER );
			JButton bu = new JButton( "Write XML-Structure" );
			bu.addActionListener( new ActionListener( )
			{

				@Override
				public void actionPerformed( ActionEvent e )
				{
					try
					{
						sp.asXMLToStream( System.out );
					}
					catch ( XMLStreamException e1 )
					{
						e1.printStackTrace( );
					}
				}
			} );

			pa_comp.add( bu, BorderLayout.SOUTH );

			SPContent content = new SPContent( DEFAULT_KEY, pa_comp, new SampleToolbar( ) );
			return content;
		}

		@Override
		public String getDefaultContentKey( )
		{
			return DEFAULT_KEY;
		}

		@Override
		public List<String> getContentKeys( )
		{
			return this.contentKeys;
		}

		@Override
		public String getName( )
		{
			return "MyContentFactory";
		}

		@Override
		public int getNewFrameID( )
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void freeFrameID( int id )
		{
			// TODO Auto-generated method stub

		}
	}

	private SPSubFrame	clicked	= null;

	@Override
	public void onSubFrameClicked( SPSubFrameEvent event )
	{
		if ( this.clicked != null )
			this.clicked.getComponentDecorator( ).setBorder( BorderFactory.createLineBorder( Color.gray ) );
		this.clicked = event.getSubFrame( );
		this.clicked.getComponentDecorator( ).setBorder( BorderFactory.createLineBorder( Color.red ) );

		System.out.println( "Clicked at OnScreen: " + event.getPosOnScreen( ) + " on sub-frame:" + event.getPos( ) );
	}

	@Override
	public void onSubFrameEntered( SPSubFrameEvent event )
	{}

	@Override
	public void onSubFrameExited( SPSubFrameEvent event )
	{}

	@Override
	public void onSubFrameRemoved( SPSubFrameEvent event )
	{
		System.out.println( "SplitPanelDemo2.onSubFrameRemoved()" );
	}

	@Override
	public void onSubFrameContentSwitched( SPSubFrameEvent event )
	{
		System.out.println( "SplitPanelDemo2.onContentSwitched()" );
	}
}
