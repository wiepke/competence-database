package uzuzjmd.competence.persistence.ontology;

/**
 *
 */
public enum Edge {
    /*operator(the verb of the competences) */
    OperatorOf,
    /*a catchword is a tag of acompetence*/
    CatchwordOf,
    /*The courseId in the lms the competence belongs to (works with single lms so far)*/
    CourseContextOfCompetence,
    /*the comment of a evidence*/
    CommentOfEvidence,
    /* the comment of a competence*/
    CommentOfCompetence,
    /* the comment of a courseId*/
    CommentOfCourse,
    /* the evidence link of the courseId context*/
    LinkOfCourseContext,
    /*The evidence relates to the user*/
    UserOfLink,
    /*the activity of an evidence link*/
    ActivityOf,
    /*links the evidence link the creator*/
    LinkCreatedByUser,
    /*Links the User with the comment he created*/
    UserOfComment,
    /* LInks the competence with the evidence link*/
    linksCompetence,
    /*a competence can be the requirement for another (in a learnpath)*/
    PrerequisiteOf,
    /*opposite of the above*/
    NotPrerequisiteOf,
    /*a competence can be suggested to be prerequisite of*/
    SuggestedCompetencePrerequisiteOf,
    /*competences can be grouped by catchword and context to a learningproject*/
    LearningProjectTemplateOf,
    /*User may decide to adopt a learning project as his own (or be assigned)*/
    UserOfLearningProjectTemplate,
    /*assessment has a user and a competence*/
    AssessmentOfUser,
    AssessmentOfCompetence,
    /* user has performed (fully) a competence*/
    UserHasEvidencedAllSubCompetences,
    /*activities may be suggested for acompetence*/
    SuggestedActivityForCompetence,
    /*competence may be a subclass of another */
    subClassOf,
    /* user may belong to a course context Context*/
    CourseContextOfUser,
    /* 2 competences may be similar based on a weight */
    SimilarTo,
    /* hide competence for User*/
    HiddenFor,
    /*The user is interested to acquire the competence*/
    InterestedIn,
    /*the user that the reflective question */
    ReflectiveQuestionForCompetence,
    UserOfReflectiveQuestionAnswer, BadgeOf, AnswerForReflectiveQuestion
}
