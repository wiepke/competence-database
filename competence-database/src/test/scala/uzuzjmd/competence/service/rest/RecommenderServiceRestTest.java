package uzuzjmd.competence.service.rest;

import config.MagicStrings;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uzuzjmd.competence.main.RestServer;
import uzuzjmd.competence.persistence.dao.Competence;
import uzuzjmd.competence.persistence.dao.User;
import uzuzjmd.competence.recommender.ActivityRecommender;
import uzuzjmd.competence.recommender.CompetenceRecommender;
import uzuzjmd.competence.recommender.CourseRecommender;
import uzuzjmd.competence.recommender.RecommenderFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * Created by dehne on 31.03.2016.
 */
public class RecommenderServiceRestTest {

    static Thread t = null;

    @Before
    public void setUp() throws Exception {

        t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RestServer.startServer();
                    Thread.sleep(500000l);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    @After
    public void tearDown() throws Exception {

        t.interrupt();
    }

    @Test
    public void testRecommendCompetences() throws Exception {
        CompetenceRecommender recommend = RecommenderFactory.createCompetenceRecommender();

        Competence a = new Competence("b1");
        a.persist();
        Competence b = new Competence("b2");
        b.persist();
        Competence c = new Competence("b3");
        c.persist();
        Competence d = new Competence("b4");
        d.persist();


        d.addRequiredCompetence(c);
        c.addRequiredCompetence(b);
        b.addRequiredCompetence(a);


        Competence e = new Competence("c1");
        e.persist();
        Competence f = new Competence("c2");
        f.persist();
        Competence g = new Competence("c3");
        g.persist();
        Competence h = new Competence("c4");
        h.persist();
        Competence i = new Competence("c5");
        i.persist();

        i.addRequiredCompetence(h);
        h.addRequiredCompetence(g);
        g.addRequiredCompetence(f);
        f.addRequiredCompetence(e);
        e.addRequiredCompetence(a);

        User user = new User("julian");
        user.persist();
        user.addCompetencePerformed(a);

        HashMap<String, Double> result = recommend.recommendCompetences(user.getId(), d.getId(), null);

        //System.out.println(result);
        Assert.assertFalse(result.isEmpty());
    }

  /*  @Test
    public void testRecommendActivities() throws Exception {
        ActivityRecommender recommender = RecommenderFactory.createActivityRecommender();
        Assert.assertFalse(recommender.recommendActivities(null, null, null).isEmpty());
    }

    @Test
    public void testRecommendCourses() throws Exception {
        CourseRecommender recommender = RecommenderFactory.createCourseRecommender();
        Assert.assertFalse(recommender.recommendCourse(null).isEmpty());
    }*/
}
