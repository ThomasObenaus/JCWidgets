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

import java.awt.Window;
import java.util.ArrayList;

import thobe.task.ProgressInformation;
import thobe.task.Task;
import thobe.task.TaskListener;
import thobe.widgets.editor.Editor;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Thomas Obenaus
 * @source TaskDialog.java
 * @date 10 Sep 2008
 */
@SuppressWarnings ( "serial")
public abstract class TaskDialog extends Editor implements TaskListener
{
	protected TaskPanel				progressPanel;
	private ArrayList<Task>			tasksToFulFill;
	protected MessagesTaskDialog	messages;

	/**
	 * @param owner
	 * @param tasksToFulFill
	 */
	public TaskDialog( Window owner, String title, ArrayList<Task> tasksToFulFill, MessagesTaskDialog messages )
	{
		super( owner, title, ModalityType.APPLICATION_MODAL );
		this.messages = messages;
		this.tasksToFulFill = tasksToFulFill;
		this.setSize( 350, 265 );

		this.progressPanel = new TaskPanel( );
		this.progressPanel.setMsgEmptyTaskChain( this.messages.progressPanel_empty_task_chain );
		this.progressPanel.setMsgInitiate( this.messages.progressPanel_initiate );
		this.progressPanel.setMsgStartingNextTask( this.messages.progressPanel_starting_next_task );
		this.progressPanel.setMsgTaskCanceled( this.messages.progressPanel_task_canceled );
		this.progressPanel.setMsgTaskChainCanceled( this.messages.progressPanel_task_chain_canceled );
		this.progressPanel.addTaskChainListener( this );

		this.buildGUI( );
	}

	/**
	 * Creates the GUI
	 */
	private void buildGUI( )
	{
		FormLayout fla_content = new FormLayout( "3dlu,fill:pref:grow,3dlu", "3dlu,fill:pref:grow,3dlu" );
		CellConstraints cc_content = new CellConstraints( );
		this.getContentPane( ).setLayout( fla_content );
		this.getContentPane( ).add( this.progressPanel, cc_content.xy( 2, 2 ) );
	}

	public void setVisible( boolean visible )
	{
		if ( visible )
			this.startComputation( );
		super.setVisible( visible );

	}

	private void startComputation( )
	{
		ArrayList<TaskPanelElement> elements = new ArrayList<TaskPanelElement>( );
		for ( Task task : this.tasksToFulFill )
		{
			TaskPanelElement progressElement = new TaskPanelElement( task, this.getWidth( ) );
			progressElement.setTxtButtonCancelTooltip( this.messages.progressElement_buttons_cancel_tooltip );
			progressElement.setTxtButtonDetails( this.messages.progressElement_buttons_details_txt );
			progressElement.setTxtButtonDetailsTooltip( this.messages.progressElement_buttons_details_tooltip );
			progressElement.setTxtTaskCanceled( this.messages.progressElement_task_canceled );
			progressElement.setTxtTaskDone( this.messages.progressElement_task_done );
			elements.add( progressElement );
		}

		this.progressPanel.rebuild( elements );
		this.progressPanel.execute( );
	}

	public abstract void taskCancelled( Task task );

	public abstract void taskDone( Task task );

	public abstract void taskStarted( Task task );

	public abstract void progressUpdate( ProgressInformation progressInfo );

}
