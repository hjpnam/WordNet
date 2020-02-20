/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        String maxDistString = nouns[0];
        int maxDist = 0;
        for (String currNoun : nouns) {
            int dist = 0;
            for (String otherNoun : nouns) {
                dist += wordnet.distance(currNoun, otherNoun);
            }
            if (dist > maxDist) {
                maxDist = dist;
                maxDistString = currNoun;
            }
        }
        return maxDistString;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
