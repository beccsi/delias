package org.teamweaver.delias.commons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.PageBook;
import org.teamweaver.delias.actions.AbstractAction;
import org.teamweaver.delias.interaction.InteractionHistory;

public class ActivationListener extends AbstractListener implements Listener {

	//private InteractionHistory interactionHistory;
	
	@Override
	public void handleEvent(Event event) {
		if (event.type == SWT.Activate){
			if (event.widget instanceof PageBook){
				System.out.println("pagebook");

			} else if (event.widget instanceof CTabFolder){
				//do nothing as activations are handled by part Listener
				//System.out.println("ctab");

			} else if (event.widget instanceof Shell){
				System.out.println("shell");
				InteractionHistory.getInstance().newShell((Shell) event.widget);
				//senseEvent(event);

			} else
				return;	
		} else if (event.type == SWT.Close){
			System.out.println("close this shell");
			interactionHistory.getInstance().cancelDetected(event);
		}
	}

}
