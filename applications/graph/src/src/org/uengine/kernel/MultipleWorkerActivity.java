package org.uengine.kernel;

import java.util.*;

import org.uengine.kernel.GlobalContext;


/**
 * @author Jinyoung Jang
 */

public class MultipleWorkerActivity extends ComplexActivity{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	public MultipleWorkerActivity(){
		super();
		setName("Multi-block");
	}
	
	protected void onEvent(String command, ProcessInstance instance, Object payload) throws Exception{
				
		//dispatching leaf childs
		if(command.equals(CHILD_DONE)){
			int currStep = getCurrentStep(instance);		
			currStep++;	
			setCurrentStep(instance, currStep);
System.out.println("--------------------------"+currStep);
			if(currStep >= getChildActivities().size()){
				//onEvent(Activity.ACTIVITY_DONE, instance, null);
				fireComplete(instance);
			}
		}else		
		super.onEvent(command, instance, this);
	}

	protected void executeActivity(ProcessInstance instance) throws Exception{		
		Vector childActivities = getChildActivities();	 			
		for(Enumeration enumeration = childActivities.elements(); enumeration.hasMoreElements(); ){
			Activity child = (Activity)enumeration.nextElement();
			
			queueActivity(child, instance);//child.executeActivity(instance);
		}
	}
	
	RoleMapping multipleWorker;
		public RoleMapping getMultipleWorker() {
			return multipleWorker;
		}
		public void setMultipleWorker(RoleMapping mapping) {
			multipleWorker = mapping;
		}

}

