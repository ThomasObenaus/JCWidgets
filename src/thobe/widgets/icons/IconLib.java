/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.icons;

import javax.swing.ImageIcon;

/**
 * @author Thomas Obenaus
 * @source IconLib.java
 * @date 14.04.2010
 */
public class IconLib
{
	private static IconLib		instance	= null;

	private static ImageIcon	helpE;
	private static ImageIcon	helpD;

	private static ImageIcon	maximizeE;
	private static ImageIcon	maximizeD;
	private static ImageIcon	maximizeSelE;

	private static ImageIcon	minimizeE;
	private static ImageIcon	minimizeD;
	private static ImageIcon	minimizeSelE;
	private static ImageIcon	errorIcon;
	private static ImageIcon	errorIconMid;
	private static ImageIcon	infoIcon;
	private static ImageIcon	warnIcon;

	private static ImageIcon	spliPanel_minD;
	private static ImageIcon	spliPanel_minE;
	private static ImageIcon	spliPanel_minEO;
	private static ImageIcon	spliPanel_minEP;

	private static ImageIcon	spliPanel_maxD;
	private static ImageIcon	spliPanel_maxE;
	private static ImageIcon	spliPanel_maxEO;
	private static ImageIcon	spliPanel_maxEP;

	private static ImageIcon	spliPanel_closeD;
	private static ImageIcon	spliPanel_closeE;
	private static ImageIcon	spliPanel_closeEO;
	private static ImageIcon	spliPanel_closeEP;

	private static ImageIcon	spliPanel_lockD;
	private static ImageIcon	spliPanel_lockE;
	private static ImageIcon	spliPanel_lockEO;
	private static ImageIcon	spliPanel_lockEP;

	private static ImageIcon	spliPanel_unLockD;
	private static ImageIcon	spliPanel_unLockE;
	private static ImageIcon	spliPanel_unLockEO;
	private static ImageIcon	spliPanel_unLockEP;

	private static ImageIcon	spliPanel_vSplitD;
	private static ImageIcon	spliPanel_vSplitE;
	private static ImageIcon	spliPanel_vSplitEO;
	private static ImageIcon	spliPanel_vSplitEP;

	private static ImageIcon	spliPanel_hSplitD;
	private static ImageIcon	spliPanel_hSplitE;
	private static ImageIcon	spliPanel_hSplitEO;
	private static ImageIcon	spliPanel_hSplitEP;

	private static ImageIcon	collapsed_P;
	private static ImageIcon	collapsed_E;
	private static ImageIcon	collapsed_EO;
	private static ImageIcon	expanded_EO;
	private static ImageIcon	expanded_E;
	private static ImageIcon	expanded_P;

	private IconLib( )
	{
		createIcons( );
	}

	public static IconLib get( )
	{
		if ( instance == null )
			instance = new IconLib( );
		return instance;
	}

	private void createIcons( )
	{
		helpE = new ImageIcon( IconLib.class.getResource( "/widgets/icons/helpE.png" ) );
		helpD = new ImageIcon( IconLib.class.getResource( "/widgets/icons/helpD.png" ) );
		maximizeE = new ImageIcon( IconLib.class.getResource( "/widgets/icons/maximizeE.png" ) );
		maximizeD = new ImageIcon( IconLib.class.getResource( "/widgets/icons/maximizeD.png" ) );
		maximizeSelE = new ImageIcon( IconLib.class.getResource( "/widgets/icons/maximizeSelE.png" ) );

		minimizeE = new ImageIcon( IconLib.class.getResource( "/widgets/icons/minimizeE.png" ) );
		minimizeD = new ImageIcon( IconLib.class.getResource( "/widgets/icons/minimizeD.png" ) );
		minimizeSelE = new ImageIcon( IconLib.class.getResource( "/widgets/icons/minimizeSelE.png" ) );

		errorIcon = new ImageIcon( IconLib.class.getResource( "/widgets/icons/err_cross.png" ) );
		errorIconMid = new ImageIcon( IconLib.class.getResource( "/widgets/icons/err_mid.png" ) );
		warnIcon = new ImageIcon( IconLib.class.getResource( "/widgets/icons/warn.png" ) );
		infoIcon = new ImageIcon( IconLib.class.getResource( "/widgets/icons/info.png" ) );

		spliPanel_maxD = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_maxD.png" ) );
		spliPanel_maxE = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_maxE.png" ) );
		spliPanel_maxEO = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_maxEO.png" ) );
		spliPanel_maxEP = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_maxEP.png" ) );

		spliPanel_minD = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_minD.png" ) );
		spliPanel_minE = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_minE.png" ) );
		spliPanel_minEO = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_minEO.png" ) );
		spliPanel_minEP = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_minEP.png" ) );

		spliPanel_closeD = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_closeD.png" ) );
		spliPanel_closeE = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_closeE.png" ) );
		spliPanel_closeEO = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_closeEO.png" ) );
		spliPanel_closeEP = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_closeEP.png" ) );

		spliPanel_vSplitD = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_vSplitD.png" ) );
		spliPanel_vSplitE = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_vSplitE.png" ) );
		spliPanel_vSplitEO = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_vSplitEO.png" ) );
		spliPanel_vSplitEP = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_vSplitEP.png" ) );

		spliPanel_hSplitD = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_hSplitD.png" ) );
		spliPanel_hSplitE = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_hSplitE.png" ) );
		spliPanel_hSplitEO = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_hSplitEO.png" ) );
		spliPanel_hSplitEP = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_hSplitEP.png" ) );

		spliPanel_lockD = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_lockD.png" ) );
		spliPanel_lockE = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_lockE.png" ) );
		spliPanel_lockEO = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_lockEO.png" ) );
		spliPanel_lockEP = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_lockEP.png" ) );

		spliPanel_unLockD = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_unlockD.png" ) );
		spliPanel_unLockE = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_unlockE.png" ) );
		spliPanel_unLockEO = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_unlockEO.png" ) );
		spliPanel_unLockEP = new ImageIcon( IconLib.class.getResource( "/widgets/icons/spa_unlockEP.png" ) );

		collapsed_P = new ImageIcon( getClass( ).getResource( "/widgets/icons/collapsed_P.png" ) );
		collapsed_E = new ImageIcon( getClass( ).getResource( "/widgets/icons/collapsed_E.png" ) );
		collapsed_EO = new ImageIcon( getClass( ).getResource( "/widgets/icons/collapsed_EO.png" ) );
		expanded_P = new ImageIcon( getClass( ).getResource( "/widgets/icons/expanded_P.png" ) );
		expanded_E = new ImageIcon( getClass( ).getResource( "/widgets/icons/expanded_E.png" ) );
		expanded_EO = new ImageIcon( getClass( ).getResource( "/widgets/icons/expanded_EO.png" ) );
	}

