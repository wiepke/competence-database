package uzuzjmd.competence.persistence.dao;

import uzuzjmd.competence.persistence.ontology.Edge;

/**
 * Created by dehne on 21.09.2016.
 * <p/>
 * Solves https://github.com/uzuzjmd/COMPBASE/issues/88
 */
public class ReflectiveQuestion extends AbstractReflectiveQuestion {
    public String question;

    public ReflectiveQuestion(
            Competence competence, String question ) {
        super(question,competence);
        this.question = question;
    }

    public ReflectiveQuestion(String questionid) {
        super(questionid);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public Dao persist() throws Exception {
         super.persist();
         if (competence.exists()) {
            createEdgeWith(Edge.ReflectiveQuestionForCompetence, competence);
         } else {
            throw new Exception("competence does not exist in database");
         }
         return this;
    }
}
