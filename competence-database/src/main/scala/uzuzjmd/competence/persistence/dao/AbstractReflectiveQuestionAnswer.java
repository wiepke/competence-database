package uzuzjmd.competence.persistence.dao;

/**
 * Created by dehne on 28.09.2016.
 */
public class AbstractReflectiveQuestionAnswer extends DaoAbstractImpl {
    public User user;
    public ReflectiveQuestion question;

    public AbstractReflectiveQuestionAnswer(String id) {
        super(id);
    }
}
