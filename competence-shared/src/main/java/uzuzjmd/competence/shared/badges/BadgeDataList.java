package uzuzjmd.competence.shared.badges;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by dehne on 04.10.2016.
 */
@XmlRootElement(name="BadgeDataList")
public class BadgeDataList extends ArrayList<BadgeData> implements Serializable {

}
