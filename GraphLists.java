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

    private int starting_vertex = 12;
    
    
    // default constructor
    public Graph(String graphFile)  throws IOException
    {
        int u, v;
        int e, wgt;

        FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);

        String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
        
        V = Integer.parseInt(parts[0]); // Number of vertices
        E = Integer.parseInt(parts[1]); // Number of edges
        
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
        for(e = 1; e < E; e++)
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
            Node newNode = new Node();
            newNode.next = adj[u];
            adj[u] = newNode;
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
        /* Search works, never goes beyond vertex A, vertex A seeks edges with all the other vertex
         * additional if statements required
        */
        int[] dist = new int[V+1];
        int[] parent = new int[V+1];
        boolean[] visited = new boolean[V+1];
        int wgt_sum = 0;

        for (int v = 0; v <= V; v++)
        {
            dist[v] = Integer.MAX_VALUE;
            parent[v] = -1;
        }

        int v = s;
        dist[0] = Integer.MAX_VALUE;

        /*
        if (adjMatrix[8][0] > 0)
        {
            System.out.println("TRUE");
            System.out.println("adjMatrix = " + adjMatrix[8][0]);
            System.out.println("toChar adjMatrix = " + (toChar(adjMatrix[8][0])));
            System.out.println("toChar adjMatrix reverse = " + (toChar(adjMatrix[0][3])));
            System.out.println("toChar u = " + (toChar(1)));
            System.out.println("toChar v = " + (toChar(2)));
            System.out.println("----\n");
        }
        else
        {
            System.out.println("FALSE");
        }*/

        //int x = 11;
        //int i = 1;

        /* 
        while (i < E)
        {
            if (adjMatrix[i][1] == x) 
            {
                System.out.println("Vertex " + toChar(adjMatrix[i][0]) + " IS adjacent to vertex " + toChar(x) + " with weight " + adjMatrix[i][2]);
                break;
            } 
            else 
            {
                System.out.println("Vertex " + toChar(adjMatrix[i][0]) + " is NOT adjacent to vertex " + toChar(x));
            }

            i++;
        }*/

        while (v != 0)
        {
            dist[v] = -dist[v];
            int min = 0;

            for (int u = 1; u <= V; u++)
            {
                //if starting vertex belongs to adjacent vertex AND weight of the edge is less than dist[u] then
                if (adjMatrix[u][1] == v && adjMatrix[u][2] < dist[u])
                {
                    dist[u] = adjMatrix[u][2];
                    System.out.println("Vertex " + toChar(adjMatrix[u][0]) + " IS adjacent to vertex " + toChar(v) + " with weight " + adjMatrix[u][2]);
                    parent[u] = v;
                    wgt_sum += adjMatrix[u][2];
                    System.out.println("dist[u] = " + dist[u]);
                }

                if (dist[u] < dist[min] && dist[u] > 0)
                {      
                    min = u;
                    System.out.println("min = " + min);
                }
            }

            v = min;
        }

        System.out.println("Weight of MST: " + wgt_sum);
        mst = parent;
                  		
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
        System.out.println("\n ---- SPT DIJKSTRA ALGORITHM ----\n");

        // initialise visited[], dist[] and prev[]
        visited = new int[V+1];
        int[] dist = new int[V+1];
        int[] prev = new int[V+1];
        int[] parent = new int[V+1];
    
        for (int v = 1; v <= V; v++) {
            dist[v] = Integer.MAX_VALUE;
            visited[v] = 0;
            prev[v] = -1;
            //parent[v] = (Integer) null;
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
        int s = 2;
        String fname = "wGraph1.txt";               

        Graph g = new Graph(fname);
       
        g.display();

       //g.DF(s);
       //g.breadthFirst(s);
       g.MST_Prim(s); 
       g.showMST();  
       //SPT_Dijkstra(s);               
    }
}
