package h3.gui.dijkstra;

import h3.graph.Graph;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

import java.util.List;

public class DijkstraInfoPane<N> extends Pane {

    private ObservableList<N> nodes;
    private Graph<N> graph;

    private AnimatedDijkstraPathCalculator<N> animation;

    public DijkstraInfoPane() {

    }

    public void init(Graph<N> graph, AnimatedDijkstraPathCalculator<N> animation) {
        this.graph = graph;
        this.animation = animation;

        createTableView();
    }

    public void refresh() {
        nodes.clear();
        nodes.addAll(graph.getNodes());
    }

    private void createTableView() {
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

        getChildren().add(scrollPane);
    }

}
