package org.teamweaver.delias.commons;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.editor.IFormPage;

public class MultiPageListener implements IPropertyListener{

	@Override
	public void propertyChanged(Object source, int propId) {
		System.out.println("new prop change");
		System.out.println(source.toString());
		System.out.println(propId);
		if (source instanceof IEditorPart){
			IEditorInput inp = ((IEditorPart) source).getEditorInput();
			if ( source instanceof ITextEditor) {
				final ITextEditor editor = (ITextEditor)source;
				IDocumentProvider prov = editor.getDocumentProvider();
				IDocument doc = prov.getDocument( editor.getEditorInput() );
				ISelection sel = editor.getSelectionProvider().getSelection();
				
				if ( sel instanceof TextSelection ) {
					final TextSelection textSel = (TextSelection)sel;
					String newText = "/*" + textSel.getText() + "*/";
					System.out.println(newText+"the text");
				}
				System.out.println(((IEditorPart) source).getEditorInput());
			}else if(source instanceof MultiPageEditorPart){
				ITextEditor textEditor = null;
				MultiPageEditorPart multiPageEditorPart = (MultiPageEditorPart) source;
				Object selectedPage = multiPageEditorPart.getSelectedPage();
				System.out.println("Multipage editor ");
				System.out.println(selectedPage);
				
				if (selectedPage instanceof ITextEditor) {
					textEditor = (ITextEditor) selectedPage;
					System.out.println("selected"+ textEditor);
				} else if (selectedPage instanceof FormEditor ){
					System.out.println("selected form editor");
				} else if (selectedPage instanceof FormPage ){
					System.out.println("selected form page");
					FormPage fp = (FormPage) selectedPage;
					System.out.println(fp.getContentDescription());
				
					IManagedForm imf = fp.getManagedForm();
					
					System.out.println(imf.getParts());
				}
			}

		}
	}
}
