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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import thobe.widgets.buttons.SmallButton;
import thobe.widgets.decoratorPanel.Decorator;
import thobe.widgets.decoratorPanel.DecoratorPanelException;
import thobe.widgets.decoratorPanel.ProgressPanel;
import thobe.widgets.decoratorPanel.Screenshot;
import thobe.widgets.icons.IconLib;

/**
 * @author Thomas Obenaus
 * @source BlockRepaintDemo.java
 * @date 06.02.2012
 */
@SuppressWarnings ( "serial")
public class BlockRepaintDemo extends JFrame
{
	private final Random	rnd	= new Random( );

	public BlockRepaintDemo( )
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

		MyPanel pa_toOverlay = new MyPanel( );
		pa_toOverlay.setLayout( new BorderLayout( ) );
		pa_toOverlay.add( new JLabel( "ein Label" ), BorderLayout.NORTH );
		pa_toOverlay.add( new JTextArea( "Etwas Text .....\n....\nnoch mehr Text...." ), BorderLayout.CENTER );

		MyMenuPane myMP = new MyMenuPane( 3 );
		try
		{
			ProgressPanel pp = new ProgressPanel( pa_toOverlay, true );//,new ScreenShotServer( "lilies.jpg" ));
			pp.setBlockRepaintOfDisabledComponent( true );
			pp.addDecorator( myMP );
			this.add( pp, BorderLayout.CENTER );
			myMP.setComponentToControl( pp );

			JButton bu_out = new JButton( "out" );
			bu_out.addActionListener( new ActionListener( )
			{
				@Override
				public void actionPerformed( ActionEvent e )
				{
					System.out.println( "BlockRepaintDemo.buildGUI().new ActionListener() {...}.actionPerformed()" );
				}
			} );
			pa_toOverlay.add( bu_out, BorderLayout.SOUTH );

			Timer t = new Timer( );
			t.schedule( new ProgressUpdate( pp ), 200, 200 );
		}
		catch ( DecoratorPanelException e )
		{
			System.err.println( "Error: " + e.getLocalizedMessage( ) );
		}
	}

	public static void main( String[] args )
	{
		BlockRepaintDemo olpd = new BlockRepaintDemo( );
		olpd.setVisible( true );
	}

	private class MyPanel extends JPanel
	{
		private long	repaints;

		public MyPanel( )
		{
			this.repaints = 0;
		}

		@Override
		public void paint( Graphics g )
		{
			super.paint( g );
			this.repaints++;
			System.out.println( "Component to be decorated: Repaint #" + this.repaints );
		}
	}

	private class ProgressUpdate extends TimerTask
	{
		private ProgressPanel	pPanel;

		public ProgressUpdate( ProgressPanel pPanel )
		{
			this.pPanel = pPanel;
		}

		@Override
		public void run( )
		{

			int nextProgress = pPanel.getProgress( );
			if ( nextProgress == 100 )
				nextProgress = 0;
			nextProgress += rnd.nextInt( 8 ) + 1;

			pPanel.setProgress( nextProgress );

		}
	}

	private class MyMenuPane extends Decorator
	{
		private final Dimension	dim	= new Dimension( 150, 40 );
		private JComponent		componentToControl1;

		public MyMenuPane( int layer )
		{
			super( layer );
			this.componentToControl1 = null;
			this.buildGUI( );
		}

		public void setComponentToControl( JComponent componentToControl1 )
		{
			this.componentToControl1 = componentToControl1;
		}

		private void buildGUI( )
		{
			this.setLayout( new FlowLayout( FlowLayout.RIGHT ) );

			SmallButton bu_enable = new SmallButton( IconLib.get( ).getMinimizeE( ) );
			bu_enable.setDisabledIcon( IconLib.get( ).getMinimizeE( ) );
			bu_enable.setToolTipText( "Dis-/ enable the panel" );
			this.add( bu_enable );
			bu_enable.addActionListener( new ActionListener( )
			{
				@Override
				public void actionPerformed( ActionEvent e )
				{
					componentToControl1.setEnabled( !componentToControl1.isEnabled( ) );
				}
			} );

			SmallButton bu_close = new SmallButton( IconLib.get( ).getHelpE( ) );
			bu_close.setToolTipText( "Close" );
			this.add( bu_close );
			bu_close.addActionListener( new ActionListener( )
			{
				@Override
				public void actionPerformed( ActionEvent e )
				{
					if ( componentToControl1 instanceof ProgressPanel )
					{
						ProgressPanel ppanel = ( ProgressPanel ) componentToControl1;
						ppanel.setDialRadius( rnd.nextInt( 80 ) + 20 );
						Color c = new Color( rnd.nextInt( 255 ), rnd.nextInt( 255 ), rnd.nextInt( 255 ) );
						ppanel.setDialColor( c );
					}
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
