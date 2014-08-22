package org.teamweaver.delias.visualization;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.teamweaver.delias.interaction.InteractionHistory;

public class InteractionTreeSwt {

	private static final long serialVersionUID = 1L;
	

	
	public static Tree InteractionTree(Composite parent, int options){
		Tree tree = new Tree(parent, options);
		System.out.println("new interaction tree");
		InteractionHistory.createSwtTree(tree);
		//DefaultMutableTreeNode top = InteractionHistory.createTree();
		//createNodes(top);
		//final JPopupMenu pop = new JPopupMenu();
//		JTree tree = new JTree(top);
//		JMenuItem mi = new JMenuItem("Remove");
//		pop.add(mi);
//		mi = new JMenuItem("Edit");
//		pop.add(mi);
//		mi = new JMenuItem("Show Details ...");
//		pop.add(mi);
//		tree.addMouseListener(new MouseListener(){
//
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				if (e.getButton() == 3){
//					pop.setOpaque(true);
//		         	pop.setLightWeightPopupEnabled(true);
//					pop.show(e.getComponent(), e.getX(), e.getY());
//				}
//			}
//
//			@Override
//			public void mousePressed(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void mouseReleased(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void mouseEntered(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void mouseExited(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}});
//		
//		JScrollPane treeView = new JScrollPane(tree);
//		JPanel workPanel = new JPanel(new BorderLayout());
//		workPanel.add(treeView);
//		JFrame frame = new JFrame("Interaction History");
//		frame.add(workPanel);
//		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
//		frame.setSize(400,400);  
//		frame.setVisible(true);
		return tree;
	}

}
