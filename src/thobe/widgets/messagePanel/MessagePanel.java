/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.messagePanel;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import thobe.widgets.utils.Utilities;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Thomas Obenaus
 * @source MessagePanel.java
 * @date 26 Aug 2008
 */
@SuppressWarnings ( "serial")
public class MessagePanel extends JPanel
{
	private MessageMap	messageMap;
	private FontMetrics	fontMetrics;

	public MessagePanel( )
	{
		this.messageMap = new MessageMap( );
		this.fontMetrics = this.getFontMetrics( this.getFont( ) );
		this.buildGUI( );

		this.addComponentListener( new ComponentAdapter( )
		{
			@Override
			public void componentResized( ComponentEvent e )
			{
				buildGUI( );

			}
		} );
	}

	/**
	 * Registers a new Message
	 * @param registeringClass - the class registering the message
	 * @param message
	 */
	public void registerMessage( Class<?> registeringClass, Message message )
	{
		if ( this.messageMap.registerMessage( registeringClass, message ) )
			this.updateMessagePanel( );

	}

	/**
	 * Unregisters a new Message
	 * @param registeringClass - the class unregistering the message
	 * @param message
	 */
	public void unregisterMessage( Class<?> registeringClass, Message message )
	{
		if ( this.messageMap.unregisterMessage( registeringClass, message ) )
			this.updateMessagePanel( );
	}

	/**
	 * Clears all messages that were registered by the given class
	 * @param registeringClass - the class whose messages should be cleared
	 */
	public void clearMessages( Class<?> registeringClass )
	{
		this.messageMap.clearMessages( registeringClass );
		this.updateMessagePanel( );
	}

	/**
	 * Returns the number of registered Messages
	 * @return
	 */
	public int getNumberOfRegisteredMessages( )
	{
		return this.messageMap.getNumberOfRegisteredMessages( );
	}

	/**
	 * Returns the number of Messages with the given MessageCategory
	 * @param category
	 * @return
	 */
	public int getNumberOfRegisteredMessages( MessageCategory category )
	{
		return this.messageMap.getNumberOfRegisteredMessages( category );
	}

	/**
	 * Returns the number of Messages with the given MessageCategory for the given registering Class
	 * @param registeringClass - the class whose number of messages should be returned
	 * @param category
	 * @return
	 */
	public int getNumberOfRegisteredMessages( Class<?> registeringClass, MessageCategory category )
	{
		return this.messageMap.getNumberOfRegisteredMessages( registeringClass, category );
	}

	private void updateMessagePanel( )
	{
		this.buildGUI( );
	}

	private void buildGUI( )
	{
		this.removeAll( );
		if ( this.getNumberOfRegisteredMessages( ) == 0 )
		{
			this.setVisible( false );
			return;
		}

		this.setVisible( true );

		int width = this.getParent( ).getWidth( );

		List<Message> errorMessages = this.messageMap.getMessages( MessageCategory.ERROR );
		List<Message> warningMessages = this.messageMap.getMessages( MessageCategory.WARNING );
		List<Message> infoMessages = this.messageMap.getMessages( MessageCategory.INFO );

		StringBuffer rowSpecBuffer = new StringBuffer( "" );
		for ( int i = 0; i < this.getNumberOfRegisteredMessages( ); i++ )
			rowSpecBuffer.append( "1dlu,top:pref," );

		String rowSpec = rowSpecBuffer.toString( );
		if ( !rowSpec.equals( "" ) )
			rowSpec += "1dlu";

		FormLayout fla_content = new FormLayout( "1dlu,3dlu,1dlu,fill:pref:grow,1dlu", rowSpec );
		CellConstraints cc_content = new CellConstraints( );

		this.setLayout( fla_content );
		/* error-messages */
		for ( int i = 0; i < errorMessages.size( ); i++ )
		{
			Color messageColor = Color.red;
			Message message = errorMessages.get( i );

			JLabel l_message = new JLabel( Utilities.resizeStringToMaxWidthHTML( this.fontMetrics, message.getText( ), width ) );
			l_message.setForeground( messageColor );
			this.add( l_message, cc_content.xy( 4, i * 2 + 2 ) );
			JLabel l_sign = new JLabel( "-" );
			l_sign.setForeground( messageColor );
			this.add( l_sign, cc_content.xy( 2, i * 2 + 2 ) );
		}

		/* warning-messages */
		for ( int i = 0; i < warningMessages.size( ); i++ )
		{
			Color messageColor = Color.DARK_GRAY;
			Message message = warningMessages.get( i );

			JLabel l_message = new JLabel( Utilities.resizeStringToMaxWidthHTML( this.fontMetrics, message.getText( ), width ) );
			l_message.setForeground( messageColor );
			this.add( l_message, cc_content.xy( 4, ( i + errorMessages.size( ) ) * 2 + 2 ) );
			JLabel l_sign = new JLabel( "-" );
			l_sign.setForeground( messageColor );
			this.add( l_sign, cc_content.xy( 2, ( i + errorMessages.size( ) ) * 2 + 2 ) );
		}

		/* info-messages */
		for ( int i = 0; i < infoMessages.size( ); i++ )
		{
			Color messageColor = Color.BLACK;
			Message message = infoMessages.get( i );

			JLabel l_message = new JLabel( Utilities.resizeStringToMaxWidthHTML( this.fontMetrics, message.getText( ), width ) );
			l_message.setForeground( messageColor );
			this.add( l_message, cc_content.xy( 4, ( i + errorMessages.size( ) + warningMessages.size( ) ) * 2 + 2 ) );
			JLabel l_sign = new JLabel( "-" );
			l_sign.setForeground( messageColor );
			this.add( l_sign, cc_content.xy( 2, ( i + errorMessages.size( ) + warningMessages.size( ) ) * 2 + 2 ) );
		}

		this.repaint( );
		this.revalidate( );
		this.validate( );
		this.invalidate( );
	}

}
