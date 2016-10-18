package uzuzjmd.competence.shared.converter;

import uzuzjmd.competence.shared.assessment.AbstractAssessment;
import uzuzjmd.competence.shared.assessment.Assessment;
import uzuzjmd.competence.shared.assessment.IAssessment;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by dehne on 23.09.2016.
 */
public class SelfAssessmentAdapter extends XmlAdapter<IAssessment, AbstractAssessment> {


    public AbstractAssessment unmarshal(IAssessment selfAssessment) throws Exception {
        String identifier = selfAssessment.getTypeOfSelfAssessment().toString();
        Integer index = selfAssessment.getAssessmentIndex();
        String[] scale = new Assessment().getItems().toArray(new String[0]);
        Integer minValue = selfAssessment.getMinValue();
        Integer maxValue = selfAssessment.getMaxValue();
        return new AbstractAssessment(identifier, index, scale, minValue , maxValue, selfAssessment.getAssessmentId());
    }

    public IAssessment marshal(AbstractAssessment abstractSelfAssessment) throws Exception {
        return abstractSelfAssessment;
    }


}
