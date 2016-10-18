package uzuzjmd.competence.persistence.dao;

/**
 * Created by dehne on 21.09.2016.
 */
public abstract class AbstractReflectiveQuestion extends DaoAbstractImpl{

    protected Competence competence;

    public AbstractReflectiveQuestion(String id, Competence competence) {
        super(id);
        this.competence = competence;
    }

    public AbstractReflectiveQuestion(String questionid)
    {
        super(questionid);
    }
}
