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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JTextField;

import thobe.widgets.icons_internal.IconLib_Internal;

/**
 * Abstract class representing a TextField whose specific implementation can be used to restrict the
 * values accepted by the TextField. For example the TextField can be restricted to accept only
 * Doubles or Integers. To get such a special TextField you have only to extend from this class.
 * Therefore the functions isValidValue(value:String):boolean and
 * renderString(strToRender:String):String have to be implemented. The function isValidValue should
 * only return true if the given value should be accepted by the TextField and false otherwise. The
 * with the Function renderString() you can specify how a valid value should be drawn into the
 * TextField. By adding an RestrictedTextfieldListener to the TextField you can listen for
 * value-change-events (occurs every time a keytype results in a valid TextField value) and you can
 * listen for value-change-commit-events (occurs every time if the focus of the TextField is lost or
 * if enter was pressed during the TextField has focus).
 * @author Thomas Obenaus
 * @source RestrictedTextfield.java
 * @date 12 Jun 2008
 */
@SuppressWarnings ( "serial")
public abstract class RestrictedTextfield<T> extends JTextField
{
	private static final int						iconSize			= 9;
	protected static final String					defaultFormatString	= "%.3f";

	/**
	 * Last working value
	 */
	protected T										lastValue;

	/**
	 * Attached listeners
	 */
	private ArrayList<RestrictedTextfieldListener>	listeners;

	/**
	 * images
	 */
	private ImageIcon								errImage;
	private ImageIcon								warnImage;
	private ImageIcon								infoImage;

	/**
	 * Value that represents the lower bound of the allowed interval
	 */
	protected T										minValue;

	/**
	 * Value that represents the upper bound of the allowed interval
	 */
	protected T										maxValue;

	/**
	 * User defined message
	 */
	private MessageType								messageType;
	private String									message;

	/**
	 * Internal message
	 */
	private MessageType								internalMessageType;
	private String									internalMessage;

	/**
	 * Formatstring used to render floating-point numbers
	 */
	protected String								formatString;

	/**
	 * User-defined tooltip-text
	 */
	private String									toolTipText;

	private boolean									contentValid;

	private boolean									automaticCorrectionOnFocusLost;

	private RestrictedTextFieldMessageBuilder		messageBuilder;

	private boolean									commitOnFocusLost	= true;

	private T										lastCommitedValue;

	/**
	 * Ctor
	 * @param automaticCorrectionOnFocusLost - if true an invalid input will be reset to the last
	 *            valid value automatically
	 */
	public RestrictedTextfield( boolean automaticCorrectionOnFocusLost )
	{
		this( 0, automaticCorrectionOnFocusLost );
	}

	/**
	 * Ctor
	 * @param columns - number of colums
	 * @param automaticCorrectionOnFocusLost - if true an invalid input will be reset to the last
	 *            valid value automatically
	 */
	public RestrictedTextfield( int columns, boolean automaticCorrectionOnFocusLost )
	{
		super( columns );
		this.lastCommitedValue = null;
		this.messageBuilder = null;
		this.contentValid = true;
		this.errImage = IconLib_Internal.get( ).getErrorIcon( );
		this.warnImage = IconLib_Internal.get( ).getWarnIcon( );
		this.infoImage = IconLib_Internal.get( ).getInfoIcon( );
		int m = iconSize / 2;
		this.setMargin( new Insets( m, m, m, m ) );
		this.automaticCorrectionOnFocusLost = automaticCorrectionOnFocusLost;
		this.toolTipText = "";
		this.message = "";
		this.internalMessage = "";
		this.internalMessageType = MessageType.NONE;
		this.messageType = MessageType.NONE;
		this.lastValue = this.getDefaultValue( );
		this.minValue = null;
		this.maxValue = null;
		this.listeners = new ArrayList<RestrictedTextfieldListener>( );
		super.setText( this.convertTypeToString( this.getDefaultValue( ) ) );
		this.addFocusListener( new FocusListener( )
		{
			public void focusGained( FocusEvent arg0 )
			{}

			public void focusLost( FocusEvent arg0 )
			{
				if ( commitOnFocusLost )
					updateValue( getText( ), RestrictedTextfield.this.automaticCorrectionOnFocusLost );
			}

		} );
		this.addActionListener( new ActionListener( )
		{
			public void actionPerformed( ActionEvent e )
			{
				updateValue( getText( ), true );
			}
		} );

		super.addKeyListener( new KeyAdapter( )
		{

			@Override
			public void keyReleased( KeyEvent e )
			{
				handleValueChanged( getText( ) );
			}

		} );

		this.addMouseMotionListener( new MouseMotionAdapter( )
		{
			@Override
			public void mouseMoved( MouseEvent e )
			{
				onMouseMoved( e );
			}
		} );
	}

