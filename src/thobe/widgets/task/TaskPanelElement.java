/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.task;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;

import thobe.task.ProgressInformation;
import thobe.task.Task;
import thobe.task.TaskListener;
import thobe.widgets.buttons.CancelButton;
import thobe.widgets.utils.Utilities;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Class representing one element which can be used to display the progress of one task.
 * @author Thomas Obenaus
 * @source ProgressElement.java
 * @date 27 Jun 2008
 */
@SuppressWarnings ( "serial")
public class TaskPanelElement extends JPanel implements TaskListener
{
	/**
	 * The progressbar
	 */
	private JProgressBar	progressBar;

	/**
	 * A label for some informations
	 */
	private JLabel			l_title;

	/**
	 * The result label.
	 */
	private JLabel			l_result;

	/**
	 * The label holding the details.
	 */
	private JTextPane		l_details;

	/**
	 * The Panel holding the details-label (hideable).
	 */
	private JPanel			pa_details;

	/**
	 * The button to show/ hide the details
	 */
	private JButton			bu_details;

	/**
	 * The cancel-button.
	 */
	private CancelButton	bu_cancel;

	/**
	 * The task to fulfill
	 */
	private Task			task;

	private String			txtButtonDetails;
	private String			txtButtonDetailsTooltip;
	private String			txtTaskCanceled;
	private String			txtTaskDone;
	private String			txtButtonCancelTooltip;
	private int				maxWidth;

	/**
	 * Ctor
	 * @param task
	 */
	public TaskPanelElement( Task task, int maxWidth )
	{
		this.maxWidth = maxWidth;
		this.txtButtonCancelTooltip = "Cancel task";
		this.txtButtonDetails = "Details";
		this.txtButtonDetailsTooltip = "Details";
		this.txtTaskCanceled = "Task canceled ";
		this.txtTaskDone = "Done";
		this.task = task;
		this.task.addListener( this );
		this.setToolTipText( task.getInformationText( ) );
		this.buildGUI( );
		this.l_title.setText( task.getInformationText( ) );
		//		this.text = "";
	}

	public void setTxtTaskDone( String txtTaskDone )
	{
		this.txtTaskDone = txtTaskDone;
	}

	public void setTxtButtonDetails( String txtButtonDetails )
	{
		this.txtButtonDetails = txtButtonDetails;
	}

	public void setTxtButtonCancelTooltip( String txtButtonCancelTooltip )
	{
		this.txtButtonCancelTooltip = txtButtonCancelTooltip;
	}

	public void setTxtButtonDetailsTooltip( String txtButtonDetailsTooltip )
	{
		this.txtButtonDetailsTooltip = txtButtonDetailsTooltip;
	}

	public void setTxtTaskCanceled( String txtTaskCanceled )
	{
		this.txtTaskCanceled = txtTaskCanceled;
	}

	/**
	 * Returns the current task
	 * @return
	 */
	public Task getTask( )
	{
		return this.task;
	}

	/**
	 * Execute the current task.
	 */
	public void execute( )
	{
		this.task.execute( );
	}

