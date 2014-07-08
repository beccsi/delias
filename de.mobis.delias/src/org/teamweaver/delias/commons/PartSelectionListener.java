package org.teamweaver.delias.commons;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

public class PartSelectionListener implements ISelectionListener {

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		System.out.println("PartSelection Listener"+selection.toString());

	}

}
