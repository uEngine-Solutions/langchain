package org.uengine.kernel.designer;

import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.icon.IconWrapperResizableIcon;
import org.jvnet.flamingo.common.icon.ResizableIcon;
import org.jvnet.flamingo.svg.SvgBatikResizableIcon;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.painter.SpecularGradientPainter;
import org.jvnet.substance.utils.SubstanceConstants;
import org.jvnet.flamingo.common.icon.IconWrapperResizableIcon;
import org.uengine.kernel.Activity;
import org.uengine.kernel.DefaultActivity;
import org.uengine.kernel.SubProcessActivity;
import org.uengine.processdesigner.ActivityDesignerListener;
import org.uengine.processdesigner.ActivityLabel;
import org.uengine.processdesigner.ArrowReceiver;
import org.uengine.processdesigner.KeyListenerTransferrer;
import org.uengine.processdesigner.LoadedDefinition;
import org.uengine.processdesigner.MouseListenerTransferrer;
import org.uengine.processdesigner.ProcessDesigner;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jinyoung Jang
 */

public class DefaultActivityDesigner extends AbstractActivityDesigner implements ArrowReceiver{

	JCommandButton nameButton;
	//JPanel activityPanel;

	public void setActivity(Activity value){
		super.setActivity(value);
		if(value.getDescription()!=null)
			setToolTipText(value.getDescription().getText());
					
		try{
			URL btnIconResourceUrl = getClass().getClassLoader().getResource(
					ActivityLabel.getSVGIconPath(value.getClass()));
			
			ResizableIcon btnSVGIcon = null;
			
			if(btnIconResourceUrl != null) {
				btnSVGIcon = SvgBatikResizableIcon.getSvgIcon(
						btnIconResourceUrl, new Dimension(24, 24));
			}
				
			if(btnSVGIcon == null){	//if there's no SVG icon for requested activity type, try to use the GIF image icon instead.
				ImageIcon activityGifImageIcon = ActivityLabel.getImageIcon(value.getClass());
				if(activityGifImageIcon!=null)
					btnSVGIcon = new IconWrapperResizableIcon(activityGifImageIcon);
			}
			

			if(btnSVGIcon==null){
				btnIconResourceUrl = getClass().getClassLoader().getResource(
						ActivityLabel.getSVGIconPath(DefaultActivity.class));

				btnSVGIcon = SvgBatikResizableIcon.getSvgIcon(
						btnIconResourceUrl, new Dimension(24, 24));				
			}

			if(btnSVGIcon != null) {
				nameButton = new JCommandButton(value.getName().getText(), btnSVGIcon);
				nameButton.putClientProperty(
						SubstanceLookAndFeel.FLAT_PROPERTY, Boolean.FALSE);
				//nameButton.putClientProperty(SubstanceLookAndFeel.GRADIENT_PAINTER_PROPERTY,
				//		  new org.jvnet.substance.painter.SpecularWaveGradientPainter());
				nameButton.setState(org.jvnet.flamingo.common.ElementState.CUSTOM, true); 
				//nameButton.setSize()

//					nameButton.putClientProperty(
//							SubstanceLookAndFeel.BUTTON_SHAPER_PROPERTY, 
//					  		new org.jvnet.substance.button.ClassicButtonShaper()); 
//					nameButton.putClientProperty(SubstanceLookAndFeel.BUTTON_NO_MIN_SIZE_PROPERTY, 
//							  Boolean.TRUE);			
				
				nameButton.setHorizontalTextPosition(AbstractButton.RIGHT);
				
				add("Center", nameButton);
				add("North", dotDotDot);
				dotDotDot.setVisible(collapsed);
				
				nameButton.addKeyListener(new KeyListenerTransferrer(this));
				nameButton.addMouseListener(new MouseListenerTransferrer(this));
			}
			

		}catch(Exception e){
		}
	}

	ComplexActivityDesigner activityParent;
		public ComplexActivityDesigner getActivityParent() {
			return activityParent;
		}
		public void setActivityParent(ComplexActivityDesigner value) {
			activityParent = value;
		}

	public DefaultActivityDesigner(){
		super();
		this.setLayout(new BorderLayout());
		//activityPanel = new JPanel();
		//activityPanel.setLayout(new BorderLayout());
		
		//this.add(activityPanel);
	}
	
	public void setText(String msg){
		if(nameButton != null){
			nameButton.setText(msg);
			nameButton.revalidate();
			nameButton.repaint();
		}
	}

	public void setBackground(Color color){
		//transfer the bg color to the button
		
		if(nameButton!=null)
			nameButton.setBackground(color);
		
		super.setBackground(color);
	}
	public void setCursor(Cursor cur){
		//transfer the cursor style to the button
		
		if(nameButton!=null)
			nameButton.setCursor(cur);
		
		super.setCursor(cur);
	}

	public void setStatus(String status) {
		Activity activitiy = getActivity();
		if(activity!=null && !activity.isDynamicChangeAllowed()){
			nameButton.setEnabled(false);
			return;
		}
		
		if(!status.equals(Activity.STATUS_READY)){
			nameButton.setEnabled(false);
		}
	}

	public JCommandButton getNameButton() {
		return nameButton;
	}
	
	protected Component getSymbolicComponent() {		
		return getNameButton();
	}
	
	public int getArrowReceivePointIn() {
		// TODO Auto-generated method stub
		return getHeight()/2;
	}
	
	public int getArrowReceivePointOut() {
		// TODO Auto-generated method stub
		return getHeight()/2;
	}
	public boolean receiveArrow() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	protected void toggle() {
		nameButton.setVisible(!collapsed);
		dotDotDot.setVisible(collapsed);
		
		revalidate();
	}
	
	JLabel dotDotDot = new JLabel("...");

}

