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

import java.util.Map;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

/**
 * @author Thomas Obenaus
 * @source IconLib.java
 * @date Apr 6, 2015
 */
public abstract class IconLib
{
	private Map<IIconType, IconContainer>	icons;
	private Logger							log;

	public IconLib( Logger log )
	{
		this.log = log;
		this.icons = getIconContainers( );
		this.icons.put( IconType.DEFAULT, new IconContainer( LOG( ), "/thobe/widgets/icons/", "default", "png" ) );
	}

	protected abstract Map<IIconType, IconContainer> getIconContainers( );

	public ImageIcon getIcon( IIconType type, boolean enabled, IconSize size )
	{
		ImageIcon result = null;
		IconContainer iCont = icons.get( type );
		if ( iCont != null )
		{
			result = iCont.getIcon( size, enabled );
		}

		// try the default-icon
		if ( result == null )
		{
			iCont = icons.get( IconType.DEFAULT );
			if ( iCont != null )
			{
				result = iCont.getIcon( size, enabled );
			}
		}

		return result;
	}

	protected Logger LOG( )
	{
		return this.log;
	}
}
