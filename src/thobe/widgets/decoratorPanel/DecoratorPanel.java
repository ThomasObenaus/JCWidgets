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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 * A Panel that allows to decorate a component with multiple {@link Decorator}s.
 * @author Thomas Obenaus
 * @source DecoratorPanel.java
 * @date 30.01.2012
 */
@SuppressWarnings ( "serial")
public class DecoratorPanel extends JPanel
{
	/**
	 * The Component that should be decorated.
	 */
	private Component		toDecorate;
	/**
	 * The {@link Decorator}s.
	 */
	private List<Decorator>	decorators;
	private JLayeredPane	layeredPane;

	/**
	 * If true the repainting of the component to be decorated will be blocked if the {@link DecoratorPanel} is disabled. If set to false
	 * the repainting of the component to be
	 * decorated won't be affected by the {@link DecoratorPanel}.
	 */
	private boolean			blockRepaintOfDecoratedComponent;
	/**
	 * Is it necessary to set the visibility of the {@link Component} to be decorated to true gets
	 * enabled? This action might be necessary if the {@link Component} to be decorated hidden on
	 * disabling the {@link DecoratorPanel} (since {@link DecoratorPanel#blockRepaintOfDecoratedComponent}) was set to true.
	 */
	private boolean			showComponentIfEnabled;

	/**
	 * Ctor
	 * @param toDecorate - The {@link Component} that should be decorated with the given {@link Decorator}s.
	 * @param decorators - A list of {@link Decorator}s that should be used to decorate the given {@link Component}.
	 */
	public DecoratorPanel( Component toDecorate, List<Decorator> decorators ) throws DecoratorPanelException
	{
		this.blockRepaintOfDecoratedComponent = false;
		this.showComponentIfEnabled = false;
		this.toDecorate = toDecorate;
		this.decorators = decorators;
		this.toDecorate.setName( "DecoratorPanel#ComponentToDecorate" );
		this.buildGUI( );
	}

	public DecoratorPanel( Component toDecorate ) throws DecoratorPanelException
	{
		this( toDecorate, new ArrayList<Decorator>( ) );
	}

	/**
	 * If true the repainting of the component to be decorated will be blocked if the {@link DecoratorPanel} is disabled. If set to false
	 * the repainting of the component to be
	 * decorated won't be affected by the {@link DecoratorPanel}.
	 * @param blockRepaintOfDecoratedComponent
	 */
	public void setBlockRepaintOfDisabledComponent( boolean blockRepaintOfDecoratedComponent )
	{
		this.blockRepaintOfDecoratedComponent = blockRepaintOfDecoratedComponent;
	}

	private void buildGUI( ) throws DecoratorPanelException
	{
		this.setLayout( new BorderLayout( ) );
		this.layeredPane = new JLayeredPane( );
		this.layeredPane.setName( "DecoratorPanel#LayeredPane" );
		this.add( this.layeredPane, BorderLayout.CENTER );

		/* search for decorators with same layer */
		Collections.sort( this.decorators, new Comparator<Decorator>( )
		{
			@Override
			public int compare( Decorator o1, Decorator o2 )
			{
				if ( o1.getLayer( ) < o2.getLayer( ) )
					return -1;
				if ( o1.getLayer( ) > o2.getLayer( ) )
					return 1;
				return 0;
			}
		} );
		int lastLayer = 0;
		for ( Decorator d : this.decorators )
		{
			int currentLayer = d.getLayer( );
			if ( currentLayer == 0 )
				throw new DecoratorPanelException( "Decorator [" + d.getName( ) + "] with layer 0 found. This is not allowed since the component to be decorated is placed at layer 0." );
			if ( currentLayer < 0 )
				throw new DecoratorPanelException( "Decorator [" + d.getName( ) + "] with layer < 0 (" + currentLayer + ") found. This is not allowed, only values > 0 are possible." );
			if ( currentLayer == lastLayer )
				throw new DecoratorPanelException( "Multiple Decorators with same layer found." );
			lastLayer = currentLayer;
		}

		/* add the component(s) + decorator to the layered pane */
		this.updateDecoratorHierarchy( );

		/* listener for observing bound-changes of the parent */
		SynchListener listener = new SynchListener( );
		this.addComponentListener( listener );
		this.addHierarchyBoundsListener( listener );
	}

