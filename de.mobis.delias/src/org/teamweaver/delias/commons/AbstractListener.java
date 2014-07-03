package org.teamweaver.delias.commons;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.teamweaver.delias.actions.ActionSetImpl;
import org.teamweaver.delias.interaction.InteractionHistory;
import org.teamweaver.delias.interaction.InteractionHistoryHandler;

public abstract class AbstractListener {

	protected InteractionHistory interactionHistory; 
	
	public AbstractListener() {
		// TODO Auto-generated constructor stub
	}
	protected boolean checkCreate(Event event){
		interactionHistory = InteractionHistory.getInstance();
		ActionSetImpl top = interactionHistory.getTop();
		if (top != null){
			Shell topShell = top.getShell();
			Shell widgetShell = (!(event.widget instanceof Control) ? null
					: ((Control) event.widget).getShell());
			if (widgetShell == null || topShell == widgetShell){
				return true;
			}
		}
		return false;
	}
	
	/*public void senseEvent(Event event){
		InteractionHistoryHandler.onInteraction(event);
	}*/

}
