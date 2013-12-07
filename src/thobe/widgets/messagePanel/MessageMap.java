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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * @author Thomas Obenaus
 * @source MessageMap.java
 * @date 12.08.2009
 */
public class MessageMap
{
	/**
	 * All registered Messages: HasMap< hash-code of the registering class, HashMap< ID of the Message,the Message>>
	 */
	private HashMap<Integer, HashMap<Integer, Message>>	registeredMessages;

	public MessageMap( )
	{
		this.registeredMessages = new HashMap<Integer, HashMap<Integer, Message>>( );

	}

	/**
	 * Registers a new Message
	 * @param registeringClass - the class registering the message
	 * @param message
	 * @return - returns true if the MessageMap has changed due to the add of the Message, false otherwise
	 */
	public boolean registerMessage( Class<?> registeringClass, Message message )
	{
		HashMap<Integer, Message> messagesOfRegisteringClass = this.registeredMessages.get( registeringClass.hashCode( ) );
		if ( messagesOfRegisteringClass == null )
		{
			messagesOfRegisteringClass = new HashMap<Integer, Message>( );
			this.registeredMessages.put( registeringClass.hashCode( ), messagesOfRegisteringClass );
		}

		Message m = messagesOfRegisteringClass.put( message.getID( ), message );
		if ( m == null || !m.equals( message ) )
			return true;

		return false;

	}

	/**
	 * Unregisters a new Message
	 * @param registeringClass - the class unregistering the message
	 * @param message
	 * @return - returns true if the MessageMap has changed due to the removal of the Message, false otherwise
	 */
	public boolean unregisterMessage( Class<?> registeringClass, Message message )
	{
		HashMap<Integer, Message> messagesOfRegisteringClass = this.registeredMessages.get( registeringClass.hashCode( ) );
		if ( messagesOfRegisteringClass == null )
			return false;

		Message m = messagesOfRegisteringClass.remove( message.getID( ) );
		if ( m != null )
			return true;

		return false;
	}

	/**
	 * Clears all messages that were registered by the given class
	 * @param registeringClass - the class whose messages should be cleared
	 */
	public void clearMessages( Class<?> registeringClass )
	{
		HashMap<Integer, Message> messagesOfRegisteringClass = this.registeredMessages.get( registeringClass.hashCode( ) );
		if ( messagesOfRegisteringClass == null )
			return;
		messagesOfRegisteringClass.clear( );
	}

	/**
	 * Returns all messages that were registered by the given class
	 * @param registeringClass - the class whose messages should be returned
	 * @return - A list of messages registered by the class with the given hascode
	 */
	public List<Message> getMessages( Class<?> registeringClass )
	{
		List<Message> messages = new ArrayList<Message>( );
		HashMap<Integer, Message> messagesOfRegisteringClass = this.registeredMessages.get( registeringClass.hashCode( ) );
		if ( messagesOfRegisteringClass == null )
			return messages;

		for ( Entry<Integer, Message> entry : messagesOfRegisteringClass.entrySet( ) )
			messages.add( entry.getValue( ) );

		return messages;
	}

	/**
	 * Returns all messages that were registered by the given class and are of the given MessageCategory
	 * @param registeringClass - the class whose messages should be returned
	 * @param category - the MessageCategory we are looking for
	 * @return - A list of messages registered by the class with the given hascode and are of the given MessageCategory
	 */
	public List<Message> getMessages( Class<?> registeringClass, MessageCategory category )
	{
		List<Message> messages = new ArrayList<Message>( );
		HashMap<Integer, Message> messagesOfRegisteringClass = this.registeredMessages.get( registeringClass.hashCode( ) );
		if ( messagesOfRegisteringClass == null )
			return messages;

		for ( Entry<Integer, Message> entry : messagesOfRegisteringClass.entrySet( ) )
		{
			if ( entry.getValue( ).getCategory( ) == category )
				messages.add( entry.getValue( ) );
		}

		return messages;
	}

	/**
	 * Returns all messages that are of the given category
	 * @param category - the MessageCategory we are looking for
	 * @return - A list of messages that are of the given category
	 */
	public List<Message> getMessages( MessageCategory category )
	{
		List<Message> messages = new ArrayList<Message>( );

		for ( Entry<Integer, HashMap<Integer, Message>> entry : this.registeredMessages.entrySet( ) )
		{
			for ( Entry<Integer, Message> mess : entry.getValue( ).entrySet( ) )
			{
				if ( mess.getValue( ).getCategory( ) == category )
					messages.add( mess.getValue( ) );
			}
		}

		return messages;
	}

	/**
	 * Returns the number of registered Messages
	 * @return
	 */
	public int getNumberOfRegisteredMessages( )
	{
		int numMessages = 0;
		for ( Entry<Integer, HashMap<Integer, Message>> entry : this.registeredMessages.entrySet( ) )
		{
			numMessages += entry.getValue( ).keySet( ).size( );
		}
		return numMessages;
	}

	/**
	 * Returns the number of Messages with the given MessageCategory
	 * @param category
	 * @return
	 */
	public int getNumberOfRegisteredMessages( MessageCategory category )
	{
		int numMessages = 0;
		for ( Entry<Integer, HashMap<Integer, Message>> entry : this.registeredMessages.entrySet( ) )
		{
			for ( Entry<Integer, Message> mess : entry.getValue( ).entrySet( ) )
			{
				if ( mess.getValue( ).getCategory( ) == category )
					numMessages++;
			}
		}
		return numMessages;
	}

	/**
	 * Returns the number of Messages with the given MessageCategory for the given registering Class
	 * @param registeringClass - the class whose number of messages should be returned
	 * @param category
	 * @return
	 */
	public int getNumberOfRegisteredMessages( Class<?> registeringClass, MessageCategory category )
	{
		int numMessages = 0;

		HashMap<Integer, Message> messagesOfRegisteringClass = this.registeredMessages.get( registeringClass.hashCode( ) );
		if ( messagesOfRegisteringClass == null )
			return 0;

		for ( Entry<Integer, Message> mess : messagesOfRegisteringClass.entrySet( ) )
		{
			if ( mess.getValue( ).getCategory( ) == category )
				numMessages++;
		}
		return numMessages;
	}
}
