package org.teamweaver.delias.commons;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.JavaModelException;

/**
 * {@link JavaElementSensingEvaluationUtility} represents a Utility which helps to evaluate the {@link IJavaElementDelta}s
 * <p>
 * <p> Here for example a bit complex evaluation of usage of this class:
 * <pre>
 * 		JavaElementSensingEvaluationUtility jeseu1 = new JavaElementSensingEvaluationUtility();
 *			jeseu1.addExpression(
 *			jeseu1.new JESCondition((JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElementDelta.ADDED,
 *					JESComparator.UNIMPORTANT, 0, 
 *					(JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElement.METHOD, 
 *					JESComparator.UNIMPORTANT, 0, 
 *					JESComparator.UNIMPORTANT, true, 
 *					(JESComparator.COMPARE | JESComparator.UNEQUAL), null),
 *			jeseu1.new JESCondition((JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElementDelta.REMOVED,
 *					JESComparator.UNIMPORTANT, 0, 
 *					(JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElement.METHOD, 
 *					JESComparator.UNIMPORTANT, 0, 
 *					JESComparator.UNIMPORTANT, true, 
 *					(JESComparator.COMPARE | JESComparator.UNEQUAL), null),
 *					JESComparator.INDIVIDUALLY);
 * </pre>
 * Using the definition above the deltas can be evaluated as follows:
 * <pre>
 * 		IJavaElementDelta aDelta = ... // Receive a delta
 * 		List&lt;IJavaElementDelta&gt; deltas = new ArrayList&lt;IJavaElementDelta&gt;();
 * 		deltas.add(aDelta);
 * 		jeseu1.evaluate(deltas);
 * </pre>
 * The last line will return TRUE if the delta aDelta is a change of a methods name.
 * 
 * @author  Damir Ismailović
 * @version 1.2 12/08/2008
 *
 */
public class JavaElementSensingEvaluationUtility {

	/**
	 * The list of expressions which should be checked with the evaluate methods
	 */
	private List<JESExpression> conditions;
	
	private String name;
			
	
	/**
	 * A standard constructor
	 */
	public JavaElementSensingEvaluationUtility(String name)
	{
		this.setName(name);
		this.conditions = new ArrayList<JESExpression>();
	}

	/**
	 * A standard constructor
	 */
	public JavaElementSensingEvaluationUtility()
	{
		this("");
	}
	
	/**
	 * Initializes the {@link JavaElementSensingEvaluationUtility} with a first expression
	 * @param firstExpression - the first expression
	 */
	public JavaElementSensingEvaluationUtility(JESExpression firstExpression)
	{
		this();
		addExpression(firstExpression);
	}

	/**
	 * Initializes the {@link JavaElementSensingEvaluationUtility} with a first expression
	 * @param firstExpression - the first expression
	 * @param c_flags - the flags used to describe the firstExpression
	 */
	public JavaElementSensingEvaluationUtility(JESExpression firstExpression, int c_flags)
	{
		this();
		firstExpression.setJes_flags(c_flags);
		addExpression(firstExpression);
	}

	/**
	 * Adds a new {@link JESExpression} with the Type {@link JESComparator}
	 * <p> The {@link JESComparator} can be used to make a binary tree of {@link JESExpression}s
	 * <p> Here for example a bit complex evaluation of each method-name changed:
	 * <pre>
	 * 		JavaElementSensingEvaluationUtility jeseu1 = new JavaElementSensingEvaluationUtility();
	 *			jeseu1.addExpression(
	 *			jeseu1.new JESCondition((JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElementDelta.ADDED,
	 *					JESComparator.UNIMPORTANT, 0, 
	 *					(JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElement.METHOD, 
	 *					JESComparator.UNIMPORTANT, 0, 
	 *					JESComparator.UNIMPORTANT, true, 
	 *					(JESComparator.COMPARE | JESComparator.UNEQUAL), null),
	 *			jeseu1.new JESCondition((JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElementDelta.REMOVED,
	 *					JESComparator.UNIMPORTANT, 0, 
	 *					(JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElement.METHOD, 
	 *					JESComparator.UNIMPORTANT, 0, 
	 *					JESComparator.UNIMPORTANT, true, 
	 *					(JESComparator.COMPARE | JESComparator.UNEQUAL), null),
	 *					JESComparator.INDIVIDUALLY);
	 *
	 * </pre>
	 * @param A - The {@link JESExpression} which should be compared with B
	 * @param B - The {@link JESExpression} which should be compared with A
	 * @param compare - The type of comparison, defined by the class variables in {@link JESComparator}
	 * <p>
	 * INDIVIDUALLY  or  (ALL | AND), (ALL | OR)
	 * <p> INDIVIDUALLY overwrites all others, so (INDIVIDUALLY | AND) is the same as INDIVIDUALLY alone
	 * <p> If the evaluation of the two Expression A and B should be done as follows: (A & B), use this code:
	 * <pre>
	 * myUtilitu.addExpression(anExpressionA, anExpressionB, JESComparator.ALL | JESComparator.AND)
	 * </pre>
	 * <p> If the evaluation of the two Expression A and B should be done as follows: (A | B), use this code:
	 * <pre>
	 * myUtilitu.addExpression(anExpressionA, anExpressionB, JESComparator.ALL | JESComparator.OR)
	 * </pre>
	 * <p> Otherwise, in each expression the comparation of the fields can be specified, so the following code can be used:
	 * <pre>
	 * myUtilitu.addExpression(anExpression, anOtherExpression, JESComparator.INDIVIDUALLY)
	 * </pre>
	 * 
	 * @see {@link JESComparator}, {@link JESCondition}
	 */
	public void addExpression(JESExpression A, JESExpression B, int compare)
	{
		JESComparator ctemp = new JESComparator(A,B,compare);
		this.addExpression(ctemp);
	}

