package org.teamweaver.delias.visualization;

import org.teamweaver.delias.interaction.InteractionHistoryHandler;

public class DeliasGUI {
	
	DeliasGUI(){
		new InteractionTree();
		//InteractionHistoryHandler.getInstance().elicitChanges();
	}

}
