package p3.gui.dijkstra;

import p3.graph.Graph;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

public class DijkstraInfoPane<N> extends Pane {

    private ObservableList<N> nodes;
    private final Graph<N> graph;
    private final AnimatedDijkstraPathCalculator<N> animation;
    private Label label;

    public DijkstraInfoPane(Graph<N> graph, AnimatedDijkstraPathCalculator<N> animation) {
        this.graph = graph;
        this.animation = animation;

        createLabel();

        VBox vBox = new VBox();
        vBox.getChildren().addAll(createTableView(), label);
        getChildren().add(vBox);
    }

    public void refresh() {
        nodes.clear();
        nodes.addAll(graph.getNodes());

        label.setText(getText());
    }

    @SuppressWarnings("DuplicatedCode")
    private ScrollPane createTableView() {
        nodes = FXCollections.observableArrayList(graph.getNodes());

        TableView<N> tableView = new TableView<>(nodes);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        TableColumn<N, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().toString()));

        TableColumn<N, String> distanceColumn = new TableColumn<>("Distance");
        distanceColumn.setCellValueFactory(data -> new SimpleStringProperty(animation.getDistance(data.getValue())));

        TableColumn<N, String> predecessorColumn = new TableColumn<>("Predecessor");
        predecessorColumn.setCellValueFactory(data -> new SimpleStringProperty(animation.getPredecessor(data.getValue())));

        tableView.getColumns().addAll(List.of(nameColumn, distanceColumn, predecessorColumn));

        return scrollPane;
    }

    private void createLabel() {
        label = new Label();
        label.setText(getText());
    }

    private String getText() {
        return "RemainingNodes: " + animation.getRemainingNodes();
    }

}
