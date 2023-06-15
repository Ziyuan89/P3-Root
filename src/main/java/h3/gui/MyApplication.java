package h3.gui;

import h3.graph.BasicGraph;
import h3.graph.EdgeImpl;
import h3.graph.Graph;
import h3.gui.dijkstra.DijkstraScene;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Set;

public class MyApplication extends Application {

    public static void launch(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Graph<String> graph = new BasicGraph<>(Set.of("A", "B", "C", "D", "E", "F"),
                Set.of(new EdgeImpl<>("A", "B", 4),
                        new EdgeImpl<>("A", "C", 1),
                        new EdgeImpl<>("B", "C", 2),
                        new EdgeImpl<>("B", "D", 2),
                        new EdgeImpl<>("D", "E", 1),
                        new EdgeImpl<>("E", "F", 3)));

        DijkstraScene<String> dijkstraScene = new DijkstraScene<>();
        dijkstraScene.init(graph, Map.of("A", new Location(1, 2),
                "B", new Location(20, 20),
                "C", new Location(-30, -20),
                "D", new Location(30, 30),
                "E", new Location(40, 40),
                "F", new Location(50, 50)), "A", "F");

        primaryStage.setScene(dijkstraScene);
        primaryStage.setTitle(dijkstraScene.getTitle());

        primaryStage.show();
    }
}
