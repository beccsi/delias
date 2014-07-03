package org.teamweaver.delias.commons;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.teamweaver.delias.interaction.InteractionHistory;


public class PartListener extends AbstractListener implements IPartListener2 {


	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		Event event = null;
		if (interactionHistory == null)
			interactionHistory = InteractionHistory.getInstance();
		
		System.out.println("Title of activated part"+partRef.getTitle());
		
		if (partRef instanceof IEditorReference){
			System.out.println("part activated "+ partRef.toString());
			System.out.println("editor reference");
		}
		/* Simulate a focus in event */
		try
		{
			event = new Event();
			event.type = SWT.FocusIn;
			Shell shell = partRef.getPage().getWorkbenchWindow()
					.getShell();
			event.display = shell.getDisplay();
			event.widget = getFirstChild(shell);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (event != null)
		{
			//senseEvent(event);
		}
		
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		Event event = null;
		if (interactionHistory == null)
			interactionHistory = InteractionHistory.getInstance();
		interactionHistory.setActivePart(partRef);
		
		System.out.println("Title of brought part"+partRef.getTitle());
		
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
		System.out.println("part closed "+ partRef.toString());
		Event event = null;
		try{
			event = new Event();
			event.type = SWT.Close;
			Shell shell = partRef.getPage().getWorkbenchWindow()
					.getShell();
			event.display = shell.getDisplay();
			event.widget = getFirstChild(shell);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (event != null)
		{
			//senseEvent(event);
		}
		
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		if (partRef instanceof IEditorReference){
			System.out.println("part opened "+ partRef.toString());
		}
		Event event = null;

		/* Simulate a focus in event */
		try
		{
			event = new Event();
			event.type = SWT.FocusIn;
			Shell shell = partRef.getPage().getWorkbenchWindow()
					.getShell();
			event.display = shell.getDisplay();
			event.widget = getFirstChild(shell);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (event != null)
		{
			//senseEvent(event);
		}
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {
		// TODO Auto-generated method stub
		
	}
	public static Control getFirstChild(Shell shell)
	{
		
		Composite partControl = (Composite) shell.getChildren()[0];
		return partControl;
	}
	
	public static Control getFirstChild(IWorkbenchPart part)
	{
		
		Composite partControl = Identifier
				.getWorkbenchPartControl(part);
		return partControl.getChildren()[0];
	}

}
