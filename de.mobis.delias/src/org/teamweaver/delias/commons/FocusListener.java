package org.teamweaver.delias.commons;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class FocusListener extends AbstractListener implements Listener {

	@Override
	public void handleEvent(Event event) {
		System.out.println("Focus Event "+event.toString());

	}

}
