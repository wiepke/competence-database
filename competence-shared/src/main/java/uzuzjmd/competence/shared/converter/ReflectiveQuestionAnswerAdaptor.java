package uzuzjmd.competence.shared.converter;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uzuzjmd.competence.shared.assessment.IReflectiveQuestionAnswerData;
import uzuzjmd.competence.shared.assessment.ReflectiveQuestionAnswerData;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by dehne on 28.09.2016.
 */
public class ReflectiveQuestionAnswerAdaptor extends XmlAdapter<IReflectiveQuestionAnswerData, ReflectiveQuestionAnswerData> {
    @Override
    public ReflectiveQuestionAnswerData unmarshal(
            IReflectiveQuestionAnswerData iReflectiveQuestionAnswerData) throws Exception {

        String text = iReflectiveQuestionAnswerData.getText();
        String userId = iReflectiveQuestionAnswerData.getUserId();
        String questionId = iReflectiveQuestionAnswerData.getQuestionId();
        return new ReflectiveQuestionAnswerData(text,userId
                , questionId, null);
    }

    @Override
    public IReflectiveQuestionAnswerData marshal(
            ReflectiveQuestionAnswerData reflectiveQuestionAnswerData) throws Exception {
        throw new NotImplementedException();
    }
}
