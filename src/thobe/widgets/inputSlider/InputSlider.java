/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.inputSlider;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import thobe.widgets.textfield.RestrictedTextFieldMessageBuilder;
import thobe.widgets.textfield.RestrictedTextfield;
import thobe.widgets.textfield.RestrictedTextfieldListener;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Abstract class that represents a template for a widgets which are a combination of a text-field
 * and a slider.
 * @author Thomas Obenaus
 * @source InputSlider.java
 * @date 18 Nov 2008
 */
@SuppressWarnings ( "serial")
public abstract class InputSlider<T> extends JPanel
{
	private static final int				sliderHeight			= 28;
	private static final int				textFieldHeight			= 30;
	private String							userDefinedToolTipText	= "";
	/**
	 * The attached listeners
	 */
	private ArrayList<InputSliderListener>	listeners;

	/**
	 * The widget that can be used to slide trough the given range.
	 */
	protected Slider<T>						slider;

	/**
	 * The text-field that shows the value the slider points to.
	 */
	protected RestrictedTextfield<T>		rtf_value;

	/**
	 * The text that says what type the values are.
	 */
	private String							name;

	private RestrictedTextfieldListener		rtfl_value;
	private ChangeListener					chl_slider;

	private boolean							mousePressed;
	private boolean							noCommitValueChangeWhenMousePressed;

	/**
	 * Ctor
	 * @param name - The name of the slider
	 * @param minValue - start of the sliders range
	 * @param maxValue - end of the sliders range
	 */
	public InputSlider( String name, T minValue, T maxValue )
	{
		this.mousePressed = false;
		this.noCommitValueChangeWhenMousePressed = false;
		this.listeners = new ArrayList<InputSliderListener>( );
		this.name = name;
		this.buildGUI( );
		this.setRange( minValue, maxValue );
		this.rtf_value.setRange( minValue, maxValue );
		this.slider.setToMin( );

	}

	/**
	 * Ctor
	 * @param name - The name of the slider
	 */
	public InputSlider( String name )
	{
		this.mousePressed = false;
		this.noCommitValueChangeWhenMousePressed = false;
		this.listeners = new ArrayList<InputSliderListener>( );
		this.name = name;
		this.buildGUI( );
		this.slider.setToMin( );

	}

	/**
	 * Ctor
	 * @param name - The name of the slider
	 * @param minValue - start of the sliders range
	 * @param maxValue - end of the sliders range
	 * @param tickSpacing - spacing between the tickmarks (won't be painted by default, so use
	 *            setPaintTicks())
	 */
	public InputSlider( String name, T minValue, T maxValue, T tickSpacing )
	{
		this.mousePressed = false;
		this.noCommitValueChangeWhenMousePressed = false;
		this.listeners = new ArrayList<InputSliderListener>( );
		this.name = name;
		this.buildGUI( );
		this.setRange( minValue, maxValue, tickSpacing );
		this.rtf_value.setRange( minValue, maxValue );
		this.slider.setToMin( );
	}

	public T getMaximumValue( )
	{
		return this.slider.getMaximumValue( );
	}

	/**
	 * Returns the sliders minimum-value.
	 * @return
	 */
	public T getMinimumValue( )
	{
		return this.slider.getMinimumValue( );
	}

	private void handleKeyPressed( KeyEvent e )
	{
		/* ctrl + left cursor ==> decrease the value of the slider by tickSpacing */
		if ( e.isControlDown( ) && e.getKeyCode( ) == KeyEvent.VK_LEFT )
		{
			this.slider.setNewValue( this.slider.getDecreasedValue( ) );
			return;
		}

		/* ctrl + right cursor ==> increase the value of the slider by tickSpacing */
		if ( e.isControlDown( ) && e.getKeyCode( ) == KeyEvent.VK_RIGHT )
		{
			this.slider.setNewValue( this.slider.getIncreasedValue( ) );
			return;
		}
	}

	public void setCommitOnFocusLost( boolean commitOnFocusLost )
	{
		this.rtf_value.setCommitOnFocusLost( commitOnFocusLost );
	}

	@Override
	public void setPreferredSize( Dimension preferredSize )
	{
		this.slider.setPreferredSize( new Dimension( preferredSize.width, sliderHeight ) );
		this.rtf_value.setPreferredSize( new Dimension( preferredSize.width, textFieldHeight ) );
		super.setPreferredSize( preferredSize );
	}

	public void setMessageBuilder( RestrictedTextFieldMessageBuilder messageBuilder )
	{
		this.rtf_value.setMessageBuilder( messageBuilder );
	}

	/**
	 * Create the slider of the given type.
	 * @return
	 */
	protected abstract Slider<T> createSlider( );

