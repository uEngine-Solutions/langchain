package org.uengine.kernel.designer;

import javax.swing.*;

import org.jvnet.flamingo.svg.SvgBatikResizableIcon;
import org.metaworks.ui.Separator;
import org.uengine.kernel.Activity;
import org.uengine.kernel.ComplexActivity;
import org.uengine.kernel.ScopeActivity;
import org.uengine.processdesigner.ActivityDesigner;
import org.uengine.processdesigner.ActivityDesignerListener;
import org.uengine.processdesigner.ActivityLabel;
import org.uengine.processdesigner.ArrowLabel;
import org.uengine.processdesigner.ArrowReceiver;
import org.uengine.processdesigner.EventHandlerPanel;
import org.uengine.processdesigner.HorizontalSeparator;
import org.uengine.processdesigner.ProxyPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.*;

/**
 * @author Jinyoung Jang
 */

public class ScopeActivityDesigner extends ComplexActivityDesigner implements ArrowReceiver{
	EventHandlerPanel eventHandlerPanel;
	JPanel innerPanel;
	SvgBatikResizableIcon evtSVGIcon;
	
	public ScopeActivityDesigner(){
		super();
		
		URL eventIconResourceUrl = getClass().getClassLoader().getResource("org/uengine/kernel/images/svg/event.svg");
		if(eventIconResourceUrl != null) {
			evtSVGIcon = SvgBatikResizableIcon.getSvgIcon(
				eventIconResourceUrl, new Dimension(32, 32));
		}

	}
	
	public void setActivity(Activity act) {
		// TODO Auto-generated method stub
		super.setActivity(act);
		
		if(eventHandlerPanel!=null)
			eventHandlerPanel.setScopeActivity((ScopeActivity)act);
	}

	protected void initialize(){
		
		JPanel designArea = new ProxyPanel();
		
		designArea.setLayout(new BorderLayout());
		designArea.setBorder(BorderFactory.createEmptyBorder());
		
			
		innerPanel = new ProxyPanel();
		
		//innerPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		innerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 2));
		innerPanel.setBorder(BorderFactory.createEmptyBorder(10,0,20,0));

		centerPanel = new ProxyPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		innerPanel.add(centerPanel);
		
		innerPanel.add(new ArrowLabel(){				
			public void onDropped() {
				setSelected(false);
				//TODO: change to use the standard drag&drop framework
				Vector selectedComps = ActivityDesignerListener.getSelectedComponents();
				if(selectedComps!=null){
					insertActivityDesigners(selectedComps, -1);
				}
	
				ActivityDesignerListener.bDragging = false;
			}
		});
		
		designArea.add("Center", innerPanel);
		
		eventHandlerPanel = new EventHandlerPanel();
		
		
		
		designArea.add("South", eventHandlerPanel);
		
		setLayout(new BorderLayout());
		//add("West", new ActivityLabel(ScopeActivity.class));
		add("Center", designArea);
		
	}
	
	public int getBaseline(int width, int height) {
		// TODO Auto-generated method stub
		return innerPanel.getHeight() + innerPanel.getY();
	}

	public synchronized void removeActivity(ActivityDesigner designer){
		ScopeActivity scopeActivityInDesign = (ScopeActivity)getActivity();
		if(scopeActivityInDesign.getChildActivities().contains(designer.getActivity())){
			super.removeActivity(designer);
		}else{
			if(eventHandlerPanel!=null)
				eventHandlerPanel.removeActivityDesigner(designer);
		}
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		if(innerPanel!=null){
			Stroke stroke = new BasicStroke(1.5f);
			stroke = new BasicStroke(0.3f,0,0,4.0f,null,0.0f);
			g2.setStroke(stroke);
			g2.setColor(new Color(150, 150, 150));

			g2.drawRoundRect(0, 1, innerPanel.getWidth()-1, innerPanel.getHeight()-2, 16, 16);
			
//			if(evtSVGIcon != null) {
//				
//				evtSVGIcon.paintIcon(this, g2, 32, innerPanel.getHeight());
//			}
		
			if(evtSVGIcon != null) {
				ScopeActivity scopeActivity = (ScopeActivity)getActivity();
				if(scopeActivity!=null && scopeActivity.getEventHandlers()!=null)
					for(int i=0; i<scopeActivity.getEventHandlers().length; i++){
						evtSVGIcon.paintIcon(this, g2, 32*i, innerPanel.getHeight()-16);
					}
			}
		}
		
		if(!(this instanceof ProcessDefinitionDesigner)) g2.dispose();
	}

	public int getArrowReceivePointIn() {
		// TODO Auto-generated method stub
		return getBaseline(0,0)/2 - 5;
	}

	public int getArrowReceivePointOut() {
		// TODO Auto-generated method stub
		return getBaseline(0,0)/2;
	}

	@Override
	public boolean receiveArrow() {
		// TODO Auto-generated method stub
		return false;
	}


}