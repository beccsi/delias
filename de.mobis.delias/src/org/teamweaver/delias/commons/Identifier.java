package org.teamweaver.delias.commons;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.CoolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.PartSite;


public class Identifier
{

	public static Composite getWorkbenchPartControl(IWorkbenchPart part)
	{
		Composite paneComposite = (Composite) ((CoolBarManager) ((PartSite) part.getSite()).getPart()).getControl();
		Control[] paneChildren = paneComposite.getChildren();
		return ((Composite) paneChildren[0]).getParent();
	}
	
	public static boolean getWidgetType(Widget widget){
		
		if (widget instanceof MenuItem){
			System.out.println("widget is menue item");
			MenuItem menuItem = (MenuItem) widget;
			if (onMenubar(menuItem)){
				return true;
			}
			
		}	else if (widget instanceof ToolItem){
			System.out.println("widget is tool item");
		} else if (widget instanceof Shell)
		{
			System.out.println("widget is shell");
		}
		else if (widget instanceof Control)
		{
			System.out.println("widget is control");
		}
		else if (widget instanceof Menu)
		{
			System.out.println("widget is menu");
		}
		else {
			return false;
		}
		return false;
	}
	
	private static boolean onMenubar(MenuItem menuItem)
	{
		Menu parent = menuItem.getParent();
		MenuItem parentItem = parent.getParentItem();

		if (parentItem != null)
		{
			return onMenubar(parentItem);
		}
		Shell theShell = parent.getShell();
		return parent == theShell.getMenuBar();
		
	}
}