	/**
	 * Creates the textfield of the given type.
	 * @return
	 */
	protected abstract RestrictedTextfield<T> createTextfield( );

	/**
	 * Compares two given values. Implement this function so that it returns 1 if val1 is bigger
	 * than val2, -1 if val1 is smaller than val2 and 0 if both are equal.
	 * @param val1
	 * @param val2
	 * @return
	 */
	protected abstract int compareValues( T val1, T val2 );

	/**
	 * Use this method to enter/ leave the 'noCommitValueChangeWhenMousePressed' mode. Within this
	 * mode changes of the slider only will be committed to the attached listeners if the
	 * mouse-button where released. Changes while the mouse is pressed won't be propagated to the
	 * listeners.
	 * @param noCommitValueChangeWhenMousePressed - true to enable the
	 *            'noCommitValueChangeWhenMousePressed'-mode, false to disable this mode.
	 */
	public void setNoCommitValueChangeWhenMousePressed( boolean noCommitValueChangeWhenMousePressed )
	{
		this.noCommitValueChangeWhenMousePressed = noCommitValueChangeWhenMousePressed;
	}

	/**
	 * Build up the gui.
	 */
	private void buildGUI( )
	{
		FormLayout fla_content = new FormLayout( "fill:pref:grow", "pref,2dlu,pref" );
		CellConstraints cc_content = new CellConstraints( );
		this.setLayout( fla_content );
		this.rtf_value = this.createTextfield( );
		this.rtfl_value = new RestrictedTextfieldListener( )
		{
			@Override
			public void valueChangeCommitted( )
			{
				performValueEntered( );
				fireValueChanged( );
			}

			@Override
			public void valueChanged( )
			{

			}

			@Override
			public void valueInvalid( String value )
			{

			}

			@Override
			public void valueOutOfBounds( String value )
			{

			}
		};
		this.rtf_value.addListener( this.rtfl_value );

		this.add( rtf_value, cc_content.xy( 1, 1 ) );

		this.slider = this.createSlider( );
		this.chl_slider = new ChangeListener( )
		{
			@Override
			public void stateChanged( ChangeEvent e )
			{
				performValueChanged( );
				/* fire value changed not if the mouse is pressed and if we are in noCommitValueChangeWhenMousePressed mode */
				if ( !( noCommitValueChangeWhenMousePressed && mousePressed ) )
					fireValueChanged( );
			}
		};
		this.slider.addChangeListener( this.chl_slider );

		this.add( this.slider, cc_content.xy( 1, 3 ) );

		int txtSize = 0;
		if ( this.name != null && !this.name.trim( ).equals( "" ) )
		{
			this.setBorder( BorderFactory.createTitledBorder( this.name ) );
			txtSize = 30;
		}

		this.setPreferredSize( new Dimension( 60, textFieldHeight + sliderHeight + txtSize ) );

		this.slider.addKeyListener( new KeyAdapter( )
		{
			@Override
			public void keyPressed( KeyEvent e )
			{
				handleKeyPressed( e );
			}
		} );

		this.rtf_value.addKeyListener( new KeyAdapter( )
		{
			@Override
			public void keyPressed( KeyEvent e )
			{
				handleKeyPressed( e );
			}
		} );

		this.slider.addMouseListener( new MouseAdapter( )
		{

			@Override
			public void mouseReleased( MouseEvent e )
			{
				if ( !slider.isEnabled( ) )
					return;
				mousePressed = false;

				/* fire value changed if we are in noCommitValueChangeWhenMousePressed mode and the mouse was released */
				if ( noCommitValueChangeWhenMousePressed )
				{
					slider.setFreezeSliderPos( false );
					fireValueChanged( );

				}
			}

			@Override
			public void mousePressed( MouseEvent e )
			{
				if ( !slider.isEnabled( ) )
					return;
				/* freeze the sliders position if we are in noCommitValueChangeWhenMousePressed mode and the mouse is pressed */
				if ( noCommitValueChangeWhenMousePressed )
					slider.setFreezeSliderPos( true );
				mousePressed = true;
			}
		} );

	}

	public void setEnabled( boolean enable )
	{
		this.slider.setEnabled( enable );
		this.rtf_value.setEnabled( enable );
	}

	/**
	 * Returns the currently selected value.
	 * @return
	 */
	public T getValue( )
	{
		return this.rtf_value.getValue( );
	}

	/**
	 * Add a new listener which listens for events caused by a change of the value selected by the
	 * slider.
	 * @param listener
	 */
	public void addListener( InputSliderListener listener )
	{
		this.listeners.add( listener );
	}

	/**
	 * Remove a listener.
	 * @param listener
	 */
	public void removeListener( InputSliderListener listener )
	{
		this.listeners.remove( listener );
	}

