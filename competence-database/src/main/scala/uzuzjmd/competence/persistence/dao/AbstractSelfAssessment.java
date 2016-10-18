package uzuzjmd.competence.persistence.dao;

/**
 * Created by dehne on 12.01.2016.
 */
public class AbstractSelfAssessment extends DaoAbstractImpl {
    protected User user;
    protected Competence competence;

    public AbstractSelfAssessment(String id) {
        super(id);
    }


}
