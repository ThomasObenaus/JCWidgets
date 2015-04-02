/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.utils;

import java.awt.Component;
import java.awt.FontMetrics;
import java.util.ArrayList;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * @author Thomas Obenaus
 * @source Utilities.java
 * @date 13.12.2006
 */
/**
 * Class offering some usefull tools/ utilities.
 */
public class Utilities
{
	/**
	 * Uses html-linebreaks ('<br>
	 * ') to resize the given string so that its width is smaller than the given parameter maxWidth.
	 * Be careful!: the height of the string displayed in a component that is able to interpret
	 * html-tags will grow with every linebreak added to the string.
	 * @param fontMetrics - the FontMetrics used by the Component within the resulting string should
	 *            be placed in. The FontMetrics can be achieved using the function
	 *            Component.getGraphics().getFontMetric().
	 * @param strToResize - The string that should be resized.
	 * @param maxWidth - The width the string should not exceed.
	 * @return
	 */
	public static String resizeStringToMaxWidthHTML( FontMetrics fontMetrics, String strToResize, int maxWidth )
	{
		return resizeStringToMaxWidthHTML( fontMetrics, strToResize, maxWidth, false );
	}

	/**
	 * Uses html-linebreaks ('<br>
	 * ') to resize the given string so that its width is smaller than the given parameter maxWidth.
	 * Be careful!: the height of the string displayed in a component that is able to interpret
	 * html-tags will grow with every linebreak added to the string.
	 * @param fontMetrics - the FontMetrics used by the Component within the resulting string should
	 *            be placed in. The FontMetrics can be achieved using the function
	 *            Component.getGraphics().getFontMetric().
	 * @param strToResize - The string that should be resized.
	 * @param maxWidth - The width the string should not exceed.
	 * @param isInsideHTMLTag - if true, the leading and the trailing 'html-tag' won't be generated.
	 * @return
	 */
	public static String resizeStringToMaxWidthHTML( FontMetrics fontMetrics, String strToResize, int maxWidth, boolean isInsideHTMLTag )
	{
		if ( strToResize == null )
			return "";
		maxWidth *= 0.7;

		// split words with the blank
		String[] words = strToResize.split( " " );

		// split path too
		ArrayList<String> subWords = new ArrayList<String>( );
		for ( int i = 0; i < words.length; i++ )
		{
			words[i] = words[i] + " ";
			String[] tmp = words[i].split( "\\\\" );
			if ( tmp.length > 1 )
			{
				for ( int n = 0; n < tmp.length; n++ )
				{
					subWords.add( tmp[n] );
					if ( ( tmp.length > 1 ) && ( n < ( tmp.length - 1 ) ) )
						subWords.add( "\\" );
				}
			}
			else
			{
				tmp = words[i].split( "/" );
				for ( int n = 0; n < tmp.length; n++ )
				{
					subWords.add( tmp[n] );
					if ( ( tmp.length > 1 ) && ( n < ( tmp.length - 1 ) ) )
						subWords.add( "/" );
				}
			}
		}

		int widthCounter = 0;
		StringBuffer result = new StringBuffer( );
		if ( !isInsideHTMLTag )
		{
			result.append( "<html>" );
		}

		for ( int i = 0; i < subWords.size( ); i++ )
		{
			widthCounter += fontMetrics.stringWidth( subWords.get( i ) );
			if ( widthCounter >= maxWidth )
			{
				result.append( "<br>" );
				widthCounter = 0;
			}
			result.append( subWords.get( i ) );
		}

		if ( !isInsideHTMLTag )
		{
			result.append( "</html>" );
		}

		return result.toString( );
	}

	public static String resizeStringToMaxWidth( FontMetrics fontMetrics, String strToResize, int maxWidth )
	{
		if ( strToResize == null )
			return "";
		maxWidth *= 0.7;

		// split words with the blank
		String[] words = strToResize.split( " " );

		// split path too
		ArrayList<String> subWords = new ArrayList<String>( );
		for ( int i = 0; i < words.length; i++ )
		{
			words[i] = words[i] + " ";
			String[] tmp = words[i].split( "\\\\" );
			if ( tmp.length > 1 )
			{
				for ( int n = 0; n < tmp.length; n++ )
				{
					subWords.add( tmp[n] );
					if ( ( tmp.length > 1 ) && ( n < ( tmp.length - 1 ) ) )
						subWords.add( "\\" );
				}
			}
			else
			{
				tmp = words[i].split( "/" );
				for ( int n = 0; n < tmp.length; n++ )
				{
					subWords.add( tmp[n] );
					if ( ( tmp.length > 1 ) && ( n < ( tmp.length - 1 ) ) )
						subWords.add( "/" );
				}
			}
		}

		int widthCounter = 0;
		StringBuffer result = new StringBuffer( "" );
		for ( int i = 0; i < subWords.size( ); i++ )
		{
			widthCounter += fontMetrics.stringWidth( subWords.get( i ) );
			if ( widthCounter >= maxWidth )
			{
				result.append( "\n" );
				widthCounter = 0;
			}
			result.append( subWords.get( i ) );
		}

		return result.toString( );
	}

	/**
	 * Takes the array of strings and returns one String which holds these Strings separated by the
	 * given separator.
	 * @param strArr
	 * @param separator
	 * @return
	 */
	public static String strArr2Str( String[] strArr, String separator )
	{
		StringBuffer temp = new StringBuffer( "" );
		for ( String str : strArr )
			temp.append( str + separator );

		String result = temp.toString( );
		if ( result.endsWith( separator ) )
		{
			int pos = result.lastIndexOf( separator );
			if ( pos > 0 )
				result = result.substring( 0, pos );
		}
		return result;
	}

	/**
	 * Takes the array of strings and returns one String which holds these Strings separated by the
	 * given separator.
	 * @param strArr
	 * @param seperator
	 * @return
	 */
	public static String strArr2Str( ArrayList<String> strArr, String separator )
	{
		StringBuffer temp = new StringBuffer( "" );
		for ( String str : strArr )
			temp.append( str + separator );

		String result = temp.toString( );
		if ( result.endsWith( separator ) )
		{
			int pos = result.lastIndexOf( separator );
			if ( pos > 0 )
				result = result.substring( 0, pos );
		}
		return result;
	}

	public static void printInstalledPLAFs( )
	{
		UIManager.LookAndFeelInfo plaf[] = UIManager.getInstalledLookAndFeels( );
		for ( int i = 0, n = plaf.length; i < n; i++ )
		{
			System.out.println( "Name: " + plaf[i].getName( ) );
			System.out.println( "  Class name: " + plaf[i].getClassName( ) );
		}
	}

	/**
	 * Sets the platform look and feel for the given {@link Component}.
	 * @param c
	 * @param lookAndFeel
	 */
	public static void setLookAndFeel( Component c, LookAndFeel lookAndFeel )
	{
		try
		{
			UIManager.setLookAndFeel( lookAndFeel.getClassName( ) );
			SwingUtilities.updateComponentTreeUI( c );
			c.repaint( );
			c.revalidate( );
		}
		catch ( Exception e )
		{
			System.err.println( "Error setting PLAF [" + lookAndFeel + "]: " + e.getLocalizedMessage( ) );
		}
	}

}
