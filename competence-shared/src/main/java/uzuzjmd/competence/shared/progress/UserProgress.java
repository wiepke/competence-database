package uzuzjmd.competence.shared.progress;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dehne on 30.05.2016.
 */
@XmlRootElement(name = "UserProgress")
public class UserProgress {
    private List<UserCompetenceProgress> userCompetenceProgressList;

    public UserProgress() {
    }

    public UserProgress(List<UserCompetenceProgress> userCompetenceProgressList) {
        this.userCompetenceProgressList = userCompetenceProgressList;
    }

    public UserProgress(UserCompetenceProgress userCompetenceProgress) {
        userCompetenceProgressList = new ArrayList<>();
        userCompetenceProgressList.add(userCompetenceProgress);
    }

    @XmlElementWrapper(name = "userCompetenceProgressList")
    public List<UserCompetenceProgress> getUserCompetenceProgressList() {
        return userCompetenceProgressList;
    }

    public void setUserCompetenceProgressList(List<UserCompetenceProgress> userCompetenceProgressList) {
        this.userCompetenceProgressList = userCompetenceProgressList;
    }
}
