package org.teamweaver.delias.actions;

import javax.swing.tree.DefaultMutableTreeNode;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.teamweaver.delias.commons.DeliasUtils;

public class SetSelectionAction extends AbstractAction {

	public SetSelectionAction(int type) {
		super(type);
	}
	
	public void process(Event event){
		boolean isCombo = event.widget instanceof Combo;
		boolean isCCombo = event.widget instanceof CCombo;

		Combo combo = isCombo ? (Combo) event.widget : null;
		CCombo ccombo = isCCombo ? (CCombo) event.widget : null;
		Object item = isCombo ? combo.getItem(combo.getSelectionIndex())
				: (isCCombo ? (Object) ccombo.getItem(ccombo
						.getSelectionIndex()) : event.item);


		if (isCombo || isCCombo){
			System.out.println("isCombo or CCombo");
			description = (isCombo ? combo.getItem(combo
					.getSelectionIndex()) : ccombo.getItem(ccombo
					.getSelectionIndex()));
		}
		else{
			System.out.println("not isCombo or CCombo");
			description = getTextField(event.item);
		}
		description = DeliasUtils.normalizeDescriptiveField(description);
	}
	
	public DefaultMutableTreeNode actionNode(){
		return new DefaultMutableTreeNode("Select " + description);
	}

}