	public ImageIcon getSpliPanel_unLockD( )
	{
		return spliPanel_unLockD;
	}

	public ImageIcon getSpliPanel_unLockE( )
	{
		return spliPanel_unLockE;
	}

	public ImageIcon getSpliPanel_unLockEO( )
	{
		return spliPanel_unLockEO;
	}

	public ImageIcon getSpliPanel_unLockEP( )
	{
		return spliPanel_unLockEP;
	}

	public ImageIcon getSpliPanel_vSplitEP( )
	{
		return spliPanel_vSplitEP;
	}

	public ImageIcon getSpliPanel_minEP( )
	{
		return spliPanel_minEP;
	}

	public ImageIcon getSpliPanel_minEO( )
	{
		return spliPanel_minEO;
	}

	public ImageIcon getSpliPanel_minE( )
	{
		return spliPanel_minE;
	}

	public ImageIcon getSpliPanel_minD( )
	{
		return spliPanel_minD;
	}

	public ImageIcon getSpliPanel_lockEP( )
	{
		return spliPanel_lockEP;
	}

	public ImageIcon getSpliPanel_hSplitEP( )
	{
		return spliPanel_hSplitEP;
	}

	public ImageIcon getSpliPanel_closeEP( )
	{
		return spliPanel_closeEP;
	}

	public ImageIcon getSpliPanel_maxEP( )
	{
		return spliPanel_maxEP;
	}

	public ImageIcon getSpliPanel_maxD( )
	{
		return spliPanel_maxD;
	}

	public ImageIcon getSpliPanel_maxE( )
	{
		return spliPanel_maxE;
	}

	public ImageIcon getSpliPanel_maxEO( )
	{
		return spliPanel_maxEO;
	}

	public ImageIcon getSpliPanel_closeD( )
	{
		return spliPanel_closeD;
	}

	public ImageIcon getSpliPanel_closeE( )
	{
		return spliPanel_closeE;
	}

	public ImageIcon getSpliPanel_closeEO( )
	{
		return spliPanel_closeEO;
	}

	public ImageIcon getSpliPanel_hSplitD( )
	{
		return spliPanel_hSplitD;
	}

	public ImageIcon getSpliPanel_hSplitE( )
	{
		return spliPanel_hSplitE;
	}

	public ImageIcon getSpliPanel_hSplitEO( )
	{
		return spliPanel_hSplitEO;
	}

	public ImageIcon getSpliPanel_vSplitD( )
	{
		return spliPanel_vSplitD;
	}

	public ImageIcon getSpliPanel_vSplitE( )
	{
		return spliPanel_vSplitE;
	}

	public ImageIcon getSpliPanel_vSplitEO( )
	{
		return spliPanel_vSplitEO;
	}

	public ImageIcon getSpliPanel_lockD( )
	{
		return spliPanel_lockD;
	}

	public ImageIcon getSpliPanel_lockE( )
	{
		return spliPanel_lockE;
	}

	public ImageIcon getSpliPanel_lockEO( )
	{
		return spliPanel_lockEO;
	}

	public ImageIcon getHelpD( )
	{
		return helpD;
	}

	public ImageIcon getHelpE( )
	{
		return helpE;
	}

	public ImageIcon getMaximizeD( )
	{
		return maximizeD;
	}

	public ImageIcon getMaximizeE( )
	{
		return maximizeE;
	}

	public ImageIcon getMaximizeSelE( )
	{
		return maximizeSelE;
	}

	public ImageIcon getMinimizeD( )
	{
		return minimizeD;
	}

	public ImageIcon getMinimizeE( )
	{
		return minimizeE;
	}

	public ImageIcon getMinimizeSelE( )
	{
		return minimizeSelE;
	}

	public ImageIcon getInfoIcon( )
	{
		return infoIcon;
	}

	public ImageIcon getWarnIcon( )
	{
		return warnIcon;
	}

	public ImageIcon getErrorIcon( )
	{
		return errorIcon;
	}

	public ImageIcon getErrorIconMid( )
	{
		return errorIconMid;
	}

	public ImageIcon getCollapsed_P( )
	{
		return collapsed_P;
	}

	public ImageIcon getExpanded_EO( )
	{
		return expanded_EO;
	}

	public ImageIcon getCollapsed_EO( )
	{
		return collapsed_EO;
	}

	public ImageIcon getExpanded_P( )
	{
		return expanded_P;
	}

	public ImageIcon getCollapsed_E( )
	{
		return collapsed_E;
	}

	public ImageIcon getExpanded_E( )
	{
		return expanded_E;
	}
}
