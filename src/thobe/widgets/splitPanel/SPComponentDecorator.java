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

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import thobe.widgets.buttons.TransparentIconButton;
import thobe.widgets.decoratorPanel.Decorator;
import thobe.widgets.decoratorPanel.DecoratorPanel;
import thobe.widgets.decoratorPanel.DecoratorPanelException;
import thobe.widgets.decoratorPanel.ProgressPanel;
import thobe.widgets.decoratorPanel.Screenshot;
import thobe.widgets.decoratorPanel.WaitingDecorator;
import thobe.widgets.icons_internal.IconLib_Internal;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Panel which can be placed into a SplitPanel (as even any other Component), but this one is
 * decorated with some useful things like:<br/>
 * <ul>
 * <li>a menubar with buttons for vertical-, horizontal-split, lock, close and maximize</li>
 * <li>a {@link WaitingDecorator} that visualizes the enable/ disable status of the panel and its subcomponents</li>
 * <li>a {@link WaitingDecorator} that can be used to visualize progress (spinning dial + progress bar)</li>
 * <li>a user defined menu-bar (bottom) for user defined actions</li>
 * </ul>
 * @author Thomas Obenaus
 * @source SplitPanelComponent.java
 * @date 06.02.2012
 */
@SuppressWarnings ( "serial")
public class SPComponentDecorator extends ProgressPanel implements KeyListener, MouseMotionListener
{
	private static List<KeyListener>			keyListener			= new ArrayList<KeyListener>( );
	private static List<MouseMotionListener>	mouseMotionListener	= new ArrayList<MouseMotionListener>( );

	static
	{
		/* register a event-listener for being able to catch mouse-events globally */
		Toolkit.getDefaultToolkit( ).addAWTEventListener( new AWTEventListener( )
		{
			@Override
			public void eventDispatched( AWTEvent event )
			{
				if ( event instanceof MouseEvent )
				{
					MouseEvent me = ( MouseEvent ) event;
					if ( me.getID( ) == MouseEvent.MOUSE_MOVED )
					{
						for ( MouseMotionListener mml : mouseMotionListener )
							mml.mouseMoved( me );
					}
					else if ( me.getID( ) == MouseEvent.MOUSE_DRAGGED )
					{
						for ( MouseMotionListener mml : mouseMotionListener )
							mml.mouseDragged( me );
					}
				}
			}
		}, AWTEvent.MOUSE_MOTION_EVENT_MASK );

		/* event-listener for catching key-events globally */
		KeyboardFocusManager.getCurrentKeyboardFocusManager( ).addKeyEventDispatcher( new KeyEventDispatcher( )
		{
			@Override
			public boolean dispatchKeyEvent( KeyEvent e )
			{
				if ( e.getID( ) == KeyEvent.KEY_PRESSED )
				{
					for ( KeyListener kl : keyListener )
						kl.keyPressed( e );
				}
				else if ( e.getID( ) == KeyEvent.KEY_RELEASED )
				{
					for ( KeyListener kl : keyListener )
						kl.keyReleased( e );
				}
				else if ( e.getID( ) == KeyEvent.KEY_TYPED )
				{
					for ( KeyListener kl : keyListener )
						kl.keyTyped( e );
				}
				return false;
			}
		} );
	}

	private static final Dimension				minSize				= new Dimension( 40, 40 );

	/**
	 * Border used to determine if the mouse is over the to menu-bar.
	 */
	private static final int					border				= 3;

	/**
	 * Listeners that want to be notified about events fired by the menubar on the top.
	 */
	private List<SPComponentDecoratorListener>	listener;

	/**
	 * Tooltip strings for the buttons of the menubar.
	 */
	private String								tt_buLock			= "Lock the window";
	private String								tt_buUnLock			= "Unlock the window";
	private String								tt_buVSplit			= "Split the window vertically";
	private String								tt_buHSplit			= "Split the window horizontally";
	private String								tt_buMax			= "Maximize the window";
	private String								tt_buMin			= "Minimize the window";
	private String								tt_buClose			= "Close the window";

