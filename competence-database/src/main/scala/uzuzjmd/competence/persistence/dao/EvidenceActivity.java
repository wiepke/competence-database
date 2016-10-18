package uzuzjmd.competence.persistence.dao;

import uzuzjmd.competence.persistence.ontology.Edge;
import uzuzjmd.competence.shared.activity.CommentData;
import datastructures.trees.ActivityEntry;

import java.util.ArrayList;

/**
 * Created by dehne on 11.01.2016.
 */
public class EvidenceActivity extends DaoAbstractImpl {

    public String printableName;

    public EvidenceActivity(String id) {
        super(id);
    }

    public EvidenceActivity(String id, String printableName) {
        super(id);
        this.printableName = printableName;
    }

    public String getUrl() {
        return this.getId();
    }

    public String getPrintableName() {
        return printableName;
    }


    public ArrayList<CommentData> getComments() throws Exception {
        ArrayList<CommentData> result = new ArrayList<>();
        java.util.List<Comment> associatedDaosAsRange = getAssociatedDaosAsRange(Edge.CommentOfEvidence, Comment.class);
        for (Comment comment : associatedDaosAsRange) {
            String course = comment.getAssociatedDaoAsDomain(Edge.CommentOfCourse, CourseContext.class).printableName;
            CommentData commentData = new CommentData(comment.getDateCreated(), this.getId(), comment.getCreator().getPrintableName(), comment.getText(), course, comment.getCreator().getRole().toString());
            commentData.setText(comment.getText());
        }
        return result;
    }

    public ActivityEntry toActivityEntry() {
        return new ActivityEntry(printableName, null, null, getId());
    }
}
