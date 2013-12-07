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

/**
 * @author Thomas Obenaus
 * @source Message.java
 * @date 12.08.2009
 */
public class Message
{
	private MessageCategory	category;
	private String			text;
	private int				ID;

	public Message( int ID, String text, MessageCategory category )
	{
		this.category = category;
		this.text = text;
		this.ID = ID;
	}

	public MessageCategory getCategory( )
	{
		return category;
	}

	public int getID( )
	{
		return ID;
	}

	public String getText( )
	{
		return text;
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj )
			return true;
		if ( obj == null || obj.getClass( ) != this.getClass( ) )
			return false;

		Message m = ( Message ) obj;
		if ( this.category != m.category )
			return false;
		if ( this.ID != m.ID )
			return false;
		if ( !this.text.equals( m.text ) )
			return false;

		return true;
	}

	public void setText( String text )
	{
		this.text = text;
	}

	@Override
	public int hashCode( )
	{
		int h = 7;
		h += ( null == this.text ? 0 : this.text.hashCode( ) );
		h += ( null == this.category ? 0 : this.category.hashCode( ) );
		h *= 31;
		return h;
	}
}
