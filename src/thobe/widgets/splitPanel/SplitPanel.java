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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * A class representing a panel that can consist of multiple sub-frames/- panels. The content of the
 * sub-frames is dictated by the {@link SPContentFactory} that can be implemented as needed. The
 * functionality of splitting(/ merging) a frame into sub-frames(/ to one frame) is implemented in
 * the {@link SplitPanel} and can be used instantly.
 * @author Thomas Obenaus
 * @source SplitPanel.java
 * @date 06.02.2012
 */
@SuppressWarnings ( "serial")
public class SplitPanel extends JPanel implements SPSubFrameListener, MouseListener, MouseMotionListener
{
	/**
	 * Some cursors
	 */
	private static final Cursor					curDefault					= new Cursor( Cursor.DEFAULT_CURSOR );
	private static final Cursor					curLeftRight				= new Cursor( Cursor.W_RESIZE_CURSOR );
	private static final Cursor					curTopBottom				= new Cursor( Cursor.N_RESIZE_CURSOR );

	/**
	 * Some colors
	 */
	private static final Color					COLOR_DIV_DRAGGING			= new Color( 140, 140, 140, 128 );
	private static final Color					COLOR_DIV_DRAGGING_H		= new Color( 180, 180, 180, 128 );
	private static final Color					COLOR_DIV_UNDER_CURSOR		= new Color( 120, 120, 120 );
	private static final Color					COLOR_DIV_UNDER_CURSOR_H	= new Color( 170, 170, 170 );
	private static final Color					COLOR_DIV					= new Color( 120, 120, 120 );
	private static final Color					COLOR_DIV_H					= new Color( 220, 220, 220 );

