package org.teamweaver.delias.rdf;


import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

import com.hp.hpl.jena.datatypes.xsd.XSDDateTime;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDFS;

public class RDFreader extends Object {
    
	static Model interactionSchema;
	static Model artefactSchema;
	
	private static void initModel (){
		OntModelSpec ontModelSpec;
		try {
				
				ontModelSpec = OntModelSpec.OWL_DL_MEM;
				

				interactionSchema = ModelFactory.createOntologyModel(ontModelSpec);
				String path = new File("/Users/beccs/Documents/int.owl").toURL().toString();
				interactionSchema.read( path,  "");


				artefactSchema = ModelFactory.createOntologyModel(ontModelSpec);
				interactionSchema.read(new File("/Users/beccs/Documents/ka.owl").toURL().toString());
		
					
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}
					// TODO Add once a user to the data
				}	
				
		

	public static OntModel getSchema() {
		return (OntModel) artefactSchema.add(interactionSchema);
	}	
		
	
	public static void main (String args[]) {
		initModel();
		String sUri = "/Users/beccs/Documents/rdf/test.rdf";
		String NS ="http://www.team-project.eu/ontologies/int.owl#";
		// create an empty model
		Model m = ModelFactory.createDefaultModel();
		try {
			m.read(new File(sUri).toURL().toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Model model = getSchema();
		//model.write(System.out);
		final StmtIterator stmts = model.listStatements(null, RDFS.subClassOf, (RDFNode) null);
		m.setNsPrefix( "eg", NS );
		Property p = ResourceFactory.createProperty(NS + "hasTimestamp");
		//m.write(System.out);

	
		StmtIterator it = m.listStatements(null, p, (RDFNode) null);
		while (it.hasNext()) {
			Statement r = it.nextStatement();
			//System.out.println(r.getString());
		}
		final StmtIterator stm = m.listStatements();
		while (stm.hasNext()){
			final Statement st = (Statement) stm.next();
			if (st.getSubject().isResource()){
				final StmtIterator sm = st.getSubject().listProperties();
				while (sm.hasNext()){
					final Statement s = (Statement) sm.next();
					//System.out.println( s.getSubject() + " " + s.getPredicate());
					if (s.getObject() instanceof Resource) {
						//System.out.println(s.getObject().toString());
					} else {
						// object is a literal
						
						Literal resL = s.getLiteral();
						if (resL.getValue() instanceof String){
							//System.out.println(s.getString());
						} else { 
							
							XSDDateTime tim = (XSDDateTime) resL.getValue();
							System.out.println(tim.toString());
							String date = tim.toString();
							
							//tim.toString();
							//System.out.println(tim.getSeconds());


						}

					}
					//System.out.println("is resoruce");
				} 
			}

			//Resource res = st.getResource();
			//System.out.println(res.toString());
			String sub = st.getSubject().toString();
			String [] subs = sub.split(Pattern.quote( "#" ));
			//System.out.println(Arrays.toString(subs));

			String pred = st.getPredicate().toString();
			String [] preds = pred.split(Pattern.quote( "#" ));
			//System.out.println(Arrays.toString(preds));

			//System.out.println( st.getSubject() + " " + st.getPredicate() +  st.getObject() );
		}
		while ( stmts.hasNext() ) {
			final Statement stmt = (Statement) stmts.next();
			//System.out.println( stmt.getSubject() + " is a subclass of " + stmt.getObject() );
		}
		// use the FileManager to find the input file
		InputStream in = FileManager.get().open(sUri);
		if (in == null) {
			throw new IllegalArgumentException(
					"File: " + sUri + " not found");
		}



	}
}
