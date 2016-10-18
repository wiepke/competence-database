package uzuzjmd.competence.persistence.dao;

import uzuzjmd.competence.persistence.ontology.Edge;
import java.util.List;

/**
 * Created by dehne on 11.01.2016.
 */
public class LearningProjectTemplate extends AbstractLearningProjectTemplate implements HasDefinition, Cascadable{

    public LearningProjectTemplate(String id) {
        super(id);
    }

    public LearningProjectTemplate(String id, List<Competence> associatedCompetences) {
        super(id);
        this.associatedCompetences = associatedCompetences;
    }

    /**
     * Definition here is the same as the readable Name
     * @return
     */
    @Override
    public String getDefinition() {
        return this.getId();
    }

    @Override
    public void persistMore() throws Exception {
        this.persist();
        if (associatedCompetences != null) {
            for (Competence associatedCompetence : associatedCompetences) {
                addCompetenceToProject(associatedCompetence);
            }
        }
    }


    public List<Competence> getAssociatedCompetences() throws Exception {
        List<Competence> competences = getAssociatedDaosAsDomain(Edge.LearningProjectTemplateOf, Competence.class);
        for (Competence competence : competences) {
            competence.learningProject = this;
        }
        return competences;
    }


    public void addCompetenceToProject(Competence competence) throws Exception {
        createEdgeWith(Edge.LearningProjectTemplateOf, competence);
    }

    @Override
    public String toString() {
        return this.getId();
    }
}
