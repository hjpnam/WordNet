/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        checkArgs(v);
        checkArgs(w);
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        return shortestAncestralVertexAndDist(bfsV, bfsW)[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        checkArgs(v);
        checkArgs(w);
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        return shortestAncestralVertexAndDist(bfsV, bfsW)[1];
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        checkArgs(v);
        checkArgs(w);
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        return shortestAncestralVertexAndDist(bfsV, bfsW)[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        checkArgs(v);
        checkArgs(w);
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        return shortestAncestralVertexAndDist(bfsV, bfsW)[1];
    }

    private void checkArgs(int v) {
        if (v < 0 || v >= G.V()) throw new IllegalArgumentException();
    }

    private void checkArgs(Iterable<Integer> v) {
        for (int vertex : v) {
            checkArgs(vertex);
        }
    }

    private int[] shortestAncestralVertexAndDist(BreadthFirstDirectedPaths bfsV,
                                                 BreadthFirstDirectedPaths bfsW) {
        int minSumDist = Integer.MAX_VALUE;
        int minCommonAncestor = -1;
        for (int vertex = 0; vertex < G.V(); vertex++) {
            if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) {
                int sumDist = bfsV.distTo(vertex) + bfsW.distTo(vertex);
                if (sumDist < minSumDist) {
                    minSumDist = sumDist;
                    minCommonAncestor = vertex;
                }
            }
        }
        if (minCommonAncestor == -1) minSumDist = -1;
        return new int[] { minSumDist, minCommonAncestor };
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
/*        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }*/
        Stack<Integer> v = new Stack<Integer>();
        v.push(13);
        v.push(23);
        v.push(24);
        Stack<Integer> w = new Stack<Integer>();
        w.push(6);
        w.push(16);
        w.push(17);

        int length = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}
