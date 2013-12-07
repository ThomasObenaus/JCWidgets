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
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import thobe.task.ProgressInformation;
import thobe.task.Task;
import thobe.task.TaskChain;
import thobe.task.TaskListener;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Panel for displaying the progress of a task-chain (list of Tasks)
 * @author Thomas Obenaus
 * @source ProgressPanel.java
 * @date 27 Jun 2008
 */
@SuppressWarnings ( "serial")
public class TaskPanel extends JPanel implements TaskListener
{

	/**
	 * List of widgets. Each represents the progress of one task.
	 */
	private ArrayList<TaskPanelElement>	progressElements;

	/**
	 * Contentpanel
	 */
	private JPanel						pa_content;

	/**
	 * Progressbar representing the overall-progress of the task-chain
	 */
	private JProgressBar				progressBar;

	/**
	 * The task-chain which should be computed.
	 */
	private TaskChain					taskChain;

	private boolean						running;
	private boolean						cancelled;

	private String						msgEmptyTaskChain;
	private String						msgTaskCanceled;
	private String						msgTaskChainCanceled;
	private String						msgStartingNextTask;
	private String						msgInitiate;

	/**
	 * Def Ctor
	 */
	public TaskPanel()
	{
		this( new ArrayList<TaskPanelElement>( ) );
	}

	/**
	 * Ctor
	 * @param progressElements
	 */
	public TaskPanel( ArrayList<TaskPanelElement> progressElements )
	{
		this.progressElements = progressElements;
		this.msgEmptyTaskChain = "TaskChain is empty. There is no Task to process";
		this.msgTaskCanceled = "The Task canceled: ";
		this.msgTaskChainCanceled = "The TaskChain was canceled";
		this.msgStartingNextTask = "Starting Task: ";
		this.msgInitiate = "Initializing TaskChain";
		this.taskChain = new TaskChain( )
		{
			@Override
			protected String msgEmptyTaskChain( )
			{
				return msgEmptyTaskChain;
			}

			@Override
			protected String msgInitiate( )
			{
				return msgInitiate;
			}

			@Override
			protected String msgStartingNextTask( String nameOfTaskToStart )
			{
				return msgStartingNextTask + nameOfTaskToStart;
			}

			@Override
			protected String msgTaskCanceled( String nameOfTaskCanceled )
			{
				return msgTaskCanceled + nameOfTaskCanceled;
			}

			@Override
			protected String msgTaskChainCanceled( )
			{
				return msgTaskChainCanceled;
			}
		};
		this.running = false;
		this.cancelled = false;
		this.buildTaskChain( );
		this.buildGUI( );
	}

	public void setMsgEmptyTaskChain( String msgEmptyTaskChain )
	{
		this.msgEmptyTaskChain = msgEmptyTaskChain;
	}

	public void setMsgTaskCanceled( String msgTaskCanceled )
	{
		this.msgTaskCanceled = msgTaskCanceled;
	}

	public void setMsgTaskChainCanceled( String msgTaskChainCanceled )
	{
		this.msgTaskChainCanceled = msgTaskChainCanceled;
	}

	public void setMsgStartingNextTask( String msgStartingNextTask )
	{
		this.msgStartingNextTask = msgStartingNextTask;
	}

	public void setMsgInitiate( String msgInitiate )
	{
		this.msgInitiate = msgInitiate;
	}

	public void addTaskChainListener( TaskListener listener )
	{
		this.taskChain.addListener( listener );
	}

	public void removeListener( TaskListener listener )
	{
		this.taskChain.removeListener( listener );
	}

	/**
	 * Creates the task-chain.
	 */
	private void buildTaskChain( )
	{
		ArrayList<Task> tasks = new ArrayList<Task>( );
		for ( TaskPanelElement progElement : this.progressElements )
			tasks.add( progElement.getTask( ) );

		this.taskChain.setTasks( tasks );
		this.taskChain.addListener( this );
	}

