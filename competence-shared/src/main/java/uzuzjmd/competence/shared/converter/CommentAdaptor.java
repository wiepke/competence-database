package uzuzjmd.competence.shared.converter;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uzuzjmd.competence.shared.activity.CommentData;
import uzuzjmd.competence.shared.activity.IComment;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by dehne on 26.09.2016.
 */
public class CommentAdaptor extends XmlAdapter<IComment, CommentData> {
    @Override
    public CommentData unmarshal(IComment iComment) throws Exception {
        return new CommentData(iComment.getCreatorId(),iComment.getText(), iComment.getDateCreated());
    }

    @Override
    public IComment marshal(CommentData commentData) throws Exception {
        throw new NotImplementedException()
                ;
    }
}
