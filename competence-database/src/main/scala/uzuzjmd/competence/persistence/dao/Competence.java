package uzuzjmd.competence.persistence.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import uzuzjmd.competence.persistence.ontology.Contexts;
import uzuzjmd.competence.persistence.ontology.Edge;
import uzuzjmd.competence.shared.assessment.TypeOfSelfAssessment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dehne on 11.01.2016.
 */
public class Competence extends AbstractCompetence implements HasDefinition, TreeLike {

    public Boolean compulsory;



    public Competence(String id) {
        super(id);
        compulsory = false;
    }

    public Competence(String id, Boolean compulsory) {
        super(id);
        this.compulsory = compulsory;
    }

    public Competence(String id, LearningProjectTemplate learningProject) {
        super(id);
        this.compulsory = false;
        this.learningProject = learningProject;
    }

    public Competence(String id, LearningProjectTemplate learningProject, List<Catchword> catchwords) {
        super(id);
        this.compulsory = false;
        this.learningProject = learningProject;
        this.catchwordList = catchwords;
    }

    public Competence(String id, List<Catchword> catchwords) {
        super(id);
        this.compulsory = false;
        this.catchwordList = catchwords;
    }

    @Override
    public String getDefinition() {
        return this.getId();
    }

    public void addSuggestedCompetenceRequirement(Competence competence) throws Exception {
        createEdgeWith(competence, Edge.SuggestedCompetencePrerequisiteOf);
    }

    public HashSet<Competence> getSuggestedCompetenceRequirements() throws Exception {
        if (learningProject != null) {
            HashSet<Competence> result = queryManager.getSuggestedCompetenceRequirements(this.getId(), Competence.class, learningProject);
            return result;
        } else {
            return Sets.newHashSet(getAssociatedDaosAsRange(Edge.SuggestedCompetencePrerequisiteOf, Competence.class));
        }
    }

    public void addSimilarCompetence(Competence competence, Double score) throws Exception {
        queryManager.createRelationShipWithWeight(competence.getId(), Edge.SimilarTo, this.getId(), score, Competence
                .class, Competence.class);
    }

    public void addRequiredCompetence(Competence competence) throws Exception {
        deleteEdgeWith(competence, Edge.NotPrerequisiteOf);
        createEdgeWith(competence, Edge.PrerequisiteOf);
        createEdgeWith(competence, Edge.SuggestedCompetencePrerequisiteOf);
    }

    public void addNotRequiredCompetence(Competence competence) throws Exception {
        deleteEdgeWith(competence, Edge.PrerequisiteOf);
        createEdgeWith(competence, Edge.NotPrerequisiteOf);
    }

    public List<Catchword> getCatchwords() throws Exception {
        if (!this.catchwordList.isEmpty()) {
            return this.catchwordList;
        }
        return getAssociatedDaosAsRange(Edge.CatchwordOf, Catchword.class);
    }

    public List<Competence> getRequiredCompetences() throws Exception {
        return getAssociatedDaosAsRange(Edge.PrerequisiteOf, Competence.class);
    }


    public String[] getRequiredCompetencesAsArray() throws Exception {
        List<Competence> requiredCompetences = getRequiredCompetences();
        String[] result = new String[requiredCompetences.size()];
        int i = 0;
        for (Competence requiredCompetence : requiredCompetences) {
            result[i] = requiredCompetence.getDefinition();
            i++;
        }
        return result;
    }

    public Boolean isLinkedAsRequired() throws Exception {
        return !(getRequiredCompetences().isEmpty());
    }

    public Boolean isLinkedAsSuggestedRequired() throws Exception {
        return !(getSuggestedCompetenceRequirements().isEmpty());
    }

    public Boolean isAllowed(User user) throws Exception {
        List<Competence> prerequisites = getAssociatedDaosAsDomain(Edge.PrerequisiteOf, Competence.class);
        Boolean result = true;
        for (Competence prerequisite : prerequisites) {
            result = result && prerequisite.hasEdge(user, Edge.UserHasEvidencedAllSubCompetences);
        }
        return result;
    }


    public void addSuperCompetence(Competence superCompetence) throws Exception {
        createEdgeWith(Edge.subClassOf, superCompetence);
    }

