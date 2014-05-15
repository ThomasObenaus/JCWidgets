/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.filter;

import java.awt.Image;
import java.awt.image.RGBImageFilter;

/**
 * Image filter able to make a given {@link Image} transparent.
 * @author Thomas Obenaus
 * @source AlphaFilter.java
 * @date 09.02.2012
 */
public class AlphaFilter extends RGBImageFilter
{
	private int	mask; 

	/**
	 * Ctor
	 * @param alpha - transparency of the image (values between 0 and 1 are allowed)
	 */
	public AlphaFilter( float alpha )
	{
		if ( alpha < 0 )
			alpha = 0f;
		if ( alpha > 1 )
			alpha = 1f;

		int alphaCh = ( int ) Math.round( 255 * alpha );
		alphaCh = alphaCh & 0xFF;
		alphaCh = alphaCh << 24;
		this.mask = alphaCh | 0xFFFFFF;
	}

	public AlphaFilter( )
	{
		this.mask = 0x80FFFFFF;
	}

	@Override
	public int filterRGB( int x, int y, int rgb )
	{
		return rgb & this.mask;
	}
}
