package p3.gui.kruskal;

import p3.graph.Graph;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Set;

public class KruskalInfoPane<N> extends Pane {

    private ObservableList<N> nodes;
    private final Graph<N> graph;
    private final AnimatedKruskalMSTCalculator<N> animation;

    public KruskalInfoPane(Graph<N> graph, AnimatedKruskalMSTCalculator<N> animation) {
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

        TableColumn<N, String> distanceColumn = new TableColumn<>("Set");
        distanceColumn.setCellValueFactory(data -> new SimpleStringProperty(getSet(data.getValue()).toString()));

        tableView.getColumns().addAll(List.of(nameColumn, distanceColumn));

        getChildren().add(scrollPane);
    }

    private Set<N> getSet(N node) {

        for (Set<N> set : animation.getMstGroups()) {
            if (set.contains(node)) {
                return set;
            }
        }

        throw new IllegalStateException("Node not found in any set");
    }

}
