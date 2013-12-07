/*
 *  Copyright (C) 2013, Thomas Obenaus. All rights reserved.
 *  Licensed under the New BSD License (3-clause lic)
 *  See attached license-file.
 *
 *	Author: 	Thomas Obenaus
 *	EMail:		obenaus.thomas@gmail.com
 *  Project:    JavaComponents/Widgets
 */
package thobe.widgets.comboBoxButton;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import sun.swing.DefaultLookup;
import thobe.widgets.buttons.IconButton;
import thobe.widgets.icons.IconLib;

/**
 * @author Thomas Obenaus
 * @source ComboboxButton.java
 * @date 22.02.2012
 */
@SuppressWarnings ( "serial")
public class ComboboxButton extends JComboBox<ComboBoxButtonEntry>
{
	private static int	IconWidth	= 36;

	public ComboboxButton( ComboBoxModel<ComboBoxButtonEntry> model )
	{
		super( model );
	}

	public ComboboxButton( ComboBoxButtonEntry[] items )
	{
		super( items );
	}

	public ComboboxButton( Vector<ComboBoxButtonEntry> items )
	{
		super( items );
	}

	//	@Override
	//	public Insets getInsets( )
	//	{
	// TODO Auto-generated method stub
	//	return new Insets(10,0,0,0);
	//	}

	@Override
	public void updateUI( )
	{

		this.setBorder( BorderFactory.createLineBorder( Color.red ) );
		super.updateUI( );

		this.setUI( ( ComboBoxUI ) ComboBoxButtonUI.createUI( this ) );
	}

	@Override
	public Object getSelectedItem( )
	{
		if ( this.isPopupVisible( ) && !this.hasFocus( ) )
			return super.getSelectedItem( );

		/* return the icon for the combobox (not for the popup) */
		ComboBoxButtonEntry entry = ( ComboBoxButtonEntry ) super.getSelectedItem( );
		return entry.getEntryIcon( );
	}

	//	@Override
	//	public int getWidth( )
	//	{
	//		IconButton b = new IconButton( IconLib.get( ).getHelpE( ), IconLib.get( ).getHelpE( ) );
	//		Dimension dim = b.getPreferredSize( );
	//		System.out.println(dim);
	//		return (int)dim.getWidth( ) * 2;
	//	}
	//	

}

class ComboBoxButtonUI extends BasicComboBoxUI implements LayoutManager
{
	public static ComponentUI createUI( JComponent c )
	{

		return new ComboBoxButtonUI( );
	}

	@Override
	public Dimension getMinimumSize( JComponent c )
	{
		if ( !isMinimumSizeDirty )
		{
			return new Dimension( cachedMinimumSize );
		}
		Dimension size = getDisplaySize( );
		Insets insets = getInsets( );
		//calculate the width and height of the button
		int buttonHeight = size.height;
		int buttonWidth = squareButton ? buttonHeight : 0;//arrowButton.getPreferredSize( ).width;
		//adjust the size based on the button width
		size.height += insets.top + insets.bottom;
		size.width += insets.left + insets.right + buttonWidth;

		cachedMinimumSize.setSize( size.width, size.height );
		isMinimumSizeDirty = false;

		return new Dimension( size );
	}

	protected void installComponents( )
	{
		//		arrowButton = createArrowButton( );
		//		comboBox.add( arrowButton );

		//		if ( arrowButton != null )
		//		{
		//			configureArrowButton( );
		//		}

		if ( comboBox.isEditable( ) )
		{
			addEditor( );
		}

		comboBox.add( currentValuePane );
	}

	protected JButton createArrowButton( )
	{
		IconButton button = new IconButton( IconLib.get( ).getHelpD( ), IconLib.get( ).getHelpD( ) );
		return button;
	}

	@Override
	protected ComboPopup createPopup( )
	{
		return new ComboBoxButtonPopup( comboBox );
	}

	@Override
	protected ListCellRenderer createRenderer( )
	{
		return new ComboBoxButtonRenderer( );
	}

