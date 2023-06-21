package p3;

import org.sourcegrade.jagr.api.rubric.*;
import p3.graph.BasicGraphTests;

import java.util.Set;

public class P3_RubricProvider implements RubricProvider {

    private static final Criterion H1_1_1 = makeCriterion(
        "Der Konstruktor von [[[BasicGraph]]] funktioniert korrekt für Graphen mit 2 Knoten.",
        JUnitTestRef.ofMethod(() -> BasicGraphTests.class.getDeclaredMethod("testTwoNodes", Set.class, Set.class))
    );
    private static final Criterion H1_1_2 = makeCriterion(
        "Der Konstruktor von [[[BasicGraph]]] funktioniert korrekt für Graphen mit 3 Knoten.",
        JUnitTestRef.ofMethod(() -> BasicGraphTests.class.getDeclaredMethod("testThreeNodes", Set.class, Set.class))
    );
    private static final Criterion H1_1_3 = makeCriterion(
        "Der Konstruktor von [[[BasicGraph]]] funktioniert korrekt für Graphen mit mehr als 3 Knoten.",
        JUnitTestRef.ofMethod(() -> BasicGraphTests.class.getDeclaredMethod("testMultipleNodes", Set.class, Set.class))
    );
    private static final Criterion H1_1 = Criterion.builder()
        .shortDescription("BasicGraph Konstruktor")
        .addChildCriteria(H1_1_1, H1_1_2, H1_1_3)
        .build();

    private static final Criterion H1_2_1 = makeUngradedCriterion(
        "[[[addEdge]]] funktioniert korrekt."
    );
    private static final Criterion H1_2_2 = makeUngradedCriterion(
        "[[[getWeight]]] funktioniert korrekt."
    );
    private static final Criterion H1_2_3 = makeUngradedCriterion(
        "[[[getAdjacent]]] funktioniert korrekt."
    );
    private static final Criterion H1_2_4 = makeUngradedCriterion(
        "[[[getAdjacentEdges]]] funktioniert korrekt, wenn alle Kanten mit mehr als 0 gewichtet sind."
    );
    private static final Criterion H1_2_5 = makeUngradedCriterion(
        "[[[getAdjacentEdges]]] funktioniert vollständig korrekt."
    );
    private static final Criterion H1_2_6 = makeUngradedCriterion(
        "[[[maps]]] wird im Konstruktor von [[[AdjacencyGraph]]] korrekt gesetzt."
    );
    private static final Criterion H1_2_7 = makeUngradedCriterion(
        "[[[edges]]] wird im Konstruktor von [[[AdjacencyGraph]]] korrekt gesetzt."
    );
    private static final Criterion H1_2 = Criterion.builder()
        .shortDescription("AdjacencyMatrix und AdjacencyGraph")
        .addChildCriteria(H1_2_1, H1_2_2, H1_2_3, H1_2_4, H1_2_5, H1_2_6, H1_2_7)
        .build();

    private static final Criterion H1 = Criterion.builder()
        .shortDescription("Datenstrukturen für Graphen")
        .addChildCriteria(H1_1, H1_2)
        .build();

