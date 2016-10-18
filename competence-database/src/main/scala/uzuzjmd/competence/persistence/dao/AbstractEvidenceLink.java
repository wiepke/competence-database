package uzuzjmd.competence.persistence.dao;

import uzuzjmd.competence.persistence.ontology.Edge;

import java.util.List;

/**
 * Created by dehne on 11.01.2016.
 */
public class AbstractEvidenceLink extends AbstractAbstractEvidenceLink implements Cascadable {

    public Long dateCreated;
    public Boolean isValidated;

    public AbstractEvidenceLink(String id) {
        super(id);
    }

    public static final String computeId (String competence,String evidenceActivityUrl) {
        return competence+evidenceActivityUrl;
    }

    public AbstractEvidenceLink(User creator,
                                User linkedUser,
                                CourseContext courseContext,
                                EvidenceActivity evidenceActivity,
                                Long dateCreated,
                                Boolean isValidated,
                                Competence competence,
                                List<Comment> comments) {
        super(computeId(competence.getId(), evidenceActivity.getId()));
        this.creator = creator;
        this.linkedUser = linkedUser;
        this.courseContext = courseContext;
        this.evidenceActivity = evidenceActivity;
        this.dateCreated = dateCreated;
        this.isValidated = isValidated;
        this.competence = competence;
        this.comments = comments;
    }



    public List<Comment> getComments() throws Exception {
        if (comments == null || comments.isEmpty()) {
            comments = getAssociatedDaosAsRange(Edge.CommentOfEvidence, Comment.class);
        }
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Competence getCompetence() {
        return competence;
    }

    public void setCompetence(Competence competence) {
        this.competence = competence;
    }

    public Boolean getValidated() {
        return isValidated;
    }

    public void setValidated(Boolean validated) {
        isValidated = validated;
    }

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public EvidenceActivity getEvidenceActivity() {
        return evidenceActivity;
    }

    public void setEvidenceActivity(EvidenceActivity evidenceActivity) {
        this.evidenceActivity = evidenceActivity;
    }

    public CourseContext getCourseContext() {
        return courseContext;
    }

    public void setCourseContext(CourseContext courseContext) {
        this.courseContext = courseContext;
    }

    public User getLinkedUser() {
        return linkedUser;
    }

    public void setLinkedUser(User linkedUser) {
        this.linkedUser = linkedUser;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Override
    public void persistMore() throws Exception{
        this.persist();
        linkedUser.persist();
        createEdgeWith(linkedUser, Edge.UserOfLink);
        creator.persist();
        createEdgeWith(Edge.LinkCreatedByUser, creator);
        courseContext.persist();
        createEdgeWith(Edge.LinkOfCourseContext, courseContext);
        evidenceActivity.persist();
        createEdgeWith(evidenceActivity, Edge.ActivityOf);
        competence.persist();
        createEdgeWith(Edge.linksCompetence, competence);
    }


    public void linkComments(List<Comment> comments) throws Exception {
        for (Comment comment : comments) {
            linkComment(comment);
        }
    }

    public void linkComment(Comment comment) throws Exception {
            comment.persist();
            createEdgeWith(comment, Edge.CommentOfEvidence);
    }

    public List<User> getAllLinkedUsers() throws Exception {
            return getAssociatedDaosAsDomain(Edge.UserOfLink, User.class);
    }

    public List<EvidenceActivity> getAllActivities() throws Exception {
            return getAssociatedDaosAsRange(Edge.ActivityOf, EvidenceActivity.class);
    }

    public List<CourseContext> getAllCourseContexts() throws Exception {
            return getAssociatedDaosAsRange(Edge.LinkOfCourseContext, CourseContext.class);
    }

    public List<Competence> getAllLinkedCompetences() throws Exception {
        return  getAssociatedDaosAsDomain(Edge.linksCompetence, Competence.class);
    }

    /**
     * use persistMore for cascadables
     * @return
     * @throws Exception
     */
    @Deprecated
    @Override
    public Dao persist() throws Exception {
        return super.persist();
    }
}
