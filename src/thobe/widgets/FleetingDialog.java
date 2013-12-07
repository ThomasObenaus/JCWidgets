/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * @author Thomas Obenaus
 * @source FleetingDialog.java
 * @date 22.09.2010
 */
@SuppressWarnings ( "serial")
public abstract class FleetingDialog extends JDialog
{

	private JPanel	pa_main;

	public FleetingDialog( Window owner, int width, int height )
	{
		super( owner );

		this.setAlwaysOnTop( true );
		this.setUndecorated( true );
		this.setResizable( false );

		this.addMouseListener( new MouseAdapter( )
		{
			public void mouseClicked( MouseEvent e )
			{
				setVisible( false );
				dispose( );
			}
		} );

		this.addKeyListener( new KeyListener( )
		{

			public void keyTyped( KeyEvent e )
			{
				e.consume( );
			}

			public void keyReleased( KeyEvent e )
			{
				e.consume( );
				setVisible( false );
				dispose( );
			}

			public void keyPressed( KeyEvent e )
			{
				e.consume( );
			}
		} );

		this.addFocusListener( new FocusAdapter( )
		{

			public void focusLost( FocusEvent e )
			{
				setVisible( false );
				dispose( );
			}
		} );

		Rectangle bounds = owner.getBounds( );

		this.setBounds( ( bounds.x + ( bounds.width / 2 ) ) - width / 2, ( bounds.y + ( bounds.height / 2 ) ) - height / 2, width, height );

		super.setLayout( new BorderLayout( ) );
		this.pa_main = new JPanel( new BorderLayout( 0, 0 ) );
		super.add( this.pa_main, BorderLayout.CENTER );
		this.pa_main.setBorder( BorderFactory.createLineBorder( Color.DARK_GRAY ) );

		this.buildGUI( );
	}

	@Override
	public Component add( Component comp )
	{
		if ( this.pa_main == null )
			return super.add( comp );
		return this.pa_main.add( comp );
	}

	@Override
	public Component add( Component comp, int index )
	{
		if ( this.pa_main == null )
			return super.add( comp, index );
		return this.pa_main.add( comp, index );
	}

	@Override
	public void add( Component comp, Object constraints )
	{
		if ( this.pa_main == null )
		{
			super.add( comp, constraints );
			return;
		}
		this.pa_main.add( comp, constraints );
	}

	@Override
	public void remove( Component comp )
	{
		if ( this.pa_main == null )
			return;
		this.pa_main.remove( comp );
	}

	@Override
	public void remove( int index )
	{
		if ( this.pa_main == null )
			return;
		this.pa_main.remove( index );
	}

	@Override
	public void removeAll( )
	{
		if ( this.pa_main == null )
			return;
		this.pa_main.removeAll( );
	}

	@Override
	public void setLayout( LayoutManager manager )
	{
		if ( this.pa_main == null )
		{
			super.setLayout( manager );
			return;
		}
		this.pa_main.setLayout( manager );
	}

	protected abstract void buildGUI( );

}
