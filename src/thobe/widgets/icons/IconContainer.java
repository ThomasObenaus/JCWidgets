/*
 *  Copyright (C) 2014, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    LogFileViewer
 */

package thobe.widgets.icons;

import java.awt.MediaTracker;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

/**
 * A container for the different states of an icon (enabled/disabled, different sizes, ...)
 * @author Thomas Obenaus
 * @source IconContainer.java
 * @date Apr 6, 2015
 */
public class IconContainer
{
	private final static String			STR_ENABLED		= "E";
	private final static String			STR_DISABLED	= "D";

	private String						iconName;
	private Map<IconSize, ImageIcon>	enabledIcons;
	private Map<IconSize, ImageIcon>	disabledIcons;
	private URL							resourcePath;
	private String						iconExtension;
	private Logger						log;

	/**
	 * @param log
	 * @param resourcePath
	 * @param iconName
	 * @param iconExtension
	 */
	public IconContainer( Logger log, URL resourcePath, String iconName, String iconExtension )
	{
		this.log = log;
		this.resourcePath = resourcePath;
		this.iconName = iconName;
		this.iconExtension = iconExtension;

		if ( !this.iconExtension.startsWith( "." ) )
		{
			this.iconExtension = "." + this.iconExtension;
		}

		this.enabledIcons = new HashMap<>( );
		this.disabledIcons = new HashMap<>( );
	}

	public ImageIcon getIcon( IconSize size, boolean enabled )
	{
		ImageIcon result = null;

		String iconPathStr = resourcePath + this.iconName + "_" + ( enabled ? STR_ENABLED : STR_DISABLED ) + "_" + size + iconExtension;
		boolean imageLoaded = true;
		try
		{
			URL iconPath = new URL( iconPathStr );

			if ( enabled )
			{
				result = this.enabledIcons.get( size );
				if ( result == null )
				{
					result = new ImageIcon( iconPath );
					if ( ( result != null ) && ( result.getImage( ) != null ) && ( result.getImageLoadStatus( ) == MediaTracker.COMPLETE ) )
					{
						this.enabledIcons.put( size, result );
					}
					else
					{
						imageLoaded = false;
					}
				}
			}
			else
			{
				result = this.disabledIcons.get( size );
				if ( result == null )
				{
					result = new ImageIcon( iconPath );
					if ( ( result != null ) && ( result.getImage( ) != null ) && ( result.getImageLoadStatus( ) == MediaTracker.COMPLETE ) )
					{
						this.disabledIcons.put( size, result );
					}
					else
					{
						imageLoaded = false;
					}
				}
			}
		}
		catch ( Exception e )
		{
			LOG( ).warning( "Unable to load icon '" + iconPathStr + "': " + e.getLocalizedMessage( ) );
		}

		if ( !imageLoaded )
		{
			LOG( ).warning( "Unable to load icon '" + iconPathStr + "' (ImageIcon=" + ( result != null ? "OK" : "NULL" ) + ", image=" + ( result != null ? ( result.getImage( ) != null ? "OK" : "NULL" ) : "N/A" ) + ", imageLoadStatus=" + ( result != null ? result.getImageLoadStatus( ) : "N/A" ) + ")" );
			result = null;
		}

		return result;
	}

	protected Logger LOG( )
	{
		return this.log;
	}

}
