package uzuzjmd.competence.main;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import uzuzjmd.competence.shared.epos.DESCRIPTORSETType;
import uzuzjmd.competence.shared.epos.EPOSTypeWrapper;


/**
 * provides a possibility to import competences via the rest interface if server is deployed
 */
public class EposRemoteImporter {

	public static void main(String[] args)
			throws JAXBException, IOException {

		Client client = ClientBuilder.newClient();
		WebTarget webResource = client
				.target("http://fleckenroller.cs.uni-potsdam.de/app/competence-database/competence/competences/learningtemplates/addEpos");
		//WebTarget webResource = client
		//		.target("http://localhost:8084/competences/learningtemplates/addEpos");
		//System.out.println(webResource.getUri());
		List<DESCRIPTORSETType> eposse = EposImporter.parseEPOSXML();
		EPOSTypeWrapper typeWrapper = new EPOSTypeWrapper();
		typeWrapper.setEposCompetences(eposse.toArray(new DESCRIPTORSETType[0]));
		Response result = webResource.request(
				MediaType.APPLICATION_XML).post(
				Entity.entity(typeWrapper,
						MediaType.APPLICATION_XML));
		//System.out.println(result);
	}
}
