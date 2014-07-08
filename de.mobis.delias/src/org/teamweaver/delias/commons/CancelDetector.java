package org.teamweaver.delias.commons;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Event;

public class CancelDetector{
	private static CancelDetector cancelDetector;
	
	List<CancelListener> listeners = new ArrayList<CancelListener>();

	public void addListener(CancelListener listener){
		listeners.add(listener);
		System.out.println("add listener cancel");
	}
	public static CancelDetector getInstance(){
		if (cancelDetector == null)
			cancelDetector = new CancelDetector();
		return cancelDetector;
	}
	public void cancelFound(Event event){
		System.out.println("method at least called");
		for (CancelListener listener : listeners){
			listener.cancelDetected(event);
			System.out.println("listeners");
		}
			
	}
}
