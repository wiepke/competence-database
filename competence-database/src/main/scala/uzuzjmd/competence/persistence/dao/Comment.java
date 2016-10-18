package uzuzjmd.competence.persistence.dao;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uzuzjmd.competence.exceptions.NoUserGivenException;
import uzuzjmd.competence.persistence.ontology.Edge;
import uzuzjmd.competence.shared.activity.CommentData;
import uzuzjmd.competence.shared.activity.IComment;

/**
 * Created by dehne on 11.01.2016.
 */
public class Comment extends AbstractComment implements Cascadable, IComment {

    public String dateCreated;
    public String text;

    public Comment(String id) {
        super(id);
    }

    public Comment(String text, User creator, Long dateCreated) {
        super(dateCreated + text);
        this.text = text;
        this.creator = creator;
        this.dateCreated = dateCreated+"";
    }

    @Override
    public Dao persist() throws Exception {
        super.persist();
        this.createEdgeWith(creator, Edge.UserOfComment);
        return this;
    }

    public Long getDateCreated() {
        return Long.parseLong(dateCreated);
    }

    @Override
    public String getEvidenceId() {
        throw new NotImplementedException();
    }

    public void setDateCreated(Long dateCreated) {
        this.dateCreated = dateCreated+"";
    }

    @Override
    public String getCreatorId() {
        return this.getCreator().getId();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public CommentData getData(){
        CommentData commentData = new CommentData(null, this.getText(), null, null, null, null);
        return commentData;
    }

    @Override
    public void persistMore() throws Exception {
        this.persist();
        if (creator == null) {
            throw new NoUserGivenException();
        }
        createEdgeWith(creator, Edge.UserOfComment);
    }

    @Override
    public User getCreator() {
        try {
            return this.getAssociatedDaoAsRange(Edge.UserOfComment, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        } return null;
    }
}
