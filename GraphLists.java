// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

class Heap {
    private int[] a; // heap array
    public int[] hPos; // hPos[h[k]] == k
    private int[] dist; // dist[v] = priority of v
    private boolean[] visited; // visited[v] = true if v is in the MST/SSSP

    private int N; // heap size

    // The heap constructor gets passed from the Graph:
    // 1. maximum heap size
    // 2. reference to the dist[] array
    // 3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos, boolean[] _visited) {
        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
        visited = _visited;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public void siftUp(int k) {
        int v = a[k];
        while (k > 1 && compare(dist[v], dist[a[k / 2]])) {
            a[k] = a[k / 2];
            hPos[a[k]] = k;
            k /= 2;
        }
        a[k] = v;
        hPos[v] = k;
    }

    public void siftDown(int k) {
        int v = a[k];
        int j;
        while (2 * k <= N) {
            j = 2 * k;
            if (j < N && compare(dist[a[j + 1]], dist[a[j]])) {
                j++;
            }
            if (compare(dist[v], dist[a[j]])) {
                break;
            }
            a[k] = a[j];
            hPos[a[k]] = k;
            k = j;
        }
        a[k] = v;
        hPos[v] = k;
    }

    public void insert(int x) {
        a[++N] = x;
        siftUp(N);
    }

    public int remove() {
        int v = a[1];
        hPos[v] = 0; // v is no longer in heap
        a[N + 1] = 0; // put null node into empty spot

        a[1] = a[N--];
        siftDown(1);

        return v;
    }

    private boolean compare(int x, int y) {
        // Returns true if x should come before y in the heap order
        if (visited[x] && !visited[y]) {
            return true;
        } else if (!visited[x] && visited[y]) {
            return false;
        } else {
            return dist[x] < dist[y];
        }
    }
}

class Graph {
    class Node {
        public int vert;
        public int wgt;
        public Node next;
    }

    private int V, E;
    private Node[] adj;
    private Node z;
    private int[][] adjMatrix;

