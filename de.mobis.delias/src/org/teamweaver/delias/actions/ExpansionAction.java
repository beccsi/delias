package org.teamweaver.delias.actions;

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.teamweaver.delias.commons.DeliasUtils;

public class ExpansionAction extends AbstractAction {

	public ExpansionAction(Shell shell, int type) {
		super(type);
	}
	
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
	
	public void process(Event event){
		System.out.println("process expansion");
		Object[] eventItems = getItemsForEvent(event);
		boolean isWidget = eventItems instanceof Widget[];
		boolean isString = false;

		if (!isWidget)
			isString = eventItems instanceof String[];
		
		description = super.getTextField(eventItems);
	}

}
