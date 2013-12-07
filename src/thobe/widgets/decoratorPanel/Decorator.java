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

import javax.swing.JPanel;

/**
 * A class that can be used to decorate an panel. The {@link Decorator} can only be used in
 * connection with the {@link DecoratorPanel}.
 * @author Thomas Obenaus
 * @source Decorator.java
 * @date 30.01.2012
 */
@SuppressWarnings ( "serial")
public abstract class Decorator extends JPanel
{
	private int	parentWidth;
	private int	parentHeight;

	/**
	 * The layer (zOrder) at witch the {@link Decorator} should be placed. The higher the layer the
	 * higher is the {@link Decorator} in the stack of Panels. Assuming that the component to be
	 * decorated is in layer 0 (like in {@link DecoratorPanel}) a {@link Decorator} with layer 1
	 * will be above that component and will overlay it.
	 */
	private int	layer;

	/**
	 * Ctor
	 * @param layer - The layer (zOrder) at witch the {@link Decorator} should be placed. The higher
	 *            the layer the higher is the {@link Decorator} in the stack of Panels. Assuming that
	 *            the component to be decorated is in layer 0 (like in {@link DecoratorPanel}) a {@link Decorator} with layer 1 will be
	 *            above that component and will overlay it.
	 */
	public Decorator( int layer )
	{
		this.layer = layer;
		this.setOpaque( false );
		this.parentHeight = 0;
		this.parentWidth = 0;
	}

	/**
	 * This method is called whenever the bounds of the container ({@link DecoratorPanel}) is
	 * updated (e.g. caused by resize or move events). You have to implement this method to seth the
	 * position and size of your {@link Decorator}.
	 * @param width
	 * @param height
	 */
	protected abstract void boundsUpdate_( int width, int height );

	protected void boundsUpdate( int width, int height )
	{
		this.parentWidth = width;
		this.parentHeight = height;
		this.boundsUpdate_( this.parentWidth, this.parentHeight );
	}

	public void refreshWithParentSize( )
	{
		this.boundsUpdate_( this.parentWidth, this.parentHeight );
	}

	/**
	 * Returns the layer of
	 * @return
	 */
	public int getLayer( )
	{
		return this.layer;
	}

	/**
	 * Returns the name of the Decorator.
	 */
	public abstract String getName( );
}
