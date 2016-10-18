package uzuzjmd.competence.shared.moodle;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by dehne on 10.10.2016.
 */
@XmlRootElement(name = "SyncDataSet")
public class SyncDataSet {
    private String userName;
    private String password;
    private Boolean syncBadges;
    private Boolean syncCourses;
    private Boolean syncActivities;
    private Boolean syncUsers;

    public SyncDataSet() {
    }

    public SyncDataSet(
            String userName, String password, Boolean syncBadges, Boolean syncCourses, Boolean syncActivities,
            Boolean syncUsers) {
        this.userName = userName;
        this.password = password;
        this.syncBadges = syncBadges;
        this.syncCourses = syncCourses;
        this.syncActivities = syncActivities;
        this.syncUsers = syncUsers;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getSyncBadges() {
        return syncBadges;
    }

    public void setSyncBadges(Boolean syncBadges) {
        this.syncBadges = syncBadges;
    }

    public Boolean getSyncCourses() {
        return syncCourses;
    }

    public void setSyncCourses(Boolean syncCourses) {
        this.syncCourses = syncCourses;
    }

    public Boolean getSyncActivities() {
        return syncActivities;
    }

    public void setSyncActivities(Boolean syncActivities) {
        this.syncActivities = syncActivities;
    }

    public Boolean getSyncUsers() {
        return syncUsers;
    }

    public void setSyncUsers(Boolean syncUsers) {
        this.syncUsers = syncUsers;
    }


}
