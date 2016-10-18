package uzuzjmd.competence.api;

import uzuzjmd.competence.shared.course.CourseData;
import uzuzjmd.competence.shared.moodle.UserTree;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by dehne on 15.04.2016.
 */

public interface CourseApi {


    /**
     * Get all courses that are linked to the given competences.
     * @param competences
     * @return
     */

    List<CourseData> getCourses(@QueryParam("competence") List<String> competences) throws Exception;

    /**
     * Add a courseId to the persistence layer with data as payload and the courseId as param.
     * @param courseId
     * @param data
     * @return
     * @throws Exception
     */

    Response addCourse(@PathParam("courseId") String courseId, CourseData data) throws Exception;

    /**
     * Delete the courseId with the given ID from the database using DELETE
     * @param courseId
     * @return
     * @throws Exception
     */

    Response deleteCourse(@PathParam("courseId") String courseId) throws Exception;



    /**
     * get all the activities from a given courseId in the form of a usertree
     * @param courseId
     * @param userId
     * @param password
     * @return
     */

    UserTree[] getActivitiesForUser(@PathParam("courseId") String courseId, @QueryParam("userId") String userId, @QueryParam("password") String password);
}
