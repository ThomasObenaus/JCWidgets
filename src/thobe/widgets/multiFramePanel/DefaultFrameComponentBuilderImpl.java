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
 * @source DefaultFrameComponentBuilderImpl.java
 * @date 10.05.2010
 */
public class DefaultFrameComponentBuilderImpl implements DefaultFrameComponentBuilder
{

	@Override
	public FrameComponent createDefaultFrameComponent( )
	{
		return new DefaultFrameComponent( );
	}

}
