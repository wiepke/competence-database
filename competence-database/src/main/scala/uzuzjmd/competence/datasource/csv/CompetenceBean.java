package uzuzjmd.competence.datasource.csv;

import javax.xml.bind.annotation.XmlRootElement;

/*
 * Diese Klasse mappt eine Zeile in der Excel Tabelle 
 * Dies muss ein Bean sein, weil das von dem Framework gefordert wird
 *
 * This class maps one row in a excel table.
 */
@XmlRootElement
public class CompetenceBean {
	private String learner;
	private String operator;
	private String catchword;
	private String competence;
	private String similarCompetence;
	private String superCompetence;
	private String reference;
	private String evidenzen;
	private String competenceArea;
	private String metaoperator;

	public CompetenceBean() {
		// TODO Auto-generated constructor stub
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getCatchword() {
		return catchword;
	}

	public void setCatchword(String catchword) {
		this.catchword = catchword;
	}

	public String getCompetence() {
		return competence;
	}

	public void setCompetence(String competence) {
		this.competence = competence;
	}

	public String getSimilarCompetence() {
		return similarCompetence;
	}

	public void setSimilarCompetence(String similarCompetence) {
		this.similarCompetence = similarCompetence;
	}

	public String getSuperCompetence() {
		return superCompetence;
	}

	public void setSuperCompetence(String superCompetence) {
		this.superCompetence = superCompetence;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getEvidenzen() {
		return evidenzen;
	}

	public void setEvidenzen(String evidenzen) {
		this.evidenzen = evidenzen;
	}

	public String getCompetenceArea() {
		return competenceArea;
	}

	public void setCompetenceArea(String competenceArea) {
		this.competenceArea = competenceArea;
	}

	public String getMetaoperator() {
		return metaoperator;
	}

	public void setMetaoperator(String metaoperator) {
		this.metaoperator = metaoperator;
	}

	public String getLearner() {
		return learner;
	}

	public void setLearner(String learner) {
		this.learner = learner;
	}
}
