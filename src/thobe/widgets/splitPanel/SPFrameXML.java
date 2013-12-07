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
 * @source SPFrameXML.java
 * @date 10.07.2012
 */
public abstract class SPFrameXML
{
	public SPFrameContainerXML asContainer( )
	{
		return null;
	}

	public SPSubFrameXML asSubFrame( )
	{
		return null;
	}

	public static SPFrame build( SPContentFactory contentFactory, SPFrameXML xmlNode, boolean isFirstChild ) throws SPTreeException
	{
		SPFrameContainerXML containerXML = xmlNode.asContainer( );
		if ( containerXML != null )
		{
			SPFrameXML firstChild = containerXML.getFirstChild( );
			SPFrameXML secondChild = containerXML.getSecondChild( );

			SPFrameContainer container = null;
			if ( firstChild != null && secondChild != null )
				container = new SPFrameContainer( contentFactory.getNewFrameID( ), isFirstChild, build( contentFactory, firstChild, true ), build( contentFactory, secondChild, false ), new SPDivider( containerXML.getDividerType( ), containerXML.getDividerPos( ) ) );
			else if ( firstChild != null && secondChild == null )
				container = new SPFrameContainer( contentFactory.getNewFrameID( ), isFirstChild, build( contentFactory, firstChild, true ) );
			else throw new SPTreeException( "Error building SPFrame-Tree from XML, xml-container-node found that has no children. Nodes without children should be leaf-nodes instead of containers!" );

			return container;
		}
		else
		{
			SPSubFrameXML subFrameXML = xmlNode.asSubFrame( );
			SPSubFrame subFrame = new SPSubFrame( contentFactory.getNewFrameID( ), isFirstChild, contentFactory, subFrameXML.getContentKey( ) );
			return subFrame;
		}
	}

	public static void print( SPFrameXML frame, int tabs )
	{
		SPFrameContainerXML container = frame.asContainer( );
		if ( container != null )
		{
			System.out.println( getNTabs( tabs ) + container );
			SPFrameXML fChild = container.getFirstChild( );
			if ( fChild != null )
				print( fChild, tabs + 1 );

			SPFrameXML sChild = container.getSecondChild( );
			if ( sChild != null )
				print( sChild, tabs + 1 );
			return;
		}

		System.out.println( getNTabs( tabs ) + frame.asSubFrame( ) );
	}

	private static String getNTabs( int n )
	{
		String nTabs = "";
		for ( int i = 0; i < n; i++ )
			nTabs += "\t";
		return nTabs;
	}
}
