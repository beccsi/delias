package org.teamweaver.delias.commons;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class EditorSelectionListener implements ISelectionChangedListener {

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		System.out.println("selection changed");

	}

}
