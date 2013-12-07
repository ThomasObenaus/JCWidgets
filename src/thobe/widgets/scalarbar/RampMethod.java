/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.scalarbar;

/**
 * @author Thomas Obenaus
 * @source RampMethod.java
 * @date 19.01.2009
 */
public enum RampMethod
{
	RAMP_LINEAR( "Linear Ramp" ), RAMP_SQRT( "Sqrt Ramp" ), RAMP_SCURVE( "SCurve Ramp" );
	private String	name;

	private RampMethod( String name )
	{
		this.name = name;
	}

	public String toString( )
	{
		return this.name;
	}

	public static RampMethod[] getAvailableRampMethods( )
	{
		return new RampMethod[]
		{ RAMP_LINEAR, RAMP_SCURVE, RAMP_SQRT };
	}

	public static RampMethod parseRampMethod( String nameOfRampMethod )
	{
		if ( nameOfRampMethod.equals( RAMP_LINEAR.name ) )
			return RAMP_LINEAR;
		if ( nameOfRampMethod.equals( RAMP_SQRT.name ) )
			return RAMP_SQRT;
		return RAMP_SCURVE;
	}
}
