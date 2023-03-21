package h3;

import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;

public class H3_RubricProvider implements RubricProvider {

    public static final Rubric RUBRIC = Rubric.builder()
        .title("H3")
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }
}
