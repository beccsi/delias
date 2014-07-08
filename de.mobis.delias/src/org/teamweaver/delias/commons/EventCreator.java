package org.teamweaver.delias.commons;
import java.util.List;
import org.teamweaver.delias.utils.DEvent;

public class EventCreator {

	public static void handleEvents(List<DEvent> evList){
		for (DEvent ev : evList){
			if (ev != null && ev.kind != null){
				System.out.println(ev.kind);
				switch (ev.kind){
				case "ActivateIDEWindow":
					break;
				case "DeactivateIDEWindow":
					break;
				case "ActivateIDEPart":
					//System.out.println(ev.toString());
					break;
				case "TInteractionEvent":
					//System.out.println(ev.toString());
					break;
				case "InactivityEvent":
					break;
				case "JavaElementEvent":
					System.out.println(ev.toString());
					break;
			default:
				//System.out.println(ev.toString());
				}
			}
		}
	}
}
