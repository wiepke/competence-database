package uzuzjmd.competence.persistence.dao;

import java.util.List;

/**
 * Created by dehne on 01.06.2016.
 */
public class AbstractAbstractEvidenceLink extends DaoAbstractImpl {
    public User creator;
    public User linkedUser;
    public CourseContext courseContext;
    public EvidenceActivity evidenceActivity;
    public Competence competence;
    public List<Comment> comments;

    public AbstractAbstractEvidenceLink(String id) {
        super(id);
    }
}
