package org.teamweaver.delias.commons;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.INodeChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.NodeChangeEvent;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class PreferenceListener extends AbstractListener implements INodeChangeListener,
		IPreferenceChangeListener {
	
	private static PreferenceListener listener;
	private static PreferenceChangeEvent lastEvent = null;

	@Override
	public void preferenceChange(PreferenceChangeEvent event) {
		lastEvent = event;
	}

	@Override
	public void added(NodeChangeEvent event) {
		IEclipsePreferences children = (IEclipsePreferences) event.getChild();
		//System.out.println(event.toString()+"nodechange event");
		addListener(children);
	}

	@Override
	public void removed(NodeChangeEvent event) {
		// TODO Auto-generated method stub

	}
	
	public PreferenceChangeEvent getLastEvent(){
		if (lastEvent != null){
			System.out.println("last event");
			return lastEvent;
		}
		return 	null;
	}
	public void addListeners(IEclipsePreferences node) throws CoreException {
	    addListener(node);
	    try {
			for (String childName : node.childrenNames()) {
				IEclipsePreferences child = (IEclipsePreferences) node.node(childName);
			    this.addListeners(child);
			}
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	public void addListener(IEclipsePreferences node) {
	    node.addNodeChangeListener(listener);
	    node.addPreferenceChangeListener(listener);
	  }

	public static PreferenceListener getInstance() {
		if (listener == null)
			return listener = new PreferenceListener();
		return listener;
	}
}
