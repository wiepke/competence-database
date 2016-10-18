package uzuzjmd.competence.shared.learningtemplate;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@ManagedBean(name = "SuggestedCompetenceGrid")
@ViewScoped
public class SuggestedCompetenceGrid implements
        Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{suggestedCompetenceRows}")
    private List<SuggestedCompetenceRow> suggestedCompetenceRows;

    public SuggestedCompetenceGrid() {
        suggestedCompetenceRows = new ArrayList<SuggestedCompetenceRow>();
    }

    public List<SuggestedCompetenceRow> getSuggestedCompetenceRows() {
        return suggestedCompetenceRows;
    }

    public void setSuggestedCompetenceRows(
            List<SuggestedCompetenceRow> suggestedCompetenceRows) {
        this.suggestedCompetenceRows = suggestedCompetenceRows;
    }

    @PostConstruct
    public void init() {
        if (suggestedCompetenceRows == null) {
            suggestedCompetenceRows = new ArrayList<SuggestedCompetenceRow>();
        }
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String result = "";

        for (SuggestedCompetenceRow suggestedCompetenceRow : suggestedCompetenceRows) {
            result += suggestedCompetenceRow.toString()
                    + "\n";
        }

        return result;
    }

}
