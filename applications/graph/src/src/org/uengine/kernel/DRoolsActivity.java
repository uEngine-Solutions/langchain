/*
 * Created on 2004-04-12
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.uengine.kernel;

import org.metaworks.Type;
import org.uengine.processdesigner.ProcessDesigner;
import org.uengine.processmanager.ProcessManagerBean;
import org.uengine.processmanager.ProcessManagerRemote;
import org.uengine.util.ForLoop;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.base.ClassObjectType;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.rule.Declaration;
import org.drools.rule.Package;
import org.drools.rule.Rule;
import org.drools.spi.ObjectType;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DRoolsActivity extends DefaultActivity {
	private static final long serialVersionUID = org.uengine.kernel.GlobalContext.SERIALIZATION_UID;

	protected final static String SUBPROCESS_INST_ID = "instanceIdOfSubProcess";

	protected final static String SUBPROCESS_INST_ID_COMPLETED = "completedInstanceIdOfSPs";

	public DRoolsActivity() {
		super();
		setName("fire drules");
	}

	String definitionId;
		public String getDefinitionId() {
			return definitionId;
		}
		public void setDefinitionId(String l) {
			definitionId = l;
		}

	ParameterContext[] variableBindings;
		public ParameterContext[] getVariableBindings() {
			return variableBindings;
		}
		public void setVariableBindings(ParameterContext[] contexts) {
			variableBindings = contexts;
		}

	protected void executeActivity(ProcessInstance instance) throws Exception {
 
		// load up the rulebase
		RuleBase ruleBase = readRule(instance);
		final WorkingMemory workingMemory = ruleBase.newWorkingMemory();

		// asserts objects
		HashMap objects = new HashMap();

		ParameterContext[] params = getVariableBindings();
		for (int i = 0; i < params.length; i++) {
			ParameterContext param = params[i];

			String clsName = param.getArgument().getText();
			String[] clsAndMemberName = clsName.split(":");
			String memberName = null;
			if (clsAndMemberName.length > 1) {
				memberName = clsAndMemberName[1];
System.out.println("memberName "+ memberName);
				clsName = clsAndMemberName[0];
System.out.println("clsName " + clsName);				
			}

			// Class theClass = Class.forName(clsName);
			Class theClass = getClass().getClassLoader().loadClass(clsName);
			Object objInstance = null;

			Object theMappingValue = param.getVariable().get(instance, "");

			if (memberName == null) {
				objInstance = theMappingValue;
				objects.put(theClass, objInstance);
				continue;
			}

			if (objects.containsKey(theClass)) {
				objInstance = objects.get(theClass);
			} else {
				objInstance = theClass.newInstance();
			}
			
			objects.put(theClass, objInstance);		
			
			if (ParameterContext.DIRECTION_OUT.equals(param.getDirection())){
				continue;
			}else{
				//Method theSetter = theClass.getMethod("set" + memberName,new Class[] { theMappingValue.getClass() });
				Method theSetter = getMethod(theClass,"set" + memberName);
				theSetter.invoke(objInstance, new Object[] { theMappingValue });				
			}
		}

		ForLoop assertingLoop = new ForLoop() {

			public void logic(Object target) {
				workingMemory.assertObject(target);
			}

		};

		assertingLoop.run(objects);

		// go!
		workingMemory.fireAllRules();

		// get the results
		for (int i = 0; i < params.length; i++) {
			ParameterContext param = params[i];

			if (ParameterContext.DIRECTION_IN.equals(param.getDirection()))
				continue;

			String clsName = param.getArgument().getText();
			String[] clsAndMemberName = clsName.split(":");
			String memberName = null;
			if (clsAndMemberName.length > 1) {
				memberName = clsAndMemberName[1];
				clsName = clsAndMemberName[0];
			}

			// Class theClass = Class.forName(clsName);
			Class theClass = getClass().getClassLoader().loadClass(clsName);
			Object objInstance = null;

			if (objects.containsKey(theClass)) {
				objInstance = objects.get(theClass);
			} else {
				objInstance = theClass.newInstance();
			}

			if (memberName == null) {
				param.getVariable().set(instance, "",
						(Serializable) objInstance);
				continue;
			}

			//Method theGetter = theClass.getMethod("get" + memberName,new Class[] {});
			Method theGetter = getMethod(theClass,"get" + memberName);
			Object mappingValue = theGetter.invoke(objInstance, new Object[] {});
			param.getVariable().set(instance, "", (Serializable) mappingValue);
		}

		fireComplete(instance);
	}
	
    private static Method getMethod(Class src, String name) {
        Method meths[] = src.getMethods();
        for (int i = 0; i < meths.length; i++) {
            if (meths[i].getName().equals(name))
                return meths[i];
        }
        return null;
    }	

	public String getDefinitionVersionId(ProcessInstance instance)
			throws Exception {
		ProcessManagerRemote pm = new ProcessManagerBean();

		String versionId = null;
		String definitionId = null;

		String[] defIdAndVersionId = ProcessDefinition
				.splitDefinitionAndVersionId(getDefinitionId());
		definitionId = defIdAndVersionId[0];
		versionId = defIdAndVersionId[1];

		try {
			versionId = pm.getProcessDefinitionProductionVersion(definitionId);
		} catch (Exception e) {
			e.printStackTrace();

			try {
				versionId = pm.getFirstProductionVersionId(definitionId);
			} catch (Exception ex) {
				ex.printStackTrace();
				versionId = pm
						.getProcessDefinitionProductionVersion(definitionId);
			}
		}

		return versionId;
	}

	private RuleBase readRule(ProcessInstance instance) throws Exception {
		// read in the source
		String ruleDefId = getDefinitionVersionId(instance);
System.out.println(ruleDefId);

		Reader source = new InputStreamReader(ProcessDefinitionFactory
				.getInstance(instance.getProcessTransactionContext())
				.getResourceStream(ruleDefId));
System.out.println(source.toString());

		// optionally read in the DSL (if you are using it).
		// Reader dsl = new InputStreamReader(
		// DroolsTest.class.getResourceAsStream( "/mylang.dsl" ) );

		// Use package builder to build up a rule package.
		// An alternative lower level class called "DrlParser" can also be
		// used...
		PackageBuilderConfiguration pkgBuilderCfg = new PackageBuilderConfiguration();
		pkgBuilderCfg.setCompiler(PackageBuilderConfiguration.JANINO);
		PackageBuilder builder = new PackageBuilder(pkgBuilderCfg);
 
		// this wil parse and compile in one step
		// NOTE: There are 2 methods here, the one argument one is for normal
		// DRL.
		builder.addPackageFromDrl(source);

		// Use the following instead of above if you are using a DSL:
		// builder.addPackageFromDrl( source, dsl );

		// get the compiled package (which is serializable)
		Package pkg = builder.getPackage();

		// add the package to a rulebase (deploy the rule package).
		RuleBase ruleBase = RuleBaseFactory.newRuleBase();
		ruleBase.addPackage(pkg);
		return ruleBase;
	}
	
	public static List getParameterList(ProcessManagerRemote pm, String ruleDefId) throws Exception{
		//load up the rulebase
		String def = pm.getResource(ruleDefId);
		Reader source = new StringReader(def);
		PackageBuilderConfiguration pkgBuilderCfg = new PackageBuilderConfiguration();
		pkgBuilderCfg.setCompiler(PackageBuilderConfiguration.JANINO);
		PackageBuilder builder = new PackageBuilder(pkgBuilderCfg);
		builder.addPackageFromDrl( source );
		Package pkg = builder.getPackage();
		RuleBase ruleBase = RuleBaseFactory.newRuleBase();
		ruleBase.addPackage(pkg);
		
		HashMap classes = new HashMap();
//		Rule[] rules = pkg.getRules();
//		for(int i=0; i<rules.length; i++){
//			Rule theRule = rules[i];
//			
//			Declaration declarations[] = theRule.getDeclarations();
//			for(int j=0; j<declarations.length; j++){
//				Declaration theDeclaration = declarations[j];
//				
//				ObjectType objType = theDeclaration.getExtractor().getObjectType();
//				if(objType instanceof ClassObjectType){
//					Class theClass = ((ClassObjectType)objType).getClassType();
//					classes.put(theClass, theClass);
//				}
//				
//				objType = theDeclaration.getObjectType();
//				if(objType instanceof ClassObjectType){
//					Class theClass = ((ClassObjectType)objType).getClassType();
//					classes.put(theClass, theClass);
//				}
//			}
//		}
		List imports = pkg.getImports();
		int importsLegth = imports.size();
        
		for (int j = 0; j < importsLegth; j++) {
			String clsName = (String) imports.get(j);
			if (clsName != null && clsName.indexOf("*") == -1) {
				Class theClass = Class.forName(clsName);
				classes.put(theClass, theClass);
			}
		}
		
		ArrayList parameterList = new ArrayList();
		
		for(Iterator iter = classes.keySet().iterator(); iter.hasNext(); ){
			Class theClass = (Class)iter.next(); 

			Method methods[] = theClass.getMethods();
			String clsName = theClass.getName();

			for (int k = 0; k < methods.length; k++) {
				if (methods[k].getName().startsWith("set")) {
					parameterList.add(clsName+":"+methods[k].getName().substring(3));
				} 
			}
		}
		
		return parameterList;

	}

}
