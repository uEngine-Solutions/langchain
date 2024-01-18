package org.uengine.kernel;

import java.util.*;


/**
 * @author Jinyoung Jang
 */

public class TryCatchActivity extends ComplexActivity{
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	public TryCatchActivity(){
		super();
		setName("try~catch");
	}
	
	protected void executeActivity(ProcessInstance instance) throws Exception{		
		Vector childActivities = getChildActivities();
		Activity child = (Activity)childActivities.elementAt(0);
		 			
		queueActivity(child, instance);
	}
	
	protected void onEvent(String command, ProcessInstance instance, Object payload) throws Exception{
		//when child activity failed, invoke the fault handler
		//review: if the fault handler even fails, this routine may be recursive. In that case, this routine must report the exception to its parent.			
		if(command.equals(CHILD_FAULT)){
			//if the fault has been occurred in the 'try' clause
			if(instance.get(getTracingTag(), "_child_fault")==null){
				Vector childActivities = getChildActivities();
				Activity faultHandler = (Activity)childActivities.elementAt(1);
				queueActivity(faultHandler, instance);
				instance.set(getTracingTag(), "_child_fault", "child error");
			//if the fault has been occurred even in the 'catch' clause
			}else{
				fireFault(instance, (FaultContext)payload);
			}
		}else
		if(command.equals(CHILD_DONE)){
			fireComplete(instance);
		}else{
			super.fireComplete(instance);
		}
	}
}

