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
 * You have to sublcass this one to have a {@link IconLib} for your application.
 * All you have to do is to implement {@link IconLib#getIconContainers()} to register all {@link IconContainer}s your application needs.
 * @author Thomas Obenaus
 * @source IconLib.java
 * @date Apr 6, 2015
 */
public abstract class IconLib
{
	/**
	 * The registered {@link IconContainer}s
	 */
	private Map<IIconType, IconContainer>	icons;

	/**
	 * The {@link Logger} that should be used.
	 */
	private Logger							log;

	/**
	 * Ctro
	 * @param log - the {@link Logger} that should be used.
	 */
	public IconLib( Logger log )
	{
		this.log = log;
		this.icons = getIconContainers( );
		this.icons.put( IconType.DEFAULT, new IconContainer( LOG( ), IconLib.class.getResource( "/thobe/widgets/icons/" ), "default", "png" ) );
	}

	/**
	 * You have to implement this method to register the {@link IconContainer} your application needs.
	 * e.g.:<br>
	 * <code><br>
	 * @Override<br>
	 *               protected Map<IIconType, IconContainer> getIconContainers( )<br>
	 *               {<br>
	 *               Map<IIconType, IconContainer> icons = new HashMap<>( );<br>
	 *               icons.put( LFV_IconType.EXPAND, new IconContainer( LOG( ), "/thobe/logfileviewer/gui/icons/",
	 *               "expand", "png" ) );<br>
	 *               icons.put( LFV_IconType.COLLAPSE, new IconContainer( LOG( ), "/thobe/logfileviewer/gui/icons/",
	 *               "collapse", "png" )
	 *               );<br>
	 *               return icons;<br>
	 *               } </code>
	 * @return
	 */
	protected abstract Map<IIconType, IconContainer> getIconContainers( );

	/**
	 * Retrieves an {@link ImageIcon} from the {@link IconLib}. If the icon is missing a default icon will be returned. If even this is
	 * missing null is returned.
	 * @param type - the type of the icon. To define your own types just create an enum that implements {@link IIconType}. E.g.:<br>
	 *            <code>
	 * public enum LFV_IconType implements IIconType<br>
	 * {<br>
	 * 	EXPAND, COLLAPSE, SETTINGS;<br>
	 * }<br>
	 * </code>
	 * @param enabled
	 * @param size
	 * @return
	 */
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