	public void setCommitOnFocusLost( boolean commitOnFocusLost )
	{
		this.commitOnFocusLost = commitOnFocusLost;
	}

	public void addKeyListener( KeyListener keyListener )
	{
		super.addKeyListener( keyListener );
	}

	/**
	 * Displays-errors (conversion and range, if needed). Performs no auto-correction.
	 * @param content
	 */
	private void handleValueChanged( String content )
	{
		try
		{
			T value = this.convertStringToType( content );
			this.validateRange( value );

			this.lastValue = value;
			clearInternalMessage( );
			contentValid = true;
			fireValueChanged( );
		}
		catch ( ConvertException e )
		{
			String errMsg = e.getLocalizedMessage( );
			if ( this.messageBuilder != null )
				errMsg = this.messageBuilder.createConvertExceptionMsg( e.getValueToConvert( ), e.getTargetType( ) );
			this.setInternalMessage( errMsg, MessageType.ERROR );
			fireValueInvalid( );
		}
		catch ( InvalidValueExeption e )
		{
			String errMsg = e.getLocalizedMessage( );
			if ( this.messageBuilder != null )
				errMsg = this.messageBuilder.createInvalidValueExceptionMsg( e.getValueToConvert( ), e.getErrorMessage( ) );
			this.setInternalMessage( errMsg, MessageType.ERROR );
			fireValueInvalid( );
		}
		catch ( RangeException e )
		{
			String errMsg = e.getLocalizedMessage( );
			if ( this.messageBuilder != null )
				errMsg = this.messageBuilder.createRangeExceptionMsg( e.isLowerBoundError( ), e.getExeededBound( ) );

			this.setInternalMessage( errMsg, MessageType.ERROR );
			fireValueOutOfBounds( );
		}
	}

	public void setMessageBuilder( RestrictedTextFieldMessageBuilder messageBuilder )
	{
		this.messageBuilder = messageBuilder;
	}

	/**
	 * Sets the new value that should be displayed by the text-field. Fires a valueChangeCommited
	 * only if the value is not equal to the last value.
	 * @param value
	 */
	public void setValue( T value )
	{
		// no change detected --> return
		if ( value == null )
			return;
		/* no change detected so we don't have to set the value */
		if ( ( this.lastValue != null ) && ( this.compareValues( this.lastValue, value ) == 0 ) )
			return;

		this.updateValue( value, this.automaticCorrectionOnFocusLost );
	}

	private void updateValue( String text, boolean autoCorrect )
	{
		T value = this.lastValue;
		try
		{
			value = this.convertStringToType( text );
		}
		catch ( ConvertException e )
		{
			if ( !autoCorrect )
			{
				String errMsg = e.getLocalizedMessage( );
				if ( this.messageBuilder != null )
					errMsg = this.messageBuilder.createConvertExceptionMsg( e.getValueToConvert( ), e.getTargetType( ) );
				this.setInternalMessage( errMsg, MessageType.ERROR );
				this.fireValueInvalid( );
				return;
			}
		}
		catch ( InvalidValueExeption e )
		{
			if ( !autoCorrect )
			{
				String errMsg = e.getLocalizedMessage( );
				if ( this.messageBuilder != null )
					errMsg = this.messageBuilder.createInvalidValueExceptionMsg( e.getValueToConvert( ), e.getErrorMessage( ) );
				this.setInternalMessage( errMsg, MessageType.ERROR );
				fireValueInvalid( );
				return;
			}
		}
		updateValue( value, autoCorrect );
	}

