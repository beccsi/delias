package org.teamweaver.delias.actions;


import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.teamweaver.delias.commons.CancelDetector;
import org.teamweaver.delias.commons.DeliasUtils;
import org.teamweaver.delias.commons.PreferenceListener;

public class SimpleSelectionAction extends AbstractAction {
	private ArrayList path;
	private boolean prefsOpened = false;
	private PreferenceListener preferenceListener;
	private boolean selection;
	private int type;
	
	public SimpleSelectionAction(int type) {
		super(type);
		System.out.println("new simple selection");
	}
		
	private Boolean getSelection(Widget widget){
		//this prints out the style - has to be checked via bit shift
		//System.out.println(widget.getStyle());
		if ((widget.getStyle() & SWT.CHECK) != 0)
			System.out.println("check box");
		else if ((widget.getStyle() & SWT.TOGGLE) != 0)
			System.out.println("toggle");
		/*System.out.println(SWT.NONE);
		
		System.out.println(SWT.CHECK);
		System.out.println(SWT.RADIO);
		System.out.println(SWT.TOGGLE);
		System.out.println(SWT.PUSH);
		System.out.println(SWT.H_SCROLL);
		System.out.println(SWT.V_SCROLL);*/
		//if ((widget.getStyle() & (SWT.CHECK | SWT.RADIO)) == 0)
		//	return null;
		if (widget instanceof Button)
			return new Boolean(((Button) widget).getSelection());
		if (widget instanceof ToolItem)
			return new Boolean(((ToolItem) widget).getSelection());
		if (widget instanceof MenuItem)
			return new Boolean(((MenuItem) widget).getSelection());
		return null;
	}
	
	public void process(Event event){
		System.out.println("process event ... ");
		System.out.println("event widget "+event.widget);
		if (getSelection(event.widget))
			System.out.println("no error");
		description = getTextField(event.widget);
		//System.out.println(description);
		description = DeliasUtils.normalizeDescriptiveField(description);

		if (event.widget instanceof MenuItem){
			type = 1;
			processMenuItem(event);

		} else if (event.widget instanceof Button){
			System.out.println(event.widget);
			if ((event.widget.getStyle() & SWT.CHECK) != 0){
				System.out.println("CHECK CHECK CHECK");
				type = 3;
				processButton(event);
			} else {
				type = 2;
				processButton(event);
			}
		} 
	}

	
	private void processButton(Event event) {
		System.out.println("processButton");
		if (description.equals("Cancel")){
			//(CancelDetector.getInstance()).cancelFound(event);
		}else if (description.equals("OK")){
			PreferenceChangeEvent lastEvent = (PreferenceListener.getInstance().getLastEvent());
			if (lastEvent != null)
				System.out.println("last event key"+lastEvent.getKey());
		}	

	}


	private void processMenuItem(Event event) {
		if (description.startsWith("Preferences")){
			//System.out.println("equals prefs");
			if (!prefsOpened){
				prefsOpened = true;
				preferenceListener = PreferenceListener.getInstance();
				try {
					preferenceListener.addListeners( Platform.getPreferencesService().getRootNode());
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			path = getPath((MenuItem)(event.widget));
			if (path != null){
				for (Object obj : path){
					System.out.println("obj of path "+obj.toString());
				}
			}
			//System.out.println(getPath((MenuItem)(event.widget)));
		}
		
	}
	public DefaultMutableTreeNode actionNode(){
		if (type == 1 && selectMethod != 3)
			return new DefaultMutableTreeNode ("Select Menu " + description);
		else if (type == 1 && selectMethod == 3)
			return new DefaultMutableTreeNode("Select Context Menu " + description);
		else if (type == 2)
			return new DefaultMutableTreeNode("Select " + description);
		else if (type == 3)
			return new DefaultMutableTreeNode("Check " + description);
		else 
			return new DefaultMutableTreeNode("Select " + description);
	}
	
	public void print() {
		//System.out.println(description+" "+type+" "+ selection);
		if (type != 1)
			System.out.println("Select "+ description);
		else
			System.out.println("Select Menu " + description);
		
		if (path != null){
			for (Object obj : path){
				//System.out.print(obj.toString()+" ");
				System.out.println(obj);
			}
		}
		
	}
	
	private ArrayList getPath(MenuItem item)
	{
		ArrayList segments = new ArrayList();
		Object data = item.getData();

		if (data instanceof ContributionItem)
		{
			ContributionItem aitem = (ContributionItem) data;
			MenuManager manager = (MenuManager) aitem.getParent();
			while (manager != null)
			{
				String id = manager.getId();
				if (id == null)
					break;
				segments.add(0, id);
				manager = (MenuManager) manager.getParent();
			}
		}
		return segments.size() > 0 ? segments : null;
	}
}