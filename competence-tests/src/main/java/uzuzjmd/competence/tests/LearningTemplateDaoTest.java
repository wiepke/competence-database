package uzuzjmd.competence.tests;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import uzuzjmd.competence.evidence.service.rest.EvidenceServiceRestServerImpl;
import uzuzjmd.competence.persistence.dao.DBInitializer;
import uzuzjmd.competence.service.rest.*;
import datastructures.graph.Graph;
import datastructures.graph.GraphTriple;
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateResultSet;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This needs running RestServer in order to work
 */
public class LearningTemplateDaoTest extends JerseyTest {

	public static final String LABELNAME = "SuggestedCompetencePrerequisite";
	private static final String course = "university";
	public static String learningTemplateName = "TestLernprojekt";
	private static LearningTemplateResultSet learningTemplateResultSet;
	private static Graph graph;
	private static GraphTriple first;
	private static GraphTriple second;
	private static GraphTriple third;
	private static GraphTriple fourth;
	private static GraphTriple fifth;

	@Override
	protected javax.ws.rs.core.Application configure() {
		DBInitializer.init();
		return new ResourceConfig(LearningTemplateApiImpl.class, CompetenceApiImpl.class, UserApiImpl.class, CourseApiImpl.class, EvidenceApiImpl.class, EvidenceServiceRestServerImpl.class, ProgressApiImpl.class, CompetenceServiceRestJSON.class);
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//t.start();
		//Thread.sleep(200l);
		//initTestGraph();

	}

	public static void initTestGraph() {
		first = new GraphTriple("using tags", "using JSP tags", LABELNAME, true);
		second = new GraphTriple("using JSP tags", "using primfaces tags",
				LABELNAME, true);
		third = new GraphTriple("programming excellency", "creating api", LABELNAME, true);
		fourth = new GraphTriple("using JSP tags", "using faces tags",
				LABELNAME, true);
		fifth = new GraphTriple("creating api",
				"being the first person to generate a universal api",
				LABELNAME, true);

		learningTemplateResultSet = new LearningTemplateResultSet();
		learningTemplateResultSet
				.setNameOfTheLearningTemplate(learningTemplateName);
		learningTemplateResultSet.addTriple(first, new String[] {
				"programming", "jsp" });
		learningTemplateResultSet.addTriple(second, new String[] {
				"programming", "jsp" });
		learningTemplateResultSet.addTriple(third, new String[] {
				"programming", "api" });
		learningTemplateResultSet.addTriple(fourth, new String[] {
				"programming", "jsp" });
		learningTemplateResultSet.addTriple(fifth, new String[] {
				"programming", "api", "universality" });
	}
/**
	@AfterClass
	public static void tearDown() {
		graph = null;
		learningTemplateResultSet = null;

	}
*/


