package org.teamweaver.delias.xml;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.teamweaver.delias.utils.DEvent;

public class XMLReader {

	public static List<DEvent> readXML() throws XMLStreamException, FileNotFoundException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS zzz");
		List<DEvent> empList = new ArrayList<>();
		DEvent currEmp = null;
		String tagContent = null;
		XMLInputFactory factory = XMLInputFactory.newInstance();

		XMLStreamReader reader = factory.createXMLStreamReader( new FileReader("/Users/beccs/Documents/workspace.xml") );

		while(reader.hasNext()){
			int event = reader.next();

			switch(event){
			case XMLStreamConstants.START_ELEMENT:
				if ("interactionEvent".equals(reader.getLocalName())){
					currEmp = new DEvent();
				}
				break;

			case XMLStreamConstants.CHARACTERS:
				tagContent = reader.getText().trim();
				break;

			case XMLStreamConstants.END_ELEMENT:
				switch(reader.getLocalName()){
				case "interactionEvent":
					empList.add(currEmp);
					break;
				case "kind":
					currEmp.kind = tagContent;
					break;
				case "date":
					try {
						currEmp.dat = df.parse(tagContent);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;
				case "endDate":
					try {
						currEmp.endDate = df.parse(tagContent);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case "originID":
					currEmp.originID = tagContent;
					break;
				case "structureKind":
					currEmp.structureKind = tagContent;
				case "structureHandle":
					currEmp.structureHandle = tagContent;
				case "navigation":
					currEmp.navigation = tagContent;
				case "delta":
					currEmp.delta = tagContent;
				case "interestContribution":
					currEmp.interestContribution = tagContent;
				}
				break;

			case XMLStreamConstants.START_DOCUMENT:
				empList = new ArrayList<>();
				break;
			}

		}

		//Print the employee list populated from XML
		return empList;
	}
	
	public static void main(String[] args) throws XMLStreamException, FileNotFoundException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS zzz");
		List<DEvent> empList = new ArrayList<>();
		DEvent currEmp = null;
		String tagContent = null;
		XMLInputFactory factory = XMLInputFactory.newInstance();

		XMLStreamReader reader = factory.createXMLStreamReader( new FileReader("/Users/beccs/Documents/workspace.xml") );

		while(reader.hasNext()){
			int event = reader.next();

			switch(event){
			case XMLStreamConstants.START_ELEMENT:
				if ("interactionEvent".equals(reader.getLocalName())){
					currEmp = new DEvent();
				}
				break;

			case XMLStreamConstants.CHARACTERS:
				tagContent = reader.getText().trim();
				break;

			case XMLStreamConstants.END_ELEMENT:
				switch(reader.getLocalName()){
				case "interactionEvent":
					empList.add(currEmp);
					break;
				case "kind":
					currEmp.kind = tagContent;
					break;
				case "date":
					try {
						currEmp.dat = df.parse(tagContent);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;
				case "endDate":
					try {
						currEmp.endDate = df.parse(tagContent);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case "originID":
					currEmp.originID = tagContent;
					break;
				case "structureKind":
					currEmp.structureKind = tagContent;
				case "structureHandle":
					currEmp.structureHandle = tagContent;
				case "navigation":
					currEmp.navigation = tagContent;
				case "delta":
					currEmp.delta = tagContent;
				case "interestContribution":
					currEmp.interestContribution = tagContent;
				}
				break;

			case XMLStreamConstants.START_DOCUMENT:
				empList = new ArrayList<>();
				break;
			}

		}

		//Print the employee list populated from XML
		for ( DEvent emp : empList){
			System.out.println(emp);
		}

	}
}

