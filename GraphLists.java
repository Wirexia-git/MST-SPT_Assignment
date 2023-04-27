// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;

class Heap {
    private int[] a;        // heap array
    public int[] hPos;      // hPos[h[k]] == k
    private int[] dist;     // dist[v] = priority of v

    private int N;          // heap size
   
    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) {
        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public void siftUp(int k) {
        int v = a[k];
        while (k > 1 && dist[v] < dist[a[k / 2]]) {
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
            if (j < N && dist[a[j + 1]] < dist[a[j]]) {
                j++;
            }
            if (dist[v] <= dist[a[j]]) {
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
        hPos[v] = 0;    // v is no longer in heap
        a[N ] = 0;   // put null node into empty spot
        
        a[1] = a[N--];
        siftDown(1);
        
        return v;
    }
}

class Graph {
    class Node {
        public int vert;
        public int wgt;
        public Node next;
    }
    
    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    private int V, E;
    private Node[] adj;
    private Node z;
    private int[] mst;
    private int[][] adjMatrix;
    
    // used for traversing graph
    private int[] visited;
    private int id;

    private int starting_vertex = 12;
    int _adj;
    
    // default constructor
    public Graph(String graphFile)  throws IOException
    {
        int u, v;
        int e, wgt;
        Node t;

        FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);
	           
        String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
        
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);
        adjMatrix = new int[E][E];
        // create sentinel node
        z = new Node(); 
        z.next = z;
        
        // create adjacency lists, initialised to sentinel node z       
        adj = new Node[V+1];        
        for(v = 1; v <= V; v++)
        {
            adj[v] = z;               
        }

       // read the edges
        System.out.println("Reading edges from text file");
        for(e = 0; e < E; e++)
        {
            line = reader.readLine();
    
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            wgt = Integer.parseInt(parts[2]);

            adjMatrix[e][0] = u;
            adjMatrix[e][1] = v;
            adjMatrix[e][2] = wgt;
        }	       
    }
   
    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
    
    // method to display the graph representation
    public void display() 
    {
        int v;
        Node n;
        System.out.println("\n---- ADJACENCY LIST DISPLAY ----\n");
            /*for(v=1; v<=V; v++)
            {
                System.out.print("\nadj[" + toChar(v) + "] -> " );
                for(n = adj[v]; n != z; n = n.next) 
                {
                    System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");
                }
            }*/
        System.out.println("");
        for(int i = 0; i < adjMatrix.length;i++){
            System.out.print("\nadj[" + toChar(adjMatrix[i][0]) + " " +toChar(adjMatrix[i][1])+" "+toChar(adjMatrix[i][2])+"]"); 
            
        }
    }


    
	public void MST_Prim(int s)
	{
        System.out.println("\n---- PRIM ALGORITHM ----\n");
        int v, u;
        int wgt, wgt_sum = 0;
        int[]  dist, parent, hPos;
        Node t;

        dist = new int[V+1];
        parent = new int[V+1];
        hPos = new int[V+1];
        for (v = 1; v <= V; v++) {
            dist[v] = Integer.MAX_VALUE;
            parent[v] = 0;
            hPos[v] = 0;
        }

        dist[s] = 0;
        parent[s] = s;

        Heap h = new Heap(V, dist, hPos);
        h.insert(s);

        while (!h.isEmpty()) {
            u = h.remove();
            for (t = adj[u]; t != z; t = t.next) {
                v = t.vert;
                wgt = t.wgt;
                if (wgt < dist[v]) {
                    dist[v] = wgt;
                    parent[v] = u;
                    if (hPos[v] == 0) {
                        h.insert(v);
                    } else {
                        h.siftUp(hPos[v]);
                    }
                }
            }
        }

        mst = parent;
        for (v = 1; v <= V; v++) {
            if (mst[v] != 0) {
                System.out.println(toChar(mst[v]) + " - " + toChar(v));
                wgt_sum += dist[v];
            }
        }
        System.out.println("Total weight of MST: " + wgt_sum);
        
                  		
	}
    
    public void showMST()
    {
            System.out.print("\n\nMinimum Spanning tree parent array is:\n");
            for(int v = 1; v <= V; ++v)
                System.out.println(toChar(v) + " -> " + toChar(mst[v]));
            System.out.println("");
    }

    public void SPT_Dijkstra(int s) {
        
        System.out.println();
        System.out.println("\nSPT_Dijkstra Algorithm\n");
        // initialise visited[], dist[] and prev[]
        visited = new int[V+1];
        int []dist = new int[V+1];
        int []prev = new int[V+1];
    
        for (int v = 1; v <= V; v++) {
            visited[v] = 0;
            dist[v] = Integer.MAX_VALUE;
            prev[v] = -1;
        }
    
        // set the distance to the starting vertex to 0
        dist[s] = 0;
    
        // create heap, h, to store vertices not yet processed
        Heap h = new Heap(V, dist, new int[V+1]);
        for (int v = 1; v <= V; v++) {
            h.insert(v);
        }
    
        int v, u;
    
        // iteratively visit closest unvisited vertex
        while (!h.isEmpty()) {
            v = h.remove();
            visited[v] = 1;
    
            // iterate over adjacent vertices of v
            for (int e = 0; e < E; e++) {
                if (adjMatrix[e][0] == v) {
                    u = adjMatrix[e][1];
                    if (visited[u] == 0) {
                        if (dist[v] + adjMatrix[e][2] < dist[u]) {
                            dist[u] = dist[v] + adjMatrix[e][2];
                            prev[u] = v;
                            h.siftUp(h.hPos[u]);
                        }
                    }
                }
            }
        }
        
        // print shortest paths
        for (int i = 1; i <= V; i++) {
            if (dist[i] == Integer.MAX_VALUE) {
                System.out.println("No path from vertex " + toChar(s) + " to vertex " + toChar(i));
            } else {
                System.out.print("Distance from vertex " + toChar(s) + " to vertex " + toChar(i) + " is " + dist[i]);
                System.out.print(", path is [" + toChar(i));
                int j = i;
                while (prev[j] != -1) {
                    System.out.print(", " + toChar(prev[j]));
                    j = prev[j];
                }
                System.out.println("]");
            }
        }
        System.out.println("\n\n");
    }
}


public class GraphLists {
    public static void main(String[] args) throws IOException
    { 
        int s = 1;
        String fname = "wGraph1.txt";               

        Graph g = new Graph(fname);
       
        g.display();

       //g.DF(s);
       //g.breadthFirst(s);
       //g.MST_Prim(s);   
       g.SPT_Dijkstra(s);               
    }
}
