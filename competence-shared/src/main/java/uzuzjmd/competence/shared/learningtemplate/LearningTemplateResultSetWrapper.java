package uzuzjmd.competence.shared.learningtemplate;

import javax.annotation.ManagedBean;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@ManagedBean
public class LearningTemplateResultSetWrapper {

	public LearningTemplateResultSetWrapper() {
		// TODO Auto-generated constructor stub
	}

	private LearningTemplateResultSet learningTemplateResultSet;

	public LearningTemplateResultSet getLearningTemplateResultSet() {
		return learningTemplateResultSet;
	}

	public void setLearningTemplateResultSet(
			LearningTemplateResultSet learningTemplateResultSet) {
		this.learningTemplateResultSet = learningTemplateResultSet;
	}

}