	//		protected Dimension getDisplaySize( )
	//		{
	//	
	//			Dimension result = new Dimension( );
	//	
	//			ListCellRenderer renderer = comboBox.getRenderer( );
	//			if ( renderer == null )
	//			{
	//				renderer = new DefaultListCellRenderer( );
	//			}
	//	
	//			Object prototypeValue = comboBox.getPrototypeDisplayValue( );
	//			if ( prototypeValue != null )
	//			{
	//				// Calculates the dimension based on the prototype value
	//				result = getSizeForComponent( renderer.getListCellRendererComponent( listBox, prototypeValue, -1, false, false ) );
	//				System.out.println( "ComboBoxButtonUI.getDisplaySize() A " + result );
	//			}
	//			else
	//			{
	//				// Calculate the dimension by iterating over all the elements in the combo
	//				// box list.
	//				ComboBoxModel model = comboBox.getModel( );
	//				int modelSize = model.getSize( );
	//				int baseline = -1;
	//				Dimension d;
	//	
	//				Component cpn;
	//	
	//				if ( modelSize > 0 )
	//				{
	//					for ( int i = 0; i < modelSize; i++ )
	//					{
	//						// Calculates the maximum height and width based on the largest
	//						// element
	//						Object value = model.getElementAt( i );
	//						Component c = renderer.getListCellRendererComponent( listBox, value, -1, false, false );
	//						d = getSizeForComponent( c );
	//	
	//						result.width = Math.max( result.width, d.width );
	//						result.height = Math.max( result.height, d.height );
	//					}
	//				}
	//				else
	//				{
	//					result = getDefaultSize( );
	//					if ( comboBox.isEditable( ) )
	//					{
	//						result.width = 100;
	//					}
	//				}
	//			}
	//	
	//			if ( comboBox.isEditable( ) )
	//			{
	//				Dimension d = editor.getPreferredSize( );
	//				result.width = Math.max( result.width, d.width );
	//				result.height = Math.max( result.height, d.height );
	//			}
	//	
	//			// calculate in the padding
	//			if ( padding != null )
	//			{
	//				result.width += padding.left + padding.right;
	//				result.height += padding.top + padding.bottom;
	//			}
	//	
	////			result.height = 60;
	//			result.width = 38;
	//			System.out.println( "ComboBoxButtonUI.getDisplaySize() " + result );
	//	
	//			return result;
	//		}
	//
	//	@Override
	//	public Dimension getMinimumSize( JComponent c )
	//	{
	//		System.out.println( "ComboBoxButtonUI.getMinimumSize() " + isMinimumSizeDirty );
	//		if ( !isMinimumSizeDirty )
	//		{
	//			return new Dimension( cachedMinimumSize );
	//		}
	//		Dimension size = getDisplaySize( );
	//		Insets insets = getInsets( );
	//		//calculate the width and height of the button
	//		int buttonHeight = size.height;
	//		int buttonWidth = squareButton ? buttonHeight : arrowButton.getPreferredSize( ).width;
	//		//adjust the size based on the button width
	//		size.height += insets.top + insets.bottom;
	//		size.width += insets.left + insets.right + buttonWidth;
	//
	//		cachedMinimumSize.setSize( size.width, size.height );
	//		isMinimumSizeDirty = false;
	//
	//		return new Dimension( size );
	//	}
	//
	//	@Override
	//	public void paint( Graphics g, JComponent c )
	//	{
	//		// TODO Auto-generated method stub
	//		super.paint( g, c );
	//	}

