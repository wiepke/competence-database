package uzuzjmd.competence.tests;

/**
 * Created by dehne on 27.06.2016.
 */

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import uzuzjmd.competence.persistence.dao.DBInitializer;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ActivityApiTest.class,
        CompetencenCourseActivityTest.class,
        CoreTests.class,
        LearningTemplateApi1Test.class,
        LearningTemplateDaoTest.class,
        LearningProjectTemplateAPITest.class,
        SelectedLearningTemplateDAOTest.class,
        SubCompetencenTest.class,
        ProgressApiTests.class,
})

/*ProgressApiTests.class,*/
public class TestSuite {
    public TestSuite() {
        DBInitializer.init();
    }
}
