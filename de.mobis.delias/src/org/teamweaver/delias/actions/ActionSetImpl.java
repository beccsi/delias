package org.teamweaver.delias.actions;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import org.eclipse.swt.widgets.Shell;
import org.teamweaver.delias.commons.CancelDetector;
import org.teamweaver.delias.commons.CancelListener;
import org.teamweaver.delias.commons.XMLUtil;



public class ActionSetImpl extends AbstractActionSet implements CancelListener{
	private String ID;
	private ArrayList actions;
	private Shell parent;
	private Shell shell; 
	private DefaultMutableTreeNode child = null;
	private CancelDetector cancelDetector;
	private boolean dispose = false;
	private AbstractAction actionBuffer = null;
	//private Event lastEvent = null;
	
	public ActionSetImpl(String name, Shell parent, Shell shell) {
		this.ID = shell.getText();
		this.shell = shell;
		this.parent = parent;
		actions = new ArrayList();
		this.cancelDetector = CancelDetector.getInstance();
		this.cancelDetector.addListener(this);
		
	}
	public void addToActionBuffer(AbstractAction action){
		actionBuffer = action;
	}
	public boolean actionsEmpty(){
		return actions.isEmpty();
	}
	public void addAction(AbstractAction action){
		if (actionBuffer != null){
			System.out.println("action buffer not empty");
			actionBuffer.print();
			actions.add(actionBuffer);
			actionBuffer = null;
		}
		actions.add(action);
	}

	public void addShell(ActionSetImpl actionSet){
		System.out.println("add shell");
		actions.add(actionSet);
	}
	public void removeShell(){
		System.out.println("remove shell");
		//first remove shell
		if (!actions.isEmpty()){
			actions.remove(actions.size()-1);
			//then previous action
			if (!actions.isEmpty())
				actions.remove(actions.size()-1);
		}
	}
	
	
	public Shell getShell(){
		return shell;
	}
	
	public void cleanList(){
		actions.clear();
	}
	
	public void write(StringBuffer buffer){
		XMLUtil.addElement(buffer, XMLUtil.SHELL_ELEMENT);
	}
	public String getID(){
		return ID;
	}
	public DefaultMutableTreeNode createActionNode(DefaultMutableTreeNode root){
		for (Object actSet : actions){
			if(actSet instanceof ActionSetImpl){
				System.out.println("new root");
				DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode("Dialog "+((ActionSetImpl) actSet).getID());
				root.add(newRoot);
				((ActionSetImpl) actSet).createActionNode(newRoot);
			} else if (actSet instanceof AbstractAction){
				root.add(((AbstractAction)actSet).actionNode());
			}
		}
		return root;
	}
	public TreeItem createActionNodeSwt(TreeItem root){
		for (Object actSet : actions){
			if(actSet instanceof ActionSetImpl){
				System.out.println("new root");
				TreeItem newRoot = new TreeItem(root, SWT.NONE);
				newRoot.setText("Dialog "+((ActionSetImpl) actSet).getID());
				//DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode("Dialog "+((ActionSetImpl) actSet).getID());
				//root.add(newRoot);
				((ActionSetImpl) actSet).createActionNodeSwt(newRoot);
			} else if (actSet instanceof AbstractAction){
				TreeItem newRoot = new TreeItem(root, SWT.NONE);
				newRoot.setText("Default");
				//root.add(((AbstractAction)actSet).actionNode());
			}
		}
		return root;
	}
	
	
	
	
	
	public void print(){
		if (actions == null || actions.size() <= 0)
			return;
		//System.out.println(ID+" ID");
		System.out.println("----- action set -----");
		//System.out.println(this.shell.toString()+ " the parent shell");
		for (Object actSet : actions){
			if(actSet instanceof ActionSetImpl){
				((ActionSetImpl) actSet).print();
			} else if (actSet instanceof AbstractAction){
				((AbstractAction)actSet).print();
			}
		}
		System.out.println("-----action set end------");
	}
	
	public boolean same(Shell shell)
	{
		if (this.shell != null && this.shell.equals(shell)){
			System.out.println("is the same");
			return true;
		}else{
			System.out.println("different");
			return false;
		}
	}
	@Override
	public void cancelDetected(Event event) {
		System.out.println("cancel detected yes");
		dispose = true;	
	}
	public boolean toDispose(){
		return dispose;
	}

}