	private void fireValueChanged( )
	{
		if ( !this.isEnabled( ) )
			return;
		for ( InputSliderListener listener : this.listeners )
			listener.valueChanged( );
	}

	/**
	 * Specify if the tick-marks should be painted or not.
	 * @param paint
	 */
	public void setPaintTicks( boolean paint )
	{
		this.slider.setPaintTicks( paint );
	}

	/**
	 * Set the slider to a specific value.
	 * @param value
	 */
	public void setValue( T value )
	{
		this.slider.setNewValue( value );
	}

	/**
	 * Specify if the labels should be painted.
	 * @param paint
	 */
	public void setPaintLabels( boolean paint )
	{
		this.slider.setPaintLabels( paint );
	}

	/**
	 * Define the sliders range.
	 * @param minValue - start of the sliders range
	 * @param maxValue - end of the sliders range
	 * @param tickSpacing - spacing between the tickmarks (won't be painted by default, so use
	 *            setPaintTicks())
	 */
	public void setRange( T minimum, T maximum, T tickSpacing )
	{
		this.slider.setRange( minimum, maximum, tickSpacing );
	}

	/**
	 * Define the sliders range.
	 * @param minValue - start of the sliders range
	 * @param maxValue - end of the sliders range
	 */
	public void setRange( T minimum, T maximum )
	{
		this.slider.setRange( minimum, maximum );
	}

	/**
	 * Spacing/ resolution used to compute valid values. This is important if continuous mode is
	 * enabled (setEnableContinuousMode()).
	 * @param tickSpacing
	 */
	public void setTickSpacing( T tickSpacing )
	{
		this.slider.setRange( this.slider.getMinimumValue( ), this.slider.getMaximumValue( ), tickSpacing );
	}

	/**
	 * Enable/ disable the continuous slider mode. Not enabled: values that does not match a value
	 * that is a multiple of tickspacing will be rounded to the nearest value that matches this
	 * restriction. Enabled: all values are allowed.
	 * @param enable
	 */
	public void setEnableContinuousMode( boolean enable )
	{
		this.slider.setEnableContinuousMode( enable );
	}

	private void performValueChanged( )
	{
		this.rtf_value.removeListener( this.rtfl_value );
		this.handleValueChanged( );
		this.rtf_value.addListener( this.rtfl_value );
	}

	private void performValueEntered( )
	{
		this.slider.removeChangeListener( this.chl_slider );
		this.handleValueEntered( );
		this.slider.addChangeListener( this.chl_slider );

	}

	/**
	 * Implement this method to specify how the widget should handle a change of the slider's
	 * position (e.g. update the text-field or the tooltip)
	 */
	private void handleValueChanged( )
	{
		this.rtf_value.setValue( this.slider.getCurrentValue( ) );
		this.updateTooltips( );
	}

	/**
	 * Implement this method to specify how the value should be formatted/ rendered.
	 * @param value
	 * @return
	 */
	protected abstract String renderValue( T value );

	/**
	 * Implement this method to specify how the widget should handle a change of the value typed in
	 * the text-field (e.g. update the sliders position or the tooltip)
	 */
	private void handleValueEntered( )
	{
		T value = this.rtf_value.getValue( );

		/* if value is greater than the maximum-value reduce it to the sliders maximum-value */
		if ( this.compareValues( value, this.slider.getMaximumValue( ) ) == 1 )
			value = this.slider.getMaximumValue( );

		/* if value is smaller than the minimum-value reduce it to the sliders minimum-value */
		if ( this.compareValues( value, this.slider.getMinimumValue( ) ) == -1 )
			value = this.slider.getMinimumValue( );

		this.rtf_value.removeListener( this.rtfl_value );
		this.slider.setNewValue( value );
		this.rtf_value.setValue( this.slider.getCurrentValue( ) );
		this.rtf_value.addListener( this.rtfl_value );

		this.updateTooltips( );
	}

	private void updateTooltips( )
	{
		/* set the tooltip text */
		String tooltipTxt = this.userDefinedToolTipText + " " + this.renderValue( this.rtf_value.getValue( ) );
		this.rtf_value.setToolTipText( tooltipTxt );
		this.slider.setToolTipText( tooltipTxt );
	}

	/**
	 * Sets the format-string (for example %2.5g) that should be used to render floating-point
	 * numbers.
	 * @param formatString
	 */
	public void setFormatString( String formatString )
	{
		this.rtf_value.setFormatString( formatString );
		this.handleValueEntered( );
	}

	@Override
	public void setToolTipText( String text )
	{
		this.userDefinedToolTipText = text;
		this.updateTooltips( );
	}
}