	private void updateValue( T value, boolean autoCorrect )
	{
		this.contentValid = false;
		try
		{
			this.validateRange( value );
		}
		catch ( RangeException e )
		{
			value = this.maxValue;
			if ( e.isLowerBoundError( ) )
				value = this.minValue;

			if ( !autoCorrect )
			{
				String errMsg = e.getLocalizedMessage( );
				if ( this.messageBuilder != null )
					errMsg = this.messageBuilder.createRangeExceptionMsg( e.isLowerBoundError( ), e.getExeededBound( ) );

				this.setInternalMessage( errMsg, MessageType.ERROR );
				this.fireValueOutOfBounds( );
				return;
			}
		}

		super.setText( this.convertTypeToString( value ) );
		this.lastValue = value;
		this.contentValid = true;
		this.clearInternalMessage( );
		this.fireValueChangeCommitted( );
	}

	/**
	 * Throws an RangeException if the given value is out of bounds.
	 * @param value
	 * @throws RangeException
	 */
	private void validateRange( T value ) throws RangeException
	{

		/* is a range-check possible? */
		if ( ( this.minValue != null ) && ( this.maxValue != null ) )
		{
			/* value is smaller than the lower-bound */
			if ( this.compareValues( value, this.minValue ) == -1 )
			{
				String errStr = "<html>The given value was <b>smaller</b><br>than the lower-bound [" + this.convertTypeToString( this.minValue ) + "].";
				throw new RangeException( errStr, true, this.convertTypeToString( this.minValue ) );
			}

			/* value is greater than the upper-bound */
			if ( this.compareValues( value, this.maxValue ) == 1 )
			{
				String errStr = "<html>The given value was <b>greater</b><br>than the upper-bound [" + this.convertTypeToString( this.maxValue ) + "].";
				throw new RangeException( errStr, false, this.convertTypeToString( this.maxValue ) );
			}
		}
	}

	@Override
	public void setToolTipText( String text )
	{
		this.toolTipText = text;
		super.setToolTipText( text );
	}

	/**
	 * Implement this method so that it returns 1 if val1 is greater than val2, -1 if val1 is
	 * smaller than val2 and 0 if both values are equal.
	 * @param val1
	 * @param val2
	 * @return
	 */
	protected abstract int compareValues( T val1, T val2 );

	/**
	 * Implement this function so that the default value for this type of text-field will be
	 * returned
	 * @return
	 */
	protected abstract T getDefaultValue( );

	/**
	 * Set the range within the user-input should be treated as valid input.
	 * @param minValue
	 * @param maxValue
	 */
	public void setRange( T minValue, T maxValue )
	{
		/* min is greater than max */
		if ( this.compareValues( minValue, maxValue ) == 1 )
		{
			System.err.println( "Error in RestrictedTextField.setRange() minValue [" + minValue + "] is greater than maxValue [" + maxValue + "]" );
			this.minValue = null;
			this.maxValue = null;
		}

		this.minValue = minValue;
		this.maxValue = maxValue;
		this.updateValue( this.getValue( ), this.automaticCorrectionOnFocusLost );
	}

	/**
	 * Add a listener to listen for value-change- and value-change-committed-events.
	 * @param listener
	 */
	public void addListener( RestrictedTextfieldListener listener )
	{
		this.listeners.add( listener );
	}

	/**
	 * Remove a listener.
	 * @param listener
	 */
	public void removeListener( RestrictedTextfieldListener listener )
	{
		this.listeners.remove( listener );
	}

	/**
	 * Implement this function to specify how the given type should be rendered.
	 * @param typeToConvert
	 * @return
	 */
	protected abstract String convertTypeToString( T typeToConvert );

	/**
	 * Implement this function so that it tries to convert the given String to the specific type. If
	 * that is not possible the function should throw a ConvertException
	 * @param stringToConvert
	 * @return
	 * @throws ConvertException
	 */
	protected abstract T convertStringToType( String stringToConvert ) throws ConvertException, InvalidValueExeption;

	/**
	 * Returns the current value typed into the text-field.
	 * @return
	 */
	public T getValue( )
	{
		return this.lastValue;
	}

	/**
	 * Overwritten function
	 */
	public void setText( String text )
	{
		System.err.println( "Don't use the function setText() it is not implemented. Use the function setValue() instead." );
	}

	private void fireValueInvalid( )
	{
		this.contentValid = false;
		for ( RestrictedTextfieldListener listener : this.listeners )
			listener.valueInvalid( this.getText( ) );
	}

	private void fireValueOutOfBounds( )
	{
		this.contentValid = false;
		for ( RestrictedTextfieldListener listener : this.listeners )
			listener.valueOutOfBounds( this.getText( ) );
	}

