package org.teamweaver.delias.commons;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.teamweaver.delias.commons.JavaElementSensingEvaluationUtility.JESExpression;
import org.teamweaver.delias.commons.JavaElementSensingEvaluationUtility.JESCondition;
//import org.teamweaver.context.data.DataStore;
//import org.teamweaver.context.data.eclipse.ArtefactManager;
//import org.teamweaver.context.event.ContextEvent;
//import org.teamweaver.context.sensing.AbstractSensingStrategy;
//import org.teamweaver.context.sensing.eclipse.JavaElementSensingEvaluationUtility.JESCondition;
//import org.teamweaver.context.sensing.eclipse.JavaElementSensingEvaluationUtility.JESExpression;

/**
 * Detects which IJavaElement(i.e. IImportDeclaration,
 * IMethod..) is changed by the user in the Eclipse java editor. <p>
 * A new event is only fired when the user changes the element.
 * <p>
 * Following Changes are observed:
 * <list>
 * <li>New Method</li>
 * <li>Method Visibility Change</li>
 * <li>Method Parameter Change</li>
 * <li>Method Return-Type Change</li>
 * <li>New Class</li>
 * <li>New Interface</li>
 * <li>New Package</li>
 * </list>
 * 
 * @author  Walid Maalej, Damir Ismailović
 * @version 1.1 12/08/2008
 */

public class JavaElementListener implements IElementChangedListener {
	
	public static final String CONDITIONS = "org.teamweaver.context.sensing.eclipse.JavaElementSensing.conditions";
	public static final String ANTI_CONDITIONS = "org.teamweaver.context.sensing.eclipse.JavaElementSensing.anti_conditions";
	
	public static final String CHANGED_ELEMENT = "concerns";
	private static final String CHANGE_TYPE = "Type";
	
	
	/**
	 * AntiConditions is a list of conditions, which are stopping the search for other conditions.
	 * <p> So for example typing empty space in a .java file, throws an {@link ElementChangedEvent},
	 * but we do not need to search through the whole list of {@link JavaElementListener#conditions}
	 * when we find an anti-condition ;)
	 */
	private List<JavaElementSensingEvaluationUtility> antiConditions; 
	
	/**
	 * This list of conditions describe this strategy.
	 * Each time something changes in the source-code, or other Eclipse-AST-Element,
	 * this class will look if this change matches one of the conditions.
	 * If yes, a new event will be thrown to the attached Sensor
	 */
	private List<JavaElementSensingEvaluationUtility> conditions;
//	private Map<String, JavaElementSensingEvaluationUtility> conditions;
	
