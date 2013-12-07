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

/**
 * @author Thomas Obenaus
 * @source AbstractMultiFrameLayout.java
 * @date 25.04.2010
 */
public abstract class AbstractMultiFrameLayout implements MultiFrameLayout
{
	private boolean			dragging;
	private FrameContainer	fcUnderCursor;

	public AbstractMultiFrameLayout( )
	{
		this.dragging = false;
	}

	@Override
	public void doLayout( BoundingBox boundingBox )
	{
		FrameTreeNode root = this.getFrameTreeRoot( );
		if ( root == null )
			return;
		root.setBounds( boundingBox.getX( ), boundingBox.getY( ), boundingBox.getWidth( ), boundingBox.getHeight( ) );
		root.doLayout( );
		if ( root.getFrame( ) != null )
			root.getFrame( ).setFrameVisible( true );
	}

	@Override
	public void onMouseExited( )
	{
		if ( this.fcUnderCursor == null )
			return;

		Divider div = fcUnderCursor.getDivider( );
		div.setUnderCursor( false );

	}

	@Override
	public void onMouseDragged( int x, int y )
	{
		if ( this.fcUnderCursor == null )
			return;

		Divider div = fcUnderCursor.getDivider( );
		div.setDragging( this.dragging );

		BoundingBox bbox = fcUnderCursor.getBoundingBox( );
		/* relative x-position within the FrameContainer currently under cursor 
		 * relX = absX - fcUnderCursor.getBoundingBox().getX();
		 */
		int relX = x - bbox.getX( );
		/* relative y-position within the FrameContainer currently under cursor 
		 * relY = absY - fcUnderCursor.getBoundingBox().getY();
		 */
		int relY = y - bbox.getY( );

		double newDividerPosition = 0;
		switch ( div.getType( ) )
		{
		case HORIZONTAL:
			int minTop = fcUnderCursor.getFirstChild( ).getMinHeight( );
			int minBottom = fcUnderCursor.getSecondChild( ).getMinHeight( );

			if ( relY < minTop )
				relY = minTop;
			if ( relY > bbox.getHeight( ) - minBottom )
				relY = bbox.getHeight( ) - minBottom;

			newDividerPosition = relY / ( double ) bbox.getHeight( );

			div.setBounds( bbox.getX( ), relY + bbox.getY( ), bbox.getWidth( ), div.getThickness( ) );
			break;
		case VERTICAL:
			int minLeft = fcUnderCursor.getFirstChild( ).getMinWidth( );
			int minRight = fcUnderCursor.getSecondChild( ).getMinWidth( );

			if ( relX < minLeft )
				relX = minLeft;
			if ( relX > bbox.getWidth( ) - minRight )
				relX = bbox.getWidth( ) - minRight;

			newDividerPosition = relX / ( double ) bbox.getWidth( );

			div.setBounds( relX + bbox.getX( ), bbox.getY( ), div.getThickness( ), bbox.getHeight( ) );
			break;
		}

		div.setDividerPosition( newDividerPosition );

	}

	@Override
	public void onMouseMoved( int x, int y )
	{
		if ( this.fcUnderCursor != null )
		{
			this.fcUnderCursor.getDivider( ).setDragging( false );
			this.fcUnderCursor.getDivider( ).setUnderCursor( false );
		}

		this.fcUnderCursor = this.getFrameContainerWithDividerUnderCursor( this.getFrameTreeRoot( ), x, y );
		if ( this.fcUnderCursor == null )
			return;

		fcUnderCursor.getDivider( ).setDragging( this.dragging );
		fcUnderCursor.getDivider( ).setUnderCursor( true );

	}

	@Override
	public FrameContainer getFrameContainerWithDividerUnderCursor( FrameTreeNode node, int x, int y )
	{
		FrameContainer fcUnderCursor = node.getFrameContainer( );
		if ( fcUnderCursor == null )
			return null;

		/* check the containers bbox */
		BoundingBox bbox = fcUnderCursor.getBoundingBox( );
		if ( !bbox.isInside( x, y ) )
			return null;

		/* check the dividers bbox */
		if ( fcUnderCursor.getDivider( ).getBoundingBox( ).isInside( x, y ) )
			return fcUnderCursor;

		/* decent to the children of the container */
		FrameContainer fcLeft = this.getFrameContainerWithDividerUnderCursor( fcUnderCursor.getFirstChild( ), x, y );
		if ( fcLeft != null )
			return fcLeft;

		FrameContainer fcRight = this.getFrameContainerWithDividerUnderCursor( fcUnderCursor.getSecondChild( ), x, y );
		if ( fcRight != null )
			return fcRight;

		return null;
	}

	@Override
	public void onMousePressed( int x, int y )
	{
		this.dragging = true;
		if ( this.fcUnderCursor != null )
			fcUnderCursor.getDivider( ).setDragging( this.dragging );

	}

	@Override
	public void onMouseReleased( int x, int y )
	{
		this.dragging = false;
		if ( this.fcUnderCursor != null )
			fcUnderCursor.getDivider( ).setDragging( this.dragging );
	}
}
