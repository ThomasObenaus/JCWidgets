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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
import thobe.widgets.decoratorPanel.WaitingDecorator;
import thobe.widgets.icons_internal.IconLib_Internal;

/**
 * @author Thomas Obenaus
 * @source AnimatedDecoratorDemo.java
 * @date 31.01.2012
 */
@SuppressWarnings ( "serial")
public class WaitingDecoratorDemo extends JFrame
{
	private final Random	rnd	= new Random( );

	public WaitingDecoratorDemo( )
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

		JPanel pa_toOverlay = new JPanel( );
		pa_toOverlay.setLayout( new BorderLayout( ) );
		pa_toOverlay.add( new JLabel( "ein Label" ), BorderLayout.NORTH );
		pa_toOverlay.add( new JTextArea( "Etwas Text .....\n....\nnoch mehr Text...." ), BorderLayout.CENTER );

		MyMenuPane myMP = new MyMenuPane( 3 );
		final WaitingDecorator ad = new WaitingDecorator( 2, pa_toOverlay, true );

		List<Decorator> decorators = new ArrayList<Decorator>( );
		decorators.add( myMP );
		decorators.add( ad );

		try
		{
			DecoratorPanel p = new DecoratorPanel( pa_toOverlay, decorators );
			this.add( p, BorderLayout.CENTER );
			myMP.setComponentsToControl( p, ad );
		}
		catch ( DecoratorPanelException e )
		{
			System.err.println( "Error: " + e.getLocalizedMessage( ) );
		}

		//		p.setEnabled( true );

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

		Timer t = new Timer( );
		t.schedule( new ProgressUpdate( ad ), 200, 200 );
	}

	public static void main( String[] args )
	{
		WaitingDecoratorDemo olpd = new WaitingDecoratorDemo( );
		olpd.setVisible( true );
	}

	private class ProgressUpdate extends TimerTask
	{
		private WaitingDecorator	wd;

		public ProgressUpdate( WaitingDecorator wd )
		{
			this.wd = wd;
		}

		@Override
		public void run( )
		{

			int nextProgress = wd.getProgress( );
			if ( nextProgress == 100 )
				nextProgress = 0;
			nextProgress += rnd.nextInt( 8 ) + 1;

			wd.setProgress( nextProgress );

		}
	}

	private class MyMenuPane extends Decorator
	{
		private final Dimension	dim	= new Dimension( 150, 40 );
		private JComponent		componentToControl1;
		private JComponent		componentToControl2;

		public MyMenuPane( int layer )
		{
			super( layer );
			this.componentToControl1 = null;
			this.componentToControl2 = null;
			this.buildGUI( );
		}

		public void setComponentsToControl( JComponent componentToControl1, JComponent componentToControl2 )
		{
			this.componentToControl1 = componentToControl1;
			this.componentToControl2 = componentToControl2;
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
					if ( componentToControl2 instanceof WaitingDecorator )
					{
						WaitingDecorator wd = ( WaitingDecorator ) componentToControl2;
						wd.setWaiting( !wd.isWaiting( ) );
					}
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
					if ( componentToControl2 instanceof WaitingDecorator )
					{
						WaitingDecorator wd = ( WaitingDecorator ) componentToControl2;
						wd.setDialRadius( rnd.nextInt( 80 ) + 20 );
						Color c = new Color( rnd.nextInt( 255 ), rnd.nextInt( 255 ), rnd.nextInt( 255 ) );
						wd.setDialColor( c );
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
}