	/**
	 * The top-left menubar of the {@link SPComponentDecorator}
	 */
	private MenuPanel							menuPanel;

	/**
	 * The current key-code used to hide/ show the menu-bar.
	 */
	private int									keyCodeForMenuBar;

	/**
	 * The component that will be used to switch the {@link SPComponentDecorator}s content.
	 */
	private SPContentSwitch						contentSwitch;

	/**
	 * Panel/ Container for the toolbar elements
	 */
	private JPanel								pa_toolbar;

	/**
	 * Ctor
	 * @param toDecorate - see {@link DecoratorPanel}
	 * @throws DecoratorPanelException
	 */
	public SPComponentDecorator( Component toDecorate ) throws DecoratorPanelException
	{
		this( toDecorate, false, null );
	}

	/**
	 * Ctor
	 * @param toDecorate - see {@link DecoratorPanel}
	 * @param screenshot - see {@link WaitingDecorator}
	 * @throws DecoratorPanelException
	 */
	public SPComponentDecorator( Component toDecorate, Screenshot screenshot ) throws DecoratorPanelException
	{
		this( toDecorate, false, screenshot );
	}

	/**
	 * Ctor
	 * @param toDecorate - see {@link DecoratorPanel}
	 * @param withProgressBar - see {@link WaitingDecorator}
	 * @throws DecoratorPanelException
	 */
	public SPComponentDecorator( Component toDecorate, boolean withProgressBar ) throws DecoratorPanelException
	{
		this( toDecorate, withProgressBar, null );
	}

	/**
	 * Ctor
	 * @param toDecorate - see {@link DecoratorPanel}
	 * @param withProgressBar - see {@link WaitingDecorator}
	 * @param screenshot - see {@link WaitingDecorator}
	 * @throws DecoratorPanelException
	 */
	public SPComponentDecorator( Component toDecorate, boolean withProgressBar, Screenshot screenshot ) throws DecoratorPanelException
	{
		super( toDecorate, withProgressBar, screenshot );
		this.listener = new ArrayList<SPComponentDecoratorListener>( );

		this.menuPanel = new MenuPanel( 5 );
		this.addDecorator( menuPanel );

		this.keyCodeForMenuBar = KeyEvent.VK_ALT;

		FormLayout fla_bottom = new FormLayout( "0dlu,pref,3dlu,fill:default:grow,0dlu", "0dlu,fill:20,0dlu" );
		CellConstraints cc_bottom = new CellConstraints( );
		JPanel pa_bottom = new JPanel( fla_bottom );
		this.add( pa_bottom, BorderLayout.SOUTH );

		this.contentSwitch = new SPContentSwitch( );
		pa_bottom.add( this.contentSwitch, cc_bottom.xy( 2, 2 ) );

		this.pa_toolbar = new JPanel( new BorderLayout( 0, 0 ) );
		pa_bottom.add( this.pa_toolbar, cc_bottom.xy( 4, 2 ) );

		keyListener.add( this );
		mouseMotionListener.add( this );
	}

	public void setToolbar( JPanel toolbar )
	{
		this.pa_toolbar.removeAll( );

		// add the new tool-bar
		if ( toolbar != null )
			this.pa_toolbar.add( toolbar, BorderLayout.CENTER );
		this.pa_toolbar.revalidate( );
		this.pa_toolbar.repaint( );
	}

	public void addContentSwitchListener( SPContentSwitchListener l )
	{
		this.contentSwitch.addContentSwitchListener( l );
	}

	public SPContentSwitch getContentSwitch( )
	{
		return contentSwitch;
	}

	public void setProgress( int progress )
	{
		this.waitingDecorator.setProgress( progress );
	}

	public int getProgress( )
	{
		return this.waitingDecorator.getProgress( );
	}

	public void setWaiting( boolean waiting )
	{
		this.waitingDecorator.setWaiting( waiting );
	}

	public boolean isWaiting( )
	{
		return this.waitingDecorator.isWaiting( );
	}

