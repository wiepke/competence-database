package uzuzjmd.competence.persistence.dao;

import uzuzjmd.competence.persistence.ontology.Contexts;

/**
 * Created by dehne on 15.01.2016.
 */
public class DBInitializer {

    private static Boolean initialized = false;
    public final static String COMPETENCEROOT = "Kompetenz";
    public final static String OPERATORROOT = "Verb";
    public final static String CATCHWORDROOT = "Stichwort";

    public static void init()  {
        if (!initialized) {
            try {
                if (!new Competence(COMPETENCEROOT).exists()) {
                        new Competence(COMPETENCEROOT).persist();
                        new Catchword(CATCHWORDROOT).persist();
                        new Operator(OPERATORROOT).persist();
                        new CourseContext(Contexts.university.toString(), "Alle", "Keine Beschreibung").persist();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            initialized = true;
        }
    }
}
