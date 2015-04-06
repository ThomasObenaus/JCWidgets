/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.multiFramePanel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Map.Entry;

import javax.swing.JPanel;

import thobe.widgets.action.AbstrAction;
import thobe.widgets.icons_internal.IconLib_Internal;
import thobe.widgets.multiFramePanel.actions.Action;
import thobe.widgets.multiFramePanel.actions.ActionGroup;
import thobe.widgets.multiFramePanel.layouts.Layout_1Single;
import thobe.widgets.multiFramePanel.layouts.Layout_2SideBySide;
import thobe.widgets.multiFramePanel.layouts.Layout_2Stacked;
import thobe.widgets.multiFramePanel.layouts.Layout_3StackedLeft;
import thobe.widgets.multiFramePanel.layouts.Layout_3StackedRight;
import thobe.widgets.multiFramePanel.layouts.Layout_4SideBySide;
import thobe.widgets.multiFramePanel.layouts.Layout_4Stacked;

/**
 * @author Thomas Obenaus
 * @source MultiFramePanel.java
 * @date 16.04.2010
 */
@SuppressWarnings ( "serial")
public class MultiFramePanel extends JPanel implements Observer
{
	private List<Frame>						visibleFrames;
	private MultiFrameLayout				layout;
	private boolean							dragging;
	private BoundingBox						boundingBox;

	private static final Cursor				curDefault					= new Cursor( Cursor.DEFAULT_CURSOR );
	private static final Cursor				curLeftRight				= new Cursor( Cursor.W_RESIZE_CURSOR );
	private static final Cursor				curTopBottom				= new Cursor( Cursor.N_RESIZE_CURSOR );

	private static final Color				COLOR_DIV_DRAGGING			= new Color( 140, 140, 140, 128 );
	private static final Color				COLOR_DIV_DRAGGING_H		= new Color( 180, 180, 180, 128 );
	private static final Color				COLOR_DIV_UNDER_CURSOR		= new Color( 120, 120, 120 );
	private static final Color				COLOR_DIV_UNDER_CURSOR_H	= new Color( 170, 170, 170 );
	private static final Color				COLOR_DIV					= new Color( 120, 120, 120 );
	private static final Color				COLOR_DIV_H					= new Color( 220, 220, 220 );

	private List<Divider>					storedDividers;

	private boolean							oneFrameMaximized			= false;
	private MultiFrameLayout				lastLayout;
	private List<Frame>						invisibleFrames;
	private List<Act_MaximzeAndRestore>		maximzeActions;
	private Act_MaximzeAndRestore			currentlyActiveMaxAction;

	/**
	 * A map of registered layouts - identified by its ID (key).
	 */
	private Map<Integer, MultiFrameLayout>	registeredLayouts;

	private DefaultFrameComponentBuilder	defaultFrameComponentBuilder;

	private List<Frame>						registeredFrames;

	public MultiFramePanel( )
	{
		this( true );
	}

	public MultiFramePanel( boolean initStandardLayouts )
	{
		this( new DefaultFrameComponentBuilderImpl( ), initStandardLayouts );
	}

