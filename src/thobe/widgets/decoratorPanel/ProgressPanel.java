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

/**
 * Class/ Panel that will decorate a given {@link Component} with a waiting indicator (spinning
 * dial) if the Panel is disabled and optionally an progress bar.
 * @author Thomas Obenaus
 * @source ProgressPanel.java
 * @date 05.02.2012
 */
@SuppressWarnings ( "serial")
public class ProgressPanel extends DecoratorPanel
{
	protected WaitingDecorator	waitingDecorator;

	/**
	 * Ctor - {@link ProgressPanel} without {@link Screenshot}-server and without progress bar
	 * (spinning dial only)
	 * @param toDecorate - see {@link DecoratorPanel#DecoratorPanel(Component)}
	 * @throws DecoratorPanelException
	 */
	public ProgressPanel( Component toDecorate ) throws DecoratorPanelException
	{
		this( toDecorate, false, null );
	}

	/**
	 * Ctor - {@link ProgressPanel} without {@link Screenshot}-server
	 * @param toDecorate - see {@link DecoratorPanel#DecoratorPanel(Component)}
	 * @param withProgressBar - display a progress bar or not, see {@link WaitingDecorator#WaitingDecorator(int, Component, boolean)}
	 * @throws DecoratorPanelException
	 */
	public ProgressPanel( Component toDecorate, boolean withProgressBar ) throws DecoratorPanelException
	{
		this( toDecorate, withProgressBar, null );
	}

	/**
	 * Ctor - {@link ProgressPanel} without progress bar (spinning dial only)
	 * @param toDecorate - see {@link DecoratorPanel#DecoratorPanel(Component)}
	 * @param screenshot - see {@link DisableDecorator#DisableDecorator(int, Component, Screenshot)}
	 * @throws DecoratorPanelException
	 */
	public ProgressPanel( Component toDecorate, Screenshot screenshot ) throws DecoratorPanelException
	{
		this( toDecorate, false, screenshot );
	}

	/**
	 * Ctor
	 * @param toDecorate - see {@link DecoratorPanel#DecoratorPanel(Component)}
	 * @param withProgressBar - display a progress bar or not, see {@link WaitingDecorator#WaitingDecorator(int, Component, boolean)}
	 * @param screenshot - see {@link DisableDecorator#DisableDecorator(int, Component, Screenshot)}
	 * @throws DecoratorPanelException
	 */
	public ProgressPanel( Component toDecorate, boolean withProgressBar, Screenshot screenshot ) throws DecoratorPanelException
	{
		super( toDecorate );
		this.waitingDecorator = new WaitingDecorator( 2, toDecorate, screenshot, withProgressBar );
		this.addDecorator( this.waitingDecorator );
	}

	@Override
	public void setEnabled( boolean enabled )
	{
		super.setEnabled( enabled );
		this.waitingDecorator.setEnabled( enabled );
	}

	public void setWaiting( boolean waiting )
	{
		this.waitingDecorator.setWaiting( waiting );
	}

	public boolean isWaiting( )
	{
		return this.waitingDecorator.isWaiting( );
	}

	public void setDialRadius( int dialRadius )
	{
		this.waitingDecorator.setDialRadius( dialRadius );
	}

	public void setDialColor( Color dialColor )
	{
		this.waitingDecorator.setDialColor( dialColor );
	}

	/**
	 * Set the size of the spikes of the spinning dial.
	 * @param dialStrokeWidth
	 */
	public void setDialStrokeWidth( float dialStrokeWidth )
	{
		this.waitingDecorator.setDialStrokeWidth( dialStrokeWidth );
	}

	/**
	 * Set the value of the progress-bar of the {@link ProgressPanel}. Note that the progress-bar is
	 * only visible if the {@link ProgressPanel} is in state waiting and if the visibility of the
	 * progress bar was enabled via {@link ProgressPanel#setWithProgressbar(boolean)}.
	 * @param progress
	 */
	public void setProgress( int progress )
	{
		this.waitingDecorator.setProgress( progress );
	}

	public int getProgress( )
	{
		return this.waitingDecorator.getProgress( );
	}

	/**
	 * Control the visibility of the progress bar.
	 * @param withProgressbar - True: a progress bar will be drawn, False: no progress bar
	 */
	public void setWithProgressbar( boolean withProgressbar )
	{
		this.waitingDecorator.setWithProgressbar( withProgressbar );
	}
}
