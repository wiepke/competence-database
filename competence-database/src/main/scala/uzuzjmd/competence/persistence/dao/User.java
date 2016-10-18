package uzuzjmd.competence.persistence.dao;

import com.google.common.collect.Lists;
import uzuzjmd.competence.persistence.ontology.Edge;
import java.util.List;

/**
 * Created by dehne on 11.01.2016.
 */
public class User extends AbstractUser {
    public String role;
    public String printableName;
    public String lmsSystem;

    public User(String id) {
        super(id);
    }


    public User(String id, String role, String printableName, String lmsSystem, CourseContext ... courseContexts) {
        super(id);
        this.role = role;
        this.printableName = printableName;
        this.courseContexts = Lists.newArrayList(courseContexts);
        this.lmsSystem = lmsSystem;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return getId();
    }

    public List<LearningProjectTemplate> getAssociatedLearningProjectTemplates() throws Exception {
        return getAssociatedDaosAsDomain(Edge.UserOfLearningProjectTemplate, LearningProjectTemplate.class);
    }

    public List<String> getAssociatedLearningProjectTemplateIds() throws Exception {
        return getAssociatedDaoIdsAsDomain(Edge.UserOfLearningProjectTemplate);
    }

    public String getPrintableName() {
        return printableName;
    }

    @Override
    public String toString() {
        return this.getId();
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    public void addCompetencePerformed(Competence a) throws Exception {
        createEdgeWith(Edge.UserHasEvidencedAllSubCompetences, a);
    }


}
