package h3.gui;

import h3.graph.EdgeImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MyApplication extends Application {

    public static void launch(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GraphPane<Object> graphPane = new GraphPane<>();

        LocationNode<Object> a = new LocationNode<>("A", new Location(1, 2));
        LocationNode<Object> b = new LocationNode<>("B", new Location(20, 20));
        LocationNode<Object> c = new LocationNode<>("C", new Location(-30, -20));

        graphPane.addNode(a);
        graphPane.addNode(b);
        graphPane.addNode(c);

        graphPane.addEdge(new EdgeImpl<>(a, b, 10));
        graphPane.addEdge(new EdgeImpl<>(b, c, 20));
        graphPane.addEdge(new EdgeImpl<>(a, c, 30));

        graphPane.setPrefSize(600, 600);

        primaryStage.setScene(new Scene(graphPane));

        primaryStage.show();
    }
}