	/**
	 * Enable/ disable a button of the {@link SPComponentDecorator}s top menu-bar.
	 * @param type - defines which button should be affected by this method-call.
	 * @param enabled - true -> enable, false -> disable the button
	 */
	public void setButtonEnabled( SPComponentDecoratorButtonType type, boolean enabled )
	{
		this.menuPanel.setButtonEnabled( type, enabled );
	}

	@Override
	public void setEnabled( boolean enabled )
	{
		super.setEnabled( enabled );
		this.contentSwitch.setEnabled( enabled );
		this.waitingDecorator.setEnabled( !enabled );
	}

	@Override
	public boolean isEnabled( )
	{
		return super.isEnabled( );
	}

	/**
	 * Sets the time (in milliseconds) elapsed until the menu-bar disappears automatically.
	 * @param timeUntilAutomaticMenuDisappear
	 */
	public void setTimeUntilAutomaticMenuDisappear( long timeUntilAutomaticMenuDisappear )
	{
		this.menuPanel.setTimeUntilAutomaticMenuDisappear( timeUntilAutomaticMenuDisappear );
	}

	/**
	 * Lock (disable) the whole {@link SPComponentDecorator}.
	 * @param lock
	 */
	public void setLock( boolean lock )
	{
		this.setEnabled( !lock );
		this.waitingDecorator.setEnabled( !lock );
	}

	public boolean isLocked( )
	{
		return !this.waitingDecorator.isEnabled( );
	}

	public void addListener( SPComponentDecoratorListener l )
	{
		this.listener.add( l );
	}

	public void removeListener( SPComponentDecoratorListener l )
	{
		this.listener.remove( l );
	}

	@Override
	public String getName( )
	{
		return "SplitPanelComponent";
	}

	private void fireClose( )
	{
		for ( SPComponentDecoratorListener l : this.listener )
			l.onClose( );
	}

	private void fireVSplit( )
	{
		for ( SPComponentDecoratorListener l : this.listener )
			l.onVerticalSplit( );
	}

	private void fireHSplit( )
	{
		for ( SPComponentDecoratorListener l : this.listener )
			l.onHorizontalSplit( );
	}

	private void fireMaximize( )
	{
		for ( SPComponentDecoratorListener l : this.listener )
			l.onMaximize( );
	}

	private void fireMinimize( )
	{
		for ( SPComponentDecoratorListener l : this.listener )
			l.onMinimize( );
	}

	@Override
	public Dimension getMinimumSize( )
	{
		return minSize;
	}

	/**
	 * Set the key code to hide/ show the menu-bar (one of the {@link KeyEvent} key codes). The
	 * default value is {@link KeyEvent#VK_ALT}.
	 * @param keyCodeForMenuBar
	 */
	public void setKeyCodeForMenuBar( int keyCodeForMenuBar )
	{
		this.keyCodeForMenuBar = keyCodeForMenuBar;
	}

	/**
	 * The {@link SPComponentDecorator}s top right menu-bar
	 * @author Thomas Obenaus
	 * @date 14.02.2012
	 * @file SPComponent.java
	 */
	private class MenuPanel extends Decorator
	{
		private Dimension				dimVisible		= new Dimension( 223, 27 );
		private Dimension				dimInVisible	= new Dimension( 123, 5 );
		private Dimension				currentDim;
		private final Color				bgColor			= new Color( 220, 220, 220, 40 );
		private final Color				borderColor		= new Color( 20, 20, 20, 40 );

		private TransparentIconButton	bu_lock;
		private TransparentIconButton	bu_vSplit;
		private TransparentIconButton	bu_hSplit;
		private TransparentIconButton	bu_close;
		private TransparentIconButton	bu_max;

		private boolean					maximized;
		private boolean					menuInvisible;

		private Timer					timer;
		private long					timeUntilAutomaticMenuDisappear;

		public MenuPanel( int layer )
		{
			super( layer );
			this.timeUntilAutomaticMenuDisappear = 8000;
			this.timer = null;
			this.maximized = false;
			this.buildGUI( );
			this.updateGUI( );

			this.dimVisible = this.getPreferredSize( );
			this.dimInVisible = new Dimension( ( int ) this.dimVisible.getWidth( ), 5 );
			this.setInvisible( false );
		}