	@Override
	public void paintCurrentValue( Graphics g, Rectangle bounds, boolean hasFocus )
	{
		ListCellRenderer renderer = comboBox.getRenderer( );
		Component c;

		if ( hasFocus && !isPopupVisible( comboBox ) )
		{
			c = renderer.getListCellRendererComponent( listBox, comboBox.getSelectedItem( ), -1, true, false );
		}
		else
		{
			c = renderer.getListCellRendererComponent( listBox, comboBox.getSelectedItem( ), -1, false, false );
			c.setBackground( UIManager.getColor( "ComboBox.background" ) );
		}
		c.setFont( comboBox.getFont( ) );
		if ( hasFocus && !isPopupVisible( comboBox ) )
		{
			c.setForeground( listBox.getSelectionForeground( ) );
			c.setBackground( listBox.getSelectionBackground( ) );
		}
		else
		{
			if ( comboBox.isEnabled( ) )
			{
				c.setForeground( comboBox.getForeground( ) );
				c.setBackground( comboBox.getBackground( ) );
			}
			else
			{
				c.setForeground( DefaultLookup.getColor( comboBox, this, "ComboBox.disabledForeground", null ) );
				c.setBackground( DefaultLookup.getColor( comboBox, this, "ComboBox.disabledBackground", null ) );
			}
		}

		// Fix for 4238829: should lay out the JPanel.
		boolean shouldValidate = false;
		if ( c instanceof JPanel )
		{
			shouldValidate = true;
		}

		int x = bounds.x, y = bounds.y, w = bounds.width, h = bounds.height;
		if ( padding != null )
		{
			x = bounds.x + padding.left;
			y = bounds.y + padding.top;
			w = bounds.width - ( padding.left + padding.right );
			h = bounds.height - ( padding.top + padding.bottom );
		}

		currentValuePane.paintComponent( g, c, comboBox, x, y, w, h, shouldValidate );
	}

	@Override
	protected LayoutManager createLayoutManager( )
	{

		return this;
	}

	public void addLayoutComponent( String name, Component comp )
	{}

	public void removeLayoutComponent( Component comp )
	{}

	public Dimension preferredLayoutSize( Container parent )
	{
		return parent.getPreferredSize( );
	}

	public Dimension minimumLayoutSize( Container parent )
	{
		return parent.getMinimumSize( );
	}

	@Override
	public void layoutContainer( Container parent )
	{
		JComboBox cb = ( JComboBox ) parent;
		int width = 40;// cb.getWidth( );
		int height = cb.getHeight( );

		Insets insets = getInsets( );
		int buttonHeight = height - ( insets.top + insets.bottom );
		int buttonWidth = buttonHeight;
		if ( arrowButton != null )
		{
			Insets arrowInsets = arrowButton.getInsets( );
			buttonWidth = squareButton ? buttonHeight : arrowButton.getPreferredSize( ).width + arrowInsets.left + arrowInsets.right;
		}
		Rectangle cvb;

		if ( arrowButton != null )
		{
			if ( cb.getComponentOrientation( ).isLeftToRight( ) )
			{
				arrowButton.setBounds( width - ( insets.right + buttonWidth ), insets.top, buttonWidth, buttonHeight );
			}
			else
			{
				arrowButton.setBounds( insets.left, insets.top, buttonWidth, buttonHeight );
			}
		}
		if ( editor != null )
		{
			cvb = rectangleForCurrentValue( );
			editor.setBounds( cvb );
		}

	}

}

class bb extends JLabel
{
	public bb( )
	{
		this.setIcon( IconLib.get( ).getHelpE( ) );
	}
}

class ComboBoxButtonPopup extends BasicComboPopup
{
	public ComboBoxButtonPopup( JComboBox combo )
	{
		super( combo );
	}

	@Override
	public void show( Component invoker, int x, int y )
	{
		Dimension popupSize = comboBox.getSize( );
		Insets insets = getInsets( );

		// reduce the width of the scrollpane by the insets so that the popup
		// is the same width as the combo box.
		popupSize.setSize( popupSize.width - ( insets.right + insets.left ), getPopupHeightForRowCount( comboBox.getMaximumRowCount( ) ) );
		Rectangle popupBounds = computePopupBounds( 0, comboBox.getBounds( ).height, popupSize.width, popupSize.height );

		y -= popupBounds.height + comboBox.getHeight( );
		super.show( invoker, x, y );
	}

}

class ComboBoxButtonRenderer extends BasicComboBoxRenderer
{
	public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
	{
		JLabel c = ( JLabel ) super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );

		if ( value instanceof Icon )
			c.setText( "" );
		else c.setIcon( null );
		return c;
	}
}