	/**
	 * Ctor
	 */
	public MultiFramePanel( DefaultFrameComponentBuilder defaultComponentBuilder, boolean initStandardLayouts )
	{
		this.defaultFrameComponentBuilder = defaultComponentBuilder;
		this.registeredFrames = new ArrayList<Frame>( );
		this.registeredLayouts = new HashMap<Integer, MultiFrameLayout>( );
		this.maximzeActions = new ArrayList<Act_MaximzeAndRestore>( );
		this.storedDividers = new ArrayList<Divider>( );
		this.visibleFrames = new ArrayList<Frame>( );
		this.dragging = false;
		this.boundingBox = new BoundingBox( );

		if ( initStandardLayouts )
			this.initStandardLayouts( );

		this.addMouseMotionListener( new MouseMotionListener( )
		{
			@Override
			public void mouseMoved( MouseEvent e )
			{
				/* dispatch mouse-event to layout */
				layout.onMouseMoved( e.getX( ), e.getY( ) );
				if ( !dragging )
				{
					FrameContainer containerUnderCursor = layout.getFrameContainerWithDividerUnderCursor( layout.getFrameTreeRoot( ), e.getX( ), e.getY( ) );
					if ( containerUnderCursor == null )
					{
						setCursor( curDefault );
						repaint( );
						return;
					}
					switch ( containerUnderCursor.getDivider( ).getType( ) )
					{
					case HORIZONTAL:
						setCursor( curTopBottom );
						break;
					case VERTICAL:
						setCursor( curLeftRight );
						break;
					}
				}

				repaint( );
			}

			@Override
			public void mouseDragged( MouseEvent e )
			{
				/* dispatch mouse-event to layout */
				layout.onMouseDragged( e.getX( ), e.getY( ) );

				repaint( );

			}
		} );

		this.addMouseListener( new MouseAdapter( )
		{

			@Override
			public void mouseReleased( MouseEvent e )
			{
				dragging = false;
				/* dispatch mouse-event to layout */
				layout.onMouseReleased( e.getX( ), e.getY( ) );

				repaint( );
				revalidate( );
			}

			@Override
			public void mousePressed( MouseEvent e )
			{
				dragging = true;
				/* dispatch mouse-event to layout */
				layout.onMousePressed( e.getX( ), e.getY( ) );

				repaint( );
			}

			@Override
			public void mouseExited( MouseEvent e )
			{
				if ( !dragging )
					setCursor( curDefault );
				/* dispatch mouse-event to layout */
				layout.onMouseExited( );
				repaint( );
			}
		} );
	}

	/**
	 * Register a new {@link MultiFrameLayout} that should be selectable in the MenuBar.
	 * @param layout
	 */
	public void registerLayout( MultiFrameLayout layout )
	{
		this.registeredLayouts.put( layout.getID( ), layout );
		this.registerGlobalActions( );
	}

	/**
	 * Remove a {@link MultiFrameLayout} from the selection-menu of the MenuBar.
	 * @param layout
	 */
	public void unregisterLayout( MultiFrameLayout layout )
	{
		this.registeredLayouts.remove( layout.getID( ) );
		this.registerGlobalActions( );
	}

	private void initStandardLayouts( )
	{
		MultiFrameLayout laySingle = new Layout_1Single( );
		this.registeredLayouts.put( laySingle.getID( ), laySingle );

		MultiFrameLayout newLayout = new Layout_2SideBySide( );
		this.registeredLayouts.put( newLayout.getID( ), newLayout );
		newLayout = new Layout_2Stacked( );
		this.registeredLayouts.put( newLayout.getID( ), newLayout );
		newLayout = new Layout_3StackedRight( );
		this.registeredLayouts.put( newLayout.getID( ), newLayout );
		newLayout = new Layout_3StackedLeft( );
		this.registeredLayouts.put( newLayout.getID( ), newLayout );
		newLayout = new Layout_4SideBySide( );
		this.registeredLayouts.put( newLayout.getID( ), newLayout );
		newLayout = new Layout_4Stacked( );
		this.registeredLayouts.put( newLayout.getID( ), newLayout );

		this.setLayout( laySingle );
	}

	private void paintDivider( Graphics g, Divider divider )
	{

		int highlightThickness = ( int ) ( divider.getThickness( ) / 4 );
		if ( highlightThickness < 1 )
			highlightThickness = 1;

		BoundingBox divBB = divider.getBoundingBox( );
		if ( divider.isDragging( ) )
			g.setColor( COLOR_DIV_DRAGGING );
		else if ( divider.isUnderCursor( ) )
			g.setColor( COLOR_DIV_UNDER_CURSOR );
		else g.setColor( COLOR_DIV );

		g.fillRect( divBB.getX( ), divBB.getY( ), divBB.getWidth( ), divBB.getHeight( ) );

		if ( divider.isDragging( ) )
			g.setColor( COLOR_DIV_DRAGGING_H );
		else if ( divider.isUnderCursor( ) )
			g.setColor( COLOR_DIV_UNDER_CURSOR_H );
		else g.setColor( COLOR_DIV_H );

		if ( divider.getType( ) == DividerType.VERTICAL )
		{
			g.fillRect( divBB.getX( ) + ( divBB.getWidth( ) - highlightThickness ), divBB.getY( ), highlightThickness, divBB.getHeight( ) );
			g.fillRect( divBB.getX( ), divBB.getY( ), highlightThickness, divBB.getHeight( ) );
		}
		else
		{
			g.fillRect( divBB.getX( ), divBB.getY( ) + ( divBB.getHeight( ) - highlightThickness ), divBB.getWidth( ), highlightThickness );
			g.fillRect( divBB.getX( ), divBB.getY( ), divBB.getWidth( ), highlightThickness );
		}
	}

