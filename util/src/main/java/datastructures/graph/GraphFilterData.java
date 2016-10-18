package datastructures.graph;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dehne on 18.12.2015.
 */
public class GraphFilterData {
    private List<String> competencesSelected;
    private String course;


    public GraphFilterData(String course, String ... competencesSelected) {
        this.competencesSelected = Arrays.asList(competencesSelected);
        this.course = course;
    }

    public List<String> getCompetencesSelected() {
        return competencesSelected;
    }

    public void setCompetencesSelected(List<String> competencesSelected) {
        this.competencesSelected = competencesSelected;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
