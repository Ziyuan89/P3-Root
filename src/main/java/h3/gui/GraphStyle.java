package h3.gui;

import javafx.scene.paint.Color;

public class GraphStyle {

    // --- Node --- //

    public static final double NODE_DIAMETER = 15;

    // --- Stroke --- //
    public static final int NODE_STROKE_WIDTH = 2;
    public static final int EDGE_STROKE_WIDTH = 2;

    // --- Grid --- //

    public static final float GRID_FIVE_TICKS_WIDTH = .125f;
    public static final float GRID_TEN_TICKS_WIDTH = .25f;
    public static final Color GRID_LINE_COLOR = Color.LIGHTGRAY;

    // --- Text --- //

    public static final Color TEXT_COLOR = Color.GRAY;

    // --- Default --- //

    public static final Color DEFAULT_NODE_COLOR = Color.DARKGRAY;
    public static final Color DEFAULT_EDGE_COLOR = Color.LIGHTGRAY;

    // --- Dijkstra --- //

    public static final Color DIJKSTRA_VISITED_NODE = Color.RED;

    public static final Color DIJKSTRA_PREDECESSOR_EDGE = Color.RED;

    public static final Color DIJKSTRA_CURRENT_NODE = Color.GREEN;
    public static final Color DIJKSTRA_CURRENT_EDGE = Color.GREEN;

    public static final Color DIJKSTRA_UNVISITED_NODE = DEFAULT_NODE_COLOR;
    public static final Color DIJKSTRA_UNVISITED_EDGE = DEFAULT_EDGE_COLOR;

    public static final Color DIJKSTRA_RESULT_NODE = Color.GREEN;
    public static final Color DIJKSTRA_RESULT_EDGE = Color.GREEN;

    // --- Kruskal --- //

    public static final Color KRUSKAL_ACCEPTED_EDGE = Color.GREEN;
    public static final Color KRUSKAL_REJECTED_EDGE = Color.RED;
    public static final Color KRUSKAL_UNVISITED_EDGE = DEFAULT_EDGE_COLOR;
    public static final Color KRUSKAL_RESULT_EDGE = Color.GREEN;
}
