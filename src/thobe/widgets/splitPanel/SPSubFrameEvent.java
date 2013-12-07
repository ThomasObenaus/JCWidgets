/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.splitPanel;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/**
 * @author Thomas Obenaus
 * @source SPSubFrameEvent.java
 * @date 28.08.2012
 */
public class SPSubFrameEvent
{
	private SPSubFrame				subFrame;
	private SPComponentDecorator	componentDecorator;
	private String					contentKey;
	private SPContent				content;
	private int						mouseButton;
	private Point					posOnScreen;
	private Point					pos;

	public SPSubFrameEvent( SPSubFrame subFrame )
	{
		// no mouse-click event
		this( subFrame, -1, new Point( ) );
	}

	public SPSubFrameEvent( SPSubFrame subFrame, Point posOnScreen )
	{
		// no mouse-click event
		this( subFrame, -1, posOnScreen );
	}

	public SPSubFrameEvent( SPSubFrame subFrame, int mouseButton, Point posOnScreen )
	{
		this.subFrame = subFrame;
		this.posOnScreen = posOnScreen;
		this.pos = new Point( posOnScreen );
		SwingUtilities.convertPointFromScreen( pos, subFrame.getComponentDecorator( ) );

		this.mouseButton = mouseButton;
		if ( this.mouseButton != MouseEvent.BUTTON1 && this.mouseButton != MouseEvent.BUTTON2 && this.mouseButton != MouseEvent.BUTTON3 )
			this.mouseButton = -1;

		this.componentDecorator = this.subFrame.getComponentDecorator( );
		this.contentKey = this.subFrame.getCurrentContentKey( );
		this.content = this.subFrame.getCurrentContent( );
	}

	/**
	 * Returns the {@link SPComponentDecorator} of this {@link SPSubFrame}
	 * @return
	 */
	public SPComponentDecorator getComponentDecorator( )
	{
		return componentDecorator;
	}

	/**
	 * Returns the content of the {@link SPSubFrame}
	 * @return
	 */
	public SPContent getContent( )
	{
		return content;
	}

	/**
	 * Returns the key identifying the content of the {@link SPSubFrame}
	 * @return
	 */
	public String getContentKey( )
	{
		return contentKey;
	}

	/**
	 * Returns the mouse-button that was clicked, or -1 if no click has occured
	 * @return
	 */
	public int getMouseButton( )
	{
		return mouseButton;
	}

	/**
	 * Returns the sub-frame on which the event occurred
	 * @return
	 */
	public SPSubFrame getSubFrame( )
	{
		return subFrame;
	}

	/**
	 * Returns the position of the mouse-cursor on the {@link SPSubFrame}'s {@link SPComponentDecorator}.
	 * @return
	 */
	public Point getPos( )
	{
		return pos;
	}

	/**
	 * Returns the position of the mouse-cursor on screen
	 * @return
	 */
	public Point getPosOnScreen( )
	{
		return posOnScreen;
	}
}
