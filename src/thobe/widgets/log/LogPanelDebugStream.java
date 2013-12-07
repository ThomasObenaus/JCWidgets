/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.log;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Thomas Obenaus
 * @source LogPanelDebugStream.java
 * @date 14.06.2009
 */
public class LogPanelDebugStream extends OutputStream
{

	private LogPanel		logPanel;
	private StringBuffer	buffer;

	public LogPanelDebugStream( LogPanel logPanel )
	{
		this.buffer = new StringBuffer( );
		this.logPanel = logPanel;
	}

	@Override
	public void write( int b ) throws IOException
	{
		if ( ( char ) b == '\n' )
		{
			this.logPanel.addDebug( buffer.toString( ) );
			this.buffer = new StringBuffer( );
		}
		else buffer.append( ( char ) b );

	}

}