	/**
	 * Adds a new {@link JESExpression} with the Type {@link JESComparator}
	 * <p>
	 * This method calls the method {@link JavaElementSensingEvaluationUtility#addExpression(JESExpression, JESExpression, int)} as follows:
	 * <pre>
	 * this.addExpression(A,B,JESComparator.INDIVIDUALLY);
	 * </pre>
	 * @param A - The {@link JESExpression} which should be compared with B
	 * @param B - The {@link JESExpression} which should be compared with A
	 * @see {@link JavaElementSensingEvaluationUtility#addExpression(JESExpression, JESExpression, int)}
	 */
	public void addExpression(JESExpression A, JESExpression B)
	{
		JESComparator ctemp = new JESComparator(A,B);
		this.addExpression(ctemp);
	}
	
	/**
	 * Adds a new {@link JESExpression} of the type {@link JESCondition}.
	 * <p>
	 * Each parameter has a set of flags defined by the parameters cck, ccf, cet, cef, cexists and cname.
	 * <p> This parameters can take the OR-combined values of the following flags:
	 * <list>
	 * <li>{@link JESExpression#ALL}
	 * <li>{@link JESExpression#INDIVIDUALLY}
	 * <li>{@link JESExpression#UNIMPORTANT}
	 * <li>{@link JESExpression#VALIDATE}
	 * <li>{@link JESExpression#COMPARE}
	 * <li>{@link JESExpression#AND}
	 * <li>{@link JESExpression#OR}
	 * <li>{@link JESExpression#EQUAL}
	 * <li>{@link JESExpression#UNEQUAL}
	 * </list>
	 * <p> For example the following combination of flags:
	 * <pre>(JESComparator.VALIDATE | JESComparator.EQUAL)</pre> means that the value
	 * in the parameter should be checked, to be equal to the received value.
	 * <p>The following example of the usage of this method shows how to add an {@link JESExpression}
	 * which should evaluate each new added method
	 * <pre>
	 *	JavaElementSensingEvaluationUtility jeseu1 = new JavaElementSensingEvaluationUtility();
	 *		jeseu1.addExpression((JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElementDelta.ADDED,
	 *			JESComparator.UNIMPORTANT,0, 
	 *			(JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElement.METHOD, 
	 *			JESComparator.UNIMPORTANT, 0, JESComparator.UNIMPORTANT, true, JESComparator.UNIMPORTANT, null);
	 * </pre>
	 * @param cck - a combination of flags defined by the ({@link JESComparator}).
	 * Typicaly used values are {@link JESExpression#VALIDATE}, {@link JESExpression#COMPARE}, {@link JESExpression#UNIMPORTANT}
	 * <p> in combination with {@link JESExpression#EQUAL}, {@link JESExpression#UNEQUAL}
	 * @param changeKind - typically used values are {@link IJavaElementDelta#CHANGED},
	 * {@link IJavaElementDelta#ADDED} and {@link IJavaElementDelta#REMOVED}
	 * @param ccf - a combination of flags defined by the ({@link JESComparator})
	 * @param changeFlags - typically used values are defined by the interface {@link IJavaElementDelta}
	 * @param cet - a combination of flags defined by the ({@link JESComparator})
	 * @param elementType - is the type defined in the interface {@link IJavaElement}, e.g. {@link IJavaElement#METHOD}
	 * @param cef - a combination of flags defined by the ({@link JESComparator})
	 * @param elementFlags - are just defined for the type {@link IMember} and can be received by with the method {@link IMember#getFlags()}
	 * @param cexists - a combination of flags defined by the ({@link JESComparator})
	 * @param exists - is a value that will be checked with the value received by the method {@link IJavaElement#exists()}
	 * @param cname - a combination of flags defined by the ({@link JESComparator})
	 * @param elementName - A value that will be checked with the name of the {@link IJavaElement}
	 * @see {@link JESComparator}, {@link JESCondition}
	 */
	public void addExpression(int cck, int changeKind, 
			int ccf, int changeFlags, 
			int cet, int elementType, 
			int cef, int elementFlags,
			int cexists, boolean exists, 
			int cname, String elementName)
	{
		this.addExpression(new JESCondition(cck,changeKind,ccf,changeFlags,cet,elementType,cef,elementFlags,cexists,exists,cname,elementName));
	}
	
