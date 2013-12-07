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

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import thobe.task.ProgressInformation;
import thobe.task.Task;

/**
 * @author Thomas Obenaus
 * @source TaskDialog.java
 * @date 27 Aug 2008
 */
@SuppressWarnings ( "serial")
public class InterruptableTaskDialog extends TaskDialog
{
	private JButton	bu_ok;
	private JButton	bu_cancel;

	/**
	 * Ctor
	 * @param owner
	 */
	public InterruptableTaskDialog( Window owner, String title, ArrayList<Task> tasksToFulFill, MessagesInterruptableTaskDialog messages )
	{
		super( owner, title, tasksToFulFill, messages );
		this.setSize( 350, 265 );
		this.bu_ok.setText( ( ( MessagesInterruptableTaskDialog ) this.messages ).button_ok_txt );
		this.bu_ok.setToolTipText( ( ( MessagesInterruptableTaskDialog ) this.messages ).button_ok_tooltip );
		this.bu_cancel.setText( ( ( MessagesInterruptableTaskDialog ) this.messages ).button_cancel_txt );
		this.bu_cancel.setToolTipText( ( ( MessagesInterruptableTaskDialog ) this.messages ).button_cancel_tooltip );
	}

	protected void handleWindowClosing_( )
	{
		String[] options =
		{ "ja", "nein" };
		int result = JOptionPane.showOptionDialog( this.owner, "Schliessen??", "???", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1] );
		if ( result == JOptionPane.YES_OPTION )
		{
			this.progressPanel.cancelAllRunningTasks( );
			this.close_dlg( );
		}

	}

	public void ok_( )
	{
		this.close_dlg( );
	}

	public void cancel_( )
	{
		this.progressPanel.cancelAllRunningTasks( );
	}

	@Override
	public Dimension getMinimumEditorSize( )
	{
		return new Dimension( 200, 200 );
	}

	public void taskCancelled( Task task )
	{
		this.bu_ok.setEnabled( true );
		this.bu_cancel.setEnabled( false );
	}

	public void taskDone( Task task )
	{
		this.bu_ok.setEnabled( true );
		this.bu_cancel.setEnabled( false );
	}

	public void taskStarted( Task task )
	{
		this.bu_ok.setEnabled( false );
		this.bu_cancel.setEnabled( true );
	}

	@Override
	protected JPanel createButtonPanel( )
	{
		JPanel result = new JPanel( );
		this.bu_cancel = new JButton( "" );//((MessagesInterruptableTaskDialog)this.messages).button_cancel_txt );//Language.get( ).getRessource( LanguageRessource.INTERRUPTED_TASK_DIALOG_BUTTONS_CANCEL_NAME ) );
		//		this.bu_cancel.setToolTipText(  ((MessagesInterruptableTaskDialog)this.messages).button_cancel_tooltip );//Language.get( ).getRessource( LanguageRessource.INTERRUPTED_TASK_DIALOG_BUTTONS_CANCEL_TOOLTIP ) );
		this.bu_cancel.addActionListener( new ActionListener( )
		{
			public void actionPerformed( ActionEvent e )
			{
				cancel_( );
			}
		} );

		this.bu_ok = new JButton( "" );// ((MessagesInterruptableTaskDialog)this.messages).button_ok_txt );//Language.get( ).getRessource( LanguageRessource.INTERRUPTED_TASK_DIALOG_BUTTONS_OK_NAME ) );
		//		this.bu_ok.setToolTipText(  ((MessagesInterruptableTaskDialog)this.messages).button_ok_tooltip );//Language.get( ).getRessource( LanguageRessource.INTERRUPTED_TASK_DIALOG_BUTTONS_OK_TOOLTIP ) );
		this.bu_ok.addActionListener( new ActionListener( )
		{
			public void actionPerformed( ActionEvent e )
			{
				ok_( );
			}
		} );

		result.add( this.bu_ok );
		result.add( this.bu_cancel );
		return result;
	}

	@Override
	public void progressUpdate( ProgressInformation progressInfo )
	{
		// TODO Auto-generated method stub

	}

}
