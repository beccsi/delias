package org.teamweaver.delias.interaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PerspectiveAdapter;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.internal.WorkbenchPage;
import org.teamweaver.delias.commons.ActivationListener;
import org.teamweaver.delias.commons.ClipBoardListener;
import org.teamweaver.delias.commons.EditorSelectionListener;
import org.teamweaver.delias.commons.EventCreator;
import org.teamweaver.delias.commons.FocusListener;
import org.teamweaver.delias.commons.GenericEventListener;
import org.teamweaver.delias.commons.JavaElementListener;
import org.teamweaver.delias.commons.ModifyListener;
import org.teamweaver.delias.commons.MultiPageListener;
import org.teamweaver.delias.commons.PartListener;
import org.teamweaver.delias.commons.PartSelectionListener;
import org.teamweaver.delias.commons.ResourceListener;
import org.teamweaver.delias.commons.SelectionEventListener;
import org.teamweaver.delias.commons.ServiceSelectionListener;
import org.teamweaver.delias.delta.DeltaHandler;
import org.teamweaver.delias.rdf.CSVreader;
import org.teamweaver.delias.utils.DEvent;
import org.teamweaver.delias.xml.XMLReader;



public class InteractionHistoryHandler {
	private IWorkbenchPage activePage;

	private static InteractionHistory interactionHistory;
	
	private PartListener partListener;
	private GenericEventListener eventListener;
	private SelectionEventListener selectionListener;
	private ClipBoardListener clipboardListener;
	private JavaElementListener javaElementListener;
	private ActivationListener activationListener;
	private ServiceSelectionListener serviceListener;
	
	private MultiPageListener multipageListener;
	private ModifyListener modifyListener;
	private FocusListener focusListener;
	//private PartSelectionListener partSelectionListener;
	//private EditorSelectionListener editorListener;
	
	private DeltaHandler deltaHandler;
	
	private static InteractionHistoryHandler interactionHistoryHandler;
	private static List<DEvent> evList;
	
	
	public static final String EXTENSION_POINT_ID = "org.teamweaver.delias.interaction.evaluater";
	
	private InteractionHistoryHandler(){
		
		eventListener = new GenericEventListener();
		selectionListener = new SelectionEventListener();
		//partSelectionListener = new PartSelectionListener();
		clipboardListener = new ClipBoardListener();
		javaElementListener = new JavaElementListener();
		activationListener = new ActivationListener();
		deltaHandler = new DeltaHandler();
		serviceListener = new ServiceSelectionListener();
		multipageListener = new MultiPageListener();
		modifyListener = new ModifyListener();
		focusListener = new FocusListener();
		//editorListener = new EditorSelectionListener();
		capture();
	}
	public InteractionHistoryHandler(boolean input) {
		System.out.println("the new method");
		fromInput();
		readInput();
	}

	public static InteractionHistoryHandler getInstance(){
		if (interactionHistoryHandler == null)
			interactionHistoryHandler = new InteractionHistoryHandler();
		return interactionHistoryHandler;
	}
	public static InteractionHistoryHandler getInstance(boolean input){
		if (interactionHistoryHandler == null)
			interactionHistoryHandler = new InteractionHistoryHandler(input);
		return interactionHistoryHandler;
	}
	public void fromInput(){
		final Display display = PlatformUI.getWorkbench().getDisplay();
	
		
		display.syncExec(new Runnable()
		{
			public void run()
			{
				interactionHistory = InteractionHistory.getInstance();
				interactionHistory.startInteractionSequence(display);
				
				}
		});
	}
	
	public void capture(){
		final Display display = PlatformUI.getWorkbench().getDisplay();
		display.syncExec(new Runnable()
		{
			public void run()
			{
				hookPartListener();
				//display.addFilter(SWT.MouseUp, eventListener);
				//display.addFilter(SWT.KeyUp, eventListener);
				//display.addFilter(SWT.KeyDown, eventListener);
				display.addFilter(SWT.Selection, selectionListener);
				//display.addFilter(SWT.DefaultSelection, selectionListener);
				display.addFilter(SWT.Expand, eventListener);
				display.addFilter(SWT.Collapse, eventListener);
				display.addFilter(SWT.Modify, modifyListener);
				display.addFilter(SWT.Activate, activationListener);
				display.addFilter(SWT.Close, activationListener);
				display.addFilter(SWT.FocusIn, modifyListener);
				display.addFilter(SWT.Traverse, modifyListener);
				//display.addFilter(SWT.MouseDoubleClick, eventListener);
				display.addFilter(SWT.MouseDown, selectionListener);
				display.addFilter(SWT.MouseDown, modifyListener);
				
				interactionHistory = InteractionHistory.getInstance();
				interactionHistory.startInteractionSequence(display);
				
				
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.addPerspectiveListener(new PerspectiveAdapter()
				{
					public void perspectiveActivated(
							IWorkbenchPage page,
							IPerspectiveDescriptor perspective)
					{
						handlePerspectiveChange(page, perspective);
					}
				});
				((ICommandService) PlatformUI.getWorkbench().getService(
						ICommandService.class)).addExecutionListener(clipboardListener);
				//PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				//.getSelectionService().addSelectionListener(serviceListener);
				
				IWorkbenchPage page = (IWorkbenchPage) PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				
				/*IEditorReference[] editorReferences = page.getEditorReferences();

				for (IEditorReference ref : editorReferences){
					System.out.println("editor refs");
					ref.addPropertyListener(multipageListener);
				}*/


				//JavaCore.addElementChangedListener(javaElementListener);
				//IWorkspace workspace = ResourcesPlugin.getWorkspace();
				//ResourceListener listener = new ResourceListener();
				//workspace.addResourceChangeListener(listener);

			}
		});
		
	}
	private void handlePerspectiveChange(IWorkbenchPage page,
			IPerspectiveDescriptor perspective){
		System.out.println("perspective change");
	}
	
	public void stop(){
		File tmp;
		try {
			tmp = File.createTempFile("temp", "xml");
			saveHistory(tmp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void saveHistory(File tmp) {
		if (interactionHistory != null){
			StringBuffer sb = new StringBuffer();
			interactionHistory.write(sb);
		}
	}

	
	private void hookPartListener()
	{
		activePage = Activator.getDefault().getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		partListener = new PartListener();
		activePage.addPartListener(partListener);
	}
	

	public static void onInteraction(Event event) {
		if (interactionHistory == null)
			return;
		//interactionHistory.addInteraction(event);
		//action.process(event);
		
	}

	public void readInput(){
		//CSVreader cs = new CSVreader();
		//evList = cs.readCSV();
		XMLReader xr = new XMLReader();
		try {
			evList = xr.readXML();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EventCreator ev = new EventCreator();
		EventCreator.handleEvents(evList);
	}
	
	public void print() {
		this.interactionHistory.print();
		deltaHandler.elicitChanges();
		
	}
}