	private void updateDecoratorHierarchy( ) throws DecoratorPanelException
	{
		this.layeredPane.removeAll( );

		/* add the  component to be decorated */
		this.layeredPane.add( this.toDecorate, new Integer( 0 ) );

		/* hide the  component to be decorated  but only if the DecoratorPanel is disenabled and if 
		 * we want to block the repaint of the Component to be decorated if the DecoratorPanel 
		 * is disabled. */
		if ( !this.isEnabled( ) && this.blockRepaintOfDecoratedComponent )
		{
			this.toDecorate.setVisible( false );
			this.showComponentIfEnabled = true;
		}

		/* add the decorators the decorators */
		for ( Decorator d : this.decorators )
		{
			if ( d.getLayer( ) == 0 )
				throw new DecoratorPanelException( "Decorator with layer 0 found. This is not allowed since the component to be decorated is placed at lacer 0." );
			this.layeredPane.add( d, new Integer( d.getLayer( ) ) );
		}
	}

	/**
	 * Adds a new {@link Decorator} to the hierarchy of decorators. If the {@link Decorator} to be
	 * added has the same layer like another {@link Decorator} already added to the {@link DecoratorPanel} the new one will replace the
	 * original one. The {@link Decorator} that
	 * was replaced by the new one will be returned by this method. If no {@link Decorator} was
	 * replaced, this method returns null.
	 * @param decorator - the {@link Decorator} to be added
	 * @return - the {@link Decorator} that was replaced by the new one (caused by a layer-conflict)
	 *         or null if there was no layer-conflict.
	 * @throws DecoratorPanelException
	 */
	public Decorator addDecorator( Decorator decorator ) throws DecoratorPanelException
	{
		/* find decorator with layer of decorator to add
		 * this decorator will be replaced by the new one */
		Decorator decoratorToBeReplaced = null;
		for ( Decorator d : this.decorators )
		{
			if ( d.getLayer( ) == decorator.getLayer( ) )
			{
				decoratorToBeReplaced = d;
				break;
			}

		}

		this.decorators.add( decorator );
		this.updateDecoratorHierarchy( );
		return decoratorToBeReplaced;
	}

	@Override
	public void setEnabled( boolean enabled )
	{
		super.setEnabled( enabled );

		/* Remove the component to be decorated to block calls of its repaint-method 
		 * but only if the DecoratorPanel is not enabled AND if we want to block
		 * the repaint of the compinent to be decorated. */
		if ( !this.isEnabled( ) && this.blockRepaintOfDecoratedComponent )
		{
			this.toDecorate.setVisible( false );
			this.showComponentIfEnabled = true;
		}

		/* add the the component to be decorated, but only if the DecoratorPanel is enabled OR if 
		 * we don't want to block the repaint of the Component to be decorated if the DecoratorPanel 
		 * is disabled AND it is necessary to re add the component */
		if ( ( this.isEnabled( ) || !this.blockRepaintOfDecoratedComponent ) && this.showComponentIfEnabled )
		{
			this.toDecorate.setVisible( true );
			this.showComponentIfEnabled = false;
		}

		/* dis-/ enable all decorators too */
		for ( Decorator d : this.decorators )
			d.setEnabled( enabled );
		this.updateBounds( );
	}

	@Override
	public void setVisible( boolean aFlag )
	{
		super.setVisible( aFlag );
		this.updateBounds( );
	}

	/**
	 * Updates the bounds of the panel and all {@link Decorator}s attached to the panel.
	 */
	private void updateBounds( )
	{
		this.toDecorate.setBounds( 0, 0, this.layeredPane.getWidth( ), this.layeredPane.getHeight( ) );
		/* update the bounds of the attached decorators */
		for ( Decorator d : this.decorators )
			d.boundsUpdate( this.layeredPane.getWidth( ), this.layeredPane.getHeight( ) );

		this.revalidate( );
	}

	@Override
	public void doLayout( )
	{
		super.doLayout( );
		this.toDecorate.doLayout( );
		/* update the bounds of the attached decorators */
		for ( Decorator d : this.decorators )
			d.doLayout( );
	}

	@Override
	public String getName( )
	{
		return "DecoratorPanel";
	}

	/**
	 * Listener that is able to observe moving and resizing events of this panel. This events will
	 * be delegated to the {@link DecoratorPanel#updateBounds()} method.
	 * @author Thomas Obenaus
	 * @date 31.01.2012
	 * @file DecoratorPanel.java
	 */
	private final class SynchListener extends ComponentAdapter implements HierarchyBoundsListener
	{
		public void ancestorResized( HierarchyEvent e )
		{
			updateBounds( );
		}

		public void ancestorMoved( HierarchyEvent e )
		{
			updateBounds( );
		}

		public void componentMoved( ComponentEvent e )
		{
			updateBounds( );
		}

		public void componentResized( ComponentEvent e )
		{
			updateBounds( );
		}

		public void componentHidden( ComponentEvent e )
		{
			//			setVisible( false );
		}

		public void componentShown( ComponentEvent e )
		{
			//			setVisible( true );
		}

	}
}
