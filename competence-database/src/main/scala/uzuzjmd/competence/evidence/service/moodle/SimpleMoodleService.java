package uzuzjmd.competence.evidence.service.moodle;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import config.MagicStrings;
import scala.collection.immutable.List;
import uzuzjmd.competence.shared.badges.BadgeData;
import uzuzjmd.competence.shared.badges.BadgeDataList;
import uzuzjmd.competence.shared.moodle.UserCourseListResponse;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.net.URLDecoder;

/**
 * DTOs für den Moodle REST-Service
 *
 * DTOs for the moodle rest service
 *
 * @author Julian Dehne
 */
public class SimpleMoodleService {

    private Logger logger = LogManager
            .getLogger(getClass());
    private Token mooodleStandardInterfaceToken;

    private Token competenceInterfaceToken;

    public SimpleMoodleService(String username,
                               String password) {
        //password = URLDecoder.decode(password).replaceAll(" ", "+").replaceAll("#","%23" );
        //username = URLDecoder.decode(username).replaceAll(" ", "+").replaceAll("#","%23" );

        mooodleStandardInterfaceToken = initToken(username,
                password, "moodle_mobile_app");
        competenceInterfaceToken = initToken(username,
                password, "upcompetence");
        if (mooodleStandardInterfaceToken == null) {
            logger.error("could not get moodle standard interface token");
        }
        if (competenceInterfaceToken == null) {
            logger.error("could not get moodle competence interface token");
        }

        //logger.info(MagicStrings.MOODLEURL);

    }

    private Token initToken(String username,
                            String userpassword, String serviceShortName) {
        String connectionPath = MagicStrings.MOODLEURL
                + "/login/token.php?username=" + username
                + "&password=" + userpassword + "&service="
                + serviceShortName;
        return sendRequest(connectionPath, Token.class);
    }

    private <T> T sendRequest(String url,
                              Class<T> responseTyp) {
        Client client = ClientBuilder.newClient();
        WebTarget webResource = client.target(url);
        T response = webResource.request(
                MediaType.APPLICATION_JSON)
                .get(responseTyp);
        logger.trace("gotten moodle query result: "
                + response.toString());
        return response;
    }

    public Boolean wasError() {
        if (!mooodleStandardInterfaceToken
                .containsKey("error")) {
            return false;
        } else
            return true;
    }

    public MoodleContentResponseList getMoodleContents(
            String courseId) {
        String moodleRestBase = getMoodleRestBase();
        String requestString = MagicStrings.MOODLEURL
                + moodleRestBase
                + "core_course_get_contents&courseid="
                + courseId;

        logger.info("getting moodle content with url: "
                + requestString);
        return sendRequest(requestString,
                MoodleContentResponseList.class);
    }

    public UserCourseListResponse getMoodleCourseList() {
        String moodleRestBase = getMoodleCompetenceRestBase();
        String requestString = MagicStrings.MOODLEURL
                + moodleRestBase
                + "local_upcompetence_get_courses_for_user";
        logger.info("getting moodle courselist with url: "
                + requestString);
        UserCourseListResponse result = sendRequest(requestString,
                UserCourseListResponse.class);

        return result;
    }

    private String getMoodleRestBase() {
        String moodleRestBase = "/webservice/rest/server.php?moodlewsrestformat=json&wstoken="
                + mooodleStandardInterfaceToken
                .get("token") + "&wsfunction=";
        return moodleRestBase;
    }

    private String getMoodleCompetenceRestBase() {
        String moodleRestBase = "/webservice/rest/server.php?moodlewsrestformat=json&wstoken="
                + competenceInterfaceToken.get("token")
                + "&wsfunction=";
        return moodleRestBase;
    }

    public BadgeDataList getMoodleBadges(String userId) {
        String requestString = MagicStrings.MOODLEURL
                + getMoodleCompetenceRestBase()
                + "local_upcompetence_get_badges_for_user&username="
                + userId;
        return sendRequest(requestString, BadgeDataList.class);
    }

    public MoodleEvidenceList getMoodleEvidenceList(
            String courseId) {
        if (courseId == null
                || courseId.equals("undefined")) {
            String message = "courseId is null or undefined when getting Moodle evidences for courseId: "
                    + courseId;
            logger.error(message);
            throw new WebApplicationException(
                    new Exception(message));
        }

        String moodleRestBase = getMoodleCompetenceRestBase();
        String requestString = MagicStrings.MOODLEURL
                + moodleRestBase
                + "local_upcompetence_get_evidences_for_course&courseId="
                + courseId;
        logger.info("getting moodle evidences with url: "
                + requestString);
        return sendRequest(requestString,
                MoodleEvidenceList.class);
    }

}
