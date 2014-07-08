package org.teamweaver.delias.actions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

public abstract class AbstractAction implements IAction{
	
	protected ActionSetImpl parent;
	
	protected Shell shell;
	
	protected String description;
	
	protected ArrayList itemList;
	
	protected int type;
	
	protected int selectMethod;
	
	public AbstractAction(int type) {
		this.type = type;
	}
	
	public void setShell(Shell shell){
		this.shell = shell;
	}
	
	public Shell getShell(){
		return this.shell;
	}
	protected Object[] getParameters(Event event){
		return null;
		
	}
	public String getTextField(Object obj){
		try {
			Method getText = (obj.getClass()).getMethod("getText", null);
			description = (String) getText.invoke(obj, null);
			if (description == ""){
				 Method getToolText = (obj.getClass()).getMethod("getToolTipText", null);
				 description = (String) getToolText.invoke(obj, null);
				System.out.println("the descirption of tooltip "+ description);	
			}else 
				System.out.println("the description "+ description);
			return description;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public String getTextField(Object[] objects){
		String descript = "";
		for (Object obj : objects){
			if (descript.length() > 0)
				descript += ", ";
			String text = getTextField(obj);
			if (text != null && text.length() > 0)
				descript += text;
		}
		description = descript;
		return descript;
		
	}
		public void process(Event event){
			
		}
		
		public void sliceEvent(Event event){
			Object[] items = getItems(event);
			boolean isWidget = items instanceof Widget[];
			boolean isString = false;
			if (!isWidget)
				isString = items instanceof String[];

			

			if (items != null)
			{
				

				for (int i = 0; i < items.length; i++)
				{
					Map attributeValuePairs = null;

					if (isWidget){
						//attributeValuePairs = getItemAttributes((Widget) items[i]);
					}
					else if (isString){
						//here resolve issues
					}

					if (attributeValuePairs != null)
					{
						itemList.add(attributeValuePairs);
					}
				}
			}

			getTextField(items);
		}
	
		
		/*public ActionSetImpl getParent(){
			return this.parent;
		}*/
		
		private Object[] getItems(Event event) {
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
		
		public void print() {
			System.out.println(description+" "+type);
		}

		public DefaultMutableTreeNode actionNode(){
			return new DefaultMutableTreeNode("default");
		}
		
		public void setSelectionMethod(int Method) {
			selectMethod = Method;
			
		}
}