	private void paintDivider( Graphics g, FrameTreeNode node )
	{
		FrameContainer container = node.getFrameContainer( );
		if ( container == null )
			return;

		/* draw the divider */
		Divider divider = container.getDivider( );
		this.paintDivider( g, divider );
		if ( !this.dragging )
		{
			try
			{
				Divider cloned = ( Divider ) divider.clone( );
				cloned.setUnderCursor( false );
				this.storedDividers.add( cloned );
			}
			catch ( CloneNotSupportedException e )
			{}
		}

		/* recursive call to paint the containers children's divider  */
		this.paintDivider( g, container.getFirstChild( ) );
		this.paintDivider( g, container.getSecondChild( ) );
	}

	@Override
	public void paint( Graphics g )
	{
		super.paint( g );

		/* paint the stored dividers */
		if ( this.dragging )
		{
			for ( Divider divider : this.storedDividers )
				this.paintDivider( g, divider );
		}
		else
		{
			/* paint the dividers */
			this.storedDividers.clear( );
		}
		this.paintDivider( g, this.layout.getFrameTreeRoot( ) );

	}

	@Override
	public void setEnabled( boolean enabled )
	{
		super.setEnabled( enabled );
		for ( Frame frame : this.registeredFrames )
			frame.setEnabled( enabled );
	}

	/**
	 * Sets the given {@link FrameComponent} into the {@link Frame} at the given index.
	 * @param component
	 * @param index
	 * @return - returns the {@link FrameComponent} that will be replaced by the new {@link FrameComponent}. This method might return
	 *         <code>null</code>.
	 */
	public FrameComponent setComponent( FrameComponent component, int index )
	{
		List<Frame> currentFrameList = this.visibleFrames;
		if ( this.oneFrameMaximized )
			currentFrameList = this.invisibleFrames;

		if ( index > currentFrameList.size( ) )
			index = currentFrameList.size( );
		if ( index < 0 )
			index = 1;

		Frame frame = null;
		FrameComponent oldComponent = null;

		if ( currentFrameList.size( ) > 0 && ( index < currentFrameList.size( ) ) )
			frame = currentFrameList.get( index );
		if ( frame == null )
		{
			frame = new Frame( component );
			frame.addObserver( this );

		}
		else oldComponent = frame.setComponent( component );

		if ( currentFrameList.size( ) == 0 || index >= currentFrameList.size( ) )
		{
			currentFrameList.add( frame );
			this.registeredFrames.add( frame );
		}
		this.registerGlobalActions( );

		this.addFramesToContainer( );
		this.doLayout( );
		this.revalidate( );

		return oldComponent;
	}

	/**
	 * Returns all {@link Frame} committed to the MultiFramePanel.
	 * @return
	 */
	public List<Frame> getFrames( )
	{
		return registeredFrames;
	}

	private void registerGlobalActions( )
	{
		/* add action for max-/ minimize the frame */
		this.maximzeActions.clear( );
		for ( Frame frame : this.registeredFrames )
		{
			frame.removeAllGlobalActions( );

			/* add actions for choosing the layout */
			ActionGroup actGr_layout = new ActionGroup( "Layout", "Choose a layout" );
			for ( Entry<Integer, MultiFrameLayout> entry : this.registeredLayouts.entrySet( ) )
				actGr_layout.addChild( new Action( new Act_SetLayout( entry.getValue( ) ) ) );
			if ( actGr_layout.getNumberOfChildren( ) > 0 )
				frame.registerGlobalAction( actGr_layout );

			Act_MaximzeAndRestore maxAction = new Act_MaximzeAndRestore( frame );
			if ( frame.isMaximized( ) )
			{
				this.currentlyActiveMaxAction = maxAction;
				this.currentlyActiveMaxAction.setSelectedIcons( IconLib_Internal.get( ).getMinimizeSelE( ), IconLib_Internal.get( ).getMinimizeSelE( ) );
				this.currentlyActiveMaxAction.setIcon( IconLib_Internal.get( ).getMinimizeE( ), IconLib_Internal.get( ).getMinimizeD( ) );
			}
			this.maximzeActions.add( maxAction );
			frame.registerGlobalAction( new Action( maxAction ) );
		}
	}

