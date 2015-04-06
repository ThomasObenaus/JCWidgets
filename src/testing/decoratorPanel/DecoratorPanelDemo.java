/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package testing.decoratorPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import thobe.widgets.buttons.SmallButton;
import thobe.widgets.decoratorPanel.Decorator;
import thobe.widgets.decoratorPanel.DecoratorPanel;
import thobe.widgets.decoratorPanel.DecoratorPanelException;
import thobe.widgets.decoratorPanel.DisableDecorator;
import thobe.widgets.decoratorPanel.Screenshot;
import thobe.widgets.icons_internal.IconLib_Internal;

/**
 * @author Thomas Obenaus
 * @source OverlayPanelDemo.java
 * @date 27.01.2012
 */
@SuppressWarnings ( "serial")
public class DecoratorPanelDemo extends JFrame
{
	public DecoratorPanelDemo( )
	{
		this.buildGUI( );
		this.setSize( 300, 250 );
		this.addWindowListener( new WindowAdapter( )
		{
			@Override
			public void windowClosing( WindowEvent e )
			{
				System.exit( 0 );
			}
		} );
	}

	private void buildGUI( )
	{
		this.setLayout( new BorderLayout( ) );

		JPanel pa_toOverlay = new JPanel( new BorderLayout( ) );
		pa_toOverlay.add( new JLabel( "ein Label" ), BorderLayout.NORTH );
		pa_toOverlay.add( new JTextArea( "Etwas Text .....\n....\nnoch mehr Text...." ), BorderLayout.CENTER );

		MyMenuPane myMP = new MyMenuPane( 2 );
		DisableDecorator dd = new DisableDecorator( 1, pa_toOverlay, new ScreenShotServer( "lilies.jpg" ) );

		List<Decorator> decorators = new ArrayList<Decorator>( );
		decorators.add( myMP );
		decorators.add( dd );
		try
		{
			DecoratorPanel p = new DecoratorPanel( pa_toOverlay, decorators );
			this.add( p, BorderLayout.CENTER );
			myMP.setComponentToControl( p );
			dd.setText( "Disabled" );
		}
		catch ( DecoratorPanelException e )
		{
			System.err.println( "Error: " + e.getLocalizedMessage( ) );
		}

		//		p.setEnabled( false );

		JButton bu_out = new JButton( "out" );
		bu_out.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				System.out.println( "OverlayPanelDemo.buildGUI().new ActionListener() {...}.actionPerformed()" );
			}
		} );
		pa_toOverlay.add( bu_out, BorderLayout.SOUTH );
	}

	public static void main( String[] args )
	{
		DecoratorPanelDemo olpd = new DecoratorPanelDemo( );
		olpd.setVisible( true );
	}

	private class MyMenuPane extends Decorator
	{
		private final Dimension	dim	= new Dimension( 150, 40 );
		private JComponent		componentToControl;

		public MyMenuPane( int layer )
		{
			super( layer );
			this.componentToControl = null;
			this.buildGUI( );
		}

		public void setComponentToControl( JComponent componentToControl )
		{
			this.componentToControl = componentToControl;
		}

		private void buildGUI( )
		{
			this.setLayout( new FlowLayout( FlowLayout.RIGHT ) );

			SmallButton bu_enable = new SmallButton( IconLib_Internal.get( ).getMinimizeE( ) );
			bu_enable.setDisabledIcon( IconLib_Internal.get( ).getMinimizeE( ) );
			bu_enable.setToolTipText( "Dis-/ enable the panel" );
			this.add( bu_enable );
			bu_enable.addActionListener( new ActionListener( )
			{
				@Override
				public void actionPerformed( ActionEvent e )
				{
					componentToControl.setEnabled( !componentToControl.isEnabled( ) );
				}
			} );

			SmallButton bu_close = new SmallButton( IconLib_Internal.get( ).getHelpE( ) );
			bu_close.setToolTipText( "Close" );
			this.add( bu_close );
			bu_close.addActionListener( new ActionListener( )
			{
				@Override
				public void actionPerformed( ActionEvent e )
				{
					System.exit( 0 );
				}
			} );
		}

		@Override
		public void boundsUpdate_( int width, int height )
		{
			this.setBounds( width - ( int ) dim.getWidth( ), 0, ( int ) dim.getWidth( ), ( int ) dim.getHeight( ) );

		}

		@Override
		public String getName( )
		{
			return "MenuPane";
		}

	}

	private class ScreenShotServer implements Screenshot
	{
		private Image	image;

		public ScreenShotServer( String filename )
		{
			this.image = null;
			try
			{
				this.image = ImageIO.read( new File( filename ) );
			}
			catch ( IOException e )
			{
				System.err.println( "Error: " + e.getLocalizedMessage( ) );
			}
		}

		@Override
		public Image getScreenshot( )
		{
			return this.image;
		}

	}
}
