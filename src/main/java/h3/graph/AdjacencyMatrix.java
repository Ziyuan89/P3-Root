package h3.graph;

public class AdjacencyMatrix {

    final int[][] matrix;

    public AdjacencyMatrix(int size) {
        matrix = new int[size][size];
    }

    public void addEdge(int a, int b, int weight) {
        matrix[a][b] = weight;
        matrix[b][a] = weight;
    }

    public int getWeight(int a, int b) {
        return matrix[a][b];
    }
}
