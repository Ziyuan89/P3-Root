package p3.graph;

/**
 * A light wrapper around a 2D array of integers that represents an adjacency matrix.
 *
 * <p>Invariants:</p>
 * <ul>
 *     <li><code>matrix.length == matrix[0].length</code></li>
 *     <li><code>matrix[i][j] == matrix[j][i]</code></li>
 * </ul>
 */
public class AdjacencyMatrix {

    final int[][] matrix;

    public AdjacencyMatrix(int size) {
        matrix = new int[size][size];
    }

    /**
     * Adds an edge between the given indices with the given weight.
     *
     * @param a the index of the first node
     * @param b the index of the second node
     * @param weight the weight of the edge
     */
    public void addEdge(int a, int b, int weight) {
        matrix[a][b] = weight;
        matrix[b][a] = weight;
    }

    /**
     * Returns the weight of the edge between the given indices.
     *
     * @param a the index of the first node
     * @param b the index of the second node
     * @return the weight of the edge between the given indices
     */
    public int getWeight(int a, int b) {
        return matrix[a][b];
    }

    /**
     * Returns an array of the weights of the edges adjacent to the given index.
     *
     * @param index the index of the node to get the adjacent edges of
     * @return an array of the weights of the edges adjacent to the given index
     */
    public int[] getAdjacent(int index) {
        int[] adjacent = new int[matrix.length];
        System.arraycopy(matrix[index], 0, adjacent, 0, adjacent.length);
        return adjacent;
    }
}
