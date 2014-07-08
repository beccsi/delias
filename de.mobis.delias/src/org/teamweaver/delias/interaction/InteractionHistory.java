package org.teamweaver.delias.interaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

import javax.swing.tree.DefaultMutableTreeNode;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPartReference;
import org.teamweaver.delias.actions.AbstractAction;
import org.teamweaver.delias.actions.ActionSetImpl;
import org.teamweaver.delias.commons.CancelDetector;
import org.teamweaver.delias.commons.CancelListener;
import org.teamweaver.delias.commons.XMLUtil;
import org.teamweaver.delias.rdf.CSVreader;

public class InteractionHistory{

	private String name;

	private static ArrayList<ActionSetImpl> actionSetsList;
	
	private Stack<ActionSetImpl> actionSetsStack;

	
	private static InteractionHistory interactionHistory;
	private IWorkbenchPartReference partRef;
	
	public InteractionHistory(){
		System.out.println("create Interaction History");
		actionSetsList = new ArrayList<ActionSetImpl>();	
	}
	
	public static InteractionHistory getInstance(){
		if (interactionHistory == null)
			interactionHistory = new InteractionHistory();
		return interactionHistory;
	}
	
	public void startInteractionSequence(Display display){
		actionSetsStack = new Stack<ActionSetImpl>();
		actionSetsList.clear();
		Shell active = display.getActiveShell();
		ActionSetImpl actionSet = createActionSet(active);
		actionSetsStack.push(actionSet);
		actionSetsList.add(actionSet);
		}
	
	
	private ActionSetImpl createActionSet(Shell shell){
		System.out.println("createActionSet");
	
		
		if (shell.getData() instanceof Dialog){
			System.out.println("create Dialog Action set");
		}
		
		return new ActionSetImpl("ID", null, shell);
		
	}
	public void bufferInteraction(AbstractAction action){
		System.out.println("addInteractionToBuffer");
		ActionSetImpl top = getTop();
		top.addToActionBuffer(action);
	}
	
	public void addInteraction(AbstractAction action){
		System.out.println("addInteraction");
		ActionSetImpl top = getTop();
		top.addAction(action);
		if (top.toDispose())
			cancelDetected(action);
	}
		
	public void setActivePart(IWorkbenchPartReference ref){
		partRef = ref;
	}
	
	public String getActivePartName(){
		if (partRef != null){
			return partRef.getTitle();
		}
		else 
			return "";
	}
	
	public ActionSetImpl getTop(){
		if (!actionSetsStack.isEmpty())
			return (ActionSetImpl) actionSetsStack.peek();
		return null;
	}
	
	public void newShell(Shell shell){
		if (shell.isDisposed()){
			//System.out.println("is disposed");
			return;
		}
		Object data = shell.getData();
		
		if (data instanceof Dialog){
		
			if (!isActive(shell)){
				ActionSetImpl parent = null;
				if (actionSetsStack.size() -2 >= 0){
					//System.out.println("we have a parent");
					parent = actionSetsStack.get(actionSetsStack.size()-2);
				}
				
				if (parent != null && parent.same(shell)){
					System.out.println("we pop the stack");
					pop();
				}else{
					ActionSetImpl actionSet = createActionSet(shell);
					if (getTop() == null){
						//System.out.println("there is nothing on the stack");
						actionSetsStack.push(actionSet);
						actionSetsList.add(actionSet);
						
					}else{
						//add a new shell to current actionSet
						getTop().addShell(actionSet);
						actionSetsStack.push(actionSet);
						//actionSetsList.add(actionSet);
					}
				}
			}
		} else if (data instanceof Window){
			System.out.println("instance of window");
		}else{
			//System.out.println("instance of whatever");
			if (!isActive(shell)){
				ActionSetImpl parent = null;
				if (actionSetsStack.size() -2 >= 0){
					System.out.println("we have a parent");
					parent = actionSetsStack.get(actionSetsStack.size()-2);
				}
				
				if (parent != null && parent.same(shell)){
					//System.out.println("we pop the stack");
					pop();
				}else{
					ActionSetImpl actionSet = createActionSet(shell);
					if (getTop() == null){
						//System.out.println("there is nothing on the stack");
						actionSetsStack.push(actionSet);
						actionSetsList.add(actionSet);
					}else{
						getTop().addShell(actionSet);
						actionSetsStack.push(actionSet);
						//actionSetsList.add(actionSet);
					}
				}
			}
		}
			
	}
	
	private void pop() {
		if (actionSetsStack.isEmpty())
			return;
		actionSetsStack.pop();
		
	}

	private void removeFromList(){
		if (!actionSetsList.isEmpty()){
			actionSetsList.remove(actionSetsList.get(actionSetsList.size() - 1));
		}
	}
	private boolean isActive(Shell shell) {
		if (actionSetsStack.isEmpty())
			return false;
		ActionSetImpl actionSet = actionSetsStack.peek();
		return actionSet.same(shell);
	}
	public static DefaultMutableTreeNode createTree(){
		DefaultMutableTreeNode root =
				new DefaultMutableTreeNode("Actions");
		for (ActionSetImpl actSet : actionSetsList){
			actSet.createActionNode(root);
		}
		//DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode("Edit Source file: Activator.java");
		//DefaultMutableTreeNode Child = new DefaultMutableTreeNode("Change method");
		//newRoot.add(Child);
		//Child = new DefaultMutableTreeNode("Change return type");
		//newRoot.add(Child);
		//root.add(newRoot);
		return root;
		
	}
	public void print(){
		if (actionSetsList.size() <= 0)
			return;
		System.out.println("---- Interaction History-------");
		for (ActionSetImpl actSet : actionSetsList){
			actSet.print();
		}
	}
	public void printStack(){
		while (!actionSetsStack.isEmpty()){
			actionSetsStack.peek().print();
			actionSetsStack.pop();
		}
	}

	public void write(StringBuffer sb){
		XMLUtil.addElement(sb,XMLUtil.ACTION_SEQUENCE_ELEMENT);
		
		for (Object actionSet : actionSetsList){
			ActionSetImpl actSet = (ActionSetImpl) actionSet;
			actSet.write(sb);
		}
		XMLUtil.addElement(sb, XMLUtil.ACTION_SEQUENCE_ELEMENT);
	}

	public void cancelDetected(AbstractAction action){
		System.out.println("new cancel method");
			ActionSetImpl current = actionSetsStack.peek();
			if (current.same(action.getShell())){
				actionSetsStack.pop();
				if (!actionSetsStack.isEmpty()){
					current = actionSetsStack.peek();
					current.removeShell();
				}
			}
	}
	
	public void cancelDetected(Event event) {
		if (!event.widget.isDisposed())
			System.out.println(event.widget.toString());
		if (event.widget instanceof Shell){
			System.out.print("close shell");
			ActionSetImpl current = actionSetsStack.peek();
			if (current.same(((Shell)event.widget).getShell())){
				System.out.println("same shells");
				actionSetsStack.pop();
				if (!actionSetsStack.isEmpty()){
					current = actionSetsStack.peek();
					current.removeShell();
				}
			}
		}
	}
}
