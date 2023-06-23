# 1 Graphenrepräsentation - 10 Punkte

a) BasicGraph Konstruktor 3 Punkte
- korrekt mit 2 nodes
- korrekt mit 3 nodes
- korrekt mit >3 nodes

b) AdjacencyMatrix & -Graph 7 Punkte
- addEdge korrekt
- getWeight korrekt
- getAdjacent korrekt
- maps in adjacencyGraph Konstruktor korrekt
- edges korrekt in Konstruktor hinzugefügt
- getAdjacentEdges korrekt wenn alle edges weights > 0 haben
- getAdjacentEdges vollständig korrekt


# 2 Kruskal - 10 Punkte

- Edge.compareTo korrekt
- init korrekt
- Groups beinhalten die korrekten Werte nach joinGroups
- joinGroups fügt Werte in das größere Set hinzu (vollständig korrekt)
- acceptEdge korrekt wenn beide Werte in erstem Set
- acceptEdge korrekt wenn ein Werte in erstem Set
- acceptEdge korrekt wenn nicht gejoint wurde
- acceptEdge korrekt wenn gejoint wurde
- createMSt ruft acceptEdge für alle edges auf
- createMST vollständig korrekt (sortiert, mstEdges)

# 3 Dijkstra - 10 Punkte

- init korrekt
- extractMin korrekt, wenn remainingNode alle nodes beinhaltet
- extractMin vollständig korrekt
- relax passt distances korrekt an
- relax passt predecessors korrekt an
- reconstructPath korrekt (2 Punkte)
- calculatePath korrekt für Graph mit 2 Nodes
- calculatePath korrekt für Graph mit 3 Nodes
- calculatePath korrekt für Graph mit >3 Nodes
