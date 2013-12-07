/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.floatingPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import thobe.widgets.layout.WidgetLayout;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Thomas Obenaus
 * @source FloatingPanel.java
 * @date 24.07.2009
 */
@SuppressWarnings ( "serial")
public abstract class ExpandableFloatingPanel extends FloatingPanel
{
	private JToggleButton				bu_expand;
	protected FloatingPanelMessageLabel	message_label;
	private boolean						show_message_label;

	private JPanel						content_panel;
	private JScrollPane					scrpa_content;
	private boolean						expanded;
	private String						name;
	private JLabel						l_title;
	private JPanel						pa_header;
	private JPanel						pa_additionalHeaderPanel;

	private int							componentHeaderSize;

	private Color						headerBGActive		= WidgetLayout.HEADER_BACKGROUND;
	private Color						headerFGActive		= WidgetLayout.HEADER_FOREGROUND;
	private Color						headerBGInActive	= WidgetLayout.HEADER_BACKGROUND_INACTIVE;
	private Color						headerFGInActive	= WidgetLayout.HEADER_FOREGROUND_INACTIVE;
	private Icon						collapsedPressedIcon;
	private Icon						expandedPressedIcon;
	private String						ttTxtExpaned;
	private String						ttTxtCollapsed;

	public ExpandableFloatingPanel( String name, boolean expanded )
	{
		this.componentHeaderSize = 0;
		this.show_message_label = false;
		this.content_panel = new JPanel( );
		this.name = name;
		this.expanded = expanded;
		this.ttTxtCollapsed = "Expand";
		this.ttTxtExpaned = "Collapse";
		this.buidGUI( );

		this.setHeaderBackground( headerBGActive );
		this.setHeaderForeground( headerFGActive );
		this.addComponentListener( new ComponentAdapter( )
		{
			@Override
			public void componentResized( ComponentEvent e )
			{
				handleResized( );
			}
		} );

		this.pa_header.addComponentListener( new ComponentAdapter( )
		{
			@Override
			public void componentResized( ComponentEvent e )
			{
				handleHeaderResized( );
			}

		} );
	}

	public void setIconSet( Icon collapsed, Icon collapsedRollover, Icon collapedPressed, Icon expanded, Icon expandedRollover, Icon expandedPressed )
	{
		this.bu_expand.setRolloverIcon( collapsedRollover );
		this.collapsedPressedIcon = collapedPressed;
		this.bu_expand.setPressedIcon( this.collapsedPressedIcon );
		this.bu_expand.setIcon( collapsed );

		this.bu_expand.setRolloverSelectedIcon( expandedRollover );
		this.bu_expand.setSelectedIcon( expanded );
		this.expandedPressedIcon = expandedPressed;

		this.bu_expand.setBorderPainted( false );
		this.bu_expand.setContentAreaFilled( false );
		this.bu_expand.setFocusPainted( false );
		this.bu_expand.setMargin( new Insets( 0, 0, 0, 0 ) );
		this.bu_expand.setBorder( null );
		this.bu_expand.setText( "" );
	}

	private boolean hasIconSet( )
	{
		if ( this.bu_expand.getSelectedIcon( ) != null )
			return true;
		if ( this.bu_expand.getIcon( ) != null )
			return true;
		if ( this.bu_expand.getRolloverIcon( ) != null )
			return true;
		if ( this.bu_expand.getRolloverSelectedIcon( ) != null )
			return true;
		if ( this.bu_expand.getPressedIcon( ) != null )
			return true;
		if ( this.expandedPressedIcon != null )
			return true;
		if ( this.collapsedPressedIcon != null )
			return true;
		return false;
	}

	/**
	 * @deprecated Use {@link ExpandableFloatingPanel#setIconSet(Icon)} instead.
	 * @param iconCollapsedEnabled
	 * @param iconCollapsedDisabled
	 * @param iconExpandedEnabled
	 * @param iconExpandedDisabled
	 */
	public void setIcons( Icon iconCollapsedEnabled, Icon iconCollapsedDisabled, Icon iconExpandedEnabled, Icon iconExpandedDisabled )
	{}

	private void handleHeaderResized( )
	{
		if ( this.componentHeaderSize == 0 )
		{
			this.componentHeaderSize = getPanelHeight( );
			if ( this.getFloatingPanelContainer( ) != null )
				this.getFloatingPanelContainer( ).doLayout( );
		}
	}

