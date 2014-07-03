package org.teamweaver.delias.commons;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IExecutionListenerWithChecks;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.IWorkbenchActionDefinitionIds;

public class ClipBoardListener implements IExecutionListenerWithChecks {

	@Override
	public void notHandled(String commandId, NotHandledException exception) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postExecuteFailure(String commandId,
			ExecutionException exception) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postExecuteSuccess(String commandId, Object returnValue) {
		if (commandId.equals(IWorkbenchActionDefinitionIds.COPY)
				|| commandId.equals(IWorkbenchActionDefinitionIds.CUT)
				|| commandId.equals(IWorkbenchActionDefinitionIds.PASTE)) {

			String type = commandId;

			//  assuming that the source must be the active window
			// in most cases getTitleToolTip (i.e. JavaEditor) is a good
			// description of the source
			// TODO Check if there exists a better solution than the TitleToolTip?
			String source = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage().getActivePart()
					.getTitleToolTip();

			// get the Text from the clipboard
			Clipboard clipboard = new Clipboard(PlatformUI.getWorkbench()
					.getDisplay());
			TextTransfer textTransfer = TextTransfer.getInstance();

			String text = (String) clipboard.getContents(textTransfer);
			clipboard.dispose();

			// use the event only if the source was an ITextEditor
			if (PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().getActivePart() instanceof ITextEditor) {
				
				// TODO get the number of lines from the clipBoard
				// store the number of lines copied and pasted
				int NOL = text.split("\n").length -1 ;
				
			}
			System.out.println("new clipboard");
		}
	}

	@Override
	public void preExecute(String commandId, ExecutionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notDefined(String commandId, NotDefinedException exception) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notEnabled(String commandId, NotEnabledException exception) {
		// TODO Auto-generated method stub

	}

}
