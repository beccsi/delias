package org.teamweaver.delias.actions;



import java.lang.reflect.Method;

import javax.swing.tree.DefaultMutableTreeNode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;

public class SelectionAction extends AbstractAction {
	
	public SelectionAction(int type) {
		super(type);
	}
	
	public static final String ITEM_SELECT = "item-select";
	public static final String DEFAULT_SELECT = "default-select";
	private String part = null;

	
	protected Object[] getParameters(Event event){
		{
			if (event.widget instanceof List){
				return ((List) event.widget).getSelection();
			}
			
			else if (event.widget instanceof Tree){
				return ((Tree) event.widget).getSelection();
			}
			
			else if (event.widget instanceof Table){
				return ((Table) event.widget).getSelection();	
			}
			return super.getParameters(event);
		}
	}
	protected Object[] getItemsForEvent(Event event)
	{
		Widget item = null;
		if (event.item != null)
			item = event.item;
		else if (event.widget instanceof Item)
			item = event.widget;
		if (item != null)
			return new Widget[]
			{ item };
		return null;
	}
	public void addPart(String partName){
		part = partName;
	}
	
	public void process(Event event){
		System.out.println("process selection");
		Object[] eventItems = getItemsForEvent(event);
		boolean isWidget = eventItems instanceof Widget[];
		boolean isString = false;

		if (!isWidget)
			isString = eventItems instanceof String[];
		
		description = super.getTextField(eventItems);
	}
	public void print() {
		System.out.println("Select "+description+" from " + part);
		
	}
	
	public DefaultMutableTreeNode actionNode(){
		if (selectMethod == 3)
			return new DefaultMutableTreeNode ("Right Click -> "+description+" from "+ part);
		else if (part != null)
			return new DefaultMutableTreeNode ("Select "+description+" from " + part);
		else 
			return new DefaultMutableTreeNode ("Select "+description);
	}
	
	
}