	/**
	 * This method should return a JPanel with the components that should be placed in the header of
	 * the FloatingPanel. If no additional components should be placed in the FloatingPanel-Header
	 * you should implement this method returning null.
	 * @return
	 */
	public abstract JPanel getAdditionalHeaderPanel( );

	private void handleResized( )
	{
		Rectangle rect = this.content_panel.getVisibleRect( );
		this.scrpa_content.setPreferredSize( new Dimension( rect.width, rect.height ) );
		this.message_label.updateTextSize( );
	}

	public void updateGUI( int width )
	{
		super.updateGUI( width );
		this.message_label.updateGUI( width );
	}

	public void setPanelSize( int width, int height )
	{
		super.setPanelSize( width, height );
		if ( this.isRestrictShrinking( ) && expanded )
		{
			if ( this.panelHeight < this.getMinimumSize( ).height )
				this.panelHeight = this.getMinimumSize( ).height;
			if ( this.panelWidth < this.getMinimumSize( ).width )
				this.panelWidth = this.getMinimumSize( ).width;
		}
	}

	public void setExpandCollapseButtonTooltipText( String ttTxtCollapsed, String ttTxtExpaned )
	{
		this.ttTxtCollapsed = ttTxtCollapsed;
		this.ttTxtExpaned = ttTxtExpaned;

		if ( this.expanded )
			this.bu_expand.setToolTipText( this.ttTxtExpaned );
		else this.bu_expand.setToolTipText( this.ttTxtCollapsed );

	}

	public int getPanelHeight( )
	{
		if ( !this.expanded || this.panelHeight == 0 )
			return this.pa_header.getSize( ).height + 2;
		else return this.panelHeight;
	}

	@Override
	public int getMinimumHeight( )
	{
		return this.pa_header.getHeight( ) - 6;
	}

	private void setHeaderBG( Color bg )
	{
		this.pa_header.setBackground( bg );
		if ( this.pa_additionalHeaderPanel != null )
			this.pa_additionalHeaderPanel.setBackground( bg );
	}

	private void setHeaderFG( Color fg )
	{
		this.l_title.setForeground( fg );
	}

	public void setHeaderFont( Font font )
	{
		this.l_title.setFont( font );
	}

	public void setHeaderBackgroundInActive( Color headerBGInActive )
	{
		this.headerBGInActive = headerBGInActive;
		if ( !this.isEnabled( ) )
			this.setHeaderBG( this.headerBGInActive );
	}

	public void setHeaderForeGroundInActive( Color headerFGInActive )
	{
		this.headerFGInActive = headerFGInActive;
		if ( !this.isEnabled( ) )
			this.setHeaderFG( this.headerFGInActive );
	}

	public void setHeaderBackground( Color bg )
	{
		this.headerBGActive = bg;
		if ( this.isEnabled( ) )
			this.setHeaderBG( this.headerBGActive );
	}

	public void setHeaderForeground( Color fg )
	{
		this.headerFGActive = fg;

		if ( this.isEnabled( ) )
			this.setHeaderFG( this.headerFGActive );
	}

