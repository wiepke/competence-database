package uzuzjmd.competence.persistence.ontology;

/**
 * This enum specifies the class concepts of the competence ontology
 *
 * The classes are divided in 2 groups.
 * SingletonClasses are mapped to exactly one Individual and represent a class hierarchy in the sense of
 * a taxonomy.
 *
 * Normal classes function like classes in object oriented programming. They have many instances.
 * For instance courses:
 *
 * n:Course {id:4}
 * n2:Course {id:3}
 * etc...
 *
 *
 * Important SingletonClasses are:
 *
 * Competence
 * Operator
 * Catchword
 *
 * in the graph they are represented like
 *
 * c' {isClass:false} individualOf (c'Class {isClass:true} : Competence)
 * d' {isClass:false} individualOf (d'Class {isClass:true} : Competence)
 *
 * c' subClassOf d'
 *
 * The id and the definition of both c' and c' Class should be identical if set.
 * It is sufficient to set it on one of them and the other should be created correspondingly.
 *
 * Other edges are always linked to c' and NOT to c'Class.
 *
 * This setup is necessary to ensure compatibility with rdf/owl.
 *
 *
 */
public enum Label {
    MetaCatchword,
    Competence,
    Evidence,
    Operator, Catchword,
    CompetenceDescription,
    DescriptionElement,
    CompetenceArea,
    Learner,
    CompetenceSpec,
    SubOperator,
    MetaOperator,
    CourseContext,
    AbstractEvidenceLink,
    Comment,
    EvidenceActivity,
    Role,
    StudentRole,
    TeacherRole,
    User,
    MetaCompetence,
    LearningProjectTemplate,
    SelectedLearningProjectTemplate,
    SelfAssessment,
    Individual,
    ReflectiveQuestion,
    ReflectiveQuestionAnswer,
    Badge
}