	/**
	 * Adds a new {@link JESExpression} of the type {@link JESCondition}.
	 * <p>
	 * Each parameter has a set of flags defined by the parameters cck, ccf, cet, cef, cexists and cname.
	 * <p> This parameters can take the OR-combined values of the following flags:
	 * <list>
	 * <li>{@link JESExpression#ALL}
	 * <li>{@link JESExpression#INDIVIDUALLY}
	 * <li>{@link JESExpression#UNIMPORTANT}
	 * <li>{@link JESExpression#VALIDATE}
	 * <li>{@link JESExpression#COMPARE}
	 * <li>{@link JESExpression#AND}
	 * <li>{@link JESExpression#OR}
	 * <li>{@link JESExpression#EQUAL}
	 * <li>{@link JESExpression#UNEQUAL}
	 * </list>
	 * <p> For example the following combination of flags:
	 * <pre>(JESComparator.VALIDATE | JESComparator.EQUAL)</pre> means that the value
	 * in the parameter should be checked, to be equal to the received value.
	 * <p>The following example of the usage of this method shows how to add an {@link JESExpression}
	 * which should evaluate each new added method
	 * <pre>
	 *	JavaElementSensingEvaluationUtility jeseu1 = new JavaElementSensingEvaluationUtility();
	 *		jeseu1.addExpression((JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElementDelta.ADDED,
	 *			JESComparator.UNIMPORTANT,0, 
	 *			(JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElement.METHOD, 
	 *			JESComparator.UNIMPORTANT, 0, JESComparator.UNIMPORTANT, true, JESComparator.UNIMPORTANT, null);
	 * </pre>
	 * @param cck - a combination of flags defined by the ({@link JESComparator}).
	 * Typicaly used values are {@link JESExpression#VALIDATE}, {@link JESExpression#COMPARE}, {@link JESExpression#UNIMPORTANT}
	 * <p> in combination with {@link JESExpression#EQUAL}, {@link JESExpression#UNEQUAL}
	 * @param changeKind - typically used values are {@link IJavaElementDelta#CHANGED},
	 * {@link IJavaElementDelta#ADDED} and {@link IJavaElementDelta#REMOVED}
	 * @param ccf - a combination of flags defined by the ({@link JESComparator})
	 * @param changeFlags - typically used values are defined by the interface {@link IJavaElementDelta}
	 * @param cet - a combination of flags defined by the ({@link JESComparator})
	 * @param elementType - is the type defined in the interface {@link IJavaElement}, e.g. {@link IJavaElement#METHOD}
	 * @param cef - a combination of flags defined by the ({@link JESComparator})
	 * @param elementFlags - are just defined for the type {@link IMember} and can be received by with the method {@link IMember#getFlags()}
	 * @param cexists - a combination of flags defined by the ({@link JESComparator})
	 * @param exists - is a value that will be checked with the value received by the method {@link IJavaElement#exists()}
	 * @param cname - a combination of flags defined by the ({@link JESComparator})
	 * @param elementName - A value that will be checked with the name of the {@link IJavaElement}
	 * @see {@link JESComparator}, {@link JESCondition}
	 */
	public void addExpression(int cck, int changeKind, 
			int ccf, int changeFlags, 
			int cet, int elementType, 
			int cef, int elementFlags,
			int cexists, boolean exists, 
			int cname, String elementName, int c_flags)
	{
		this.addExpression(new JESCondition(cck,changeKind,ccf,changeFlags,cet,elementType,cef,elementFlags,cexists,exists,cname,elementName), c_flags);
	}


	/**
	 * Adds an expression 
	 * @param c - the expression to add
	 * @see {@link JESExpression}
	 * @see {@link JavaElementSensingEvaluationUtility#addExpression(JESExpression, JESExpression, int)}
	 * @see {@link JavaElementSensingEvaluationUtility#addExpression(int, int, int, int, int, int, int, int, int, boolean, int, String)}
	 */
	public void addExpression(JESExpression  c)
	{
		this.addExpression(c, 0);
	}
	
