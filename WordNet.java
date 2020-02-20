/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    private final HashMap<String, Stack<Integer>> wordToIdMap;
    private final ArrayList<String> synsetList;
    private final SAP sap;
    private final int vertexCount;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        wordToIdMap = new HashMap<>();
        synsetList = new ArrayList<>();
        vertexCount = storeSynsets(synsets);
        sap = new SAP(createHypernymGraph(hypernyms));
    }

    private int storeSynsets(String synsets) {
        In synsetsFile = new In(synsets);
        int setCount = 0;
        while (!synsetsFile.isEmpty()) {
            // add synsets to synsetList
            String synset = synsetsFile.readLine();
            String[] splitSynset = synset.split(",");
            int id = Integer.parseInt(splitSynset[0]);
            String synonymSet = splitSynset[1];
            synsetList.add(id, synonymSet);
            setCount++;

            // add word to id iterable mappings to wordToIdMap
            String[] nouns = splitSynset[1].split(" ");
            for (String noun : nouns) {
                // if mapping already exists, add to existing id stack
                if (wordToIdMap.containsKey(noun)) wordToIdMap.get(noun).push(id);
                    // else create new stack with id pushed in and add mapping
                else {
                    Stack<Integer> idStack = new Stack<>();
                    idStack.push(id);
                    wordToIdMap.put(noun, idStack);
                }
            }
        }
        return setCount;
    }


    private Digraph createHypernymGraph(String hypernyms) {
        In hypernymsFile = new In(hypernyms);
        Digraph wordNet = new Digraph(vertexCount);
        while (!hypernymsFile.isEmpty()) {
            String hypernym = hypernymsFile.readLine();
            String[] splitHypernym = hypernym.split(",");
            int id = Integer.parseInt(splitHypernym[0]);
            for (int i = 1; i < splitHypernym.length; i++) {
                wordNet.addEdge(id, Integer.parseInt(splitHypernym[i]));
            }
        }
        if (isCycle(wordNet) || !isRooted(wordNet)) throw new IllegalArgumentException();
        return wordNet;
    }

    private boolean isCycle(Digraph g) {
        boolean[] visited = new boolean[vertexCount];
        boolean[] recursiveArr = new boolean[vertexCount];

        for (int i = 0; i < vertexCount; i++) {
            if (isCycleHelper(i, g, visited, recursiveArr)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCycleHelper(int vertex, Digraph g, boolean[] visited,
                                  boolean[] recursiveArr) {
        visited[vertex] = true;
        recursiveArr[vertex] = true;

        for (int adj : g.adj(vertex)) {
            if (!visited[adj] && isCycleHelper(adj, g, visited, recursiveArr)) return true;
            else if (recursiveArr[adj]) return true;
        }
        recursiveArr[vertex] = false;
        return false;
    }

    private boolean isRooted(Digraph G) {
        int rootCount = 0;
        for (int i = 0; i < vertexCount && rootCount < 2; i++) {
            if (G.outdegree(i) == 0) rootCount++;
        }
        return rootCount == 1;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return wordToIdMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return wordToIdMap.containsKey(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        Iterable<Integer> idsA = wordToIdMap.get(nounA);
        Iterable<Integer> idsB = wordToIdMap.get(nounB);
        return sap.length(idsA, idsB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        Iterable<Integer> idsA = wordToIdMap.get(nounA);
        Iterable<Integer> idsB = wordToIdMap.get(nounB);
        int ancestor = sap.ancestor(idsA, idsB);
        return synsetList.get(ancestor);
    }

    public static void main(String[] args) {
        WordNet net = new WordNet(args[0], args[1]);
        while (!StdIn.isEmpty()) {
            String nounA = StdIn.readString();
            String nounB = StdIn.readString();
            StdOut.println(net.isNoun(nounA));
            StdOut.println(net.isNoun(nounB));
            int dist = net.distance(nounA, nounB);
            //String ancestor = net.sap(nounA, nounB);
            StdOut.printf("%s, %s dist: %d\n", nounA, nounB, dist);
            //StdOut.printf("%s, %s ancestor: %s", nounA, nounB, ancestor);
        }
    }

}