	/**
	 * Create the GUI.
	 */
	private void buildGUI( )
	{
		FormLayout fla_content = new FormLayout( "fill:pref:grow", "2dlu,center:pref,2dlu,pref,0dlu,center:pref,2dlu" );
		CellConstraints cc_content = new CellConstraints( );
		this.setLayout( fla_content );

		this.l_title = new JLabel( "text...." );
		this.add( this.l_title, cc_content.xy( 1, 2 ) );

		//		FormLayout fla_progressBar = new FormLayout( "fill:pref:grow,min(10dlu;pref)", "top:min(10dlu;pref)" );
		FormLayout fla_progressBar = new FormLayout( "fill:pref:grow,3dlu,pref", "pref" );
		CellConstraints cc_progressBar = new CellConstraints( );
		JPanel pa_progar = new JPanel( fla_progressBar );
		pa_progar.setBackground( Color.white );
		this.bu_cancel = new CancelButton( );
		this.bu_cancel.setToolTipText( this.txtButtonCancelTooltip );
		this.bu_cancel.setEnabled( false );
		this.bu_cancel.setMargin( new Insets( 0, 0, 0, 0 ) );

		this.progressBar = new JProgressBar( );
		this.progressBar.setForeground( this.task.getDefaultProgressBarColor( ) );
		this.progressBar.setBorder( BorderFactory.createLineBorder( this.task.getDefaultProgressBarColor( ) ) );
		this.progressBar.setBorderPainted( true );
		this.progressBar.setMaximumSize( new Dimension( Integer.MAX_VALUE, 10 ) );
		pa_progar.add( this.progressBar, cc_progressBar.xy( 1, 1 ) );

		if ( this.task.canCancelTask( ) ) pa_progar.add( this.bu_cancel, cc_progressBar.xy( 3, 1 ) );

		this.add( pa_progar, cc_content.xy( 1, 4 ) );

		this.setBackground( Color.white );
		this.l_title.setBackground( Color.white );

		this.bu_cancel.addActionListener( new ActionListener( )
		{
			public void actionPerformed( ActionEvent e )
			{
				task.kill( );
			}
		} );

		FormLayout fla_results = new FormLayout( "0dlu,fill:pref:grow,1dlu,pref,0dlu", "1dlu,top:pref,1dlu,pref" );
		JPanel pa_results = new JPanel( fla_results );
		pa_results.setBackground( Color.white );
		CellConstraints cc_results = new CellConstraints( );

		this.l_result = new JLabel( );
		Font font = new Font( "Arial", Font.ITALIC, 12 );
		this.l_result.setFont( font );

		//		JScrollPane scrpa_result = new JScrollPane( this.l_result );
		//		scrpa_result.setPreferredSize( new Dimension( 200, 40 ) );
		//		scrpa_result.setBorder( BorderFactory.createLineBorder( Color.DARK_GRAY ) );
		//		pa_results.add( scrpa_result, cc_results.xy( 2, 2 ) );

		pa_results.add( this.l_result, cc_results.xy( 2, 2 ) );

		this.bu_details = new JButton( this.txtButtonDetails );
		this.bu_details.setToolTipText( this.txtButtonDetailsTooltip );
		this.bu_details.setVisible( false );
		this.bu_details.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				pa_details.setVisible( !pa_details.isVisible( ) );
			}
		} );
		pa_results.add( this.bu_details, cc_results.xy( 4, 2 ) );

		this.l_details = new JTextPane( );
		this.pa_details = new JPanel( new BorderLayout( 5, 5 ) );
		this.pa_details.setVisible( false );
		pa_details.setBorder( BorderFactory.createLineBorder( new Color( 180, 180, 180 ) ) );
		pa_details.add( this.l_details, BorderLayout.CENTER );
		l_details.setBackground( new Color( 220, 220, 220 ) );
		this.l_details.setBorder( BorderFactory.createEmptyBorder( 2, 4, 2, 2 ) );
		pa_results.add( pa_details, cc_results.xyw( 2, 4, 3 ) );

		this.add( pa_results, cc_content.xy( 1, 6 ) );
	}

	/**
	 * Add an action to the cancelButton
	 * @param actionlistener
	 */
	public void addCancelButtonActionlistener( ActionListener actionlistener )
	{
		this.bu_cancel.addActionListener( actionlistener );
	}

	/**
	 * Sets the information text
	 * @param text
	 */
	public void setText( String text )
	{
		this.l_title.setText( text );
	}

	@Override
	public void taskCancelled( Task task )
	{
		this.progressBar.setIndeterminate( false );
		this.l_result.setText( Utilities.resizeStringToMaxWidthHTML( this.getFontMetrics( this.getFont( ) ), this.txtTaskCanceled + ": " + this.task.getLastTaskException( ).getMessage( ), this.maxWidth - this.bu_details.getWidth( ) - 35 ) );
		//		this.text += Utilities.resizeStringToMaxWidth( this.getFontMetrics( this.getFont( ) ), this.txtTaskCanceled + ": " + this.task.getLastTaskException( ).getMessage( ), this.maxWidth - this.bu_details.getWidth( ) - 35 );
		//		this.l_result.setText( this.text );
		String detailsTooltip = Utilities.resizeStringToMaxWidthHTML( this.getFontMetrics( this.getFont( ) ), this.task.getLastTaskException( ).getDetails( ), this.maxWidth );
		String details = Utilities.resizeStringToMaxWidth( this.getFontMetrics( this.getFont( ) ), this.task.getLastTaskException( ).getDetails( ), this.maxWidth );

		this.l_details.setText( details );
		this.l_details.setToolTipText( detailsTooltip );

		this.bu_details.setVisible( true );
		progressBar.setBorder( BorderFactory.createLineBorder( Color.red ) );
		progressBar.setForeground( Color.red );
		this.bu_cancel.setEnabled( false );

		this.invalidate( );
		this.validate( );
		this.revalidate( );
		this.repaint( );
	}

	@Override
	public void taskDone( Task task )
	{
		this.progressBar.setIndeterminate( false );
		this.l_result.setText( Utilities.resizeStringToMaxWidthHTML( this.getFontMetrics( this.getFont( ) ), this.txtTaskDone, this.maxWidth ) );
		//		this.text += Utilities.resizeStringToMaxWidth( this.getFontMetrics( this.getFont( ) ), this.txtTaskDone, this.maxWidth );
		//		this.l_result.setText( this.text );
		this.bu_cancel.setEnabled( false );
		this.validate( );
		this.repaint( );
	}

	@Override
	public void taskStarted( Task task )
	{
		if ( !this.task.hasValidProgressInformation( ) ) this.progressBar.setIndeterminate( true );
		this.bu_cancel.setEnabled( true );
	}

	@Override
	public void progressUpdate( ProgressInformation progressInfo )
	{
		this.progressBar.setValue( progressInfo.getProgress( ) );
		if ( !progressInfo.getNameOfNextTask( ).trim( ).equals( "" ) )
		{
			//			this.text += Utilities.resizeStringToMaxWidth( this.getFontMetrics( this.getFont( ) ), progressInfo.getNameOfNextTask( ), this.maxWidth ) + "\n";
			//			this.l_result.setText( this.text );
			this.l_result.setText( Utilities.resizeStringToMaxWidthHTML( this.getFontMetrics( this.getFont( ) ), progressInfo.getNameOfNextTask( ), this.maxWidth ) );
		}

		this.validate( );
		this.repaint( );
	}
}
