package uzuzjmd.competence.shared.user;

import io.swagger.annotations.ApiModelProperty;
import uzuzjmd.competence.shared.course.CourseData;
import uzuzjmd.competence.shared.learningtemplate.LearningTemplateData;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dehne on 06.10.2016.
 */
@XmlRootElement(name = "UserOverview")
public class UserOverview {
    @ApiModelProperty(value = "the courses the user is enrolled in", required = true)
    private java.util.List<CourseData> courses;
    @ApiModelProperty(value = "the learning templates the user has selected to take interest in", required = true)
    private java.util.List<LearningTemplateData> learningTemplates;

    public UserOverview(
            List<CourseData> courses, List<LearningTemplateData> learningTemplates) {
        this.courses = courses;
        this.learningTemplates = learningTemplates;
    }

    public UserOverview() {
        courses = new ArrayList<>();
        learningTemplates = new ArrayList<>();
    }

    public void setCourses(List<CourseData> courses) {
        this.courses = courses;
    }

    @XmlElementWrapper(name = "courseList")
    public List<CourseData> getCourses() {
        return courses;
    }

    public void setLearningTemplates(
            List<LearningTemplateData> learningTemplates) {
        this.learningTemplates = learningTemplates;
    }

    @XmlElementWrapper(name = "learningTemplateList")
    public List<LearningTemplateData> getLearningTemplates() {
        return learningTemplates;
    }
}
