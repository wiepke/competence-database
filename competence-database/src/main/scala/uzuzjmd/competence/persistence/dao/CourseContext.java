package uzuzjmd.competence.persistence.dao;

import uzuzjmd.competence.persistence.ontology.Edge;
import uzuzjmd.competence.persistence.ontology.Contexts;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dehne on 11.01.2016.
 */
public class CourseContext extends DaoAbstractImpl {

    public String printableName;
    public String requirement;


    public CourseContext(String id) {
        super(id);
        this. requirement = "";
    }

    public CourseContext(String id, String printableName) {
        super(id);
        this.requirement = "";
        this.printableName = printableName;
    }

    public CourseContext(String id, String printableName, String requirement) {
        super(id);
        this.printableName = printableName;
        this.requirement = requirement;
    }

    public CourseContext(Contexts context) {
        super(context.name());
    }

    public List<Competence> getLinkedCompetences() throws Exception {
        List<Competence> result = getAssociatedDaosAsDomain(Edge.CourseContextOfCompetence, Competence.class);
        if (result == null) {
            return new LinkedList<>();
        }
        return result;
    }


    public List<User> getLinkedUser() throws Exception {
        return getAssociatedDaosAsDomain(Edge.CourseContextOfUser, User.class);
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    @Override
    public CourseContext getFullDao() throws Exception {
        return super.getFullDao();
    }

    @Override
    public Dao persist() throws Exception {
        if (exists()) {
            return this;
        }
        if (printableName == null) {
            logger.warn("no printable Name for course given - better explicitly persist course first before using api that involes this course; setting id as name");
            printableName = getId();
        }
        return super.persist();
    }
/*
    public String getPrintableName() {
        return this.printableName;
    }*/

    @Override
    public String toString() {
        return super.toString();
    }
}
