package org.teamweaver.delias.commons;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.teamweaver.delias.actions.AbstractAction;
import org.teamweaver.delias.actions.ModifyAction;

public class ModifyListener extends AbstractListener implements 
	Listener{

	private Widget focus = null;
	private Widget mouseWidget = null;
	@Override
	public void handleEvent(Event event) {
		if (event.type == SWT.MouseDown && event.widget instanceof Text){
			handleMouse(event);
			return;
		}
		
		if (event.type == SWT.FocusIn && (event.widget instanceof Text || event.widget instanceof CCombo || event.widget instanceof Combo)){
			System.out.println("gain focus");
			focus = event.widget;
			return;
		}
		
		if (!checkCreate(event))
			return;
		if (event.type == SWT.Modify && focus == event.widget && mouseWidget == event.widget){
		
			AbstractAction action = null;
			if (event.widget instanceof Text){
				//System.out.println(event.data);
				// make sure it is not an empty modification event
				String t = ((Text)event.widget).getText();
				if (!t.equals("")){
					action = new ModifyAction(event.type);
					action.process(event);
					interactionHistory.bufferInteraction(action);
				}

			}
		} else if(event.type == SWT.Modify &&  event.widget instanceof Text){
			AbstractAction action = null;
			System.out.println(event.data);
			// make sure it is not an empty modification event
			String t = ((Text)event.widget).getText();
			if (!t.equals("")){
				action = new ModifyAction(event.type);
				action.process(event);
				interactionHistory.bufferInteraction(action);
			}

		}
	}
	private void handleMouse(Event event) {
		mouseWidget = event.widget;
		
	}
}
