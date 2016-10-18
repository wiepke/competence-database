package uzuzjmd.competence.persistence.dao;

import uzuzjmd.competence.persistence.ontology.Edge;

/**
 * Created by dehne on 11.01.2016.
 */
public class Operator extends DaoAbstractImpl implements HasDefinition {
    public Operator(String id) {
        super(id);
    }

    @Override
    public String getDefinition() {
        return this.getId();
    }

    @Override
    public Dao persist() throws Exception {
        super.persist();
        createEdgeWith(Edge.subClassOf, new Operator(DBInitializer.OPERATORROOT));
        return this;
    }
}
