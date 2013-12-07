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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import thobe.widgets.splitPanel.SPContent;
import thobe.widgets.splitPanel.SPContentFactory;
import thobe.widgets.splitPanel.SPTreeException;
import thobe.widgets.splitPanel.SplitPanel;

/**
 * @author Thomas Obenaus
 * @source SplitPanelDemo.java
 * @date 13.02.2012
 */
@SuppressWarnings ( "serial")
public class SplitPanelDemo extends JFrame
{

	public SplitPanelDemo( )
	{
		this.addWindowListener( new WindowAdapter( )
		{
			@Override
			public void windowClosing( WindowEvent e )
			{
				System.exit( 0 );
			}
		} );

		this.setSize( 300, 250 );

		try
		{
			MySPContentFactory cf = new MySPContentFactory( );
			SplitPanel sp = new SplitPanel( cf );
			this.setLayout( new BorderLayout( ) );
			this.add( sp );
		}
		catch ( SPTreeException e )
		{
			e.printStackTrace( );
		}

	}

	public static void main( String[] args )
	{
		SplitPanelDemo demo = new SplitPanelDemo( );
		demo.setVisible( true );
	}

	private static int	zz	= 0;

	private class MySPContentFactory implements SPContentFactory
	{
		private final String	DEFAULT_KEY	= "DEFAULT";

		private List<String>	contentKeys;

		public MySPContentFactory( )
		{
			this.contentKeys = new ArrayList<>( );
			this.contentKeys.add( DEFAULT_KEY );
		}

		public SPContent createContentForKey( String key, int frameID )
		{
			if ( key.equals( DEFAULT_KEY ) )
				return this.createDefaultContent( );
			throw new NotImplementedException( );
		}

		private SPContent createDefaultContent( )
		{
			zz++;
			System.out.println( "creating: " + zz );
			JPanel pa_comp = new JPanel( new BorderLayout( ) );
			pa_comp.add( new JLabel( "SP-Demo " + zz ), BorderLayout.NORTH );
			pa_comp.add( new JTextArea( ), BorderLayout.CENTER );
			pa_comp.add( new JButton( "SP-Demo " + zz ), BorderLayout.SOUTH );

			SPContent content = new SPContent( DEFAULT_KEY, pa_comp );
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
			return "MySPContentFactory";
		}

		@Override
		public int getNewFrameID( )
		{
			return 0;
		}

		@Override
		public void freeFrameID( int id )
		{}
	}
}