	/**
	 * This function will interrupt the TaskChain and will cancel all running Tasks.
	 */
	public void cancelAllRunningTasks( )
	{
		this.taskChain.kill( );
	}

	/**
	 * Execute the task-chain
	 */
	public void execute( )
	{
		this.taskChain.execute( );
	}

	/**
	 * Creates the gui
	 */
	private void buildGUI( )
	{
		this.removeAll( );
		this.setLayout( new BorderLayout( 0, 10 ) );
		this.pa_content = new JPanel( );
		this.pa_content.setBackground( Color.white );
		this.pa_content.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );

		JScrollPane scrpa_content = new JScrollPane( pa_content );
		this.add( scrpa_content, BorderLayout.CENTER );

		//		String rowSpec = "5dlu";
		StringBuffer rowSpec = new StringBuffer( "5dlu" );
		for ( int i = 0; i < this.progressElements.size( ); i++ )
			rowSpec.append( ",pref" );
		rowSpec.append( ",5dlu" );

		FormLayout fla_content = new FormLayout( "2dlu,fill:pref:grow,2dlu", rowSpec.toString( ) );
		this.pa_content.setLayout( fla_content );
		CellConstraints cc_content = new CellConstraints( );

		for ( int i = 0; i < this.progressElements.size( ); i++ )
			this.pa_content.add( this.progressElements.get( i ), cc_content.xy( 2, i + 2 ) );

		this.progressBar = new JProgressBar( );
		this.progressBar.setForeground( this.taskChain.getDefaultProgressBarColor( ) );
		this.progressBar.setBorder( BorderFactory.createLineBorder( this.taskChain.getDefaultProgressBarColor( ) ) );
		this.progressBar.setBorderPainted( true );
		this.add( this.progressBar, BorderLayout.SOUTH );

	}

	/**
	 * Sets the progress of the progressbar
	 * @param value
	 */
	public void setProgress( int value )
	{
		this.progressBar.setValue( value );
	}

	/**
	 * Returns the progresselements
	 * @return
	 */
	public ArrayList<TaskPanelElement> getProgressElements( )
	{
		return this.progressElements;
	}

	/**
	 * Rebuilds the taskchain and the gui using the given progresselements
	 * @param progressElements
	 */
	public void rebuild( ArrayList<TaskPanelElement> progressElements )
	{
		this.progressElements = progressElements;
		this.buildTaskChain( );
		this.buildGUI( );
		this.revalidate( );
	}

	/**
	 * Clears the taskchain, rebuilds the taskchain and rebuilds the gui.
	 */
	public void clear( )
	{
		this.progressElements.clear( );
		this.buildTaskChain( );
		this.buildGUI( );
		this.repaint( );
	}

	/**
	 * Returns true if there are still Tasks left which are not completed already, false otherwise.
	 * @return
	 */
	public boolean isRunning( )
	{
		return this.running;
	}

	/**
	 * Returns true if all Tasks are processed or if at least one Task/ the TaskChain was canceled,
	 * false otherwise.
	 * @return
	 */
	public boolean isDone( )
	{
		return !this.running;
	}

	/**
	 * Returns true if at least one Task or the whole TaskChain was canceled.
	 * @return
	 */
	public boolean isCancelled( )
	{
		return this.cancelled;
	}

	@Override
	public void taskCancelled( Task task )
	{
		this.cancelled = true;
		this.running = false;
		progressBar.setBorder( BorderFactory.createLineBorder( Color.red ) );
		progressBar.setForeground( Color.red );
	}

	@Override
	public void taskDone( Task task )
	{
		this.running = false;
	}

	@Override
	public void taskStarted( Task task )
	{
		this.running = true;
	}

	@Override
	public void progressUpdate( ProgressInformation progressInfo )
	{
		this.progressBar.setValue( progressInfo.getProgress( ) );
	}
}
