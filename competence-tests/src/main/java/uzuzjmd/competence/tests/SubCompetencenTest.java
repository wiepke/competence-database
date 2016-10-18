package uzuzjmd.competence.tests;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import datastructures.lists.SortedList;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import scala.collection.immutable.List;
import uzuzjmd.competence.evidence.service.rest.EvidenceServiceRestServerImpl;
import uzuzjmd.competence.persistence.dao.DBInitializer;
import uzuzjmd.competence.service.rest.*;
import uzuzjmd.competence.shared.competence.CompetenceXMLTree;

import java.util.ArrayList;
import java.util.Arrays;

public class SubCompetencenTest extends JerseyTest {

    @Override
    protected javax.ws.rs.core.Application configure() {
        DBInitializer.init();
        return new ResourceConfig(
                LearningTemplateApiImpl.class, CompetenceApiImpl.class, UserApiImpl.class, CourseApiImpl.class,
                EvidenceApiImpl.class, EvidenceServiceRestServerImpl.class, CompetenceServiceRestJSON.class);
    }

    @Test
    public void testCreateSubCompetence() {
        final int status = createSubCompetence("creating api", "unter Kompetence", "op1", "catW 1");
        final int status1 = createSubCompetence("creating api", "unter Kompetence 1", "op1", "catW 1");
        final int status2 = createSubCompetence("creating api", "unter Kompetence 2", "op1", "catW 1");
        Assert.assertEquals(200, status);
        Assert.assertEquals(200, status1);
        Assert.assertEquals(200, status2);


        String subCompetencen = getSubCompetence("creating api");

        System.out.println(subCompetencen);

    }

    public int createSubCompetence(
            String superCompetence, String subCompetence, String operator, String... catchwords) {
        final Response response = target("/competences/addOne/").queryParam("competence", superCompetence)
                .queryParam("operator", operator).queryParam("superCompetences", List.empty())
                .queryParam("catchwords", Arrays.asList(catchwords))
                .queryParam("subCompetences", Arrays.asList(subCompetence)).request().post(null);

        return response.getStatus();
    }

    public String getSubCompetence(String rootCompetence) {
        final String context = "university";
        final String response =
                target("/api1/competences").queryParam("rootCompetence", rootCompetence).queryParam("asTree", false)
                        .queryParam("courseId", context).request().get(String.class);
        return response;
    }

    public String getSubCompetence2(String rootCompetence) {
        final String context = "university";
        final List<CompetenceXMLTree> result =
                target("/api1/competences").queryParam("rootCompetence", rootCompetence).queryParam("asTree", true)
                        .queryParam("courseId", context).request().get(List.class);
        ArrayList<String> result1 = new ArrayList<String>();
        if (result != null) {
            SortedList<CompetenceXMLTree> children = result.iterator().next().getChildren();
            for (CompetenceXMLTree competenceXMLTree : children) {
                result1.add(competenceXMLTree.getName());
            }
        }
        return result1.get(0);
    }
}