    public void removeSuperCompetence(Competence superCompetence) throws Exception {
        deleteEdgeWith(superCompetence, Edge.subClassOf);
    }


    @Override
    public String toString() {
        return this.getId();
    }

    public SelfAssessment getAssessment(User user, TypeOfSelfAssessment typeOfSelfAssessment) throws Exception {
      return queryManager.getSelfAssessment(this, user, typeOfSelfAssessment);
    }

    public List<Competence> getShortestPathToSubCompetence(Competence subCompetence) throws Exception {
        return queryManager.getShortestSubClassPath(subCompetence.getId(), this.getId(), Competence.class);
    }

    public void addCatchword(Catchword dao) throws Exception {
        dao.persist();
        dao.createEdgeWith(Edge.CatchwordOf, this);
    }

    public void addLearningTemplate(LearningProjectTemplate learningTemplate) throws Exception {
        if (!learningTemplate.exists()) {
            learningTemplate.persist();
        }
        createEdgeWith(learningTemplate, Edge.LearningProjectTemplateOf);
    }

    public List<Operator> getOperators() throws Exception {
        return getAssociatedDaosAsDomain(Edge.OperatorOf, Operator.class);
    }

    public void addCourseContext(CourseContext course) throws Exception {
        createEdgeWith(course, Edge.CourseContextOfCompetence);
        addSuperCompetencesToCourse(this, course);
    }

    public void addSuperCompetencesToCourse(Competence competence, CourseContext course) throws Exception {
        Set<Competence> superCompetences = competence.listSuperClasses(Competence.class);
        for (Competence superCompetence : superCompetences) {
            superCompetence.createEdgeWith(course, Edge.CourseContextOfCompetence);
        }
    }

    public Boolean isSuperClassOf(Competence subCompetence) {
        try {
            return listSubClasses(this.getClass()).contains(subCompetence);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean isSubClassOf(Competence superCompetence) throws Exception {
        Set<? extends Competence> result = listSuperClasses(this.getClass());
        try {
            return result.contains(superCompetence);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Competence> getFollowingCompetences() throws Exception {
        return getAssociatedDaosAsRange(Edge.SuggestedCompetencePrerequisiteOf, Competence.class);
    }

    public List<Competence> getShortestSuggestedLearningPath(Competence competenceToReach) throws Exception {
        if (this.getId().equals(competenceToReach.getId())) {
            throw new Exception("cannot find path to itself");
        }
        return Lists.newArrayList(queryManager.getShortestPath(this.getId(), competenceToReach.getId(), Edge.SuggestedCompetencePrerequisiteOf, this.getClass()));
    }

    @Override
    public void deleteTree() throws Exception {
        Set<Competence> subClasses = listSubClasses();
        for (Competence subClass : subClasses) {
            if (!subClass.equals(this)) {
                subClass.deleteTree();
            }
        }
        this.delete();
    }

    @Override
    public Dao persist() throws Exception {
        super.persist();
        logger.info("saving competence");
        createEdgeWith(Edge.subClassOf, new Competence(DBInitializer.COMPETENCEROOT));
        CourseContext universityContext = new CourseContext(Contexts.university);
        createEdgeWith(universityContext, Edge.CourseContextOfCompetence);
        if (learningProject != null) {
            createEdgeWith(learningProject, Edge.LearningProjectTemplateOf);
        }
        if (!catchwordList.isEmpty()) {
            for (Catchword catchword : catchwordList) {
                addCatchword(catchword);
            }
        }

        // TODO add if needed
       /* final Competence competence = this;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("updating similarities 1");
                SimilaritiesUpdater.updateSimilarCompetencies(competence);
            }
        });
        t.start();*/
        return this;
    }

    public ArrayList<String> getSimilarCompetences() throws Exception {
        return queryManager.getClosestEdges(this.getId(), Edge.SimilarTo);
    }


    public Set<Competence> listSubClasses() throws Exception {
        return (HashSet<Competence>) super.listSubClasses(getClass());
    }

    public void hideFor(User user) throws Exception {
        createEdgeWith(Edge.HiddenFor, user);
    }
}
