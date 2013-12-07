/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.textfield;

/**
 * @author Thomas Obenaus
 * @source RestrictedTextFieldMessages.java
 * @date 01.12.2009
 */
public interface RestrictedTextFieldMessageBuilder
{
	public String createRangeExceptionMsg( boolean lowerBoundExeeded, String exeededBound );

	public String createConvertExceptionMsg( String valueToConvert, String targetType );

	public String createInvalidValueExceptionMsg( String valueToConvert, String errorMessage );
}
