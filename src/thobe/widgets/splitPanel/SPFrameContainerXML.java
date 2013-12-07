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
 * @source SPFrameContainerXML.java
 * @date 10.07.2012
 */
public class SPFrameContainerXML extends SPFrameXML
{
	private SPFrameXML		firstChild;
	private SPFrameXML		secondChild;

	private SPDividerType	dividerType;
	private double			dividerPos;
	private int				dividerThickness;

	public SPFrameContainerXML( )
	{
		this.firstChild = null;
		this.secondChild = null;
		this.dividerPos = 0.5;
		this.dividerType = SPDividerType.HORIZONTAL;
		this.dividerThickness = 4;
	}

	public void addChild( SPFrameXML child ) throws SPTreeException
	{
		if ( this.firstChild != null && this.secondChild != null )
			throw new SPTreeException( "[SPFrameContainerXML] Error adding a child. Both (first and second) children are already present. Doing nothing to prevent to overwrite one of the childs" );
		if ( this.firstChild == null )
			this.firstChild = child;
		else this.secondChild = child;
	}

	public void setFirstChild( SPFrameXML firstChild )
	{
		this.firstChild = firstChild;
	}

	public void setSecondChild( SPFrameXML secondChild )
	{
		this.secondChild = secondChild;
	}

	public SPFrameXML getFirstChild( )
	{
		return firstChild;
	}

	public SPFrameXML getSecondChild( )
	{
		return secondChild;
	}

	public void setDividerPos( String posAsDoubleStr ) throws SPTreeException
	{
		try
		{
			this.dividerPos = Double.parseDouble( posAsDoubleStr );
		}
		catch ( NumberFormatException e )
		{
			throw new SPTreeException( "[SPFrameContainerXML] Error parsing the dividerPos value: " + e.getLocalizedMessage( ) + " taking default value (" + this.dividerPos + ")" );
		}
	}

	public void setDividerThickness( String thicknessAsIntegerStr ) throws SPTreeException
	{
		try
		{
			this.dividerThickness = Integer.parseInt( thicknessAsIntegerStr );
		}
		catch ( NumberFormatException e )
		{
			throw new SPTreeException( "[SPFrameContainerXML] Error parsing the dividerThickness value: " + e.getLocalizedMessage( ) + " taking default value (" + this.dividerThickness + ")" );
		}
	}

	public void setDividerType( String typeAsSPDividerTypeString )
	{
		this.dividerType = SPDividerType.valueOf( typeAsSPDividerTypeString );
	}

	public double getDividerPos( )
	{
		return dividerPos;
	}

	public int getDividerThickness( )
	{
		return dividerThickness;
	}

	public SPDividerType getDividerType( )
	{
		return dividerType;
	}

	@Override
	public SPFrameContainerXML asContainer( )
	{
		return this;
	}

	@Override
	public String toString( )
	{
		return "FC divType=" + this.dividerType + " divPos=" + this.dividerPos + " divThickness=" + this.dividerThickness;
	}

}
