package h3.gui;

import h3.graph.BasicGraph;
import h3.graph.EdgeImpl;
import h3.graph.Graph;
import h3.gui.dijkstra.DijkstraScene;
import h3.gui.kruskal.KruskalScene;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Set;

public class MyApplication extends Application {

    @Override
    public void start(Stage primaryStage) {

        AnimationScene scene = createDijkstraScene();

        primaryStage.setScene(scene);
        primaryStage.setTitle(scene.getTitle());

        primaryStage.show();
    }

    private static AnimationScene createDijkstraScene() {
        Graph<String> graph = createGraph();

        DijkstraScene<String> scene = new DijkstraScene<>();
        scene.init(graph, Map.of("A", new Location(1, 2),
            "B", new Location(0, 20),
            "C", new Location(-30, -20),
            "D", new Location(30, -10),
            "E", new Location(45, -5),
            "F", new Location(50, 10)), "A", "F");

        return scene;
    }

    private static AnimationScene createKruskalScene() {
        Graph<String> graph = createGraph();

        KruskalScene<String> scene = new KruskalScene<>();
        scene.init(graph, Map.of("A", new Location(1, 2),
                "B", new Location(0, 20),
                "C", new Location(-30, -20),
                "D", new Location(30, -10),
                "E", new Location(45, -5),
                "F", new Location(50, 10)));
        return scene;
    }

    private static Graph<String> createGraph() {
        return new BasicGraph<>(Set.of("A", "B", "C", "D", "E", "F"),
                Set.of(new EdgeImpl<>("A", "B", 4),
                        new EdgeImpl<>("A", "C", 1),
                        new EdgeImpl<>("B", "C", 3),
                        new EdgeImpl<>("B", "D", 5),
                        new EdgeImpl<>("D", "E", 1),
                        new EdgeImpl<>("E", "F", 3),
                        new EdgeImpl<>("D", "F", 2),
                        new EdgeImpl<>("C", "D", 10)));
    }
}