	private static List<MouseMotionListener>	mouseMotionListener			= new ArrayList<MouseMotionListener>( );
	private static List<MouseListener>			mouseListener				= new ArrayList<MouseListener>( );

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
					else if ( me.getID( ) == MouseEvent.MOUSE_CLICKED )
					{
						for ( MouseListener mml : mouseListener )
							mml.mouseClicked( me );
					}
				}
			}
		}, AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK );
	}

	/**
	 * The factory being able to create the content.
	 */
	private SPContentFactory					contentFactory;

	/**
	 * The root node of the tree that represents the hierarchy of sub-frames of the {@link SplitPanel}
	 */
	private SPFrame								root;

	/**
	 * The sub-frame that should be displayed maximized.
	 */
	private SPSubFrame							maximizedNode;

	/**
	 * The {@link SPFrameContainer} whose {@link SPDivider} is currently under the mouse-cursor
	 */
	private SPFrameContainer					containerWhoseDividerIsUnderCursor;

	/**
	 * Dragging or not?
	 */
	private boolean								dragging;

	private List<SplitPanelListener>			listeners;

	private SPSubFrame							currentSubFrameUnderCursor;

	/**
	 * Ctor
	 * @param contentFactory - An instance of a class implementing the {@link SPContentFactory} interface. This one has to be implemented if
	 *            you want to use the {@link SplitPanel}.
	 *            This factory offers three different methods that will be called by the {@link SplitPanel} internally. E.g. if a frame
	 *            should be splitted into two subframes
	 *            the {@link SplitPanel} calls the method {@link SPContentFactory#createContentForKey(String)} to create a new instance of
	 *            the
	 *            frame that should be splitted.
	 * @throws SPTreeException
	 */
	public SplitPanel( SPContentFactory contentFactory ) throws SPTreeException
	{
		this.setLayout( null );
		this.listeners = new ArrayList<>( );
		this.dragging = false;
		this.contentFactory = contentFactory;
		this.containerWhoseDividerIsUnderCursor = null;
		this.maximizedNode = null;
		this.currentSubFrameUnderCursor = null;

		// create new root-node with one SPSubFrame inside
		SPSubFrame leaf = new SPSubFrame( this.contentFactory.getNewFrameID( ), true, this.contentFactory );
		this.root = new SPFrameContainer( this.contentFactory.getNewFrameID( ), true, leaf );

		this.add( leaf.getComponentDecorator( ) );
		leaf.addListener( this );

		this.updateStateOfButtons( );
		this.addMouseListener( this );
		mouseListener.add( this );
		mouseMotionListener.add( this );
	}

	public void addListener( SplitPanelListener l )
	{
		this.listeners.add( l );
	}

	public void removeListener( SplitPanelListener l )
	{
		this.listeners.remove( l );
	}

	@Override
	public void doLayout( )
	{
		super.doLayout( );

		if ( this.maximizedNode != null )
			this.maximizedNode.doLayout( );
		else this.root.doLayout( );
	}

	@Override
	public String getName( )
	{
		return "SplitPanel";
	}

	@Override
	public void setBounds( int x, int y, int width, int height )
	{
		super.setBounds( x, y, width, height );

		if ( this.maximizedNode != null )
			this.maximizedNode.setBounds( x, y, width, height );
		else this.root.setBounds( x, y, width, height );
	}

	public SPFrame getRootNode( )
	{
		return root;
	}

	/**
	 * Set a new root. Using this method results in a replacement of the whole tree that represents
	 * the hierarchy of sub-frames which are displayed by this {@link SplitPanel}.
	 * @param root
	 */
	public void setRoot( SPFrame root )
	{
		/* retrieve all leav-nodes */
		List<SPSubFrame> leafNodes = new ArrayList<SPSubFrame>( );
		this.getAllLeafNodes( this.root, leafNodes );
		/* remove the SplitPanel from the listener-list of the leaf-nodes */
		for ( SPSubFrame leaf : leafNodes )
			leaf.removeListener( this );

		this.root = root;
		this.removeAll( );

		leafNodes.clear( );
		this.getAllLeafNodes( this.root, leafNodes );

		/* add the splitpanel as listener and add the content panels of the leaf nodes to the splitpanel */
		for ( SPSubFrame leaf : leafNodes )
		{
			this.add( leaf.getComponentDecorator( ) );
			leaf.addListener( this );
		}

		this.updateStateOfButtons( );

		this.repaint( );
		this.revalidate( );
	}

	private void getAllLeafNodes( SPFrame node, List<SPSubFrame> leafNodes )
	{
		SPFrameContainer container = node.getFrameContainer( );
		if ( container != null )
		{
			SPFrame fChild = container.getFirstChild( );
			if ( fChild != null )
				this.getAllLeafNodes( fChild, leafNodes );

			SPFrame sChild = container.getSecondChild( );
			if ( sChild != null )
				this.getAllLeafNodes( sChild, leafNodes );

			return;
		}
		leafNodes.add( node.getSubFrame( ) );
	}

	private void getAllDivider( SPFrame node, List<SPDivider> divider )
	{
		SPFrameContainer container = node.getFrameContainer( );
		if ( container != null )
		{
			divider.add( container.getDivider( ) );
			SPFrame fChild = container.getFirstChild( );
			if ( fChild != null )
				this.getAllDivider( fChild, divider );

			SPFrame sChild = container.getSecondChild( );
			if ( sChild != null )
				this.getAllDivider( sChild, divider );

			return;
		}
	}

	private void split( SPSubFrame leaf, SPDividerType type )
	{
		/* get parent container */
		SPFrameContainer parent = leaf.getParent( );

		try
		{
			/* create the new leaf */
			SPSubFrame newLeaf = new SPSubFrame( this.contentFactory.getNewFrameID( ), !leaf.isFirstChild( ), this.contentFactory, leaf.getCurrentContentKey( ) );

			/* add the new leaf to the panel */
			this.add( newLeaf.getComponentDecorator( ) );
			newLeaf.addListener( this );

			/* insert the leaf, newLeaf and the new parent to the tree */
			if ( leaf.isFirstChild( ) )
			{
				SPFrameContainer newParent = new SPFrameContainer( this.contentFactory.getNewFrameID( ), true, leaf, newLeaf, new SPDivider( type, 0.5 ) );
				parent.setChildren( newParent, parent.getSecondChild( ) );
			}
			else
			{
				SPFrameContainer newParent = new SPFrameContainer( this.contentFactory.getNewFrameID( ), false, newLeaf, leaf, new SPDivider( type, 0.5 ) );
				parent.setChildren( parent.getFirstChild( ), newParent );
			}
		}
		catch ( SPTreeException e )
		{
			e.printStackTrace( );
			System.err.println( "Error [" + type + "]: unable to create the content for the new leaf-node: " + e.getLocalizedMessage( ) );
			return;
		}
		this.repaint( );
		this.revalidate( );
	}

	@Override
	public void onVerticalSplit( SPSubFrame leaf )
	{
		this.split( leaf, SPDividerType.VERTICAL );
		this.updateStateOfButtons( );
	}

	@Override
	public void onHorizontalSplit( SPSubFrame leaf )
	{
		this.split( leaf, SPDividerType.HORIZONTAL );
		this.updateStateOfButtons( );
	}

	@Override
	public void onMaximize( SPSubFrame leaf )
	{
		if ( this.maximizedNode != null )
			this.maximizedNode.registerToRemoveListener( this );

		List<SPSubFrame> leafNodes = new ArrayList<SPSubFrame>( );
		this.getAllLeafNodes( this.root, leafNodes );
		for ( SPSubFrame l : leafNodes )
		{
			l.setVisible( false );
			l.registerToRemoveListener( this );
		}

		this.maximizedNode = leaf;
		this.setLayout( new BorderLayout( ) );
		this.maximizedNode.setVisible( true );
		this.maximizedNode.registerToAddListener( this );

		this.repaint( );
		this.revalidate( );
	}

	@Override
	public void onMinimize( SPSubFrame leaf )
	{
		this.setLayout( null );
		this.maximizedNode.registerToRemoveListener( this );
		this.maximizedNode = null;

		List<SPSubFrame> leafNodes = new ArrayList<SPSubFrame>( );
		this.getAllLeafNodes( this.root, leafNodes );
		for ( SPSubFrame l : leafNodes )
		{
			l.setVisible( true );
			l.registerToAddListener( this );
		}

		this.repaint( );
		this.revalidate( );
	}

	private void updateStateOfButtons( )
	{
		/* check if there are more than one leaf in tree --> enable close button */
		List<SPSubFrame> leafNodes = new ArrayList<SPSubFrame>( );
		this.getAllLeafNodes( this.root, leafNodes );
		if ( leafNodes.size( ) > 1 )
		{
			for ( SPSubFrame l : leafNodes )
			{
				l.setButtonEnabled( SPComponentDecoratorButtonType.CLOSE, true );
				l.setButtonEnabled( SPComponentDecoratorButtonType.MAXIMIZE, true );
			}
		}
		else if ( leafNodes.size( ) == 1 )
		{
			SPSubFrame lastLeaf = leafNodes.get( 0 );
			lastLeaf.setButtonEnabled( SPComponentDecoratorButtonType.CLOSE, false );
			lastLeaf.setButtonEnabled( SPComponentDecoratorButtonType.MAXIMIZE, false );
		}
	}

	@Override
	public void paint( Graphics g )
	{
		super.paint( g );

		if ( this.maximizedNode != null )
			return;
		/* paint the divider */
		List<SPDivider> divider = new ArrayList<SPDivider>( );
		this.getAllDivider( this.root, divider );
		for ( SPDivider d : divider )
			this.paintDivider( g, d );

	}

	private void paintDivider( Graphics g, SPDivider divider )
	{
		int highlightThickness = ( int ) ( divider.getThickness( ) / 4 );
		if ( highlightThickness < 1 )
			highlightThickness = 1;

		Rectangle divBB = divider.getBoundingBox( );
		if ( divider.isDragging( ) )
			g.setColor( COLOR_DIV_DRAGGING );
		else if ( divider.isUnderCursor( ) )
			g.setColor( COLOR_DIV_UNDER_CURSOR );
		else g.setColor( COLOR_DIV );

		g.fillRect( divBB.x, divBB.y, divBB.width, divBB.height );

		if ( divider.isDragging( ) )
			g.setColor( COLOR_DIV_DRAGGING_H );
		else if ( divider.isUnderCursor( ) )
			g.setColor( COLOR_DIV_UNDER_CURSOR_H );
		else g.setColor( COLOR_DIV_H );

		if ( divider.getType( ) == SPDividerType.VERTICAL )
		{
			g.fillRect( divBB.x + ( divBB.width - highlightThickness ), divBB.y, highlightThickness, divBB.height );
			g.fillRect( divBB.x, divBB.y, highlightThickness, divBB.height );
		}
		else
		{
			g.fillRect( divBB.x, divBB.y + ( divBB.height - highlightThickness ), divBB.width, highlightThickness );
			g.fillRect( divBB.x, divBB.y, divBB.width, highlightThickness );
		}
	}

	private SPFrameContainer getContainerWithDividerUnderCursor( SPFrame node, int x, int y )
	{
		if ( node == null )
			return null;
		SPFrameContainer cUnderCursor = node.getFrameContainer( );
		if ( cUnderCursor == null )
			return null;

		/* check the containers bbox */
		Rectangle bbox = cUnderCursor.getBounds( );
		if ( !bbox.contains( x, y ) )
			return null;

		/* check the dividers bbox */
		if ( cUnderCursor.getDivider( ).getBoundingBox( ).contains( x, y ) )
			return cUnderCursor;

		/* decent to the children of the container */
		SPFrameContainer cLeft = this.getContainerWithDividerUnderCursor( cUnderCursor.getFirstChild( ), x, y );
		if ( cLeft != null )
			return cLeft;

		SPFrameContainer cRight = this.getContainerWithDividerUnderCursor( cUnderCursor.getSecondChild( ), x, y );
		if ( cRight != null )
			return cRight;
		return null;
	}

	/**
	 * Returns the {@link SPSubFrame} whose bounding-box contains position (x,y)
	 * @param node
	 * @param x
	 * @param y
	 * @return
	 */
	private SPFrame getSubFrameAt( SPFrame node, int x, int y )
	{
		if ( node == null )
			return null;

		// its a SPSubFrame
		SPSubFrame subUnderCursor = node.getSubFrame( );
		if ( subUnderCursor != null )
		{
			/* check the containers bbox */
			Rectangle bbox = subUnderCursor.getBounds( );
			if ( bbox.contains( x, y ) )
				return subUnderCursor;
			// leaf reached, but not under cursor
			return null;
		}

		// it should be a container
		SPFrameContainer cUnderCursor = node.getFrameContainer( );

		/* check the containers bbox */
		Rectangle bbox = cUnderCursor.getBounds( );
		if ( !bbox.contains( x, y ) )
			return null;

		/* decent to the children of the container */
		SPFrame cLeft = this.getSubFrameAt( cUnderCursor.getFirstChild( ), x, y );
		if ( cLeft != null )
			return cLeft;

		SPFrame cRight = this.getSubFrameAt( cUnderCursor.getSecondChild( ), x, y );
		if ( cRight != null )
			return cRight;
		return null;
	}

	@Override
	public void onClose( SPSubFrame leaf )
	{
		this.removeNode( leaf );
		this.updateStateOfButtons( );

		this.fireOnSubFrameRemoved( leaf );

		this.repaint( );
		this.revalidate( );
	}

	private void removeNode( SPFrame node )
	{
		SPSubFrame leaf = node.getSubFrame( );
		if ( leaf != null )
		{
			/* remove from panel, unregister listener */
			this.remove( leaf.getComponentDecorator( ) );
			leaf.registerToRemoveListener( this );
			this.contentFactory.freeFrameID( leaf.getId( ) );
		}

		/* get parent container */
		SPFrameContainer parent = node.getParent( );
		if ( parent == null )
			return;

		// 'node' was removed --> the other child of node's parent will be the new frame 
		SPFrame newFrame = null;
		if ( node.isFirstChild( ) )
			newFrame = parent.getSecondChild( );
		else newFrame = parent.getFirstChild( );

		// remove the parent container, since a container containing only one child 
		// is not necessary
		SPFrameContainer parentsParent = parent.getParent( );
		if ( parentsParent == null )
			return;

		if ( parent.isFirstChild( ) )
		{
			newFrame.setFirstChild( true );
			parentsParent.setFirstChild( newFrame );
		}
		else
		{
			newFrame.setFirstChild( false );
			parentsParent.setChildren( parentsParent.getFirstChild( ), newFrame );
		}
	}

	@Override
	public void onContentSwitched( SPSubFrame leaf, String key )
	{
		this.fireOnSubFrameContentSwitched( leaf );
	}

	@Override
	public void mousePressed( MouseEvent e )
	{
		this.dragging = true;
		if ( this.containerWhoseDividerIsUnderCursor != null )
			containerWhoseDividerIsUnderCursor.getDivider( ).setDragging( this.dragging );
		repaint( );
	}

	@Override
	public void mouseExited( MouseEvent e )
	{
		if ( !dragging )
			setCursor( curDefault );

		if ( this.containerWhoseDividerIsUnderCursor == null )
			return;
		SPDivider div = containerWhoseDividerIsUnderCursor.getDivider( );
		div.setUnderCursor( false );

		repaint( );
	}

	@Override
	public void mouseReleased( MouseEvent e )
	{
		this.dragging = false;
		if ( this.containerWhoseDividerIsUnderCursor != null )
			containerWhoseDividerIsUnderCursor.getDivider( ).setDragging( this.dragging );
		repaint( );
		revalidate( );

	}

	@Override
	public void mouseDragged( MouseEvent e )
	{
		if ( this.containerWhoseDividerIsUnderCursor == null )
			return;

		SPDivider div = containerWhoseDividerIsUnderCursor.getDivider( );
		div.setDragging( this.dragging );

		Rectangle bbox = containerWhoseDividerIsUnderCursor.getBounds( );
		/* relative x-position within the FrameContainer currently under cursor 
		 * relX = absX - fcUnderCursor.getBoundingBox().getX();
		 */
		int relX = e.getX( ) - bbox.x;
		/* relative y-position within the FrameContainer currently under cursor 
		 * relY = absY - fcUnderCursor.getBoundingBox().getY();
		 */
		int relY = e.getY( ) - bbox.y;

		double newDividerPosition = 0;
		Dimension minSizeFirstChild = containerWhoseDividerIsUnderCursor.getFirstChild( ).getMinSize( );
		Dimension minSizeSecondChild = containerWhoseDividerIsUnderCursor.getSecondChild( ).getMinSize( );

		switch ( div.getType( ) )
		{
		case HORIZONTAL:
			int minTop = minSizeFirstChild.height;
			int minBottom = minSizeSecondChild.height;

			if ( relY < minTop )
				relY = minTop;
			if ( relY > bbox.getHeight( ) - minBottom )
				relY = bbox.height - minBottom;

			newDividerPosition = relY / ( double ) bbox.getHeight( );

			div.setBounds( bbox.x, relY + bbox.y, bbox.width, div.getThickness( ) );
			break;
		case VERTICAL:
			int minLeft = minSizeFirstChild.width;
			int minRight = minSizeSecondChild.width;

			if ( relX < minLeft )
				relX = minLeft;
			if ( relX > bbox.getWidth( ) - minRight )
				relX = bbox.width - minRight;

			newDividerPosition = relX / ( double ) bbox.getWidth( );

			div.setBounds( relX + bbox.x, bbox.y, div.getThickness( ), bbox.height );
			break;
		}

		div.setPosition( newDividerPosition );

		this.repaint( );
	}

	private void fireOnSubFrameEntered( SPSubFrame enteredSubFrame, Point posOnScreen )
	{
		SPSubFrameEvent event = new SPSubFrameEvent( enteredSubFrame, posOnScreen );
		for ( SplitPanelListener l : this.listeners )
			l.onSubFrameEntered( event );
	}

	private void fireOnSubFrameExited( SPSubFrame exitedSubFrame, Point posOnScreen )
	{
		SPSubFrameEvent event = new SPSubFrameEvent( exitedSubFrame, posOnScreen );
		for ( SplitPanelListener l : this.listeners )
			l.onSubFrameExited( event );
	}

	private void fireOnSubFrameClicked( SPSubFrame clickedSubFrame, int mousebutton, Point posOnScreen )
	{
		SPSubFrameEvent event = new SPSubFrameEvent( clickedSubFrame, mousebutton, posOnScreen );
		for ( SplitPanelListener l : this.listeners )
			l.onSubFrameClicked( event );
	}

	private void fireOnSubFrameRemoved( SPSubFrame subFrame )
	{
		SPSubFrameEvent event = new SPSubFrameEvent( subFrame );
		for ( SplitPanelListener l : this.listeners )
			l.onSubFrameRemoved( event );
	}

	private void fireOnSubFrameContentSwitched( SPSubFrame subFrame )
	{
		SPSubFrameEvent event = new SPSubFrameEvent( subFrame );
		for ( SplitPanelListener l : this.listeners )
			l.onSubFrameContentSwitched( event );
	}

	@Override
	public void mouseMoved( MouseEvent e )
	{
		// find subframe under cursor
		Point pos = MouseInfo.getPointerInfo( ).getLocation( );
		SwingUtilities.convertPointFromScreen( pos, this );
		SPFrame frame = this.getSubFrameAt( this.root, ( int ) pos.getX( ), ( int ) pos.getY( ) );
		if ( frame != this.currentSubFrameUnderCursor )
		{
			// notify about exit
			if ( this.currentSubFrameUnderCursor != null )
				this.fireOnSubFrameExited( this.currentSubFrameUnderCursor, MouseInfo.getPointerInfo( ).getLocation( ) );

			this.currentSubFrameUnderCursor = null;
			if ( frame != null )
			{
				SPSubFrame subFrame = frame.getSubFrame( );

				// notify about enter
				this.currentSubFrameUnderCursor = subFrame;
				this.fireOnSubFrameEntered( this.currentSubFrameUnderCursor, MouseInfo.getPointerInfo( ).getLocation( ) );
			}
		}

		/* restore state of old divider */
		if ( this.containerWhoseDividerIsUnderCursor != null )
		{
			this.containerWhoseDividerIsUnderCursor.getDivider( ).setDragging( false );
			this.containerWhoseDividerIsUnderCursor.getDivider( ).setUnderCursor( false );
		}

		/* get current container whos divider is under cursor */
		this.containerWhoseDividerIsUnderCursor = this.getContainerWithDividerUnderCursor( this.root, e.getX( ), e.getY( ) );
		if ( this.containerWhoseDividerIsUnderCursor == null )
		{
			if ( !this.dragging )
			{
				/* restore cursor */
				setCursor( curDefault );
				repaint( );
			}
			return;
		}

		/* update divider appearance */
		containerWhoseDividerIsUnderCursor.getDivider( ).setDragging( this.dragging );
		containerWhoseDividerIsUnderCursor.getDivider( ).setUnderCursor( true );

		/* set cursor */
		if ( !dragging )
		{
			switch ( containerWhoseDividerIsUnderCursor.getDivider( ).getType( ) )
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
	public void mouseClicked( MouseEvent e )
	{
		// find subframe under cursor
		Point pos = MouseInfo.getPointerInfo( ).getLocation( );
		SwingUtilities.convertPointFromScreen( pos, this );
		SPFrame frame = this.getSubFrameAt( this.root, ( int ) pos.getX( ), ( int ) pos.getY( ) );
		if ( frame != null )
		{
			SPSubFrame subFrame = frame.getSubFrame( );
			this.currentSubFrameUnderCursor = subFrame;
			this.fireOnSubFrameClicked( this.currentSubFrameUnderCursor, e.getButton( ), MouseInfo.getPointerInfo( ).getLocation( ) );
		}
	}

	@Override
	public void mouseEntered( MouseEvent e )
	{}

	/**
	 * Load a {@link SplitPanel} from {@link InputStream}. Build up a hierarchy of {@link SPFrame} 's, that is a {@link SplitPanel} filled
	 * with contents described within the given stream.
	 * @param reader - a {@link Reader} providing access to the stream where the xml-structured
	 *            information about the {@link SplitPanel} can be found.
	 * @param factory - an instance of a class implementing the {@link SPContentFactory} interface.
	 *            This class should be the same class that was used to create the {@link SplitPanel} hierarchy which should be loaded/ build
	 *            up from the given stream. The currently active {@link SPContentFactory} of this instance of {@link SplitPanel} will be
	 *            replaced by
	 *            the given {@link SPContentFactory}. This parameter can be null. In this case the
	 *            currently active {@link SPContentFactory} of this instance of {@link SplitPanel} will
	 *            be used (and won't be replaced).
	 * @throws XMLStreamException
	 * @throws SPTreeException
	 */
	public void fromXMLStream( Reader reader, SPContentFactory factory ) throws XMLStreamException, SPTreeException
	{
		// replace the currently active content-factory in case it is not null
		if ( factory != null )
			this.contentFactory = factory;

		// First create a new XMLInputFactory
		XMLInputFactory inputFactory = XMLInputFactory.newInstance( );
		XMLEventReader eventReader = inputFactory.createXMLEventReader( reader );

		this.fromXML( eventReader );
	}

	/**
	 * Load a {@link SplitPanel} from {@link InputStream}. Build up a hierarchy of {@link SPFrame} 's, that is a {@link SplitPanel} filled
	 * with contents described within the given stream.
	 * @param reader - a {@link Reader} providing access to the stream where the xml-structured
	 *            information about the {@link SplitPanel} can be found.
	 * @param factory - an instance of a class implementing the {@link SPContentFactory} interface.
	 *            This class should be the same class that was used to create the {@link SplitPanel} hierarchy which should be loaded/ build
	 *            up from the given stream. The currently active {@link SPContentFactory} of this instance of {@link SplitPanel} will be
	 *            replaced by
	 *            the given {@link SPContentFactory}. This parameter can be null. In this case the
	 *            currently active {@link SPContentFactory} of this instance of {@link SplitPanel} will
	 *            be used (and won't be replaced).
	 * @throws XMLStreamException
	 * @throws SPTreeException
	 */
	public void fromXMLStream( Reader reader ) throws XMLStreamException, SPTreeException
	{
		this.fromXMLStream( reader, null );
	}

	/**
	 * Load a {@link SplitPanel} from {@link InputStream}. Build up a hierarchy of {@link SPFrame} 's, that is a {@link SplitPanel} filled
	 * with contents described within the given stream.
	 * @param iStream - the stream where the xml-structured information about the {@link SplitPanel} can be found.
	 * @param factory - an instance of a class implementing the {@link SPContentFactory} interface.
	 *            This class should be the same class that was used to create the {@link SplitPanel} hierarchy which should be loaded/ build
	 *            up from the given stream. The currently active {@link SPContentFactory} of this instance of {@link SplitPanel} will be
	 *            replaced by
	 *            the given {@link SPContentFactory}. This parameter can be null. In this case the
	 *            currently active {@link SPContentFactory} of this instance of {@link SplitPanel} will
	 *            be used (and won't be replaced).
	 * @throws XMLStreamException
	 * @throws SPTreeException
	 */
	public void fromXMLStream( InputStream iStream, SPContentFactory factory ) throws XMLStreamException, SPTreeException
	{
		// replace the currently active content-factory in case it is not null
		if ( factory != null )
			this.contentFactory = factory;

		// First create a new XMLInputFactory
		XMLInputFactory inputFactory = XMLInputFactory.newInstance( );
		XMLEventReader eventReader = inputFactory.createXMLEventReader( iStream );

		this.fromXML( eventReader );
	}

	/**
	 * Load a {@link SplitPanel} from {@link InputStream}. Build up a hierarchy of {@link SPFrame} 's, that is a {@link SplitPanel} filled
	 * with contents described within the given stream.
	 * @param iStream - the stream where the xml-structured information about the {@link SplitPanel} can be found.
	 * @param factory - an instance of a class implementing the {@link SPContentFactory} interface.
	 *            This class should be the same class that was used to create the {@link SplitPanel} hierarchy which should be loaded/ build
	 *            up from the given stream. The currently active {@link SPContentFactory} of this instance of {@link SplitPanel} will be
	 *            replaced by
	 *            the given {@link SPContentFactory}. This parameter can be null. In this case the
	 *            currently active {@link SPContentFactory} of this instance of {@link SplitPanel} will
	 *            be used (and won't be replaced).
	 * @throws XMLStreamException
	 * @throws SPTreeException
	 */
	public void fromXMLStream( InputStream iStream ) throws XMLStreamException, SPTreeException
	{
		fromXMLStream( iStream, null );
	}

	private void fromXML( XMLEventReader eventReader ) throws XMLStreamException, SPTreeException
	{
		SPFrameContainerXML newRoot = null;
		while ( eventReader.hasNext( ) )
		{
			XMLEvent event = eventReader.nextEvent( );

			// start events
			if ( event.isStartElement( ) )
			{
				StartElement startElement = event.asStartElement( );
				// tag SplitPanel
				if ( startElement.getName( ).getLocalPart( ).equals( TAG_SPLIT_PANEL ) )
				{
					Attribute attribute = startElement.getAttributeByName( new QName( ATTR_CONTENT_FACTORY ) );

					// check if this SPContentFactory is able to create the SPFrame's
					if ( !attribute.getValue( ).equals( this.contentFactory.getClass( ).getName( ) ) )
						throw new SPTreeException( "Invalid SPContentFactory: " + this.contentFactory.getClass( ).getName( ) + " expected, but " + attribute.getValue( ) + " found." );
					continue;
				}

				// tag for root FrameContainer found
				if ( startElement.getName( ).getLocalPart( ).equals( TAG_FCONTAINER ) )
				{
					newRoot = new SPFrameContainerXML( );
					this.fromXML( eventReader, newRoot );
					continue;
				}
			}

			// end-events
			if ( event.isEndElement( ) )
			{
				// closing SplitPanel tag found --> trying to build the SPFrame structure (tree) 
				EndElement endElement = event.asEndElement( );
				if ( endElement.getName( ).getLocalPart( ).equals( TAG_SPLIT_PANEL ) && newRoot != null )
				{
					/*
					 *  // only for debugging purposes 
					 *  SPFrameXML.print( newRoot, 0 );
					*/
					this.setRoot( SPFrameXML.build( this.contentFactory, newRoot, true ) );
					return;
				}
			}
		}
	}

	/**
	 * Loop through the xml-events considering nodes below the SplitPanel-Node. Expecting only {@link SPFrame#TAG_FCONTAINER} and
	 * {@link SPFrame#TAG_FRAME} tags. This method will be called
	 * recursively while running down the xml-structure.
	 * @param eventReader
	 * @param parent - {@link SPFrameContainerXML} that acts as parent, subsequent {@link SPFrameXML} nodes will be added to this one as a
	 *            child.
	 * @throws XMLStreamException
	 * @throws SPTreeException
	 */
	private void fromXML( XMLEventReader eventReader, SPFrameContainerXML parent ) throws XMLStreamException, SPTreeException
	{
		while ( eventReader.hasNext( ) )
		{
			XMLEvent event = eventReader.nextEvent( );
			if ( event.isStartElement( ) )
			{
				StartElement startElement = event.asStartElement( );

				// new frame container
				if ( startElement.getName( ).getLocalPart( ).equals( TAG_FCONTAINER ) )
				{
					// read attributes
					Attribute attrDivPos = startElement.getAttributeByName( new QName( ATTR_DIVIDER_POS ) );
					Attribute attrDivThickness = startElement.getAttributeByName( new QName( ATTR_DIVIDER_THICKNESS ) );
					Attribute attrDivType = startElement.getAttributeByName( new QName( ATTR_DIVIDER_TYPE ) );

					SPFrameContainerXML container = new SPFrameContainerXML( );
					container.setDividerPos( attrDivPos.getValue( ) );
					container.setDividerThickness( attrDivThickness.getValue( ) );
					container.setDividerType( attrDivType.getValue( ) );
					parent.addChild( container );

					this.fromXML( eventReader, container );
					continue;
				}
				// new frame
				else if ( startElement.getName( ).getLocalPart( ).equals( TAG_FRAME ) )
				{
					// read attributes
					Attribute attrContentKey = startElement.getAttributeByName( new QName( ATTR_CONTENT_KEY ) );

					SPSubFrameXML subFrame = new SPSubFrameXML( attrContentKey.getValue( ) );
					parent.addChild( subFrame );
					continue;
				}
				throw new SPTreeException( "Unexpected xml-StartElement found: " + startElement.getName( ).getLocalPart( ) );
			}

			if ( event.isEndElement( ) )
			{
				EndElement endElement = event.asEndElement( );
				// closing container tag found --> return
				if ( endElement.getName( ).getLocalPart( ).equals( TAG_FCONTAINER ) )
					return;
			}
		}
	}

	/**
	 * Streams the settings of this {@link SplitPanel} using a xml-structure.
	 * @param writer
	 * @throws XMLStreamException
	 */
	public void asXMLToStream( Writer writer ) throws XMLStreamException
	{
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance( );
		XMLEventWriter xmlWriter = outputFactory.createXMLEventWriter( writer );
		this.toXML( xmlWriter );
		xmlWriter.flush( );
	}

	/**
	 * Streams the settings of this {@link SplitPanel} using a xml-structure.</br><b>example (stream
	 * to stdout):</b></br><code>SplitPanel sp = ...;</br>sp.asXMLToStream(System.out);</code>
	 * @param stream
	 * @throws XMLStreamException
	 */
	public void asXMLToStream( OutputStream stream ) throws XMLStreamException
	{
		XMLOutputFactory outputFactory = XMLOutputFactory.newInstance( );
		XMLEventWriter xmlWriter = outputFactory.createXMLEventWriter( stream );
		this.toXML( xmlWriter );
		xmlWriter.flush( );
	}

	private void toXML( XMLEventWriter xmlWriter ) throws XMLStreamException
	{
		// Create a EventFactory
		XMLEventFactory eventFactory = XMLEventFactory.newInstance( );
		XMLEvent lineBreak = eventFactory.createDTD( "\n" );

		// Create and write Start Tag
		StartDocument startDocument = eventFactory.createStartDocument( );
		xmlWriter.add( startDocument );
		xmlWriter.add( lineBreak );

		// Create open tag
		xmlWriter.add( eventFactory.createStartElement( "", "", TAG_SPLIT_PANEL ) );
		xmlWriter.add( eventFactory.createAttribute( ATTR_CONTENT_FACTORY, this.contentFactory.getClass( ).getName( ) ) );
		xmlWriter.add( eventFactory.createAttribute( ATTR_CONTENT_FACTORY_NAME, this.contentFactory.getName( ) ) );
		xmlWriter.add( lineBreak );

		// call xml-writing for the sub-frames of the SplitPanel
		this.toXML( xmlWriter, this.root );

		// closing tag
		xmlWriter.add( lineBreak );
		xmlWriter.add( eventFactory.createEndElement( "", "", TAG_SPLIT_PANEL ) );
		xmlWriter.add( lineBreak );

		// end document
		xmlWriter.add( eventFactory.createEndDocument( ) );
	}

	/**
	 * This method writes the {@link SplitPanel} hierarchy using the given instance of {@link XMLEventWriter}. This method is called
	 * recursively on its way down the tree of {@link SPFrame}'s
	 * @param writer
	 * @param frame
	 * @throws XMLStreamException
	 */
	private void toXML( XMLEventWriter writer, SPFrame frame ) throws XMLStreamException
	{
		// Create a EventFactory
		XMLEventFactory eventFactory = XMLEventFactory.newInstance( );
		XMLEvent lineBreak = eventFactory.createDTD( "\n" );

		SPSubFrame subFrame = frame.getSubFrame( );
		if ( subFrame != null )
		{
			writer.add( eventFactory.createStartElement( "", "", TAG_FRAME ) );
			writer.add( eventFactory.createAttribute( ATTR_CONTENT_KEY, subFrame.getCurrentContentKey( ) ) );

			writer.add( lineBreak );
			writer.add( eventFactory.createEndElement( "", "", TAG_FRAME ) );
		}
		SPFrameContainer container = frame.getFrameContainer( );
		if ( container != null )
		{
			writer.add( eventFactory.createStartElement( "", "", TAG_FCONTAINER ) );
			writer.add( eventFactory.createAttribute( ATTR_DIVIDER_TYPE, container.getDivider( ).getType( ).toString( ) ) );
			writer.add( eventFactory.createAttribute( ATTR_DIVIDER_POS, container.getDivider( ).getDividerPosition( ) + "" ) );
			writer.add( eventFactory.createAttribute( ATTR_DIVIDER_THICKNESS, container.getDivider( ).getThickness( ) + "" ) );

			SPFrame firstCh = container.getFirstChild( );
			if ( firstCh != null )
			{
				writer.add( lineBreak );
				this.toXML( writer, firstCh );
			}

			SPFrame secondCh = container.getSecondChild( );
			if ( secondCh != null )
			{
				writer.add( lineBreak );
				this.toXML( writer, secondCh );
			}

			writer.add( lineBreak );
			writer.add( eventFactory.createEndElement( "", "", TAG_FCONTAINER ) );
		}
	}

	/**
	 * Tags
	 */
	public static final String	TAG_FCONTAINER				= "FrameContainer";
	public static final String	TAG_FRAME					= "Frame";
	public static final String	TAG_SPLIT_PANEL				= "SplitPanel";

	/**
	 * Attributes
	 */
	public static final String	ATTR_CONTENT_FACTORY		= "contentFactory";
	public static final String	ATTR_CONTENT_FACTORY_NAME	= "contentFactoryName";
	public static final String	ATTR_CONTENT_KEY			= "contentKey";
	public static final String	ATTR_DIVIDER_TYPE			= "dividerType";
	public static final String	ATTR_DIVIDER_POS			= "dividerPos";
	public static final String	ATTR_DIVIDER_THICKNESS		= "dividerThickness";
}
