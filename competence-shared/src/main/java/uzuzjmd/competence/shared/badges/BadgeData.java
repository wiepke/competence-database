package uzuzjmd.competence.shared.badges;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by dehne on 04.10.2016.
 */
@XmlRootElement(name = "BadgeData")
public class BadgeData {

    private Integer badgeId;
    private String png;
    private String description;
    private String name;
    private Long issued;

    public BadgeData() {
    }

    public Integer getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(Integer badgeId) {
        this.badgeId = badgeId;
    }

    public String getPng() {
        return png;
    }

    public void setPng(String png) {
        this.png = png;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIssued() {
        return issued;
    }

    public void setIssued(Long issued) {
        this.issued = issued;
    }
}
