package org.teamweaver.delias.actions;

import javax.swing.tree.DefaultMutableTreeNode;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.teamweaver.delias.commons.DeliasUtils;

public class ModifyAction extends AbstractAction {

	private String contents = null;
	
	public ModifyAction(int type) {
		super(type);
		System.out.println("create modify event");
	}
	public void process(Event event){
		//System.out.println(event.toString());
		contents = getContents(event.widget);
		getLabelText4Widget(event.widget, event);
		description = DeliasUtils.normalizeDescriptiveField(description);
		//System.out.println("contents of modify " +contents);
	}
	private String getContents(Widget widget) {
		String widgetText = null;
		
		if (widget instanceof Text)
			widgetText = ((Text) widget).getText();

		else if (widget instanceof Combo)
			widgetText = ((Combo) widget).getText();

		else if (widget instanceof CCombo)
			widgetText = ((CCombo) widget).getText();

		else if (widget instanceof StyledText)
			widgetText = ((StyledText) widget).getText();

		if (widgetText != null)
			return widgetText;

		return null;
	}
	
	private void getLabelText4Widget(Widget widget, Event event)
	{
		if (!(widget instanceof Control))
			return;
		Rectangle rec = ((Control) widget).getBounds();
		double centerY = rec.y + rec.height / 2.0;
		Composite parent = ((Control) widget).getParent();
		if (parent != null)
		{
			Control[] peers = parent.getChildren();
			double minOffset = Double.MAX_VALUE;
			Control labelPeer = null;
			for (int i = 0; i < peers.length; i++)
			{
				Control peer = peers[i];
				if ((peer instanceof Label) || (peer instanceof CLabel))
				{
					rec = peer.getBounds();
					double peerCenterY = (rec.y + rec.height / 2.0);
					double offset = Math.abs(peerCenterY - centerY);
					if (offset < minOffset)
					{
						minOffset = offset;
						labelPeer = peer;
					}
				}
			}
			if (labelPeer != null)
			{
				getTextField(labelPeer);
			
			}
		}
	}
	public DefaultMutableTreeNode actionNode(){
		return new DefaultMutableTreeNode ("Enter "+description+" "+contents);
	}
	public void print() {
		System.out.println(description+" "+contents);
	}

}
