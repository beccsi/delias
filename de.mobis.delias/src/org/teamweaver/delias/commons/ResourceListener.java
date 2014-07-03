package org.teamweaver.delias.commons;

import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

public class ResourceListener implements IResourceChangeListener {

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		IResource res = event.getResource();
        switch (event.getType()) {
           case IResourceChangeEvent.PRE_CLOSE:
              System.out.print("Project ");
              System.out.print(res.getFullPath());
              System.out.println(" is about to close.");
              break;
           case IResourceChangeEvent.PRE_DELETE:
              System.out.print("Project ");
              System.out.print(res.getFullPath());
              System.out.println(" is about to be deleted.");
              break;
           case IResourceChangeEvent.POST_CHANGE:
              System.out.println("Resources have changed.");
              try {
				event.getDelta().accept(new DeltaPrinter());
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
              break;
           case IResourceChangeEvent.PRE_BUILD:
              System.out.println("Build about to run.");
              try {
				event.getDelta().accept(new DeltaPrinter());
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
              break;
           case IResourceChangeEvent.POST_BUILD:
              System.out.println("Build complete.");
              try {
				event.getDelta().accept(new DeltaPrinter());
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
              break;
        }

	}
	class DeltaPrinter implements IResourceDeltaVisitor {
	      public boolean visit(IResourceDelta delta) {
	         IResource res = delta.getResource();
	         switch (delta.getKind()) {
	            case IResourceDelta.ADDED:
	               System.out.print("Resource ");
	               System.out.print(res.getFullPath());
	               System.out.println(" was added.");
	               break;
	            case IResourceDelta.REMOVED:
	               System.out.print("Resource ");
	               System.out.print(res.getFullPath());
	               System.out.println(" was removed.");
	               break;
	            case IResourceDelta.CHANGED:
	               System.out.print("Resource ");
	               int flags = delta.getFlags();
	               if ((flags & IResourceDelta.CONTENT) != 0) {
	            	   System.out.println("--> Content Change");
	               }
	               if ((flags & IResourceDelta.REPLACED) != 0) {
	            	   System.out.println("--> Content Replaced");
	               }
	               if ((flags & IResourceDelta.MARKERS) != 0) {
	            	   System.out.println("--> Marker Change");
	            	   IMarkerDelta[] markers = delta.getMarkerDeltas();
	            	   for (IMarkerDelta md : markers)
	            		   System.out.println(md.getResource());
	            	   // if interested in markers, check these deltas
	               }
	               System.out.print(res.getFullPath());
	               System.out.println(" has changed.");
	               break;
	         }
	         return true; // visit the children
	      }
	   }
}
