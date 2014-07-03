package org.teamweaver.delias.rdf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.teamweaver.delias.utils.DEvent;

 
public class CSVreader{
	
	public static List<DEvent> readCSV(){
		//Input file which needs to be parsed
        String fileToParse = "/Users/beccs/Documents/rdfout/rdfresults.csv";
        BufferedReader fileReader = null;
         
        //Delimiter used in CSV file
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSSSSSX");
        df.setTimeZone(TimeZone.getTimeZone("CEST"));
        final String DELIMITER = ",";
        DEvent event = null;
        String dat;
        List<DEvent> evList = new ArrayList<>();
        final DateTimeFormatter dfi = DateTimeFormat
    			.forPattern("yyyy-MM-dd kk:mm:ss.SSSSSSZ");
        DateTime dateTime;
        try
        {
            String line = "";
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileToParse));
             
            //Read the file line by line
            while ((line = fileReader.readLine()) != null) 
            {
                //Get all tokens available in line
            	if (event != null){
            		evList.add(event);
            	} 
            	event = new DEvent();
                String[] tokens = line.split(DELIMITER);
                for(int i=0; i < tokens.length; i++)
                {
                    switch(tokens[i]){
                    case "hasTimestamp":
                    	//System.out.println(tokens[i+1]);
                    	dat = tokens[i+1];

                    	dateTime = dfi.withOffsetParsed()
                    			.parseDateTime(dat);

                    	Date date = dateTime.toDate();
                    	event.dat = date;
                    	break;
                    case "type":
                    	event.kind = tokens[i+1];
                    	break;
                    case "hasOrigin":
                    	event.originID = tokens[i+1];
                    	break;
                    case "concerns":
                    	event.concerns = tokens[i+1];
                    	break;
                    case "lastTimestamp":
                    	dat = tokens[i+1];
                    	dateTime = dfi.withOffsetParsed()
                    			.parseDateTime(dat);

                    	date = dateTime.toDate();
                    	event.endDate = date;
                    	break;
                    case "concernsJavaElement":
                    	event.concerns = tokens[i+1];
                    	break;
                    case "changedElementType":
                    	event.delta = tokens[i+1];
                    }
                }
            }
           // for (DEvent ev : evList){
            	//if (ev != null)
            		//System.out.println(ev.toString());
           // }
        } 
        catch (Exception e) {
            e.printStackTrace();
        } 
        finally
        {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return evList;
	}
	public static void test(){
		System.out.println("test");
	}
    public static void main(String[] args)
    {
        //Input file which needs to be parsed
        String fileToParse = "/Users/beccs/Documents/rdfout/rdfresults.csv";
        BufferedReader fileReader = null;
         
        //Delimiter used in CSV file
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSSSSSX");
        df.setTimeZone(TimeZone.getTimeZone("CEST"));
        final String DELIMITER = ",";
        DEvent event = null;
        String dat;
        List<DEvent> evList = new ArrayList<>();
        final DateTimeFormatter dfi = DateTimeFormat
    			.forPattern("yyyy-MM-dd kk:mm:ss.SSSSSSZ");
        DateTime dateTime;
        try
        {
            String line = "";
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileToParse));
             
            //Read the file line by line
            while ((line = fileReader.readLine()) != null) 
            {
                //Get all tokens available in line
            	if (event != null){
            		evList.add(event);
            	} 
            	event = new DEvent();
                String[] tokens = line.split(DELIMITER);
                for(int i=0; i < tokens.length; i++)
                {
                    switch(tokens[i]){
                    case "hasTimestamp":
                    	//System.out.println(tokens[i+1]);
                    	dat = tokens[i+1];

                    	dateTime = dfi.withOffsetParsed()
                    			.parseDateTime(dat);

                    	Date date = dateTime.toDate();
                    	event.dat = date;
                    	break;
                    case "type":
                    	event.kind = tokens[i+1];
                    	break;
                    case "hasOrigin":
                    	event.originID = tokens[i+1];
                    	break;
                    case "concerns":
                    	event.concerns = tokens[i+1];
                    	break;
                    case "lastTimestamp":
                    	dat = tokens[i+1];
                    	dateTime = dfi.withOffsetParsed()
                    			.parseDateTime(dat);

                    	date = dateTime.toDate();
                    	event.endDate = date;
                    	break;
                    case "concernsJavaElement":
                    	event.concerns = tokens[i+1];
                    	break;
                    case "changedElementType":
                    	event.delta = tokens[i+1];
                    }
                }
            }
            for (DEvent ev : evList){
            	if (ev != null)
            		System.out.println(ev.toString());
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        } 
        finally
        {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}