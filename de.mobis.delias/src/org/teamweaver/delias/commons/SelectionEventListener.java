package org.teamweaver.delias.commons;


import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.teamweaver.delias.actions.AbstractAction;
import org.teamweaver.delias.actions.SelectionAction;
import org.teamweaver.delias.actions.SetSelectionAction;
import org.teamweaver.delias.actions.SimpleSelectionAction;

public class SelectionEventListener extends AbstractListener implements
		Listener {

	private int selectMethod;
	
	@Override
	public void handleEvent(Event event) {
		if (event.type == SWT.MouseDown){
			handleMouse(event);
			return;
		}
		if (!checkCreate(event))
			return;
		AbstractAction action = null;

		if (event.widget instanceof MenuItem){
			action = new SimpleSelectionAction(event.type);
			
		} else if (event.widget instanceof ToolItem){
			System.out.println("tool");
			
		} else if (event.widget instanceof Text){
			System.out.println("text");
			
		} else if (event.widget instanceof Button){
			action = new SimpleSelectionAction(event.type);
			action.setShell(((Button) event.widget).getShell());

		} else if (event.widget instanceof Tree){
			StringBuilder sb = new StringBuilder();
			Tree tr = (Tree) event.widget;
			System.out.println("tree");
			//do not handle events of workbench window 
			
			if ((!(tr.getShell() == PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell())) ||
					selectMethod == 3){
				System.out.println("the shell "+tr.getShell());
				TreeItem[] items = ((Tree) event.widget).getSelection();

				for (TreeItem item : items){
					//System.out.println("tree item "+ item.toString());
					sb.append(item.getText());
					TreeItem selected = item;

					while(selected.getParentItem() != null){
						TreeItem parentIt = selected.getParentItem();
						//System.out.println(parentIt);
						sb.insert(0, "/");
						sb.insert(0, parentIt.getText());

						selected = parentIt;
					}
				}
				System.out.println(sb.toString());
				action = new SelectionAction(event.type);
				action.process(event);
				interactionHistory.addInteraction(action);
			}
			return;
			
		}
		
		else if (event.widget instanceof Table){
			System.out.println("table");
			if (event.detail == SWT.CHECK)
				System.out.println("check");
			String type = event.type == SWT.DefaultSelection ? SelectionAction.DEFAULT_SELECT
					: SelectionAction.ITEM_SELECT;
			
			action = new SelectionAction(event.type);
			
			((SelectionAction) action).addPart(DeliasUtils.getActivePartTitle());
		}
		else if( event.widget instanceof List){
			
			System.out.println("table, tree or list");
			if (event.detail == SWT.CHECK)
				System.out.println("check");
			String type = event.type == SWT.DefaultSelection ? SelectionAction.DEFAULT_SELECT
					: SelectionAction.ITEM_SELECT;
			action = new SelectionAction(event.type);
			((SelectionAction) action).addPart(DeliasUtils.getActivePartTitle());

		} else if (event.widget instanceof TabFolder){
			TabFolder tf = (TabFolder) event.widget;
			if (!(tf.getShell() == PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell())){
				action = new SetSelectionAction(event.type);
			}
		}
		else if( event.widget instanceof CTabFolder){
			CTabFolder tf = (CTabFolder) event.widget;
			if (!(tf.getShell() == PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell())){
				action = new SetSelectionAction(event.type);
			}

		} else if (event.widget instanceof Combo || event.widget instanceof CCombo){
			action = new SetSelectionAction(event.type);
		}

		if (action != null){
			//System.out.println("process within selection listener");
			action.setSelectionMethod(selectMethod);
			action.process(event);
			interactionHistory.addInteraction(action);
			
		}
	}

	private void handleMouse(Event event) {
		System.out.println(event.widget);
		selectMethod = event.button;
		
	}

}