	/**
	 * Notify all listeners that the value were changed caused by a keytype-event.
	 */
	private void fireValueChanged( )
	{
		for ( RestrictedTextfieldListener listener : this.listeners )
			listener.valueChanged( );
	}

	/**
	 * Notify all listeners that the value was changed and the change was commited by loosing the
	 * focus or pressing enter.
	 */
	private void fireValueChangeCommitted( )
	{
		this.contentValid = true;

		// avoid multiple events on the same value 
		if ( this.lastCommitedValue != null && this.lastCommitedValue.equals( this.lastValue ) )
			return;

		for ( RestrictedTextfieldListener listener : this.listeners )
			listener.valueChangeCommitted( );

		this.lastCommitedValue = this.lastValue;
		this.repaint( );

	}

	protected void clearInternalMessage( )
	{
		this.internalMessageType = MessageType.NONE;
		this.internalMessage = "";
		this.repaint( );
	}

	public void clearMessage( )
	{
		this.messageType = MessageType.NONE;
		this.message = "";
		this.repaint( );
	}

	protected void setInternalMessage( String message, MessageType type )
	{
		this.internalMessageType = type;
		this.internalMessage = message;
		this.repaint( );
	}

	public void setMessage( String message, MessageType messageType )
	{
		this.message = message;
		this.messageType = messageType;
	}

	public boolean isContentValid( )
	{
		return contentValid;
	}

	/**
	 * Clear the TextField.
	 */
	public void clear( )
	{
		this.setValue( this.getDefaultValue( ) );
		this.fireValueChangeCommitted( );
	}

	/**
	 * Sets the format-string (for example %2.5g) that should be used to render floating-point
	 * numbers.
	 * @param formatString
	 */
	public void setFormatString( String formatString )
	{
		this.formatString = formatString;
		try
		{
			//			String test = 
			String.format( Locale.ENGLISH, this.formatString, 100d );
		}
		catch ( Exception e )
		{
			this.formatString = defaultFormatString;
			System.err.println( "The given format-string " + formatString + " is not valid. The default one (" + this.formatString + ") will be used instead. " + e.getLocalizedMessage( ) );
		}
		this.updateValue( this.getText( ), this.automaticCorrectionOnFocusLost );
	}

	@Override
	public void paint( Graphics g )
	{

		super.paint( g );

		Dimension size = getSize( );
		Image image = null;
		if ( this.internalMessageType != MessageType.NONE )
		{

			if ( this.internalMessageType == MessageType.ERROR )
				image = this.errImage.getImage( );
			else if ( this.internalMessageType == MessageType.WARNING )
				image = this.warnImage.getImage( );
			else if ( this.internalMessageType == MessageType.INFO )
				image = this.infoImage.getImage( );

		}
		else if ( this.messageType != MessageType.NONE )
		{
			if ( this.messageType == MessageType.ERROR )
				image = this.errImage.getImage( );
			else if ( this.messageType == MessageType.WARNING )
				image = this.warnImage.getImage( );
			else if ( this.messageType == MessageType.INFO )
				image = this.infoImage.getImage( );
		}

		if ( image != null )
			g.drawImage( image, 0, ( int ) size.getHeight( ) - iconSize, iconSize, iconSize, this );

	}

	private void onMouseMoved( MouseEvent e )
	{
		int height = getHeight( );
		int left = 0;
		int right = iconSize;
		int bottom = height - iconSize;
		int top = height;
		Point currentPos = e.getPoint( );

		if ( currentPos.x >= left && currentPos.x <= right && currentPos.y >= bottom && currentPos.y <= top && ( this.internalMessageType != MessageType.NONE || this.messageType != MessageType.NONE ) )
		{
			if ( this.internalMessageType != MessageType.NONE )
			{
				if ( this.internalMessage.trim( ).length( ) == 0 )
					super.setToolTipText( null );
				else super.setToolTipText( this.internalMessage );
			}
			else if ( this.messageType != MessageType.NONE )
			{
				if ( this.message.trim( ).length( ) == 0 )
					super.setToolTipText( null );
				else super.setToolTipText( this.message );
			}
			this.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
		}
		else
		{
			this.setCursor( new Cursor( Cursor.TEXT_CURSOR ) );
			if ( this.toolTipText.trim( ).length( ) == 0 )
				super.setToolTipText( null );
			else super.setToolTipText( toolTipText );
		}
	}

}