    private static final Criterion H2_1 = makeUngradedCriterion(
        "[[[Edge.compareTo]]] funktioniert korrekt."
    );
    private static final Criterion H2_2 = makeUngradedCriterion(
        "[[[init]]] funktioniert korrekt."
    );
    private static final Criterion H2_3 = makeUngradedCriterion(
        "Alle Gruppen beinhalten die korrekten Werte nach Aufruf von [[[joinGroups]]]."
    );
    private static final Criterion H2_4 = makeUngradedCriterion(
        "[[[joinGroups]]] fügt die Werte in die größere der beiden Mengen ein."
    );
    private static final Criterion H2_5 = makeUngradedCriterion(
        "[[[acceptEdge]]] funktioniert korrekt, wenn beide Knoten in einer Menge sind."
    );
    private static final Criterion H2_6 = makeUngradedCriterion(
        "[[[acceptEdge]]] funktioniert korrekt, wenn beide Knoten in unterschiedlichen Mengen sind."
    );
    private static final Criterion H2_7 = makeUngradedCriterion(
        "[[[acceptEdge]]] ruft [[[joinGroups]]] an der richtigen Stelle auf."
    );
    private static final Criterion H2_8 = makeUngradedCriterion(
        "[[[acceptEdge]]] gibt die korrekten Werte zurück."
    );
    private static final Criterion H2_9 = makeUngradedCriterion(
        "[[[calculateMST]]] ruft [[[acceptEdge]]] mit allen Kanten auf."
    );
    private static final Criterion H2_10 = makeUngradedCriterion(
        "[[[calculateMST]]] funktioniert vollständig korrekt."
    );
    private static final Criterion H2 = Criterion.builder()
        .shortDescription("Kruskal")
        .addChildCriteria(H2_1, H2_2, H2_3, H2_4, H2_5, H2_6, H2_7, H2_8, H2_9, H2_10)
        .build();

    private static final Criterion H3_1 = makeUngradedCriterion(
        "Methode [[[init]]] funktioniert vollständig korrekt."
    );
    private static final Criterion H3_2 = makeUngradedCriterion(
        "[[[extractMin]]] funktioniert korrekt, wenn alle Knoten in [[[remainingNodes]]] sind."
    );
    private static final Criterion H3_3 = makeUngradedCriterion(
        "[[[extractMin]]] funktioniert vollständig korrekt."
    );
    private static final Criterion H3_4 = makeUngradedCriterion(
        "[[[relax]]] funktioniert korrekt."
    );
    private static final Criterion H3_5 = makeUngradedCriterion(
        "[[[reconstructPath]]] funktioniert vollständig korrekt.", 0, 2
    );
    private static final Criterion H3_6 = makeUngradedCriterion(
        "[[[calculatePath]]] funktioniert korrekt für Graphen mit 2 Knoten."
    );
    private static final Criterion H3_7 = makeUngradedCriterion(
        "[[[calculatePath]]] funktioniert korrekt für Graphen mit 3 Knoten."
    );
    private static final Criterion H3_8 = makeUngradedCriterion(
        "[[[calculatePath]]] funktioniert korrekt für Graphen mit mehr als 3 Knoten."
    );
    private static final Criterion H3 = Criterion.builder()
        .shortDescription("Dijkstra")
        .addChildCriteria(H3_1, H3_2, H3_3, H3_4, H3_5, H3_6, H3_7, H3_8)
        .build();

    public static final Rubric RUBRIC = Rubric.builder()
        .title("P3")
        .addChildCriteria(H1, H2, H3)
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }

    private static Criterion makeCriterion(String description, JUnitTestRef... jUnitTestRefs) {
        return makeCriterion(description, 0, 1, jUnitTestRefs);
    }

    private static Criterion makeCriterion(String description, int minPoints, int maxPoints, JUnitTestRef... jUnitTestRefs) {
        Grader.TestAwareBuilder graderBuilder = Grader.testAwareBuilder();
        for (JUnitTestRef jUnitTestRef : jUnitTestRefs) {
            graderBuilder.requirePass(jUnitTestRef);
        }

        return Criterion.builder()
            .shortDescription(description)
            .minPoints(minPoints)
            .maxPoints(maxPoints)
            .grader(graderBuilder
                .pointsFailedMin()
                .pointsPassedMax()
                .build())
            .build();
    }

    private static Criterion makeUngradedCriterion(String description) {
        return makeUngradedCriterion(description, 0, 1);
    }

    private static Criterion makeUngradedCriterion(String description, int minPoints, int maxPoints) {
        return Criterion.builder()
            .shortDescription(description)
            .minPoints(minPoints)
            .maxPoints(maxPoints)
            .grader((testCycle, criterion) -> GradeResult.of(minPoints, maxPoints, "Not graded by public grader"))
            .build();
    }
}
