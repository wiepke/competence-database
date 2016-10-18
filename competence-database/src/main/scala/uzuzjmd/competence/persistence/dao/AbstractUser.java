package uzuzjmd.competence.persistence.dao;

import uzuzjmd.competence.persistence.ontology.Edge;

import java.util.List;

/**
 * Created by dehne on 12.01.2016.
 */
public class AbstractUser extends DaoAbstractImpl {
    protected List<CourseContext> courseContexts;
    protected List<Competence> competencesLearned;
    protected List<Competence> competencesInterestedIn;



    public AbstractUser(String id) {
        super(id);
    }

    public List<AbstractEvidenceLink> getAssociatedLinks() throws Exception {
        return getAssociatedDaosAsDomain(Edge.UserOfLink, AbstractEvidenceLink.class);
    }

    public List<Competence> getCompetencesLearned() throws Exception {
        if (competencesLearned == null) {
            competencesLearned =  getAssociatedDaosAsDomain(Edge.UserHasEvidencedAllSubCompetences, Competence.class);
        }
        return competencesLearned;
    }

    public List<Competence> getCompetencesInterestedIn() throws Exception {
        if (competencesInterestedIn == null) {
            competencesInterestedIn =  getAssociatedDaosAsDomain(Edge.InterestedIn, Competence.class);
        }
        return competencesInterestedIn;
    }
}
