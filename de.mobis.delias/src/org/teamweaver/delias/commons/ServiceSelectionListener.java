package org.teamweaver.delias.commons;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

public class ServiceSelectionListener implements ISelectionListener {

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		
		//System.out.println("structured"+part.getTitle());
		String test = part.getSite().getId();
		System.out.println("selection changed " +test);
		if (test.equals("org.eclipse.jdt.ui.PackageExplorer")){
				//System.out.println("package explorer");
		}
		
		//System.out.println("structured"+selection);

	}

}