	private void addFramesToContainer( )
	{
		/* add all components to the container */
		super.removeAll( );
		for ( Frame f : this.visibleFrames )
		{
			if ( f.isFrameVisible( ) )
				this.add( f.getComponent( ) );
		}
	}

	private void maximizeFrame( )
	{
		this.oneFrameMaximized = !this.oneFrameMaximized;

		if ( this.oneFrameMaximized ) /* maximize */
		{
			this.currentlyActiveMaxAction.setSelectedIcons( IconLib_Internal.get( ).getMinimizeSelE( ), IconLib_Internal.get( ).getMinimizeSelE( ) );
			this.currentlyActiveMaxAction.setIcon( IconLib_Internal.get( ).getMinimizeE( ), IconLib_Internal.get( ).getMinimizeD( ) );
			this.currentlyActiveMaxAction.getFrame( ).setMaximized( true );

			this.lastLayout = this.layout;
			this.invisibleFrames = new ArrayList<Frame>( this.visibleFrames );
			this.visibleFrames.clear( );
			this.visibleFrames.add( this.currentlyActiveMaxAction.getFrame( ) );
			this.addFramesToContainer( );
			this.layout = new Layout_1Single( );
		}
		else
		/* minimize */
		{
			this.currentlyActiveMaxAction.setSelectedIcons( IconLib_Internal.get( ).getMaximizeSelE( ), IconLib_Internal.get( ).getMaximizeSelE( ) );
			this.currentlyActiveMaxAction.setIcon( IconLib_Internal.get( ).getMaximizeE( ), IconLib_Internal.get( ).getMaximizeD( ) );
			this.currentlyActiveMaxAction.getFrame( ).setMaximized( false );
			this.currentlyActiveMaxAction = null;
			this.visibleFrames.clear( );
			for ( Frame f : this.invisibleFrames )
				this.visibleFrames.add( f );
			this.addFramesToContainer( );
			this.layout = this.lastLayout;
			this.lastLayout = null;
		}

		if ( this.layout.getFrameTreeRoot( ) == null )
			this.layout.buildFrameTree( this.visibleFrames );

		this.doLayout( );
		this.revalidate( );
	}

	/**
	 * Updates the complete ui by recreating the gui and the menu-bar of all registered {@link Frame}'s.
	 */
	public void update( )
	{
		for ( Frame f : this.registeredFrames )
			f.rebuildGUI( );
	}

	/**
	 * Creates at a minimum the given number of frames (frameCount). Therefore the currently active
	 * DefaultComponentBuilder will be used to create the FrameComponents. So if you want to have
	 * Frames created with a user-defined FrameComponent use the method {@code setDefaultFrameComponentBuilder()} to register a specific
	 * DefaultFrameComponentBuilder.
	 * @param frameCount
	 */
	public void setMinFrameCount( int frameCount )
	{
		while ( this.registeredFrames.size( ) < frameCount )
			this.addComponent( this.defaultFrameComponentBuilder.createDefaultFrameComponent( ) );

		this.setLayout( this.layout );
	}

	/**
	 * Add's the given {@link FrameComponent} to the {@link MultiFramePanel}.
	 * @param component
	 */
	public void addComponent( FrameComponent component )
	{
		this.setComponent( component, this.visibleFrames.size( ) );
	}

	@Override
	public void doLayout( )
	{
		super.doLayout( );
		this.boundingBox.setBounds( 0, 0, this.getWidth( ), this.getHeight( ) );

		for ( Frame f : this.registeredFrames )
			f.setFrameVisible( false );

		this.layout.doLayout( this.boundingBox );

		this.addFramesToContainer( );

	}

	@Override
	public void removeAll( )
	{
		this.visibleFrames.clear( );
		this.registeredFrames.clear( );
		this.invisibleFrames.clear( );
		super.removeAll( );
	}