	@Test
	public void testCreateTemplate() {
		System.out.println("##### Test CreateTemplate #####");
		LearningTemplateResultSet learningTemplateResultSet = new LearningTemplateResultSet();
		learningTemplateResultSet.setNameOfTheLearningTemplate(learningTemplateName);
		Response response = target("/competences/learningtemplate/add").request(MediaType.APPLICATION_XML)
				.post(Entity.entity(learningTemplateResultSet, MediaType.APPLICATION_XML));
		Response testOnlyResponse = target("/competences/learningtemplate/add")
				.queryParam("learningTemplateName", learningTemplateResultSet)
				.request(MediaType.APPLICATION_XML)
				.post(Entity.entity(learningTemplateResultSet, MediaType.APPLICATION_XML));
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals(200, testOnlyResponse.getStatus());

		final LearningTemplateResultSet result = target("/competences/learningtemplate/get/testOnlyTemplateCreate").request(MediaType.APPLICATION_XML)
				.get(LearningTemplateResultSet.class);

		Assert.assertNotNull(result);
		
	}
/*
	@Test
	public void testGetLearningProjectTemplate() {
		testCreateTemplate();
		System.out.println("##### Test getLearningProjectTemplate #####");
		final LearningTemplateResultSet result = LearningTemplateDao
				.getLearningProjectTemplate(learningTemplateName);

		Assert.assertNotNull(result);
		Assert.assertEquals("TestLernprojekt",
				result.getNameOfTheLearningTemplate());
//
		final GraphTriple first = getGraphTriple(result.getResultGraph(),
				"using tags", "using JSP tags");
		final GraphTriple second = getGraphTriple(result.getResultGraph(),
				"using JSP tags", "using primfaces tags");
		final GraphTriple third = getGraphTriple(result.getResultGraph(),
				"programming excellency", "creating api");
		final GraphTriple fourth = getGraphTriple(result.getResultGraph(),
				"using JSP tags", "using faces tags");
		final GraphTriple fifth = getGraphTriple(result.getResultGraph(),
				"creating api",
				"being the first person to generate a universal api");

		Assert.assertNotNull(first);
		Assert.assertNotNull(second);
		Assert.assertNotNull(third);
		Assert.assertNotNull(fourth);
		Assert.assertNotNull(fifth);

		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getCatchwordMap());
		Assert.assertNotNull(result.getCatchwordMap().get(first));

		List<String> firsts = Arrays
				.asList(result.getCatchwordMap().get(first));
		Assert.assertTrue(firsts.contains("programming"));
		Assert.assertTrue(firsts.contains("jsp"));

		List<String> seconds = Arrays.asList(result.getCatchwordMap().get(
				second));
		Assert.assertTrue(seconds.contains("programming"));
		Assert.assertTrue(seconds.contains("jsp"));

		List<String> thirds = Arrays
				.asList(result.getCatchwordMap().get(third));
		Assert.assertTrue(thirds.contains("programming"));
		Assert.assertTrue(thirds.contains("api"));

		List<String> fourths = Arrays.asList(result.getCatchwordMap().get(
				fourth));
		Assert.assertTrue(fourths.contains("programming"));
		Assert.assertTrue(fourths.contains("jsp"));

		List<String> fifths = Arrays
				.asList(result.getCatchwordMap().get(fifth));
		Assert.assertTrue(fifths.contains("programming"));
		Assert.assertTrue(fifths.contains("api"));
		Assert.assertTrue(fifths.contains("universality"));

		showLearningTemplateResultSet(learningTemplateResultSet);
	}

	@Test
	public void testCreateOneCompetence() {
		System.out.println("##### Test CreateOneCompetence #####");
		
		Assert.assertEquals(200,
				LearningTemplateDao.createTemplate("TestLernprojekt2"));
		
		final int result1 = LearningTemplateDao.createOneCompetence("Java 1",
				"analyse", "TestLernprojekt2", "java");
		final int result2 = LearningTemplateDao.createOneCompetence("Java 2",
				"analyse", "TestLernprojekt2", "java");
		final int result3 = LearningTemplateDao.createOneCompetence("Java 3",
				"analyse", "TestLernprojekt2", "java");

		Assert.assertEquals(200, result1);
		Assert.assertEquals(200, result2);
		Assert.assertEquals(200, result3);

		final LearningTemplateResultSet result = LearningTemplateDao
				.getLearningProjectTemplate("TestLernprojekt2");
		showLearningTemplateResultSet(result);

		Assert.assertNotNull(result);
	}
	
	@Test
	public void testCreateOneNode() {
		System.out.println("##### Test CreateOneNode #####");
		final LearningTemplateResultSet learningTemplateResultSet = new LearningTemplateResultSet();
		learningTemplateResultSet.setNameOfTheLearningTemplate("TestLernprojekt2");
		learningTemplateResultSet.setRoot(new GraphNode("Java 2"));
		Assert.assertEquals(200, LearningTemplateDao.createTemplate(learningTemplateResultSet));
	}

	@Test
	public void testCreateTemplateWithGraph() {
		System.out.println("##### Test CreateTemplateWithGraph #####");
		Assert.assertEquals(200,
				LearningTemplateDao.createTemplate(learningTemplateName));
		Assert.assertEquals(200, LearningTemplateDao.createTemplate(learningTemplateResultSet));
	}

	@Test
	public void testFindAll() {
		testCreateTemplateWithGraph();
		System.out.println("##### Test findAll LearningTemplate #####");
		final StringList result = LearningTemplateDao.findAll();


		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getData().iterator().next());

		for (String s : result.getData()) {
			System.out.println("\t" + s);
		}
	}

	@Test
	public void testGetGraphFromCourse() {
		testCreateTemplateWithGraph();
		System.out.println("##### Test GetGraphFromCourse #####");
		Assert.assertNotNull(LearningTemplateDao.getGraphFromCourse(course));
	}

	private GraphTriple getGraphTriple(Graph resultGraph, String fromNode,
			String toNode) {

		GraphTriple tmp = null;
		for (GraphTriple triple : resultGraph.triples) {
			if (fromNode.trim().equals(triple.fromNode.trim())
					&& toNode.trim().equals(triple.toNode.trim())) {
				tmp = triple;
			}
		}
		return tmp;
	}

	private void showLearningTemplateResultSet(LearningTemplateResultSet result) {
		for (Entry<GraphTriple, String[]> e : result.getCatchwordMap()
				.entrySet()) {
			System.out.println("\t Catchword"
					+ Arrays.asList(e.getValue()).toString()
					+ " for Graphtriple: ");
			System.out.println("\t" + e.getKey().toString());
		}
	}

	public void createDefaultDescriptorTypes() {
		List<DESCRIPTORType> descriptorTypes = new ArrayList<DESCRIPTORType>();

		DESCRIPTORType desType1 = new DESCRIPTORType();
		desType1.setNAME("ich kann Funktion schreiben");
		desType1.setCOMPETENCE("pascal");
		desType1.setEVALUATIONS("gar nicht; ausreichend; befriedigend; gut");
		desType1.setGOAL((byte) 1);
		desType1.setLEVEL("niedrig");

		DESCRIPTORType desType2 = new DESCRIPTORType();
		desType2.setNAME("Ich kann Klasse schreiben");
		desType2.setCOMPETENCE("java");
		desType2.setEVALUATIONS("gar nicht; ausreichend; befriedigend; gut");
		desType2.setGOAL((byte) 1);
		desType2.setLEVEL("hoch");

		descriptorTypes.add(desType1);
		descriptorTypes.add(desType2);
	}
	
	@Test
	public void testTemplatenWithSameCompetence() {
		 System.out.println("##### Test Learning Template with same Competence #####");
		 final GraphTriple triple = new GraphTriple("cp1", "cp2", LABELNAME, true);
		 final GraphTriple triple2 = new GraphTriple("cp2", "cp3", LABELNAME, true);
		 final GraphTriple triple3 = new GraphTriple("cp3", "cp4", LABELNAME, true);
		 
		 final LearningTemplateResultSet tmp1 = new LearningTemplateResultSet();
		 final LearningTemplateResultSet tmp2 = new LearningTemplateResultSet();
		 
		 tmp1.setNameOfTheLearningTemplate("template 1");
		 tmp2.setNameOfTheLearningTemplate("template 2");
		 
		 tmp1.addTriple(triple, (String[]) Arrays.asList("cw").toArray());
		 tmp1.addTriple(triple2, (String[]) Arrays.asList("cw").toArray());
		 tmp1.addTriple(triple3, (String[]) Arrays.asList("cw").toArray());
		 tmp2.addTriple(triple, (String[]) Arrays.asList("cw").toArray());
		 
		 System.out.println("template 1 with : cp1 - cp2 - cp3 - cp4");
		 System.out.println("template 2 with : cp1 - cp2");
		 Assert.assertEquals(200, LearningTemplateDao.createTemplate(tmp1));
		 Assert.assertEquals(200, LearningTemplateDao.createTemplate(tmp2));
		 
		 final LearningTemplateResultSet result1 = LearningTemplateDao.getLearningProjectTemplate("template 1");
		 final LearningTemplateResultSet result2 = LearningTemplateDao.getLearningProjectTemplate("template 2");

		 Assert.assertNotNull(result1);
		 Assert.assertNotNull(result2);
	}
	*/
}
