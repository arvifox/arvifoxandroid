package com.arvifox.arvi.domain.graph;

// Java program to count number of shortest
// paths from a given source to every other
// vertex using BFS.
import java.io.*;
import java.util.*;

class GFG
{

    // Traverses graph in BFS manner.
    // It fills dist[] and paths[]
    static void BFS(Vector<Integer>[] adj, int src,
                    int dist[], int paths[], int n)
    {
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++)
            visited[i] = false;
        dist[src] = 0;
        paths[src] = 1;

        Queue<Integer> q = new LinkedList<>();
        q.add(src);
        visited[src] = true;
        while (!q.isEmpty())
        {
            int curr = q.peek();
            q.poll();

            // For all neighbors of current vertex do:
            for (int x : adj[curr])
            {

                // if the current vertex is not yet
                // visited, then push it to the queue.
                if (visited[x] == false)
                {
                    q.add(x);
                    visited[x] = true;
                }

                // check if there is a better path.
                if (dist[x] > dist[curr] + 1)
                {
                    dist[x] = dist[curr] + 1;
                    paths[x] = paths[curr];
                }

                // additional shortest paths found
                else if (dist[x] == dist[curr] + 1)
                    paths[x] += paths[curr];
            }
        }
    }

    // function to find number of different
    // shortest paths form given vertex s.
    // n is number of vertices.
    static void findShortestPaths(Vector<Integer> adj[],
                                  int s, int n)
    {
        int[] dist = new int[n], paths = new int[n];

        for (int i = 0; i < n; i++)
            dist[i] = Integer.MAX_VALUE;

        for (int i = 0; i < n; i++)
            paths[i] = 0;

        BFS(adj, s, dist, paths, n);

        System.out.print("Numbers of shortest Paths are: ");
        for (int i = 0; i < n; i++)
            System.out.print(paths[i] + " ");
    }

    // A utility function to add an edge in a
    // directed graph.
    static void addEdge(Vector<Integer> adj[],
                        int u, int v)
    {
        adj[u].add(v);
    }

    // Driver Code
    public static void main(String[] args)
    {
        int n = 7; // Number of vertices

        Vector<Integer>[] adj = new Vector[n];
        for (int i = 0; i < n; i++)
            adj[i] = new Vector<>();

        addEdge(adj, 0, 1);
        addEdge(adj, 0, 2);
        addEdge(adj, 1, 2);
        addEdge(adj, 1, 3);
        addEdge(adj, 2, 3);
        addEdge(adj, 3, 4);
        addEdge(adj, 3, 5);
        addEdge(adj, 4, 6);
        addEdge(adj, 5, 6);
        findShortestPaths(adj, 0, 7);
    }
}