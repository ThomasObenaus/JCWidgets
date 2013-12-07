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

import java.awt.Component;

import javax.swing.JPanel;

/**
 * @author Thomas Obenaus
 * @source SPContent.java
 * @date 17.02.2012
 */
public class SPContent
{
	private String		contentKey;
	private Component	content;
	private JPanel		toolBar;

	public SPContent( String contentKey, Component content )
	{
		this( contentKey, content, null );
	}

	public SPContent( String contentKey, Component content, JPanel toolBar )
	{
		this.contentKey = contentKey;
		this.content = content;
		this.toolBar = toolBar;
	}

	public Component getComponent( )
	{
		return content;
	}

	public JPanel getToolBar( )
	{
		return toolBar;
	}

	public String getContentKey( )
	{
		return contentKey;
	}
}