	/**
	 * Registers a class that delivers new instances of a default {@link FrameComponent} which
	 * should be inserted if, due to the selected layout, empty Frames would appear.
	 * @param defaultFrameComponentBuilder
	 */
	public void setDefaultFrameComponentBuilder( DefaultFrameComponentBuilder defaultFrameComponentBuilder )
	{
		this.defaultFrameComponentBuilder = defaultFrameComponentBuilder;
		int numRegisteredFrames = this.registeredFrames.size( );
		for ( int i = 0; i < numRegisteredFrames; i++ )
			this.setComponent( this.defaultFrameComponentBuilder.createDefaultFrameComponent( ), i );
	}

	/**
	 * Set/ apply a new layout, this will be used to layout the {@link FrameComponent}'s (and it's {@link Frame}'s). Each
	 * {@link MultiFramePanel} is able to layout <i>{@link MultiFramePanel} .numberOfFramesLayedOut( )</i> {@link FrameComponent}'s. If
	 * there are already not enough {@link FrameComponent}'s registered at the {@link MultiFramePanel} <i>n</i> default
	 * {@link FrameComponent}'s will appear. Where <i>n</i> is the difference between the number of {@link FrameComponent}'s that can be
	 * layout by the {@link MultiFramePanel} and the number of {@link FrameComponent}'s already registered at the {@link MultiFramePanel}.
	 * What {@link FrameComponent}'s should be inserted per default can be controlled by registering a {@link DefaultFrameComponentBuilder}
	 * via the <i>setDefaultFrameComponentBuilder</i> method
	 * @param layout
	 */
	public void setLayout( MultiFrameLayout layout )
	{
		if ( this.oneFrameMaximized )
		{
			this.maximizeFrame( );
		}

		this.layout = layout;

		while ( this.visibleFrames.size( ) < this.layout.numberOfFramesLayedOut( ) )
			this.addComponent( this.defaultFrameComponentBuilder.createDefaultFrameComponent( ) );

		if ( this.layout.getFrameTreeRoot( ) == null )
			this.layout.buildFrameTree( this.visibleFrames );

		/* disable all maximize actions if there will be only one frame visible */
		for ( Act_MaximzeAndRestore actMax : this.maximzeActions )
		{
			actMax.setEnabled( ( this.layout.numberOfFramesLayedOut( ) > 1 || this.oneFrameMaximized ) );
			actMax.setSelectedIcons( IconLib_Internal.get( ).getMaximizeSelE( ), IconLib_Internal.get( ).getMaximizeSelE( ) );
			actMax.setIcon( IconLib_Internal.get( ).getMaximizeE( ), IconLib_Internal.get( ).getMaximizeD( ) );
		}

		this.doLayout( );
		this.revalidate( );
	}

	/**
	 * Action used for the maximization/ minimization of the frame.
	 * @author Thomas Obenaus
	 * @date 10.05.2010
	 * @file MultiFramePanel.java
	 */
	private class Act_MaximzeAndRestore extends AbstrAction
	{
		private Frame	frame;

		public Act_MaximzeAndRestore( Frame frame )
		{
			super( "", "", "maximize", "maximize", IconLib_Internal.get( ).getMaximizeE( ), IconLib_Internal.get( ).getMaximizeD( ) );
			this.setSelectedIcons( IconLib_Internal.get( ).getMaximizeSelE( ), IconLib_Internal.get( ).getMaximizeSelE( ) );
			this.frame = frame;
		}

		@Override
		public String getActionKey( )
		{
			return "ACT_MAXIMIZE";
		}

		@Override
		public void actionPerformed( ActionEvent e )
		{
			currentlyActiveMaxAction = this;
			maximizeFrame( );
		}

		public Frame getFrame( )
		{
			return frame;
		}
	}

	private class Act_SetLayout extends AbstrAction
	{
		private MultiFrameLayout	layout;

		public Act_SetLayout( MultiFrameLayout layout )
		{
			super( layout.getName( ) );
			this.putValue( AbstrAction.SHORT_DESCRIPTION, layout.getDescription( ) );
			this.layout = layout;

		}

		@Override
		public void actionPerformed( ActionEvent e )
		{
			setLayout( this.layout );
		}

		@Override
		public String getActionKey( )
		{
			return "ACT_" + this.layout.getName( );
		}

	}

	@Override
	public void update( Observable o, Object arg )
	{
		if ( arg instanceof FrameNotification && ( ( FrameNotification ) arg ) == FrameNotification.COMPONENT_SET )
		{
			this.revalidate( );
			this.repaint( );
		}
	}
}
