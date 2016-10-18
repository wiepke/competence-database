package uzuzjmd.competence.persistence.dao;

import uzuzjmd.competence.persistence.ontology.Edge;

/**
 * Created by dehne on 04.10.2016.
 */
public class Badge extends DaoAbstractImpl {

    public String description;
    public String name;
    public String png;
    public Long issued;

    public Badge(String id) {
        super(id);
    }

    public Badge(String id, String description, String name, String png, Long issued) {
        super(id);
        this.description = description;
        this.name = name;
        this.png = png;
        this.issued = issued;
    }

    public void addUser(User user) throws Exception {
        createEdgeWith(Edge.BadgeOf, user);
    }
}
