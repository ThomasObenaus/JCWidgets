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

/**
 * @author Thomas Obenaus
 * @source SPSubFrameXML.java
 * @date 10.07.2012
 */
public class SPSubFrameXML extends SPFrameXML
{
	private String	contentKey;

	public SPSubFrameXML( String contentKey )
	{
		this.contentKey = contentKey;
	}

	@Override
	public SPSubFrameXML asSubFrame( )
	{
		return this;
	}

	public String getContentKey( )
	{
		return contentKey;
	}

	@Override
	public String toString( )
	{
		return "SubF contentKey=" + this.contentKey;
	}
}
