import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}

/*

      0    True              0 -- 1 -- 2   False
    / | \                           \  /
   1  2  3                       4 -- 3
   |
   4

 */

// there is one more apprrroachL Union Find, but is a solid 60 lines. not something i'll write


// cleaner recusive but not any faster
class Solution {

    private List<List<Integer>> adjacencyList = new ArrayList<>();
    private Set<Integer> seen = new HashSet<>();


    public boolean validTree(int n, int[][] edges) {

        if (edges.length != n - 1) return false;

        // Make the adjacency list.
        for (int i = 0; i < n; i++) {
            adjacencyList.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adjacencyList.get(edge[0]).add(edge[1]);
            adjacencyList.get(edge[1]).add(edge[0]);
        }

        // Carry out depth first search.
        dfs(0);
        // Inspect result and return the verdict.
        return seen.size() == n;
    }

    public void dfs(int node) {
        if (seen.contains(node)) return; //prevent trivial cycle
        seen.add(node);
        for (int neighbour : adjacencyList.get(node)) {
            dfs(neighbour);
        }
    }
}

// cleaner iterrative approach but stillnot as fast as recursive
class Solution {
    public boolean validTree(int n, int[][] edges) {

        if (edges.length != n - 1) return false;

        // Make the adjacency list.
        List<List<Integer>> adjacencyList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjacencyList.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adjacencyList.get(edge[0]).add(edge[1]);
            adjacencyList.get(edge[1]).add(edge[0]);
        }

        Stack<Integer> stack = new Stack<>();
        Set<Integer> seen = new HashSet<>();
        stack.push(0);
        seen.add(0);

        while (!stack.isEmpty()) {
            int node = stack.pop();
            for (int neighbour : adjacencyList.get(node)) {
                if (seen.contains(neighbour)) continue; // avoid trivial cycle
                seen.add(neighbour);
                stack.push(neighbour);
            }
        }

        return seen.size() == n;
    }
}

// recursive approach is a lot faster and about middle of the pack on efficiencies
class Solution {

    private List<List<Integer>> adjacencyList = new ArrayList<>();
    private Set<Integer> seen = new HashSet<>(); // set won't allow duplicates


    public boolean validTree(int n, int[][] edges) {

        if (edges.length != n - 1) return false;

        for (int i = 0; i < n; i++) {
            adjacencyList.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adjacencyList.get(edge[0]).add(edge[1]);
            adjacencyList.get(edge[1]).add(edge[0]);
        }

        // We return true iff no cycles were detected,
        // AND the entire graph has been reached.
        return dfs(0, -1) && seen.size() == n;
    }

    public boolean dfs(int node, int parent) {
        if (seen.contains(node)) return false;
        seen.add(node);
        for (int neighbour : adjacencyList.get(node)) {
            if (parent != neighbour) { // prevent trivial cycle back to predecessor
                boolean result = dfs(neighbour, node);
                if (!result) return false;
            }
        }
        return true; // base case where leaf is reached and did not traverse back
    }
}

// editorial 1 works but bottom of barrel. using stack, map and adjacency list to check if node visited
// before and if edges.length = n-1
class Solution {
    public boolean validTree(int n, int[][] edges) {

        List<List<Integer>> adjacencyList = new ArrayList<>();
        for (int i = 0; i < n; i++) { // for each vertex with corresponding index in the
            adjacencyList.add(new ArrayList<>()); // adjacency list, create array list to hold  neighbors
        }
        for (int[] edge : edges) { // build reciprocal edges
            adjacencyList.get(edge[0]).add(edge[1]);
            adjacencyList.get(edge[1]).add(edge[0]);
        }

        Map<Integer, Integer> parent = new HashMap<>();
        parent.put(0, -1); // key is the node, value is the predecessor. format to ensure we
        // don't go backwards across reciprocal edge in a trivial cycle. value is -1
        // since no predecessor for start node
        Stack<Integer> stack = new Stack<>();
        stack.push(0); // kick off iterative DFS with adding arbitrary start vertex

        while (!stack.isEmpty()) {
            int node = stack.pop();
            for (int neighbour : adjacencyList.get(node)) { // go through current nodes neighbors
                if (parent.get(node) == neighbour) { // if a neighbor of current node is already
                    continue; // set as the key in a map, it means we
                    // have come from that way before so we don't want to loop back in trivial cycle
                    // such as 0-1-0
                }
                if (parent.containsKey(neighbour)) { // if a value is listed as a key already
                    return false; // it means it was already visited via a different node than cur
                }
                stack.push(neighbour);
                parent.put(neighbour, node); // list neighbor as the key
            }
        } // if stack successfully empties, it means all nodes from start node have been added to map

        return parent.size() == n; // if map size != n then some nodes are unconnected so not a graph
    }
}

// just this also passes 29/45 but same BS test case [[0,1],[2,3]] two trees
// a tree is defined as having exactly n-1 edges when n = number of nodes/vertices.
// . any more = cycle, any less = unconnected
// also must
// be able to get to any other node from any start node, assuming tree/graph is undirected
// just meaning the graph is actually one graph, not two separate unconnected graphs
// this code only tests # of edges, not if vertices all connected
class Solution {
    public boolean validTree(int n, int[][] edges) {
        if(edges.length >= n){
            return false;
        }
        return true;
    }
}

// i manipulated the solution for minimum height trees for topological sort alogorithm to find # of centroids but
// this is a BS test case   [[0,1],[2,3]]
// says it gives me a graph so i'm looking for a cycle, but this is actually two graphs since they aren't connected...
// super misleading
class Solution {
    public boolean validTree(int n, int[][] edges) {
        // edge cases
        if (n < 2) {
            ArrayList<Integer> centroids = new ArrayList<>();
            for (int i = 0; i < n; i++)
                centroids.add(i); // this works becaus the nodes are labeled 0 - (n-1)
            return true;
        }

        // Build the graph with the adjacency list
        ArrayList<Set<Integer>> neighbors = new ArrayList<>();
        for (int i = 0; i < n; i++)
            neighbors.add(new HashSet<Integer>());

        for (int[] edge : edges) {
            Integer start = edge[0], end = edge[1];
            neighbors.get(start).add(end);
            neighbors.get(end).add(start);
        }

        // Initialize the first layer of leaves
        ArrayList<Integer> leaves = new ArrayList<>();
        for (int i = 0; i < n; i++)
            if (neighbors.get(i).size() == 1)
                leaves.add(i);

        // Trim the leaves until reaching the centroids
        int remainingNodes = n;
        while (remainingNodes > 2) {
            remainingNodes -= leaves.size();
            ArrayList<Integer> newLeaves = new ArrayList<>();

            // remove the current leaves along with the edges
            for (Integer leaf : leaves) {
                // the only neighbor left for the leaf node
                Integer neighbor = neighbors.get(leaf).iterator().next();
                // remove the edge along with the leaf node
                neighbors.get(neighbor).remove(leaf);
                if (neighbors.get(neighbor).size() == 1)
                    newLeaves.add(neighbor);
            }
            if(newLeaves.size() == 0){
                break;
            }

            // prepare for the next round
            leaves = newLeaves;
        }

        // The remaining nodes are the centroids of the graph
        return (remainingNodes <= 2);
    }
}
