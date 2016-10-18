package uzuzjmd.competence.shared.activity;

/**
 * Created by dehne on 26.09.2016.
 */
public interface IComment {
    String getCreatorId();
    String getText();
    Long getDateCreated();
    String getId();
    String getEvidenceId();
}
