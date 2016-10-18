package uzuzjmd.competence.shared.learningtemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "SuggestedCompetenceRow", eager = true)
@ViewScoped
public class SuggestedCompetenceRow implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManagedProperty("#{suggestedCompetenceColumns}")
	private List<SuggestedCompetenceColumn> suggestedCompetenceColumns;

	@ManagedProperty("#{suggestedCompetenceRowHeader}")
	private String suggestedCompetenceRowHeader;

	public SuggestedCompetenceRow() {
		suggestedCompetenceColumns = new ArrayList<SuggestedCompetenceColumn>();
	}

	public List<SuggestedCompetenceColumn> getSuggestedCompetenceColumns() {
		return suggestedCompetenceColumns;
	}

	public void setSuggestedCompetenceColumns(
			List<SuggestedCompetenceColumn> suggestedCompetenceColumns) {
		this.suggestedCompetenceColumns = suggestedCompetenceColumns;
	}

	public String getSuggestedCompetenceRowHeader() {
		return suggestedCompetenceRowHeader;
	}

	public void setSuggestedCompetenceRowHeader(
			String suggestedCompetenceRowHeader) {
		this.suggestedCompetenceRowHeader = suggestedCompetenceRowHeader;
	}

	@PostConstruct
	public void init() {
		if (suggestedCompetenceColumns == null) {
			suggestedCompetenceColumns = new ArrayList<SuggestedCompetenceColumn>();
		}
		if (suggestedCompetenceRowHeader == null) {
			suggestedCompetenceRowHeader = "";
		}
	}

	@Override
	public String toString() {
		return "Rowheader: "
				+ getSuggestedCompetenceRowHeader()
				+ " row: "
				+ getSuggestedCompetenceColumns()
						.toString();
	}
}
