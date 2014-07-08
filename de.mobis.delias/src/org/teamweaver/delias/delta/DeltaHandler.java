package org.teamweaver.delias.delta;

import java.util.ArrayList;
import java.util.Date;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.team.core.synchronize.SyncInfo;
import org.eclipse.team.core.variants.IResourceVariant;

public class DeltaHandler {
	
	final ArrayList<IResource> changed = new ArrayList<IResource>();
	
	final long inittime = System.currentTimeMillis();
	
	private LocalHistorySubscriber localHistorySubscriber;
	
	public DeltaHandler(){
		localHistorySubscriber = new LocalHistorySubscriber();
	}

	class TreeVisitor implements IResourceVisitor {

		@Override
		public boolean visit(IResource resource) throws CoreException {
			if(resource.getType() == IResource.FILE) {
				SyncInfo info = localHistorySubscriber.getSyncInfo(resource);
				if (info.getKind() != 0 ){
					//System.out.println(resource.toString()+" change happend");
					final IResourceVariant remote = info.getRemote();
					//System.out.println(remote.getContentIdentifier());
					long tim = resource.getLocalTimeStamp();
					if (tim > inittime){
						//System.out.println(new Date(resource.getLocalTimeStamp()));	
						changed.add(resource);
					}
				}
			}
			return true;
		}
	}
	public void elicitChanges(){
		changed.clear();				
		for (IResource resource : localHistorySubscriber.roots()){
			try {
				resource.accept(new TreeVisitor());
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(changed.toString());
	}
}