	private void buidGUI( )
	{
		this.show_message_label = false;

		super.setLayout( new BorderLayout( ) );

		/* header */
		FormLayout fla_header = new FormLayout( "2dlu,fill:20dlu:grow,3dlu,fill:pref:grow,3dlu,default,2dlu", "1dlu,center:default,1dlu" );
		CellConstraints cc_header = new CellConstraints( );
		this.pa_header = new JPanel( fla_header );
		this.pa_header.setToolTipText( this.name );
		this.pa_header.addMouseListener( new MouseAdapter( )
		{
			@Override
			public void mouseClicked( MouseEvent e )
			{
				if ( e.getClickCount( ) == 2 )
					handleButtonExpandPressed( );
			}
		} );
		super.add( pa_header, BorderLayout.NORTH );

		/* title */
		this.l_title = new JLabel( name );
		pa_header.add( l_title, cc_header.xy( 2, 2 ) );

		/* additional header panel */
		this.pa_additionalHeaderPanel = this.getAdditionalHeaderPanel( );
		if ( this.pa_additionalHeaderPanel != null )
		{
			this.pa_header.add( this.pa_additionalHeaderPanel, cc_header.xy( 4, 2 ) );
		}

		/* button */
		this.bu_expand = new JToggleButton( );
		String buttonTxt = "+";

		if ( this.expanded )
		{
			buttonTxt = "-";
			this.bu_expand.setSelected( true );
		}

		this.pa_header.add( this.bu_expand, cc_header.xy( 6, 2 ) );
		this.bu_expand.setText( buttonTxt );
		this.bu_expand.setPressedIcon( this.bu_expand.isSelected( ) ? this.collapsedPressedIcon : this.expandedPressedIcon );
		this.bu_expand.setToolTipText( this.expanded ? this.ttTxtExpaned : this.ttTxtCollapsed );
		this.bu_expand.addActionListener( new ActionListener( )
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				ExpandableFloatingPanel.this.setExpaned( bu_expand.isSelected( ) );

			}
		} );

		scrpa_content = new JScrollPane( this.content_panel );
		// TODO add the scrpa_content here
		//super.add( scrpa_content, BorderLayout.CENTER );
		super.add( this.content_panel, BorderLayout.CENTER );
		this.content_panel.setVisible( expanded );
		this.scrpa_content.setVisible( expanded );

		message_label = new FloatingPanelMessageLabel( "", FloatingPanelMessageLabel.INFO );
		message_label.setVisible( false );
		super.add( message_label, BorderLayout.SOUTH );

		this.setBorder( BorderFactory.createLineBorder( Color.lightGray ) );
	}

	@Override
	public void setToolTipText( String text )
	{
		this.pa_header.setToolTipText( text );
	}

	/**
	 * Controls the look when the panel is shown/ hidden.
	 */
	private void handleButtonExpandPressed( )
	{
		this.setExpaned( !this.expanded );
	}

	/**
	 * Set the text of the panels messagelabel.
	 * @param type
	 * @param text
	 */
	public void setMessageLabel( int type, String text )
	{
		this.show_message_label = true;
		this.message_label.setText( text, type );
		message_label.setVisible( this.expanded );
	}

	/**
	 * Control the visibility of the panels messagelabel.
	 * @param visible
	 */
	public void setMessageLabelVisibility( boolean visible )
	{

		this.show_message_label = visible;
		message_label.setVisible( visible );
	}

	public boolean isExpanded( )
	{
		return expanded;
	}

	@Override
	public void setLayout( LayoutManager mgr )
	{
		if ( this.content_panel != null )
			this.content_panel.setLayout( mgr );
	}

	@Override
	public void add( Component comp, Object constraints )
	{
		this.content_panel.add( comp, constraints );
	}

	@Override
	public void add( Component comp, Object constraints, int index )
	{
		this.content_panel.add( comp, constraints, index );
	}

	@Override
	public Component add( Component comp, int index )
	{
		return this.content_panel.add( comp, index );
	}

	@Override
	public Component add( Component comp )
	{
		return this.content_panel.add( comp );
	}

	public void setTitle( String title )
	{
		this.l_title.setText( title );
		this.pa_header.setToolTipText( title );
	}

	public void setExpaned( boolean expaned )
	{
		this.expanded = expaned;
		if ( !this.expanded )
		{
			if ( !this.hasIconSet( ) )
				this.bu_expand.setText( "+" );
			this.expanded = false;
			message_label.setVisible( false );
		}
		else
		{
			if ( !this.hasIconSet( ) )
				this.bu_expand.setText( "-" );
			this.expanded = true;
			if ( show_message_label )
				message_label.setVisible( true );
		}
		this.bu_expand.setSelected( this.expanded );
		this.content_panel.setVisible( this.expanded );
		this.scrpa_content.setVisible( this.expanded );
		this.bu_expand.setPressedIcon( this.expanded ? this.expandedPressedIcon : this.collapsedPressedIcon );
		this.bu_expand.setToolTipText( this.expanded ? this.ttTxtExpaned : this.ttTxtCollapsed );
		if ( this.getFloatingPanelContainer( ) != null )
			this.getFloatingPanelContainer( ).doLayout( );
	}

	public void setEnabled( boolean enabled )
	{
		super.setEnabled( enabled );

		this.scrpa_content.setEnabled( enabled );
		this.content_panel.setEnabled( enabled );
		this.bu_expand.setEnabled( enabled );

		if ( enabled )
		{
			this.setHeaderBG( this.headerBGActive );
			this.setHeaderFG( this.headerFGActive );
		}
		else
		{
			this.setHeaderBG( this.headerBGInActive );
			this.setHeaderFG( this.headerFGInActive );
		}

	}

	@Override
	public boolean canGrow( )
	{
		return this.expanded;
	}

	@Override
	public void removeAll( )
	{
		this.content_panel.removeAll( );

	}

}
