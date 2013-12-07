/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.decoratorPanel;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A {@link Decorator} indicating a behavior of a panel that is locked since it waits for an long
 * running background-task.
 * @author Thomas Obenaus
 * @source WaitingDecorator.java
 * @date 31.01.2012
 */
@SuppressWarnings ( "serial")
public class WaitingDecorator extends DisableDecorator
{
	private static final int	MIN_ALPHA			= 32;
	/**
	 * Time in milliseconds elapsed between two frames.
	 */
	private static final long	UPDATE_INTERVAL		= 100;
	private static final int	PROGRESSBAR_HEIGHT	= 10;
	private static final int	PROGRESSBAR_OFFSET	= 8;

	private int					frame;
	private Image				frames[];
	/**
	 * Number of spikes that represent the spinning dial.
	 */
	private int					spikeCount;

	/**
	 * Timer necessary for the animation
	 */
	private Timer				timer;

	/**
	 * The {@link Stroke} used to draw the spikes of the spinning dial
	 */
	private Stroke				dialStroke;
	/**
	 * The {@link Color} of the spinning dial and the progressbar
	 */
	private Color				dialColor;

	private static final Stroke	PROGRESSBAR_STROKE	= new BasicStroke( 1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
	private int					x;
	private int					y;

	/**
	 * True: a progress bar will be drawn, False: no progress bar
	 */
	private boolean				withProgressbar;

	/**
	 * Current progress that will be displayed by the progress bar
	 */
	private int					progress;
	/**
	 * Radius of the spinning dial
	 */
	private int					dialRadius;
	/**
	 * Width of the spikes of the spinning dial
	 */
	private float				dialStrokeWidth;

	private boolean				waiting;

	/**
	 * Ctor
	 * @param layer - see {@link Decorator}
	 * @param componentToBeDisabled - the component that should behind the spinning dial and that
	 *            should be disabled (see {@link DisableDecorator})
	 * @param withProgressBar - True: a progress bar will be drawn, False: no progress bar
	 */
	public WaitingDecorator( int layer, Component componentToBeDisabled, boolean withProgressBar )
	{
		this( layer, componentToBeDisabled, null, withProgressBar );
	}

	/**
	 * Ctor
	 * @param layer - see {@link Decorator}
	 * @param componentToBeDisabled - the component that should behind the spinning dial and that
	 *            should be disabled (see {@link DisableDecorator})
	 * @param screenshot - see {@link DisableDecorator}
	 */
	public WaitingDecorator( int layer, Component componentToBeDisabled, Screenshot screenshot )
	{
		this( layer, componentToBeDisabled, screenshot, false );
	}

	/**
	 * Ctor
	 * @param layer - see {@link Decorator}
	 * @param componentToBeDisabled - the component that should behind the spinning dial and that
	 *            should be disabled (see {@link DisableDecorator})
	 * @param screenshot - see {@link DisableDecorator}
	 * @param withProgressBar - True: a progress bar will be drawn, False: no progress bar
	 */
	public WaitingDecorator( int layer, Component componentToBeDisabled, Screenshot screenshot, boolean withProgressBar )
	{
		super( layer, componentToBeDisabled, screenshot );
		this.withProgressbar = withProgressBar;
		this.x = 0;
		this.y = 0;
		this.dialStrokeWidth = 8.0f;
		this.dialRadius = 40;
		this.spikeCount = 20;
		this.dialColor = new Color( 61, 126, 47 );
		this.progress = 0;
		this.reSetUp( );
	}

	/**
	 * Putting the {@link WaitingDecorator} into waiting mode will force the {@link WaitingDecorator} to disable all {@link Component}s
	 * below the {@link WaitingDecorator} and to show the spinning-wheel.
	 * @param waiting - true -> enter waiting-mode, false -> leave waiting-mode
	 */
	public void setWaiting( boolean waiting )
	{
		this.waiting = waiting;

		this.setEnabled( !this.isWaiting( ) );

		if ( !this.isWaiting( ) && this.timer != null )
		{
			this.timer.cancel( );
			this.timer = null;
		}
		else if ( this.isWaiting( ) )
		{
			if ( this.timer != null )
				return;
			this.timer = new Timer( );
			this.timer.schedule( new FramePaintingTimer( ), 0, UPDATE_INTERVAL );
		}
	}

	/**
	 * Returns true if the {@link WaitingDecorator} is in waiting-mode, false otherwise.
	 * @return
	 */
	public boolean isWaiting( )
	{
		return waiting;
	}

	public void setDialRadius( int dialRadius )
	{
		this.dialRadius = dialRadius;
		this.reSetUp( );
	}

	private void reSetUp( )
	{
		if ( this.timer != null )
			this.timer.cancel( );
		this.dialStroke = new BasicStroke( this.getDialStrokeWidth( ), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
		this.x = ( int ) Math.round( this.getWidth( ) / 2.0 ) - this.getDialRadius( );
		this.y = ( int ) Math.round( this.getHeight( ) / 2.0 ) - ( int ) Math.round( this.getDialHeight( ) / 2.0 );
		this.frame = 0;
		this.frames = new Image[this.getFrameCount( )];

		if ( this.isWaiting( ) )
		{
			this.timer = new Timer( );
			this.timer.schedule( new FramePaintingTimer( ), 0, UPDATE_INTERVAL );
		}
	}

	public void setDialColor( Color dialColor )
	{
		this.dialColor = dialColor;
		this.reSetUp( );
	}

	/**
	 * Set the size of the spikes of the spinning dial.
	 * @param dialStrokeWidth
	 */
	public void setDialStrokeWidth( float dialStrokeWidth )
	{
		this.dialStrokeWidth = dialStrokeWidth;
		this.reSetUp( );
	}

	public int getDialRadius( )
	{
		return this.dialRadius;
	}

	private int getDialHeight( )
	{
		return this.getDialRadius( ) * 2 + PROGRESSBAR_OFFSET + PROGRESSBAR_HEIGHT;
	}

	private int getFrame( )
	{
		return this.frame;
	}

	private int getFrameCount( )
	{
		return spikeCount;
	}

	/**
	 * Returns the size of the spikes of the spinning dial.
	 * @return
	 */
	public float getDialStrokeWidth( )
	{
		return dialStrokeWidth;
	}

	/**
	 * Sets the progress (to see a progress bar see {@link WaitingDecorator#setWithProgressbar(boolean)})
	 * @param progress
	 */
	public void setProgress( int progress )
	{
		this.progress = progress;
		if ( this.progress > 100 )
			this.progress = 100;
		if ( this.progress < 0 )
			this.progress = 0;
		this.repaint( );
	}

	public int getProgress( )
	{
		return progress;
	}

	/**
	 * Control the visibility of the progress bar.
	 * @param withProgressbar - True: a progress bar will be drawn, False: no progress bar
	 */
	public void setWithProgressbar( boolean withProgressbar )
	{
		this.withProgressbar = withProgressbar;
	}

	@Override
	protected void boundsUpdate_( int width, int height )
	{
		super.boundsUpdate_( width, height );
		this.x = ( int ) Math.round( this.getWidth( ) / 2.0 ) - this.getDialRadius( );
		this.y = ( int ) Math.round( this.getHeight( ) / 2.0 ) - ( int ) Math.round( this.getDialHeight( ) / 2.0 );
	}

	@Override
	public void paint( Graphics g )
	{
		super.paint( g );
		if ( this.isWaiting( ) )
			paintFrame( this, g, this.x, this.y );
	}

	private void nextFrame( )
	{
		this.frame++;
		if ( this.frame >= this.getFrameCount( ) )
			this.frame = 0;
		this.repaint( );
	}

	@Override
	public String getName( )
	{
		return "WaitingDecorator";
	}

	private void paintFrame( Component c, Graphics graphics, int x, int y )
	{
		int idx = getFrame( );
		if ( frames[idx] == null )
		{
			int w = getDialRadius( ) * 2;
			int h = this.getDialHeight( );

			Image image = c != null ? c.getGraphicsConfiguration( ).createCompatibleImage( w, h, Transparency.TRANSLUCENT ) : new BufferedImage( w, h, BufferedImage.TYPE_INT_ARGB );
			Graphics2D g = ( Graphics2D ) image.getGraphics( );
			g.setComposite( AlphaComposite.Clear );
			g.fillRect( 0, 0, w, h );
			g.setComposite( AlphaComposite.Src );
			g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

			/* draw the progress-bar */
			if ( this.withProgressbar )
				this.paintProgressBar( g );

			final int FULL_SIZE = 256;
			float strokeWidth = getDialStrokeWidth( );
			float fraction = .6f;
			g.setStroke( this.dialStroke );

			/* translate so that 0,0 is the center of the image */
			double midX = this.getDialRadius( );
			double midY = this.getDialRadius( );
			g.translate( midX, midY );
			/* scale the image */
			int size = Math.min( w, h );
			float scale = ( float ) size / FULL_SIZE;
			g.scale( scale, scale );
			int alpha = 255;
			int x1, y1, x2, y2;
			int radius = FULL_SIZE / 2 - 1 - ( int ) ( strokeWidth / 2 );
			int frameCount = getFrameCount( );

			/* draw the wait icon */
			int red = this.dialColor.getRed( );
			int green = this.dialColor.getGreen( );
			int blue = this.dialColor.getBlue( );
			for ( int i = 0; i < frameCount; i++ )
			{
				double cos = Math.cos( Math.PI * 2 - Math.PI * 2 * ( i - idx ) / frameCount );
				double sin = Math.sin( Math.PI * 2 - Math.PI * 2 * ( i - idx ) / frameCount );
				x1 = ( int ) ( radius * fraction * cos );
				x2 = ( int ) ( radius * cos );
				y1 = ( int ) ( radius * fraction * sin );
				y2 = ( int ) ( radius * sin );
				g.setColor( new Color( red, green, blue, Math.min( 255, alpha ) ) );
				g.drawLine( x1, y1, x2, y2 );
				alpha = Math.max( MIN_ALPHA, alpha * 3 / 4 );
			}

			g.dispose( );
			frames[idx] = image;
		}
		graphics.drawImage( frames[idx], x, y, this );

		/* clear the frame after drawing if we use a progress bar. using a progress bar caching is not 
		 * useful since the progress bar changes potentially every frame -> we have to create new a frame*/
		if ( this.withProgressbar )
			frames[idx] = null;
	}

	private void paintProgressBar( Graphics2D g )
	{
		int h = this.getDialHeight( );
		int w = this.getDialRadius( ) * 2 - 2;

		g.setColor( dialColor );
		g.setStroke( PROGRESSBAR_STROKE );

		int progressBarWidth = ( int ) ( ( w / 100.0 ) * this.progress );

		g.fillRect( 1, h - PROGRESSBAR_HEIGHT - 1, progressBarWidth, PROGRESSBAR_HEIGHT );
		g.drawRect( 1, h - PROGRESSBAR_HEIGHT - 1, w, PROGRESSBAR_HEIGHT );
	}

	/**
	 * {@link TimerTask} used for the animation
	 * @author Thomas Obenaus
	 * @date 31.01.2012
	 * @file WaitingDecorator.java
	 */
	private class FramePaintingTimer extends TimerTask
	{
		@Override
		public void run( )
		{
			nextFrame( );
		}
	}
}