		@Override
		protected void boundsUpdate_( int width, int height )
		{
			this.setBounds( ( int ) ( width - currentDim.getWidth( ) ), 0, ( int ) currentDim.getWidth( ), ( int ) currentDim.getHeight( ) );
		}

		@Override
		public void paint( Graphics g )
		{
			g.setColor( bgColor );
			g.fillRect( 0, -1, ( int ) this.currentDim.getWidth( ), ( int ) this.currentDim.getHeight( ) );
			g.setColor( borderColor );
			g.drawRect( 0, -1, ( int ) this.currentDim.getWidth( ), ( int ) this.currentDim.getHeight( ) );
			super.paint( g );
		}

		/**
		 * Sets the time (in milliseconds) elapsed until the menu-bar disappears automatically.
		 * @param timeUntilAutomaticMenuDisappear
		 */
		public void setTimeUntilAutomaticMenuDisappear( long timeUntilAutomaticMenuDisappear )
		{
			this.timeUntilAutomaticMenuDisappear = timeUntilAutomaticMenuDisappear;
		}

		public void setButtonEnabled( SPComponentDecoratorButtonType type, boolean enabled )
		{
			switch ( type )
			{
			case CLOSE:
				this.bu_close.setEnabled( enabled );
				break;
			case VSPLIT:
				this.bu_vSplit.setEnabled( enabled );
				break;
			case HSPLIT:
				this.bu_hSplit.setEnabled( enabled );
				break;
			case MAXIMIZE:
				this.bu_max.setEnabled( enabled );
				break;
			default:
				throw new NotImplementedException( );
			}
		}

		public void updateGUI( )
		{
			bu_lock.setToolTipText( SPComponentDecorator.this.tt_buLock );
			bu_vSplit.setToolTipText( SPComponentDecorator.this.tt_buVSplit );
			bu_hSplit.setToolTipText( SPComponentDecorator.this.tt_buHSplit );
			bu_close.setToolTipText( SPComponentDecorator.this.tt_buClose );
			bu_max.setToolTipText( SPComponentDecorator.this.tt_buMax );
		}

		private void buildGUI( )
		{
			this.setLayout( new FlowLayout( FlowLayout.RIGHT, 2, 2 ) );

			/* lock-button */
			this.bu_lock = new TransparentIconButton( IconLib_Internal.get( ).getSpliPanel_lockE( ), IconLib_Internal.get( ).getSpliPanel_lockEP( ), IconLib_Internal.get( ).getSpliPanel_lockD( ), IconLib_Internal.get( ).getSpliPanel_lockEO( ) );
			this.add( this.bu_lock );
			this.bu_lock.addActionListener( new ActionListener( )
			{
				@Override
				public void actionPerformed( ActionEvent e )
				{
					hLocked( );
				}
			} );
			this.bu_lock.setMargin( new Insets( 0, 0, 0, 0 ) );
			this.bu_lock.setOpaque( false );

			/* vSplit button */
			this.bu_vSplit = new TransparentIconButton( IconLib_Internal.get( ).getSpliPanel_vSplitE( ), IconLib_Internal.get( ).getSpliPanel_vSplitEP( ), IconLib_Internal.get( ).getSpliPanel_vSplitD( ), IconLib_Internal.get( ).getSpliPanel_vSplitEO( ) );
			this.add( this.bu_vSplit );
			this.bu_vSplit.addActionListener( new ActionListener( )
			{
				@Override
				public void actionPerformed( ActionEvent e )
				{
					SPComponentDecorator.this.fireVSplit( );
				}
			} );

			/* hSplit button */
			this.bu_hSplit = new TransparentIconButton( IconLib_Internal.get( ).getSpliPanel_hSplitE( ), IconLib_Internal.get( ).getSpliPanel_hSplitEP( ), IconLib_Internal.get( ).getSpliPanel_hSplitD( ), IconLib_Internal.get( ).getSpliPanel_hSplitEO( ) );
			this.add( this.bu_hSplit );
			this.bu_hSplit.addActionListener( new ActionListener( )
			{
				@Override
				public void actionPerformed( ActionEvent e )
				{
					SPComponentDecorator.this.fireHSplit( );
				}
			} );

			/* max button */
			this.bu_max = new TransparentIconButton( IconLib_Internal.get( ).getSpliPanel_maxE( ), IconLib_Internal.get( ).getSpliPanel_maxEP( ), IconLib_Internal.get( ).getSpliPanel_maxD( ), IconLib_Internal.get( ).getSpliPanel_maxEO( ) );
			this.add( this.bu_max );
			this.bu_max.addActionListener( new ActionListener( )
			{
				@Override
				public void actionPerformed( ActionEvent e )
				{
					hMaximized( );
				}
			} );

			/* close button */
			this.bu_close = new TransparentIconButton( IconLib_Internal.get( ).getSpliPanel_closeE( ), IconLib_Internal.get( ).getSpliPanel_closeEP( ), IconLib_Internal.get( ).getSpliPanel_closeD( ), IconLib_Internal.get( ).getSpliPanel_closeEO( ) );
			this.add( this.bu_close );
			this.bu_close.addActionListener( new ActionListener( )
			{
				@Override
				public void actionPerformed( ActionEvent e )
				{
					SPComponentDecorator.this.fireClose( );
				}
			} );

			this.bu_close.setBorderPainted( false );
			this.bu_close.setContentAreaFilled( false );
			this.bu_max.setBorderPainted( false );
			this.bu_max.setContentAreaFilled( false );
			this.bu_lock.setBorderPainted( false );
			this.bu_lock.setContentAreaFilled( false );
			this.bu_hSplit.setBorderPainted( false );
			this.bu_hSplit.setContentAreaFilled( false );
			this.bu_vSplit.setBorderPainted( false );
			this.bu_vSplit.setContentAreaFilled( false );
		}

