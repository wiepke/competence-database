package uzuzjmd.competence.shared.assessment;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dehne on 28.09.2016.
 */
@XmlRootElement(name="ReflectiveQuestionAnswerHolder")
public class ReflectiveQuestionAnswerHolder {
    private java.util.List<ReflectiveQuestionAnswerData> data;

    public ReflectiveQuestionAnswerHolder() {
        data = new ArrayList<>();
    }

    public ReflectiveQuestionAnswerHolder(
            List<ReflectiveQuestionAnswerData> data) {
        this.data = data;
    }

    public void setData(List<ReflectiveQuestionAnswerData> data) {
        this.data = data;
    }
    
    @XmlElementWrapper(name="reflectiveAnswers")
    public List<ReflectiveQuestionAnswerData> getData() {
        return data;
    }
}
