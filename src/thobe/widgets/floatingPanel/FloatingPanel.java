/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.floatingPanel;

import java.awt.Dimension;

import javax.swing.JPanel;

/**
 * @author Thomas Obenaus
 * @source FloatingComponent.java
 * @date 01.09.2009
 */
@SuppressWarnings ( "serial")
public abstract class FloatingPanel extends JPanel
{
	private FloatingPanelContainer	container;
	private boolean					restrictShrinking;
	protected int					panelWidth;
	protected int					panelHeight;

	public FloatingPanel( )
	{
		this.container = null;
		this.restrictShrinking = false;
	}

	public FloatingPanelContainer getFloatingPanelContainer( )
	{
		return container;
	}

	public boolean isRestrictShrinking( )
	{
		return restrictShrinking;
	}

	public void setContainer( FloatingPanelContainer container )
	{
		this.container = container;
	}

	public void updateGUI( int width )
	{}

	public abstract boolean canGrow( );

	public int getMinimumHeight( )
	{
		int min = ( int ) this.getMinimumSize( ).getHeight( );
		return min;
	}

	public int getPanelWidth( )
	{
		return this.panelWidth;
	}

	public void setPanelSize( int width, int height )
	{
		this.panelHeight = height;
		this.panelWidth = width;

		//		if ( restrictShrinking )
		//		{
		//			if ( this.panelHeight < this.getMinimumSize( ).height ) this.panelHeight = this.getMinimumSize( ).height;
		//			if ( this.panelWidth < this.getMinimumSize( ).width ) this.panelWidth = this.getMinimumSize( ).width;
		//		}
	}

	public int getPanelHeight( )
	{
		if ( this.panelHeight == 0 )
		{
			//			System.err.println( "FloatingPanel.getPanelHeight() is 0 the FloatingPanel won't be visible. Use setPanelSize()!" );
			this.panelHeight = ( int ) this.getMinimumSize( ).getHeight( );
		}
		return this.panelHeight;
	}

	public void setRestrictShrinking( boolean restrictShrinking, int minWidth, int minHeight )
	{
		this.restrictShrinking = restrictShrinking;
		this.setMinimumSize( new Dimension( minWidth, minHeight ) );
		this.setPanelSize( minWidth, minHeight );
	}

}