	/**
	 * Adds an expression 
	 * @param c - the expression to add
	 * @param c_flags - the flags used to describe the expression c
	 * @see {@link JESExpression}
	 * @see {@link JavaElementSensingEvaluationUtility#addExpression(JESExpression, JESExpression, int)}
	 * @see {@link JavaElementSensingEvaluationUtility#addExpression(int, int, int, int, int, int, int, int, int, boolean, int, String)}
	 */
	public void addExpression(JESExpression  c, int c_flags)
	{
		c.setJes_flags(c_flags);
		conditions.add(c);
	}

	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof JavaElementSensingEvaluationUtility))
			return false;
		return super.equals(obj);
		
	}
	
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Evaluates the list of deltas against the expressions in {@link JavaElementSensingEvaluationUtility#conditions}
	 * @param changeElementDeltas - the list of deltas to evaluate
	 * @return returns TRUE if an expression in {@link JavaElementSensingEvaluationUtility#conditions} matches the deltas
	 * otherwise it returns FALSE
	 */
	public boolean evaluate(List<IJavaElementDelta> changedElementDeltas) {
		boolean ret = false;
		int deltaSize = changedElementDeltas.size();

		for (JESExpression expression : conditions) {
			if(deltaSize == 1){
				if(!expression.isNode())
					return compare((JESCondition)expression, changedElementDeltas.get(0));
			}
			else if(deltaSize >= 2){
				if(expression.isNode())
					return evaluate((JESComparator)expression ,changedElementDeltas);
				else
					if((expression.getJes_flags() & JESExpression.OR) !=0 )
						for (IJavaElementDelta dtmp : changedElementDeltas)
							if(compare((JESCondition)expression, dtmp))
								return true;
			}
		}
		return ret;
	}

	/**
	 * A private method that evaluates a tree of conditions with a list of deltas
	 * @param condition - the condition tree
	 * @param deltas - list of deltas
	 * @return TRUE if the conditions match the deltas, otherwise FALSE
	 */
	private boolean evaluate(JESComparator condition ,List<IJavaElementDelta> deltas)
	{
		if((condition.getCompare() & JESExpression.INDIVIDUALLY) !=0)
		{
			JESExpression A = condition.getA();
			JESExpression B = condition.getB();

			if(!A.isNode() && !B.isNode() && deltas.size() == 2)
			{
				boolean a1 = compare((JESCondition)A, deltas.get(0));
				boolean b1, tmpab1 = false, tmpab2=false;
				if(a1) {
					b1 = compare((JESCondition)B, deltas.get(1));
					if(!b1) return false;
					tmpab1 = a1 && b1;
				}
				else{
					a1 = compare((JESCondition)A, deltas.get(1));
					if(!a1) return false;
					b1 = compare((JESCondition)B, deltas.get(0));
					if(!b1) return false;
					tmpab2 = a1 && b1;
				}
				
				return (tmpab1)? compare((JESCondition)A, (JESCondition)B, deltas.get(0), deltas.get(1))
					: (tmpab2)? compare((JESCondition)A, (JESCondition)B, deltas.get(1), deltas.get(0))
					: false;
			}
		}
		else if((condition.getCompare() & JESExpression.ALL) !=0){
			JESExpression A = condition.getA();
			JESExpression B = condition.getB();
			boolean b_A = (A.isNode())? 
					evaluate((JESComparator)A, deltas): 
						(compare((JESCondition)A, deltas.get(0)) 
								|| compare((JESCondition)A, deltas.get(1)));
			boolean b_B = (B.isNode())? 
					evaluate((JESComparator)B, deltas): 
						(compare((JESCondition)B, deltas.get(0)) 
								|| compare((JESCondition)B, deltas.get(1)));
			
			if((condition.getCompare() & JESExpression.AND) !=0)
				return b_A && b_B;
			else if((condition.getCompare() & JESExpression.OR) !=0)
				return b_A || b_B;
		}

		return false;
	}
	
	/**
	 * Compares two {@link IJavaElementDelta}s dA and dB with the specified {@link JESCondition}s cA and cB
	 * @param cA - a {@link JESCondition}
	 * @param cB - a {@link JESCondition}
	 * @param dA - a {@link IJavaElementDelta}
	 * @param dB - a {@link IJavaElementDelta}
	 * @return TRUE if dA and dB matches the conditions cA and cB
	 */
	private boolean compare(JESCondition cA, JESCondition cB, IJavaElementDelta dA, IJavaElementDelta dB)
	{
		int a1 = 0, b1 = 0;
		a1 = cA.getC_changeKind();
		b1 = cB.getC_changeKind();
		if((a1 & b1 & JESExpression.COMPARE) !=0 )
			if(((a1 & b1 & (JESExpression.EQUAL)) !=0 && dA.getKind() != dB.getKind())
					|| ((a1 & b1 & (JESExpression.UNEQUAL)) !=0 && dA.getKind() == dB.getKind()))
				return false;
		a1 = cA.getC_changeFlags();
		b1 = cB.getC_changeFlags();
		if((a1 & b1 & JESExpression.COMPARE) !=0)
			if((((a1 & b1 & (JESExpression.EQUAL)) !=0) && (dA.getFlags() != dB.getFlags()))
					|| (((a1 & b1 & (JESExpression.UNEQUAL)) !=0) && (dA.getFlags() != dB.getFlags()))
					|| (((a1 & b1 & (JESExpression.OR)) !=0) && ((dA.getFlags() & dB.getFlags()) == 0))
					|| (((a1 & b1 & (JESExpression.AND)) !=0) && (((dA.getFlags() & dB.getFlags()) != dA.getFlags()) 
							||  ((dA.getFlags() & dB.getFlags()) != dB.getFlags()))))
				return false;
		a1 = cA.getC_element_type();
		b1 = cB.getC_element_type();
		if((a1 & b1 & JESExpression.COMPARE) !=0)
			if(((a1 & b1 & (JESExpression.EQUAL)) !=0 && (dA.getElement().getElementType() != dB.getElement().getElementType()))
					|| ((a1 & b1 & (JESExpression.UNEQUAL)) !=0 && (dA.getElement().getElementType() == dB.getElement().getElementType())))
				return false;
		a1 = cA.getC_element_exists();
		b1 = cB.getC_element_exists();
		if((a1 & b1 & JESExpression.COMPARE) !=0)
			if(((a1 & b1 & (JESExpression.EQUAL))!=0 && (dA.getElement().exists() != dB.getElement().exists()))
					|| ((a1 & b1 & (JESExpression.UNEQUAL)) !=0 && (dA.getElement().exists() == dB.getElement().exists())))
				return false;
		a1 = cA.getC_element_flags();
		b1 = cB.getC_element_flags();
		if((a1 & b1 & JESExpression.COMPARE)!=0){
			try {
				if((((a1 & b1 & (JESExpression.EQUAL)) !=0) && (((IMember)dA.getElement()).getFlags() != ((IMember)dB.getElement()).getFlags()))
						|| (((a1 & b1 & (JESExpression.UNEQUAL)) !=0) && (((IMember)dA.getElement()).getFlags() != ((IMember)dB.getElement()).getFlags()))
						|| (((a1 & b1 & (JESExpression.OR)) !=0) && ((((IMember)dA.getElement()).getFlags() & ((IMember)dB.getElement()).getFlags()) == 0))
						|| (((a1 & b1 & (JESExpression.AND)) !=0) && (((((IMember)dA.getElement()).getFlags() & ((IMember)dB.getElement()).getFlags()) != ((IMember)dA.getElement()).getFlags()) 
								||  ((((IMember)dA.getElement()).getFlags() & ((IMember)dB.getElement()).getFlags()) != ((IMember)dB.getElement()).getFlags()))))
					return false;
			} catch (JavaModelException e) {
				return false;
			}
		}
		a1 = cA.isC_element_name();
		b1 = cB.isC_element_name();
		if((a1 & b1 & JESExpression.COMPARE) !=0)
			if(((a1 & b1 & (JESExpression.EQUAL)) !=0 && !(dA.getElement().getElementName().equals(dB.getElement().getElementName())))
					|| ((a1 & b1 & (JESExpression.UNEQUAL)) !=0 && (dA.getElement().getElementName().equals(dB.getElement().getElementName()))))
				return false;


		return true;
	}
	
	/**
	 * Compares one condition with one delta
	 * @param condition - the {@link JESCondition}
	 * @param delta - the {@link IJavaElementDelta} to evaluate
	 * @return TRUE if the condition matches the delta 
	 */
	private boolean compare(JESCondition condition, IJavaElementDelta delta)
	{
		int a1 = 0;
		a1 = condition.getC_changeKind();
		if((a1 & JESExpression.VALIDATE) !=0)
			if(((a1 & (JESExpression.EQUAL)) !=0 && delta.getKind() != condition.getChangeKind())
					|| ((a1 & (JESExpression.UNEQUAL)) !=0 && delta.getKind() == condition.getChangeKind()))
				return false;
		a1 = condition.getC_changeFlags();
		if((a1 & JESExpression.VALIDATE) !=0)
			if((((a1 & (JESExpression.AND)) !=0) && ((delta.getFlags() & condition.getChangeFlags()) != condition.getChangeFlags()))
					|| (((a1 & (JESExpression.EQUAL)) !=0) && (delta.getFlags() != condition.getChangeFlags()))
					|| (((a1 & (JESExpression.OR)) !=0) && ((delta.getFlags() & condition.getChangeFlags()) == 0))
					|| (((a1 & (JESExpression.UNEQUAL)) !=0) && (delta.getFlags() == condition.getChangeFlags())))
				return false;
		a1 = condition.getC_element_type();
		if((a1 & JESExpression.VALIDATE) !=0)
			if(((a1 & (JESExpression.EQUAL)) !=0 && (delta.getElement().getElementType() != condition.getElement_type()))
					|| ((a1 & (JESExpression.UNEQUAL)) !=0 && (delta.getElement().getElementType() == condition.getElement_type())))
				return false;
		a1 = condition.getC_element_exists();
		if((a1 & JESExpression.VALIDATE) !=0)
			if(((a1 & (JESExpression.EQUAL)) !=0 && (delta.getElement().exists() != condition.isElement_exists()))
					|| ((a1 & (JESExpression.UNEQUAL)) !=0 && (delta.getElement().exists() == condition.isElement_exists())))
				return false;
		a1 = condition.getC_element_flags();
		if(((a1 & JESExpression.VALIDATE)!=0) && delta.getElement() instanceof IMember){
			try {
				int i1 = condition.getElement_flags();
				int mFlags = (((IMember)delta.getElement()).getFlags());
				//konstruiere i2 aus i1 und der getFlags() methode
				int i2 = 0;
				
				if((i1 & JESCondition.ELEMENT_IS_INTERFACE) != 0  && Flags.isInterface(mFlags))
					i2 = i2 | JESCondition.ELEMENT_IS_INTERFACE;
				if((i1 & JESCondition.ELEMENT_IS_PRIVATE) != 0  && Flags.isPrivate(mFlags))
					i2 = i2 | JESCondition.ELEMENT_IS_PRIVATE;
				if((i1 & JESCondition.ELEMENT_IS_PUBLIC) != 0  && Flags.isPublic(mFlags))
					i2 = i2 | JESCondition.ELEMENT_IS_PUBLIC;
				if((i1 & JESCondition.ELEMENT_IS_PROTECTED) != 0  && Flags.isProtected(mFlags))
					i2 = i2 | JESCondition.ELEMENT_IS_PROTECTED;
				if(	       ((((a1 & JESExpression.OR) !=0) 		&& ((i2 & i1) == 0)))
						|| (((a1 & JESExpression.EQUAL) !=0) 	&& (i2 != i1))
						|| (((a1 & JESExpression.AND) !=0) 		&& ((i2 & i1) != i1))
						|| (((a1 & JESExpression.UNEQUAL) !=0) 	&& (i2 == i1))
						)
					return false;
			} catch (JavaModelException e) {
				return false;
			}
		}
		a1 = condition.isC_element_name();
		if((a1 & JESExpression.VALIDATE) !=0)
			if(((a1 & (JESExpression.EQUAL)) !=0 && !(delta.getElement().getElementName().equals(condition.getElement_name())))
					|| ((a1 & (JESExpression.UNEQUAL)) !=0 && (delta.getElement().getElementName().equals(condition.getElement_name()))))
				return false;


		return true;
	}
	
	/**
	 * finds all deltas, and returns a list
	 * @param delta - the delta
	 * @return - the list of all deltas
	 */
	public static List<IJavaElementDelta> traverseAndFindChangedElements(IJavaElementDelta delta)
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
	 * This abstract class is used as a type for the composite pattern.
	 * Known subclasses: {@link JESComparator} and {@link JESCondition}
	 * @author  Damir Ismailović
	 */
	public abstract class JESExpression
	{
		/**
		 * This flag is used to mark one of the Fields of an {@link JESCondition}
		 * <p>
		 * UNIMPORTANT means that this Field will not be compared or validated
		 * <p> VALIDATE means that this field will be validated against a given value
		 * <p> COMPARE means that this field will be compared against one other {@link JESCondition} Field
		 */
		public static final int UNIMPORTANT 	= 0x000001;
		/**
		 * This flag is used to mark one of the Fields of an {@link JESCondition}
		 * <p>
		 * UNIMPORTANT means that this Field will not be compared or validated
		 * <p> VALIDATE means that this field will be validated against a given value
		 * <p> COMPARE means that this field will be compared against one other {@link JESCondition} Field
		 */
		public static final int COMPARE 		= 0x000002;
		/**
		 * This flag is used to mark one of the Fields of an {@link JESCondition}
		 * <p>
		 * UNIMPORTANT means that this Field will not be compared or validated
		 * <p> VALIDATE means that this field will be validated against a given value
		 * <p> COMPARE means that this field will be compared against one other {@link JESCondition} Field
		 */
		public static final int VALIDATE 		= 0x000008;
		/**
		 * Classical NOT EQUAL
		 */
		public static final int UNEQUAL 		= 0x000010;
		/**
		 * Classical  EQUAL
		 */
		public static final int EQUAL 			= 0x000020;
		/**
		 * Classical  AND
		 */
		public static final int AND				= 0x000100;
		/**
		 * Classical  OR
		 */
		public static final int OR				= 0x000200;
		/**
		 * This flag is used to to mark the JavaElementSensingComparator type,
		 * which should be either ALL or INDIVITUALLY.
		 * <p>
		 * ALL should be used together with AND or OR.
		 */
		public static final int ALL				= 0x000800;
		/**
		 * This flag is used to to mark the JavaElementSensingComparator type,
		 * which should be either ALL or INDIVITUALLY.
		 * <p> INDIVIDUALLY overwrites all others, so (INDIVIDUALLY | AND | ALL) is the same as INDIVIDUALLY alone
		 */
		public static final int INDIVIDUALLY	= 0x001000;
		
		/**
		 * Flags used to describe this Expression
		 */
		private int			jes_flags;

		/**
		 * This method is used to differ between the Node-Types and the non-node-types
		 * @return
		 */
		public abstract boolean isNode();

		/**
		 * @param jes_flags the jes_flags to set
		 */
		public void setJes_flags(int jes_flags) {
			this.jes_flags = jes_flags;
		}

		/**
		 * @return the jes_flags
		 */
		public int getJes_flags() {
			return jes_flags;
		}
	}
	
	/**
	 * {@link JESComparator} is a {@link JESExpression} and represent a leaf of the composite pattern.
	 * <p>
	 * <p>
	 * With the Constructor {@link JESComparator#JESComparator(JESExpression, JESExpression, int)} a new {@link JESComparator} can be created as follows:
	 * <p> The {@link JESComparator} can be used to make a binary tree of {@link JESExpression}s
	 * <p> Here for example a bit complex {@link JESComparator} which evaluates the change of the method-name:
	 * <pre>
	 * 		JESComparator comp1 = new JESComparator(
	 * 			comp1.new JESCondition((JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElementDelta.ADDED,
	 *					JESComparator.UNIMPORTANT, 0, 
	 *					(JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElement.METHOD, 
	 *					JESComparator.UNIMPORTANT, 0, 
	 *					JESComparator.UNIMPORTANT, true, 
	 *					(JESComparator.COMPARE | JESComparator.UNEQUAL), null),
	 *			comp1.new JESCondition((JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElementDelta.REMOVED,
	 *					JESComparator.UNIMPORTANT, 0, 
	 *					(JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElement.METHOD, 
	 *					JESComparator.UNIMPORTANT, 0, 
	 *					JESComparator.UNIMPORTANT, true, 
	 *					(JESComparator.COMPARE | JESComparator.UNEQUAL), null),
	 *					JESComparator.INDIVIDUALLY);
	 *
	 * 
	 * @see {@link JESCondition}, {@link JESComparator#JESComparator(JESExpression, JESExpression, int)}
	 * @author  Damir Ismailović
	 */
	public class JESComparator extends JESExpression
	{

		private JESExpression A;
		private JESExpression B;
		
		/**
		 * Marks the Type of this object.
		 * <p> Either ALL, or INDIVIDUALLY
		 */
		private int compare;
		 		
		/**
		 * Creates a new {@link JESComparator}
		 * <p> The {@link JESComparator} can be used to make a binary tree of {@link JESExpression}s
		 * <p> Here for example a bit complex {@link JESComparator} which evaluates the change of the method-name:
		 * <pre>
		 * 		JESComparator comp1 = new JESComparator(
		 * 			comp1.new JESCondition((JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElementDelta.ADDED,
		 *					JESComparator.UNIMPORTANT, 0, 
		 *					(JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElement.METHOD, 
		 *					JESComparator.UNIMPORTANT, 0, 
		 *					JESComparator.UNIMPORTANT, true, 
		 *					(JESComparator.COMPARE | JESComparator.UNEQUAL), null),
		 *			comp1.new JESCondition((JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElementDelta.REMOVED,
		 *					JESComparator.UNIMPORTANT, 0, 
		 *					(JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElement.METHOD, 
		 *					JESComparator.UNIMPORTANT, 0, 
		 *					JESComparator.UNIMPORTANT, true, 
		 *					(JESComparator.COMPARE | JESComparator.UNEQUAL), null),
		 *					JESComparator.INDIVIDUALLY);
		 *
		 * </pre>
		 * @param A - The {@link JESExpression} which should be compared with B
		 * @param B - The {@link JESExpression} which should be compared with A
		 * @param compare - The type of comparison, defined by the class variables in {@link JESComparator}
		 * <p>
		 * INDIVIDUALLY  or  (ALL | AND), (ALL | OR)
		 * <p> INDIVIDUALLY overwrites all others, so (INDIVIDUALLY | AND) is the same as INDIVIDUALLY alone
		 * <p> If the evaluation of the two Expression A and B should be done as follows: (A & B), use this code:
		 * <pre>
		 * myUtilitu.addExpression(anExpressionA, anExpressionB, JESComparator.ALL | JESComparator.AND)
		 * </pre>
		 * <p> If the evaluation of the two Expression A and B should be done as follows: (A | B), use this code:
		 * <pre>
		 * myUtilitu.addExpression(anExpressionA, anExpressionB, JESComparator.ALL | JESComparator.OR)
		 * </pre>
		 * <p> Otherwise, in each expression the comparation of the fields can be specified, so the following code can be used:
		 * <pre>
		 * myUtilitu.addExpression(anExpression, anOtherExpression, JESComparator.INDIVIDUALLY)
		 * </pre>
		 * 
		 * @see {@link JESComparator}, {@link JESCondition}
		 */
		public JESComparator(JESExpression A, JESExpression B, int compare)
		{
			this.A = A;
			this.B = B;
			this.setCompare(compare);
		}

		
		public JESComparator(JESExpression A, JESExpression B)
		{
			this(A,B,JESExpression.INDIVIDUALLY);
		}
		
		/**
		 * @return the a
		 */
		public JESExpression getA() {
			return A;
		}

		/**
		 * @param a the a to set
		 */
		public void setA(JESExpression a) {
			A = a;
		}

		/**
		 * @return the b
		 */
		public JESExpression getB() {
			return B;
		}

		/**
		 * @param b the b to set
		 */
		public void setB(JESExpression b) {
			B = b;
		}

		
		/**
		 * @param compare the compare to set
		 */
		public void setCompare(int compare) {
			this.compare = compare;
		}


		/**
		 * @return the compare
		 */
		public int getCompare() {
			return compare;
		}


		@Override
		public boolean isNode() {
			return true;
		}
		
		
		
	}
	
	/**
	 * This class represents a Leaf-Node of the composite pattern.
	 * <p>
	 * The constructor  {@link JESCondition#JESCondition(int, int, int, int, int, int, int, int, int, boolean, int, String)} can be used to
	 * create a new condition
	 * <p>The following example of the usage of this constructor shows how to create a {@link JESCondition}
	 * which should evaluate each new added method:
	 * <pre>
	 *	JESCondition c = new JESCondition((JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElementDelta.ADDED,
	 *			JESComparator.UNIMPORTANT,0, 
	 *			(JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElement.METHOD, 
	 *			JESComparator.UNIMPORTANT, 0, JESComparator.UNIMPORTANT, true, JESComparator.UNIMPORTANT, null);
	 * </pre>
	 * 
	 * @author  Damir Ismailović
	 */
	public class JESCondition extends JESExpression
	{
		/**
		 * A combination of flags defined by the ({@link JESComparator}) 
		 */
		private int			c_changeKind;
		/**
		 * typically used values are {@link IJavaElementDelta#CHANGED},
		 * {@link IJavaElementDelta#ADDED} and {@link IJavaElementDelta#REMOVED}
		 */
		private int 		changeKind;
		/**
		 * A combination of flags defined by the ({@link JESComparator}) 
		 */
		private int 		c_changeFlags;
		/**
		 * typically used values are defined by the interface {@link IJavaElementDelta}
		 */
		private int			changeFlags;
		
		/**
		 * A combination of flags defined by the ({@link JESComparator}) 
		 */
		private int 		c_element_type;
		/**
		 * is the type defined in the interface {@link IJavaElement}, e.g. {@link IJavaElement#METHOD}
		 */
		private int 		element_type;
		/**
		 * A combination of flags defined by the ({@link JESComparator}) 
		 */
		private int 		c_element_flags;
		/**
		 * are just defined for the type {@link IMember} and can be received by with the method {@link IMember#getFlags()}
		 */
		private int 		element_flags;
		/**
		 * A combination of flags defined by the ({@link JESComparator}) 
		 */
		private int 		c_element_exists;
		/**
		 * is a value that will be checked with the value received by the method {@link IJavaElement#exists()}
		 */
		private boolean 	element_exists;
		/**
		 * A combination of flags defined by the ({@link JESComparator}) 
		 */
		private int			c_element_name;
		/**
		 * A value that will be checked with the name of the {@link IJavaElement}
		 */
		private String		element_name;
		
		//Element Flags
		/**
		 * A flag used for the {@link JESCondition#element_flags} flags
		 * <p>
		 * Indicates that the changed element should be interface.
		 * <p>
		 * In combination with the {@link JESCondition#element_type} flags, and the {@link IJavaElement#TYPE}
		 * this flag can serve to check if the given change is in a class or an interface.
		 */
		public static final int ELEMENT_IS_INTERFACE				= 0x000001;
		/**
		 * A flag used for the {@link JESCondition#element_flags} flags
		 * <p>
		 * Indicates that the changed element should be public.
		 * 
		 */
		public static final int ELEMENT_IS_PUBLIC					= 0x000002;
		/**
		 * A flag used for the {@link JESCondition#element_flags} flags
		 * <p>
		 * Indicates that the changed element should be private.
		 * 
		 */
		public static final int ELEMENT_IS_PRIVATE					= 0x000008;
		/**
		 * A flag used for the {@link JESCondition#element_flags} flags
		 * <p>
		 * Indicates that the changed element should be protected.
		 * 
		 */
		public static final int ELEMENT_IS_PROTECTED				= 0x000010;

		
		/**
		 * Creates a new {@link JESCondition}.
		 * <p>
		 * Each parameter has a set of flags defined by the parameters cck, ccf, cet, cef, cexists and cname.
		 * <p> This parameters can take the OR-combined values of the following flags:
		 * <list>
		 * <li>{@link JESExpression#ALL}
		 * <li>{@link JESExpression#INDIVIDUALLY}
		 * <li>{@link JESExpression#UNIMPORTANT}
		 * <li>{@link JESExpression#VALIDATE}
		 * <li>{@link JESExpression#COMPARE}
		 * <li>{@link JESExpression#AND}
		 * <li>{@link JESExpression#OR}
		 * <li>{@link JESExpression#EQUAL}
		 * <li>{@link JESExpression#UNEQUAL}
		 * </list>
		 * <p> For example the following combination of flags:
		 * <pre>(JESComparator.VALIDATE | JESComparator.EQUAL)</pre> means that the value
		 * in the parameter should be checked, to be equal to the received value.
		 * <p>The following example of the usage of this constructor shows how to create a {@link JESCondition}
		 * which should evaluate each new added method:
		 * <pre>
		 *	JESCondition c = new JESCondition((JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElementDelta.ADDED,
		 *			JESComparator.UNIMPORTANT,0, 
		 *			(JESComparator.VALIDATE | JESComparator.EQUAL), IJavaElement.METHOD, 
		 *			JESComparator.UNIMPORTANT, 0, JESComparator.UNIMPORTANT, true, JESComparator.UNIMPORTANT, null);
		 * </pre>
		 * @param cck - a combination of flags defined by the ({@link JESComparator}).
		 * Typicaly used values are {@link JESExpression#VALIDATE}, {@link JESExpression#COMPARE}, {@link JESExpression#UNIMPORTANT}
		 * <p> in combination with {@link JESExpression#EQUAL}, {@link JESExpression#UNEQUAL}
		 * @param changeKind - typically used values are {@link IJavaElementDelta#CHANGED},
		 * {@link IJavaElementDelta#ADDED} and {@link IJavaElementDelta#REMOVED}
		 * @param ccf - a combination of flags defined by the ({@link JESComparator})
		 * @param changeFlags - typically used values are defined by the interface {@link IJavaElementDelta}
		 * @param cet - a combination of flags defined by the ({@link JESComparator})
		 * @param elementType - is the type defined in the interface {@link IJavaElement}, e.g. {@link IJavaElement#METHOD}
		 * @param cef - a combination of flags defined by the ({@link JESComparator})
		 * @param elementFlags - are just defined for the type {@link IMember} and can be received by with the method {@link IMember#getFlags()}
		 * @param cexists - a combination of flags defined by the ({@link JESComparator})
		 * @param exists - is a value that will be checked with the value received by the method {@link IJavaElement#exists()}
		 * @param cname - a combination of flags defined by the ({@link JESComparator})
		 * @param elementName - A value that will be checked with the name of the {@link IJavaElement}
		 * @see {@link JESComparator}
		 */
		public JESCondition(int cck, int changeKind, 
				int ccf, int changeFlags, 
				int cet, int elementType, 
				int cef, int elementFlags,
				int cexists, boolean exists, 
				int cname, String elementName)
		{
			setChangeKind(changeKind);
			setChangeFlags(changeFlags);
			setElement_type(elementType);
			setElement_flags(elementFlags);
			setElement_exists(exists);
			setElement_name(elementName);
			
			setC_changeKind(cck);
			setC_changeFlags(ccf);
			setC_element_type(cet);
			setC_element_flags(cef);
			setC_element_exists(cexists);
			setC_element_name(cname);

		}

		/**
		 * @return the c_changeKind
		 */
		public int getC_changeKind() {
			return c_changeKind;
		}

		/**
		 * @param kind the c_changeKind to set
		 */
		public void setC_changeKind(int kind) {
			c_changeKind = kind;
		}

		/**
		 * @return the changeKind
		 */
		public int getChangeKind() {
			return changeKind;
		}

		/**
		 * @param changeKind the changeKind to set
		 */
		public void setChangeKind(int changeKind) {
			this.changeKind = changeKind;
		}

		/**
		 * @return the c_changeFlags
		 */
		public int getC_changeFlags() {
			return c_changeFlags;
		}

		/**
		 * @param flags the c_changeFlags to set
		 */
		public void setC_changeFlags(int flags) {
			c_changeFlags = flags;
		}

		/**
		 * @return the changeFlags
		 */
		public int getChangeFlags() {
			return changeFlags;
		}

		/**
		 * @param changeFlags the changeFlags to set
		 */
		public void setChangeFlags(int changeFlags) {
			this.changeFlags = changeFlags;
		}

		/**
		 * @return the c_element_type
		 */
		public int getC_element_type() {
			return c_element_type;
		}

		/**
		 * @param c_element_type the c_element_type to set
		 */
		public void setC_element_type(int c_element_type) {
			this.c_element_type = c_element_type;
		}

		/**
		 * @return the element_type
		 */
		public int getElement_type() {
			return element_type;
		}

		/**
		 * @param element_type the element_type to set
		 */
		public void setElement_type(int element_type) {
			this.element_type = element_type;
		}

		/**
		 * @return the c_element_flags
		 */
		public int getC_element_flags() {
			return c_element_flags;
		}

		/**
		 * @param c_element_flags the c_element_flags to set
		 */
		public void setC_element_flags(int c_element_flags) {
			this.c_element_flags = c_element_flags;
		}

		/**
		 * @return the element_flags
		 */
		public int getElement_flags() {
			return element_flags;
		}

		/**
		 * @param element_flags the element_flags to set
		 */
		public void setElement_flags(int element_flags) {
			this.element_flags = element_flags;
		}

		/**
		 * @return the c_element_exists
		 */
		public int getC_element_exists() {
			return c_element_exists;
		}

		/**
		 * @param c_element_exists the c_element_exists to set
		 */
		public void setC_element_exists(int c_element_exists) {
			this.c_element_exists = c_element_exists;
		}

		/**
		 * @return the element_exists
		 */
		public boolean isElement_exists() {
			return element_exists;
		}

		/**
		 * @param element_exists the element_exists to set
		 */
		public void setElement_exists(boolean element_exists) {
			this.element_exists = element_exists;
		}

		/**
		 * @return the c_element_name
		 */
		public int isC_element_name() {
			return c_element_name;
		}

		/**
		 * @param c_element_name the c_element_name to set
		 */
		public void setC_element_name(int c_element_name) {
			this.c_element_name = c_element_name;
		}

		/**
		 * @return the element_name
		 */
		public String getElement_name() {
			return element_name;
		}

		/**
		 * @param element_name the element_name to set
		 */
		public void setElement_name(String element_name) {
			this.element_name = element_name;
		}

		@Override
		public boolean isNode() {
			return false;
		}

		
		
	}
	
}