		private void hLocked( )
		{
			SPComponentDecorator.this.setLock( !isLocked( ) );

			/* change tooltip */
			if ( isLocked( ) )
			{
				bu_lock.setToolTipText( tt_buUnLock );
				this.bu_lock.setIconEnabled( IconLib_Internal.get( ).getSpliPanel_unLockE( ) );
				this.bu_lock.setIconDisabled( IconLib_Internal.get( ).getSpliPanel_unLockD( ) );
				this.bu_lock.setIconMouseOver( IconLib_Internal.get( ).getSpliPanel_unLockEO( ) );
				this.bu_lock.setIconPressed( IconLib_Internal.get( ).getSpliPanel_unLockEP( ) );
			}
			else
			{
				bu_lock.setToolTipText( tt_buLock );
				this.bu_lock.setIconEnabled( IconLib_Internal.get( ).getSpliPanel_lockE( ) );
				this.bu_lock.setIconDisabled( IconLib_Internal.get( ).getSpliPanel_lockD( ) );
				this.bu_lock.setIconMouseOver( IconLib_Internal.get( ).getSpliPanel_lockEO( ) );
				this.bu_lock.setIconPressed( IconLib_Internal.get( ).getSpliPanel_lockEP( ) );
			}

		}

		private void hMaximized( )
		{
			if ( !maximized )
				SPComponentDecorator.this.fireMaximize( );
			else SPComponentDecorator.this.fireMinimize( );
			maximized = !maximized;

			/* change tooltip */
			if ( !maximized )
			{
				bu_max.setToolTipText( tt_buMax );
				this.bu_max.setIconEnabled( IconLib_Internal.get( ).getSpliPanel_maxE( ) );
				this.bu_max.setIconDisabled( IconLib_Internal.get( ).getSpliPanel_maxD( ) );
				this.bu_max.setIconMouseOver( IconLib_Internal.get( ).getSpliPanel_maxEO( ) );
				this.bu_max.setIconPressed( IconLib_Internal.get( ).getSpliPanel_maxEP( ) );
			}
			else
			{
				bu_max.setToolTipText( tt_buMin );
				this.bu_max.setIconEnabled( IconLib_Internal.get( ).getSpliPanel_minE( ) );
				this.bu_max.setIconDisabled( IconLib_Internal.get( ).getSpliPanel_minD( ) );
				this.bu_max.setIconMouseOver( IconLib_Internal.get( ).getSpliPanel_minEO( ) );
				this.bu_max.setIconPressed( IconLib_Internal.get( ).getSpliPanel_minEP( ) );
			}

			/* enable/ disable buttons */
			this.bu_close.setEnabled( !maximized );
			this.bu_hSplit.setEnabled( !maximized );
			this.bu_vSplit.setEnabled( !maximized );
		}

