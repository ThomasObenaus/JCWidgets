/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.buttons;

import java.awt.Color;
import java.awt.image.FilteredImageSource;

import javax.swing.ImageIcon;

import thobe.widgets.filter.AlphaFilter;

/**
 * Class representing an button having an Icon and no text, no margin etc. The buttons disabled and
 * enabled icons will be displayed semi-transparent.
 * @author Thomas Obenaus
 * @source TransparentIconButton.java
 * @date 09.02.2012
 */
@SuppressWarnings ( "serial")
public class TransparentIconButton extends IconButton
{
	/**
	 * Icons ...
	 */
	private ImageIcon	disabledTransparent;
	private ImageIcon	enabledTransparent;
	private ImageIcon	disabledOpaque;
	private ImageIcon	enabledOpaque;

	private Color		borderColor;
	private Color		borderColorTransp;

	/**
	 * Transparent display enabled/ disabled
	 */
	private boolean		opaque;

	/**
	 * The opacity;
	 */
	private float		opacity;

	/**
	 * Ctor see {@link IconButton}
	 * @param enabled
	 * @param pressed
	 */
	public TransparentIconButton( ImageIcon enabled, ImageIcon pressed )
	{
		this( enabled, pressed, null, null, 1f );
	}

	public TransparentIconButton( ImageIcon enabled, ImageIcon pressed, ImageIcon disabled, ImageIcon mouseOver, float opacity )
	{
		super( enabled, pressed, disabled, mouseOver );
		this.opaque = false;
		this.opacity = opacity;

		this.updateBorder( );

		this.filterImage( );

		/* set the icons to transparent */
		this.enabledOpaque = this.enabled;
		this.disabledOpaque = this.disabled;
		this.enabled = this.enabledTransparent;
		this.disabled = this.disabledTransparent;
		this.setIcon( this.enabledTransparent );
		this.setDisabledIcon( this.disabledTransparent );
	}

	/**
	 * Ctor see {@link IconButton}
	 * @param enabled
	 * @param pressed
	 * @param disabled
	 * @param mouseOver
	 */
	public TransparentIconButton( ImageIcon enabled, ImageIcon pressed, ImageIcon disabled, ImageIcon mouseOver )
	{
		this( enabled, pressed, disabled, mouseOver, 1f );
	}

	private void filterImage( )
	{
		AlphaFilter filter = new AlphaFilter( this.opacity );
		FilteredImageSource fis = new FilteredImageSource( this.disabled.getImage( ).getSource( ), filter );
		this.disabledTransparent = new ImageIcon( this.createImage( fis ) );

		fis = new FilteredImageSource( this.enabled.getImage( ).getSource( ), filter );
		this.enabledTransparent = new ImageIcon( this.createImage( fis ) );
	}

	private void updateBorder( )
	{
		this.borderColor = this.getButtonBorder( ).getBorder( );

		this.borderColorTransp = new Color( this.borderColor.getRed( ), this.borderColor.getGreen( ), this.borderColor.getBlue( ), ( int ) Math.round( opacity * this.borderColor.getAlpha( ) ) );

		if ( this.opaque )
			this.getButtonBorder( ).setBorder( this.borderColorTransp );
		else this.getButtonBorder( ).setBorder( this.borderColor );
	}

	/**
	 * Set the opacity value for the enabled and the disabled icon.
	 * @param opacity
	 */
	public void setOpacity( float opacity )
	{
		this.opacity = opacity;
		this.filterImage( );
		this.updateBorder( );
		this.setTransparent( this.isTransparent( ) );
	}

	public float getTransparency( )
	{
		return opacity;
	}

	public boolean isTransparent( )
	{
		return !opaque;
	}

	/**
	 * Enable/ disable transparent display of the button-icons.
	 * @param opaque
	 */
	public void setTransparent( boolean opaque )
	{
		this.opaque = opaque;
		if ( !this.opaque )
		{
			this.enabled = this.enabledTransparent;
			this.disabled = this.disabledTransparent;
			this.setIcon( this.enabledTransparent );
			this.setDisabledIcon( this.disabledTransparent );
		}
		else
		{
			this.enabled = this.enabledOpaque;
			this.disabled = this.disabledOpaque;
			this.setIcon( this.enabledOpaque );
			this.setDisabledIcon( this.disabledOpaque );
		}
	}

}
