# 1 Graphenrepräsentation - 10 Punkte

a) BasicGraph Konsturktor 3 Punkte
- korrekt mit 2 nodes
- korrekt mit 3 nodes
- korrekt mit >3 nodes

b) AdjacencyMatrix & -Graph 6 Punkte
- addEdge korrekt
- getWeight korrekt
- getAdjacent korrekt
- getAdjacentsEdges korrekt wenn alle edges weights > 0 haben
- getAdjacentsEdges vollständig korrekt
- maps in adjacencyGraph Konstruktor korrekt
- edges korrekt in Konstruktor hinzugefügt


# 2 Kruskal - 10 Punkte

- Graph.Edge.compareTo korrekt
- init korrekt
- Sets beinhalten die korrekten Werte nach joinSets
- joinSets fügt Werte in das größere Set hinzu (vollständig korrekt)
- acceptEdge korrekt wenn beide Werte in erstem Set
- acceptEdge korrekt wenn ein Werte in erstem Set
- acceptEdge korrekt wenn nicht gejoint wurde
- acceptEdge korrekt wenn gejoint wurde
- createMSt ruft acceptEdge für alle edges auf
- createMST vollständig korrekt (sortiert, mstEdges)

# 3 Dijkstra - 9 Punkte

- init korrekt
- extractMin korrekt wenn remainingNode alle nodes beinhaltet
- extractMin vollständig korrekt
- relax korrekt (nur relax aufrufen wenn edge noch nicht besucht?
- reconstructPath korrekt (2 Punkte)
- calculatePath korrekt für Graph mit 2 Nodes
- calculatePath korrekt für Graph mit 3 Nodes
- calculatePath korrekt für Graph mit >3 Nodes