		public void setInvisible( boolean invisible )
		{
			this.menuInvisible = invisible;

			if ( this.isInvisible( ) )
			{
				this.currentDim = dimInVisible;
				this.stopTimer( );
			}
			else
			{
				this.currentDim = dimVisible;
				this.restartTimer( );
			}

			this.bu_close.setVisible( !this.isInvisible( ) );
			this.bu_lock.setVisible( !this.isInvisible( ) );
			this.bu_hSplit.setVisible( !this.isInvisible( ) );
			this.bu_vSplit.setVisible( !this.isInvisible( ) );
			this.bu_max.setVisible( !this.isInvisible( ) );

			this.refreshWithParentSize( );
		}

		private void stopTimer( )
		{
			if ( this.timer != null )
				this.timer.cancel( );
			this.timer = null;
		}

		private void restartTimer( )
		{
			if ( this.timer != null )
				this.timer.cancel( );
			this.timer = new Timer( );

			this.timer.schedule( new HideMenuTimer( ), timeUntilAutomaticMenuDisappear );
		}

		public boolean isInvisible( )
		{
			return this.menuInvisible;
		}

		@Override
		public String getName( )
		{
			return "SplitPanelComponent#MenuPanel";
		}

		private boolean isCursorInside( )
		{
			Point pos = MouseInfo.getPointerInfo( ).getLocation( );
			SwingUtilities.convertPointFromScreen( pos, this );
			pos.x += this.getX( );
			pos.y += this.getY( );

			Rectangle bounds = this.getBounds( );
			if ( bounds.contains( pos ) )
				return true;
			return false;
		}

		private class HideMenuTimer extends TimerTask
		{
			@Override
			public void run( )
			{
				/* hide the menupanel only if the mouse cursor is not over the panel */
				if ( !isCursorInside( ) )
					setInvisible( true );
				else restartTimer( );
			}
		}
	}

	@Override
	public void keyTyped( KeyEvent e )
	{

	}

	@Override
	public void keyPressed( KeyEvent e )
	{
		Point pos = MouseInfo.getPointerInfo( ).getLocation( );
		SwingUtilities.convertPointFromScreen( pos, this );
		pos.x += this.getX( );
		pos.y += this.getY( );
		if ( this.isShowing( ) && this.getBounds( ).contains( pos ) && e.getKeyCode( ) == this.keyCodeForMenuBar )
		{
			this.menuPanel.setInvisible( !this.menuPanel.isInvisible( ) );
			if ( this.menuPanel.isInvisible( ) )
				this.repaint( this.menuPanel.getBounds( ) );
		}
	}

	@Override
	public void keyReleased( KeyEvent e )
	{}

	@Override
	public void mouseDragged( MouseEvent e )
	{}

	@Override
	public void mouseMoved( MouseEvent e )
	{
		Point pos = MouseInfo.getPointerInfo( ).getLocation( );
		SwingUtilities.convertPointFromScreen( pos, this );
		pos.x += this.getX( );
		pos.y += this.getY( );

		if ( this.isShowing( ) && this.menuPanel.isInvisible( ) )
		{
			if ( !this.getBounds( ).contains( pos ) )
				return;

			Rectangle bounds = this.menuPanel.getBounds( );
			Rectangle bigBB = new Rectangle( bounds.x - border, bounds.y - border, bounds.width + ( 2 * border ), bounds.height + ( 2 * border ) );
			pos.x -= this.getX( );
			pos.y -= this.getY( );
			if ( !bigBB.contains( pos ) )
				return;
			this.menuPanel.setInvisible( false );
		}

	}

}
