// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;
import java.util.Arrays;

class Heap
{
    private int[] a;	   // heap array
    public int[] hPos;	   // hPos[h[k]] == k
    private int[] dist;    // dist[v] = priority of v

    private int N;         // heap size
   
    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) 
    {
        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }

    public boolean isEmpty() 
    {
        return N == 0;
    }

    public void siftUp(int k) 
    {
        int v = a[k];
        while (k > 1 && dist[v] < dist[a[k / 2]]) 
        {
            a[k] = a[k / 2];
            hPos[a[k]] = k;
            k /= 2;
        }
        a[k] = v;
        hPos[v] = k;
    }

    public void siftDown(int k) 
    {
        int v = a[k];
        int j;
        while (2 * k <= N) 
        {
            j = 2 * k;
            if (j < N && dist[a[j + 1]] < dist[a[j]]) 
            {
                j++;
            }
            if (dist[v] <= dist[a[j]]) 
            {
                break;
            }
            a[k] = a[j];
            hPos[a[k]] = k;
            k = j;
        }
        a[k] = v;
        hPos[v] = k;
    }

    public void insert(int x) 
    {
        a[++N] = x;
        siftUp(N);
    }

    public int remove() 
    {
        int v = a[1];
        hPos[v] = 0; // v is no longer in heap
        a[N+1] = 0;  // put null node into empty spot
        
        a[1] = a[N--];
        siftDown(1);
        
        return v;
    }

}

class Graph 
{
    class Node 
    {
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
    
    
    // default constructor
    public Graph(String graphFile)  throws IOException
    {
        int u, v;
        int e, wgt;
        Node t;

        FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);

        int[] visited = new int[V+1];

        String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
        
        V = Integer.parseInt(parts[0]); // Number of vertices
        E = Integer.parseInt(parts[1]); // Number of edges
        
        adjMatrix = new int[E+1][E];

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
        for(e = 1; e <= E; e++)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]); // starting vertex
            v = Integer.parseInt(parts[1]); // adjacent vertex
            wgt = Integer.parseInt(parts[2]); // weight of the edge between two

            // write code to put edge into adjacency matrix 
            adjMatrix[e][0] = u;
            adjMatrix[e][1] = v;
            adjMatrix[e][2] = wgt;

            // fill adjacency list
            t = new Node();
            t.vert = v;
            t.wgt = wgt;
            t.next = adj[u];
            adj[u] = t;

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
        System.out.println("\n---- ADJACENCY LIST DISPLAY ----\n");
        System.out.println("");
        for(int i = 1; i < adjMatrix.length; i++)
        {
            //System.out.println("adjMatrix[e][0] = u = " + adjMatrix[i][0] + " ;;;adjMatrix[e][1] = v = " + adjMatrix[i][1] + " ;;;adjMatrix[e][2] = wgt =" + adjMatrix[i][2] + "\n"); 
            System.out.println("" + (toChar(adjMatrix[i][0])) + " --> " + (toChar(adjMatrix[i][1])) + " == " + adjMatrix[i][2]);
        }
    }

    
	public void MST_Prim(int s)
	{
        System.out.println("\n---- MST PRIM ALGORITHM ----\n");

        int u, v;
        int wgt, wgt_sum = 0;
        int[] dist = new int[V+1]; 
        int[] parent = new int[V+1];
        int[] hPos = new int [V+1];
        Node t;

        //code here
        for (v = 1; v <= V; v++)
        {
            dist[v] = Integer.MAX_VALUE;
            parent[v] = 0;
            hPos[v] = 0;
        }
        
        dist[s] = 0;
        
        Heap h =  new Heap(V, dist, hPos);
        h.insert(s);
        
        while (!h.isEmpty())  
        {
            v = h.remove();
            dist[v] = -dist[v];
            
            for (t = adj[v]; t != null; t = t.next)
            {
                if (t.wgt < dist[t.vert] && dist[t.vert] != 0)
                {
                    dist[t.vert] = t.wgt;
                    parent[t.vert] = v;
                    wgt_sum = wgt_sum + t.wgt;
                }

                if (hPos[t.vert] == 0)
                {
                    h.insert(t.vert);
                }
                else
                {
                    h.siftUp(hPos[t.vert]);
                }
            }
        }
        System.out.print("\n\nWeight of MST = " + wgt_sum + "\n");
                  		
	}
    
    public void showMST()
    {
        System.out.print("\n\nMinimum Spanning tree parent array is:\n");
        for(int v = 1; v <= V; ++v)
        {
            System.out.println(toChar(v) + " -> " + toChar(mst[v]));
        }
        System.out.println("");
    }

    public void SPT_Dijkstra(int s) 
    {    
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
    public static void main(String[] args) throws IOException
    {
        int s = 12;
        String fname = "wGraph1.txt";
        
        

        Graph g = new Graph(fname);
       
        g.display();

       //g.DF(s);
       //g.breadthFirst(s);
       g.MST_Prim(s); 
       g.showMST();  
       //g.SPT_Dijkstra(s);               
    }
}
