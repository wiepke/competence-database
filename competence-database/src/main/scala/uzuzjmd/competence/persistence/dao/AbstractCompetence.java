package uzuzjmd.competence.persistence.dao;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by dehne on 18.01.2016.
 */
public class AbstractCompetence extends DaoAbstractImpl {



    protected LearningProjectTemplate learningProject;
    // this cache will be dirty in one lifespan of the competence object
    protected HashMap<User,SelfAssessment> userAssessmentMap = new HashMap<>();

    protected java.util.List<Catchword> catchwordList = new LinkedList<Catchword>();

    public AbstractCompetence(String id) {
        super(id);
    }
}
