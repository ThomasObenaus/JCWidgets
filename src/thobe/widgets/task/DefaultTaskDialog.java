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
import javax.swing.JPanel;

import thobe.task.ProgressInformation;
import thobe.task.Task;

/**
 * @author Thomas Obenaus
 * @source DefaultTaskDialog.java
 * @date 10 Sep 2008
 */
@SuppressWarnings ( "serial")
public class DefaultTaskDialog extends TaskDialog
{
	private JButton	bu_ok;

	public DefaultTaskDialog( Window owner, String title, ArrayList<Task> tasksToFulFill, MessagesDefaultTaskDialog messages )
	{
		super( owner, title, tasksToFulFill, messages );
		this.setSize( 360, 295 );
		this.bu_ok.setText( ( ( MessagesDefaultTaskDialog ) this.messages ).button_ok_txt );
		this.bu_ok.setToolTipText( ( ( MessagesDefaultTaskDialog ) this.messages ).button_ok_tooltip );
	}

	@Override
	public void taskCancelled( Task task )
	{
		this.bu_ok.setEnabled( true );
	}

	@Override
	public void taskDone( Task task )
	{
		this.bu_ok.setEnabled( true );
		this.setVisible( false );
	}

	@Override
	public void taskStarted( Task task )
	{
		this.bu_ok.setEnabled( false );
	}

	private void ok_( )
	{
		this.setVisible( false );
	}

	@Override
	protected JPanel createButtonPanel( )
	{
		JPanel result = new JPanel( );

		this.bu_ok = new JButton( "" );//( ( MessagesDefaultTaskDialog ) this.messages ).button_ok_txt );//Language.get( ).getRessource( LanguageRessource.DEFAULT_TASK_DIALOG_BUTTONS_OK_NAME ) );
		//		this.bu_ok.setToolTipText( ( ( MessagesDefaultTaskDialog ) this.messages ).button_ok_tooltip );//Language.get( ).getRessource( LanguageRessource.DEFAULT_TASK_DIALOG_BUTTONS_OK_TOOLTIP ) );
		this.bu_ok.addActionListener( new ActionListener( )
		{
			public void actionPerformed( ActionEvent e )
			{
				ok_( );
			}
		} );

		result.add( this.bu_ok );
		return result;
	}

	@Override
	public Dimension getMinimumEditorSize( )
	{
		return new Dimension( 200, 200 );
	}

	@Override
	public void progressUpdate( ProgressInformation progressInfo )
	{
		// TODO Auto-generated method stub

	}

}