	/**
	 * A standard constructor.
	 * <p>
	 * For now, the configuration is done in this constructor.
	 */
	/*
	 * Not designed to be a singlteon. The monitor / singleltong is the Utility 
	 */
	public JavaElementListener() {
		super();
		// construct conditions
		List<JavaElementSensingEvaluationUtility> tmpAntiConditions = new ArrayList<JavaElementSensingEvaluationUtility>();
		
		JavaElementSensingEvaluationUtility anti1 = new JavaElementSensingEvaluationUtility();
		anti1.addExpression(
				(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElementDelta.CHANGED,
				(JESExpression.VALIDATE | JESExpression.EQUAL),(IJavaElementDelta.F_CONTENT | IJavaElementDelta.F_FINE_GRAINED | IJavaElementDelta.F_AST_AFFECTED), 
				(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElement.COMPILATION_UNIT, 
				JESExpression.UNIMPORTANT, 0, JESExpression.UNIMPORTANT, true, JESExpression.UNIMPORTANT, null);
		JavaElementSensingEvaluationUtility anti2 = new JavaElementSensingEvaluationUtility();
		anti2.addExpression(
				(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElementDelta.CHANGED,
				(JESExpression.VALIDATE | JESExpression.OR),(IJavaElementDelta.F_AST_AFFECTED | IJavaElementDelta.F_PRIMARY_RESOURCE), 
				(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElement.COMPILATION_UNIT, 
				JESExpression.UNIMPORTANT, 0, JESExpression.UNIMPORTANT, true, JESExpression.UNIMPORTANT, null);

		tmpAntiConditions.add(anti1);
		tmpAntiConditions.add(anti2);
		
		// construct conditions
		List<JavaElementSensingEvaluationUtility> tmpConditions = new ArrayList<JavaElementSensingEvaluationUtility>();
				
		JavaElementSensingEvaluationUtility u1 = new JavaElementSensingEvaluationUtility();
		u1.addExpression(
				(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElementDelta.ADDED,
				JESExpression.UNIMPORTANT,0, 
				(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElement.METHOD, 
				JESExpression.UNIMPORTANT, 0, JESExpression.UNIMPORTANT, true, JESExpression.UNIMPORTANT, null, JESExpression.OR);
		JavaElementSensingEvaluationUtility u2 = new JavaElementSensingEvaluationUtility();
		u2.addExpression(
				(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElementDelta.CHANGED,
				(JESExpression.VALIDATE | JESExpression.AND),(IJavaElementDelta.F_CONTENT), 
				(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElement.METHOD, 
				JESExpression.UNIMPORTANT, 0, JESExpression.UNIMPORTANT, true, JESExpression.UNIMPORTANT, null);

		JavaElementSensingEvaluationUtility u3 = new JavaElementSensingEvaluationUtility();
		u3.addExpression(
				(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElementDelta.CHANGED,
				(JESExpression.VALIDATE | JESExpression.OR),(IJavaElementDelta.F_MODIFIERS), 
				(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElement.METHOD, 
				JESExpression.UNIMPORTANT, 0, JESExpression.UNIMPORTANT, true, JESExpression.UNIMPORTANT, null);
		
		JavaElementSensingEvaluationUtility u4 = new JavaElementSensingEvaluationUtility();
		u4.addExpression(
				u4.new JESCondition((JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElementDelta.ADDED,
						JESExpression.UNIMPORTANT, 0, 
						(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElement.METHOD, 
						JESExpression.UNIMPORTANT, 0,  JESExpression.UNIMPORTANT, true, 
						(JESExpression.COMPARE | JESExpression.UNEQUAL), null),
				u4.new JESCondition((JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElementDelta.REMOVED,
						JESExpression.UNIMPORTANT, 0, 
						(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElement.METHOD, 
						JESExpression.UNIMPORTANT, 0, JESExpression.UNIMPORTANT, true, 
						(JESExpression.COMPARE | JESExpression.UNEQUAL), null),
						JESExpression.INDIVIDUALLY);
		
		JavaElementSensingEvaluationUtility u5 = new JavaElementSensingEvaluationUtility();
		u5.addExpression(
				u5.new JESCondition((JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElementDelta.ADDED,
						JESExpression.UNIMPORTANT, 0, 
						(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElement.METHOD, 
						JESExpression.UNIMPORTANT, 0, 
						JESExpression.UNIMPORTANT, true, 
						(JESExpression.COMPARE | JESExpression.EQUAL), null),
				u5.new JESCondition((JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElementDelta.REMOVED,
						JESExpression.UNIMPORTANT, 0, 
						(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElement.METHOD, 
						JESExpression.UNIMPORTANT, 0, 
						JESExpression.UNIMPORTANT, true, 
						(JESExpression.COMPARE | JESExpression.EQUAL), null),
						JESExpression.INDIVIDUALLY);
		
		JavaElementSensingEvaluationUtility u11 = new JavaElementSensingEvaluationUtility();
		u11.addExpression(
				(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElementDelta.ADDED,
				JESExpression.UNIMPORTANT,0, 
				(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElement.TYPE, 
				(JESExpression.VALIDATE | JESExpression.UNEQUAL), JESCondition.ELEMENT_IS_INTERFACE, 
				JESExpression.UNIMPORTANT, true, JESExpression.UNIMPORTANT, null);
		JavaElementSensingEvaluationUtility u12 = new JavaElementSensingEvaluationUtility();
		u12.addExpression(
				(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElementDelta.ADDED,
				JESExpression.UNIMPORTANT,0, 
				(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElement.TYPE, 
				(JESExpression.VALIDATE | JESExpression.EQUAL), JESCondition.ELEMENT_IS_INTERFACE, 
				JESExpression.UNIMPORTANT, true, JESExpression.UNIMPORTANT, null);
		JavaElementSensingEvaluationUtility u13 = new JavaElementSensingEvaluationUtility();
		u13.addExpression(
				(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElementDelta.ADDED,
				JESExpression.UNIMPORTANT,0, 
				(JESExpression.VALIDATE | JESExpression.EQUAL), IJavaElement.PACKAGE_DECLARATION, 
				JESExpression.UNIMPORTANT, 0, JESExpression.UNIMPORTANT, true, JESExpression.UNIMPORTANT, null);
		
		u2. setName("ChangeReturnType")				;tmpConditions.add(u2 );
		u3. setName("ChangeVisibility")				;tmpConditions.add(u3 );
		u4. setName("ChangeMethod")					;tmpConditions.add(u4 );
		u5. setName("ChangeParameter")				;tmpConditions.add(u5 );
		u1. setName( "CreateMethod")					;tmpConditions.add(u1 );
		u11.setName("CreateClass")					;tmpConditions.add(u11);
		u12.setName("CreateInterface")				;tmpConditions.add(u12);
		u13.setName("CreatePackage")					;tmpConditions.add(u13);
		
		Map<String, Object> tmpConfig = new HashMap<String, Object>();
		tmpConfig.put(JavaElementListener.CONDITIONS, tmpConditions);
		tmpConfig.put(JavaElementListener.ANTI_CONDITIONS, tmpAntiConditions);
		
		this.config(tmpConfig);
	}

	
	/**
	 * @param antiConditions the antiConditions to set
	 */
	public void setAntiConditions(
			List<JavaElementSensingEvaluationUtility> antiConditions) {
		this.antiConditions = antiConditions;
	}

	/**
	 * @param conditions the conditions to set
	 */
	public void setConditions(
			List<JavaElementSensingEvaluationUtility> conditions) {
		this.conditions = conditions;
	}


	/*
	 * interface method 
	 */
	public void elementChanged(ElementChangedEvent event) {
//		Debugging
//		printoutDebugMessages(event);
		
		List<IJavaElementDelta> deltas = traverseAndFindChangedElements(event.getDelta());
		checkConditions(deltas);
	}
	
	/**
	 * help method: finds all deltas, and returns a list
	 * @param delta - the delta
	 * @return - the list of all deltas
	 */
	private List<IJavaElementDelta> traverseAndFindChangedElements(IJavaElementDelta delta)
	{
		List<IJavaElementDelta> ret = new ArrayList<IJavaElementDelta>();
		
        if ((delta.getFlags() & IJavaElementDelta.F_CHILDREN) != 0) {
            IJavaElementDelta[] children = delta.getAffectedChildren();
            for (int i = 0; i < children.length; i++) {
            	ret.addAll(traverseAndFindChangedElements(children[i]));
            }
        }
        else{
        	ret.add(delta);
        }

		return ret;
	}
	
	/**
	 * Checks all conditions
	 * @param changedElementDeltas
	 */
	private void checkConditions(List<IJavaElementDelta> changedElementDeltas){
		if(changedElementDeltas.size() == 0)
			return;
		for (JavaElementSensingEvaluationUtility ut : antiConditions) 
			if(ut.evaluate(changedElementDeltas)){
//				System.out.println("ANTI FOUND");
				return;
			}
		for (int i = 0; i < conditions.size(); i++) {
			JavaElementSensingEvaluationUtility ut = conditions.get(i);
			if(ut.evaluate(changedElementDeltas))
			{
//				System.out.println(ut.getName());
				sense(ut.getName(), changedElementDeltas);
				return;
			}
		}
		
	}

	@SuppressWarnings("unchecked")

	public void config(Map<String, Object> o) {
		this.setConditions((List<JavaElementSensingEvaluationUtility>) o.get(JavaElementListener.CONDITIONS));
		this.setAntiConditions((List<JavaElementSensingEvaluationUtility>) o.get(JavaElementListener.ANTI_CONDITIONS));
	}

	public void start() {
		JavaCore.addElementChangedListener(this);
	}

	public synchronized void stop() {
		JavaCore.removeElementChangedListener(this);
	}

	
	protected Set<String> getPropertyNames() {
		Set<String> retVal = new HashSet<String>();
		retVal.add(CHANGE_TYPE);
		retVal.add(CHANGE_TYPE);
		return retVal;
	}

	/**
	 * A common way for calling the {@link org.teamweaver.context.sensing.ISensor#dataSensed()} method
	 * @param key - specifies the Type of the thrown Event
	 * @param deltas - the deltas, which can be used to receive informations about the changes occured
	 */
	public void sense(String key, List<IJavaElementDelta> deltas) {
		
		IJavaElement changedElement = deltas.get(0).getElement();
		
		System.out.println(changedElement.getElementName());
		System.out.println((changedElement.getResource()).getFullPath().toOSString());
		if(key.equals("ChangeReturnType"))
			System.out.println(key.toString()+changedElement.toString());
		if(key.equals("ChangeVisibility"))
			System.out.println(key.toString());
		if(key.equals("ChangeMethod"))			
			System.out.println(key.toString()+changedElement.toString());
		if(key.equals("ChangeParameter"))	
			System.out.println(key.toString());
		if(key.equals("CreateMethod"))
			System.out.println(key.toString()+changedElement.toString());
		if(key.equals("CreateClass"))		
			System.out.println(key.toString());
		if(key.equals("CreateInterface"))
			System.out.println(key.toString());
		if(key.equals("CreatePackage"))
			System.out.println(key.toString());

	}
	
	
	

}