    // default constructor
    public Graph(String graphFile) throws IOException {

        int u, v;
        int e, wgt;

        FileReader fr = new FileReader(graphFile);
        try (BufferedReader reader = new BufferedReader(fr)) {
            String splits = " +"; // multiple whitespace as delimiter
            String line = reader.readLine();
            String[] parts = line.split(splits);
            System.out.println("Parts[] = " + parts[0] + " " + parts[1]);

            V = Integer.parseInt(parts[0]); // Number of vertices
            E = Integer.parseInt(parts[1]); // Number of edges
            System.out.println(E);
            adjMatrix = new int[E][E];

            // create sentinel node
            z = new Node();
            z.next = z;

            // create adjacency lists, initialised to sentinel node z
            adj = new Node[V + 1];
            for (v = 1; v <= V; v++) {
                adj[v] = z;
            }

            // read the edges
            System.out.println("Reading edges from text file");
            for (e = 0; e < E; e++) {
                line = reader.readLine();
                parts = line.split(splits);
                u = Integer.parseInt(parts[0]); // starting vertex
                v = Integer.parseInt(parts[1]); // adjacent vertex
                wgt = Integer.parseInt(parts[2]); // weight of the edge between two

                // write code to put edge into adjacency matrix
                adjMatrix[e][0] = u;
                adjMatrix[e][1] = v;
                adjMatrix[e][2] = wgt;
            }
        } catch (NumberFormatException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    // convert vertex into char for pretty printing
    private char toChar(int u) {
        return (char) (u + 64);
    }

    // method to display the graph representation
    public void display() {
        System.out.println("\n---- ADJACENCY LIST DISPLAY ----\n");
        System.out.println("");
        for (int i = 0; i < adjMatrix.length; i++) {
            for(int j = 0; j < 3; j++){
                char u = (char)(adjMatrix[i][j] + 'A');
                char v = (char)(adjMatrix[i][j] + 'A');
                int wgt = adjMatrix[i][j];
                System.out.println("Graph Edge " + u + "--(" + wgt + ")--" + v);
            }
            
        }
    }

    public void MST_Prim(int s) {
        System.out.println("\n===========================================");
        System.out.println("=  Minimum Spanning Tree and weight of MST  =");
        System.out.println("=============================================\n");

        // Initialize arrays
        int[] dist = new int[V + 1];
        int[] parent = new int[V + 1];
        boolean[] visited = new boolean[V + 1];
        int wgt_sum = 0;

        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[s] = 0;

        // Run Prim's algorithm
        for (int i = 1; i <= V; i++) {
            int u = -1;
            for (int j = 1; j <= V; j++) {
                if (!visited[j] && (u == -1 || dist[j] < dist[u])) {
                    u = j;
                }
            }

            visited[u] = true;

            for (int v = 1; v <= V; v++) {
                if (adjMatrix[u][v] != 0 && !visited[v] && adjMatrix[u][v] < dist[v]) {
                    dist[v] = adjMatrix[u][v];
                    parent[v] = u;
                }
            }
        }

        // Print the MST and total weight
        System.out.println("Minimum Spanning Tree:");
        System.out.println("_____________________________________________\n");
        for (int v = 1; v <= V; v++) {
            if (parent[v] != -1) {
                System.out.println(toChar(parent[v]) + " - " + toChar(v) + ": " + adjMatrix[parent[v]][v]);
                wgt_sum += adjMatrix[parent[v]][v];
            }
        }
        System.out.println("_____________________________________________\n");
        System.out.println("Total weight of MST: " + wgt_sum);
    }

    /**
     * 
     * This method calculates the shortest paths and distances in a graph using
     * Dijkstra's algorithm,
     * 
     * starting from a source vertex s. It prints the results to the console.
     * 
     * @param s the index of the source vertex
     */
    public void SPT_Dijkstra(int s) {
        // Initialize the distance, parent, and visited arrays
        int[] dist = new int[V + 1];
        int[] parent = new int[V + 1];
        boolean[] visited = new boolean[V + 1];

        // Set all distances to infinity and visited to false
        for (int i = 1; i <= V; i++) {
            dist[i] = Integer.MAX_VALUE;
            visited[i] = false;
        }

        // Set the distance to the source vertex to 0
        dist[s] = 0;

        // Run Dijkstra's algorithm
        for (int i = 1; i <= V; i++) {
            int u = -1;
            for (int j = 1; j <= V; j++) {
                // Find the unvisited vertex with the smallest distance
                if (!visited[j] && (u == -1 || dist[j] < dist[u])) {
                  
                    u = j;
                }
            }

            visited[u] = true;

            // Update the distances and parents of the adjacent vertices
            for (int v = 1; v <= V; v++) {
                if (adjMatrix[u][v] != 0 && !visited[v]) {
                    int alt = dist[u] + adjMatrix[u][v];
                    if (alt < dist[v]) {
                        dist[v] = alt;
                        parent[v] = u;
                    }
                }
            }
        }

        System.out.println("Vertex\tDistance\tParent");
for (int i = 1; i <= V; i++) {
    System.out.println(i + "\t" + dist[i] + "\t\t" + parent[i]);
}

        // Print the shortest paths and distances
        System.out.println("\n==================================");
        System.out.println("= SHORTEST PATHS AND DISTANCES =");
        System.out.println("==================================\n");
        System.out.println("Source vertex: " + toChar(s));
        System.out.println("__________________________________\n");

        for (int v = 1; v <= V; v++) {
            if (dist[v] == Integer.MAX_VALUE) {
                // If there is no path to the source vertex, print a message
                System.out.println("Path from " + toChar(s) + " to " + toChar(v) + ": no path to the source");
            } else {
                // Otherwise, construct the path and print the result
                String path = "";
                int p = v;
                while (p != s) {
                    path = "-" + toChar(p) + path;
                    p = parent[p];
                }
                path = toChar(s) + path;
                System.out.println("Path from " + toChar(s) + " to " + toChar(v) + ": " + path + ": " + dist[v]);
            }
        }
        System.out.println("__________________________________\n");
    }
}

public class GraphLists {
    public static void main(String[] args) throws IOException {
        //int s = 12;
        String fname = "wGraph1.txt";

        Graph g = new Graph(fname);

        g.display();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the source vertex s: ");
        int s = scanner.nextInt();
        
        g.MST_Prim(s);
        g.SPT_Dijkstra(s);
    }
}