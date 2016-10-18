package uzuzjmd.competence.tests;

import org.junit.matchers.StringContains;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import uzuzjmd.competence.datasource.epos.EposImporter;
import uzuzjmd.competence.main.RestServer;
import uzuzjmd.competence.reflexion.dao.LearningTemplateDao;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException, URISyntaxException {
        System.out.println( "Hello World!" );
        RestServer.singleStartServer();
        JUnitCore junit = new JUnitCore();
        List<String> strings = new ArrayList<String>();
        Result result;
        result = junit.run(CompetencenCourseActivityTest.class);
        strings.add(resultToString(CompetencenCourseActivityTest.class.getName(), result));
        result = junit.run(LearningTemplateDaoTest.class);
        strings.add(resultToString(LearningTemplateDaoTest.class.getName(), result));
        result = junit.run(SelectedLearningTemplateDAOTest.class);
        strings.add(resultToString(SelectedLearningTemplateDAOTest.class.getName(), result));
        for (String str :
                strings) {
            System.out.println(str);
        }
        RestServer.stopServer();
    }

    public static String resultToString(String workClass, Result result) {
        return "Test " + workClass + " (Fails/Tests):"
                + result.getFailureCount()
                + "/" + result.getRunCount() + " in " + result.getRunTime() + "s";
    }
}
