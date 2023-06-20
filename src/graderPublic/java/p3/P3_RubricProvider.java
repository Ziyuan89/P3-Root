package p3;

import org.sourcegrade.jagr.api.rubric.*;
import p3.graph.BasicGraphTests;

import java.util.Set;

public class P3_RubricProvider implements RubricProvider {

    private static final Criterion H1_1_1 = Criterion.builder()
        .shortDescription("")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() -> BasicGraphTests.class.getDeclaredMethod("testTwoNodes", Set.class, Set.class)))
            .pointsFailedMin()
            .pointsPassedMax()
            .build())
        .build();

    private static final Criterion H1_1 = Criterion.builder()
        .shortDescription("")
        .addChildCriteria(H1_1_1)
        .build();

    private static final Criterion H1 = Criterion.builder()
        .shortDescription("")
        .addChildCriteria(H1_1)
        .build();

    public static final Rubric RUBRIC = Rubric.builder()
        .title("P3")
        .addChildCriteria(H1)
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }
}
