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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.FilteredImageSource;

import javax.swing.GrayFilter;
import javax.swing.SwingUtilities;

/**
 * A {@link Decorator} that decorates a component in a manner as it will be if the component is
 * disabled.
 * @author Thomas Obenaus
 * @source GlassPane.java
 * @date 27.01.2012
 */
@SuppressWarnings ( "serial")
public class DisableDecorator extends Decorator implements KeyEventDispatcher
{
	/**
	 * An instance that is able to offer an screenshot. This screenshot will be displayed on top of
	 * the component that should be disabled. So this image will fully overlay that component (not
	 * visible any more). This mechanism of using a screenshot to gray out the component that should
	 * be disabled is only necessary if its a heavy-weight component since heavy- and light-weight
	 * components can't mix its painting operations in a manner to provide transparency. So to see
	 * the component and to be able to see it grayed out (disabled) wee need a screenshot of this
	 * component (if we can't overlay it with an other half transparent component). This parameter
	 * can be null if no screenshot is necessary.
	 */
	private Screenshot			screenshot;

	/**
	 * The component that should be disabled.
	 */
	private Component			componentToBeDisabled;

	/**
	 * The screenshot that was severed by the {@link Screenshot} instance. Might be null.
	 */
	private Image				image;
	private GrayFilter			grayFilter;

	/**
	 * The text that will be displayed on top of the disabled component.
	 */
	private String				text;
	private static final Font	font		= new Font( "Arial", Font.BOLD, 20 );
	private static final Color	textColor	= new Color( 100, 100, 100, 250 );

	/**
	 * Ctor
	 * @param layer - see {@link Decorator}
	 * @param componentToBeDisabled - the component that should be disabled
	 * @param screenshot - An instance that is able to offer an screenshot. This screenshot will be
	 *            displayed on top of the component that should be disabled. So this image will fully
	 *            overlay that component (not visible any more). This mechanism of using a screenshot to
	 *            gray out the component that should be disabled is only necessary if its a heavy-weight
	 *            component since heavy- and light-weight components can't mix its painting operations
	 *            in a manner to provide transparency. So to see the component and to be able to see it
	 *            grayed out (disabled) wee need a screenshot of this component (if we can't overlay it
	 *            with an other half transparent component). This parameter can be null if no screenshot
	 *            is necessary.
	 */
	public DisableDecorator( int layer, Component componentToBeDisabled )
	{
		this( layer, componentToBeDisabled, null );
	}

	/**
	 * Ctor
	 * @param layer - see {@link Decorator}
	 * @param componentToBeDisabled - the component that should be disabled
	 * @param screenshot - An instance that is able to offer an screenshot. This screenshot will be
	 *            displayed on top of the component that should be disabled. So this image will fully
	 *            overlay that component (not visible any more). This mechanism of using a screenshot to
	 *            gray out the component that should be disabled is only necessary if its a heavy-weight
	 *            component since heavy- and light-weight components can't mix its painting operations
	 *            in a manner to provide transparency. So to see the component and to be able to see it
	 *            grayed out (disabled) wee need a screenshot of this component (if we can't overlay it
	 *            with an other half transparent component). This parameter can be null if no screenshot
	 *            is necessary.
	 */
	public DisableDecorator( int layer, Component componentToBeDisabled, Screenshot screenshot )
	{
		super( layer );
		this.text = null;
		this.componentToBeDisabled = componentToBeDisabled;
		this.screenshot = screenshot;
		this.image = null;
		this.grayFilter = new GrayFilter( true, 80 );
		this.setVisible( false );

		/* add listeners to capture mouse-events */
		KeyboardFocusManager.getCurrentKeyboardFocusManager( ).addKeyEventDispatcher( this );
		this.addMouseListener( new MouseAdapter( )
		{} );
		this.addMouseMotionListener( new MouseMotionAdapter( )
		{} );
		this.addMouseWheelListener( new MouseWheelListener( )
		{
			@Override
			public void mouseWheelMoved( MouseWheelEvent e )
			{}
		} );
	}

	public void setGrayPercentage( int grayPercentage )
	{
		this.grayFilter = new GrayFilter( true, grayPercentage );
	}

	/**
	 * Set a text that should be displayed on top of the disabled component.
	 * @param text
	 */
	public void setText( String text )
	{
		this.text = text;
	}

	public String getText( )
	{
		return text;
	}

	@Override
	public void setEnabled( boolean enabled )
	{
		super.setEnabled( enabled );
		if ( this.isEnabled( ) )
			this.image = null;

		this.setVisible( !enabled );

	}

	/**
	 * Request the screenshot from the {@link Screenshot} instance (if it is available). This image
	 * will be modified by a {@link GrayFilter} to look like a disabled version of the screenshot.
	 */
	private void prepareScreenshot( )
	{
		if ( this.screenshot != null && this.image == null && !this.isEnabled( ) )
		{
			this.image = this.screenshot.getScreenshot( );
			if ( this.image == null )
				return;

			FilteredImageSource fis = new FilteredImageSource( this.image.getSource( ), this.grayFilter );
			this.image = this.createImage( fis );
		}
	}

	@Override
	public void paint( Graphics g )
	{
		if ( this.isEnabled( ) )
			return;
		Graphics g2 = g.create( );
		this.prepareScreenshot( );
		if ( this.image != null )
			g2.drawImage( this.image, 0, 0, this.componentToBeDisabled.getWidth( ), this.componentToBeDisabled.getHeight( ), this );
		else
		{
			/* gray out the component */
			Color bg = this.componentToBeDisabled.getBackground( );
			if ( bg == null )
				bg = Color.LIGHT_GRAY;
			Color c = new Color( bg.getRed( ), bg.getGreen( ), bg.getBlue( ), 128 );
			Rectangle r = this.componentToBeDisabled.getBounds( );
			g2.setColor( c );
			g2.fillRect( r.x, r.y, r.width, r.height );
		}

		/* Draw the disabled text on screen */
		if ( this.text != null )
		{
			g2.setColor( DisableDecorator.textColor );
			g2.setFont( DisableDecorator.font );
			FontMetrics fm = g2.getFontMetrics( );
			int strWidth = fm.stringWidth( this.text );
			int strHeight = fm.getHeight( );
			int x = ( int ) Math.round( ( this.componentToBeDisabled.getWidth( ) / 2.0 ) - ( strWidth / 2.0 ) );
			int y = ( int ) Math.round( ( this.componentToBeDisabled.getHeight( ) / 2.0 ) + ( strHeight / 2.0 ) );
			g2.drawChars( this.text.toCharArray( ), 0, this.text.length( ), x, y );
		}
		g2.dispose( );
	}

	protected void finalize( ) throws Throwable
	{
		KeyboardFocusManager.getCurrentKeyboardFocusManager( ).removeKeyEventDispatcher( this );
	}

	@Override
	public boolean dispatchKeyEvent( KeyEvent e )
	{
		if ( this.isEnabled( ) )
			return false;
		return SwingUtilities.isDescendingFrom( e.getComponent( ), this.componentToBeDisabled );
	}

	@Override
	protected void boundsUpdate_( int width, int height )
	{
		this.setBounds( 0, 0, width, height );
	}

	@Override
	public String getName( )
	{
		return "DisableDecorator";
	}
}
