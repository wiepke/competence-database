package uzuzjmd.competence.persistence.dao;

import java.util.List;

/**
 * Created by dehne on 12.01.2016.
 */
public class AbstractLearningProjectTemplate extends DaoAbstractImpl {
    public List<Competence> associatedCompetences;

    public AbstractLearningProjectTemplate(String id) {
        super(id);
    }
}
