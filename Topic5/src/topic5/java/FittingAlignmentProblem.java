package topic5.java;

import java.util.*;

/**
 * @author Matko
 * @version 1.0
 *


CODE CHALLENGE: Solve the Fitting Alignment Problem.
Input: Two nucleotide strings v and w, where v has length at most 1000 and w has length at most 100.
Output: A highest-scoring fitting alignment between v and w. Use the simple scoring method in which
matches count +1 and both the mismatch and indel penalties are 1.

Sample Input:
GTAGGCTTAAGGTTA
TAGATA

Sample Output:
2
TAGGCTTA
TAGA--TA
 */
public class FittingAlignmentProblem extends GlobalAlignmentProblem {

    int maxRowindex = 0, maxColumnindex = 0;


    protected void findAlignment(int i, int j){

        if (j == 0) return;

        switch (backtrack[i][j]){
            case "down":
                vOut.add(v[i-1]);
                wOut.add("-");

                findAlignment(i - 1, j);
                break;

            case "right":
                vOut.add("-");
                wOut.add(w[j-1]);

                findAlignment(i, j - 1);
                break;

            case "diagonal":
                vOut.add(v[i-1]);
                wOut.add(w[j-1]);
                findAlignment(i - 1, j - 1);
                break;

            default:
                return;

        }
    }

    private void formLastNode(){
        int maxValue = 0;
        maxColumnindex = w.length;
        for (int i = 1; i < s.length; i++) {
            if (s[i][w.length] > maxValue) {
                maxValue = s[i][w.length];
                maxRowindex = i;
            }
        }
    }

    protected void formBacktrack(){
        backtrack = new String[v.length+1][w.length+1];
        s = new int[v.length+1][w.length+1];

        for (int i =1;i<v.length+1;i++){
            for (int j = 1;j<w.length+1;j++){

                List<Integer> values = new ArrayList<>();
                values.add(s[i-1][j]-1);
                values.add(s[i][j-1]-1);

                if (v[i-1].equals(w[j-1])) values.add(s[i-1][j-1] + 1);
                else values.add(s[i-1][j-1] - 1);

                s[i][j] = Collections.max(values);

                int index = 0;
                for (int k = 0;k<values.size();k++){
                    if (values.get(k) == s[i][j]) index = k;
                }

                switch (index){
                    case 0: {
                        backtrack[i][j] = "down";
                        break;
                    }
                    case 1: {
                        backtrack[i][j] = "right";
                        break;
                    }
                    default:
                        backtrack[i][j] = "diagonal";
                }
            }
        }


        //form last node of a backtrack matrix
        formLastNode();

    }

    @Override
    public void execute() {

        List<String> lines = loadFromFiles("FittingAlignmentProblem.txt");
        v = lines.get(0).split("");
        w = lines.get(1).split("");

        formBacktrack();
        findAlignment(maxRowindex, maxColumnindex);
        score = s[maxRowindex][maxColumnindex];

        Collections.reverse(vOut);
        Collections.reverse(wOut);

        //printing
        System.out.println(score);

        for (int i = 0;i<vOut.size();i++){
            System.out.print(vOut.get(i));
        }
        System.out.println();
        for (int i = 0;i<wOut.size();i++){
            System.out.print(wOut.get(i));
        }

    }


}
