package topic6.java;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Matko
 * @version 1.0
 * CODE CHALLENGE: Implement GREEDYSORTING.
Input: A permutation P.
Output: The sequence of permutations corresponding to applying GREEDYSORTING to P, ending with
the identity permutation.

Sample Input:
(-3 +4 +1 +5 -2)

Sample Output:
(-1 -4 +3 +5 -2)
(+1 -4 +3 +5 -2)
(+1 +2 -5 -3 +4)
(+1 +2 +3 +5 +4)
(+1 +2 +3 -4 -5)
(+1 +2 +3 +4 -5)
(+1 +2 +3 +4 +5)

 Algorithm:
GREEDYSORTING(P)
    approxReversalDistance ← 0
    for k = 1 to |P|
        if element k is not sorted
            apply the k-sorting reversal to P
        approxReversalDistance ← approxReversalDistance + 1
        if k-th element of P is −k
            apply the k-sorting reversal to P
            approxReversalDistance ← approxReversalDistance + 1
    return approxReversalDistance

 ----------------------------------------------------------------------------------------

Number of Breakpoints Problem: Find the number of breakpoints in a permutation.
Input: A permutation.
Output: The number of breakpoints in this permutation.

CODE CHALLENGE: Solve the Number of Breakpoints Problem.

Sample Input:
(+3 +4 +5 -12 -8 -7 -6 +1 +2 +10 +9 -11 +13 +14)

Sample Output:
8
 */
public class GreedySorting extends LoadAndExecute {

    private List<Integer> p = new ArrayList<>();
    private PrintWriter writer;
    private int numberOfBreakpoints = 0;
    private int numberOfAdjacencies = 0;

    private int numberOfSteps = 0;

    protected List<String> addPlusToPositive(List<Integer> p){
        List<String> retList = new ArrayList<>();
        for (Integer num : p){
            if (num > 0) retList.add("+"+num.toString());
            else retList.add(num.toString());
        }
        return retList;
    }

    private void openWriter(String outFileName){
        String out = "C:\\Users\\Matko\\IntelliJProjects\\Bioinformatics-Algorithms\\Topic6\\src\\topic6\\out\\"+outFileName;
        try {
            writer = new PrintWriter(out, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writePtoFile(){
        numberOfSteps++;
        List<String> out = addPlusToPositive(p);
        writer.print("(");
        for (int i = 0;i<out.size();i++){
            if (i ==  out.size()-1) writer.print(out.get(i));
            else writer.print(out.get(i)+" ");

        }
        writer.println(")");
    }

    private void reverseP(int k){

        int secondIndex = 0;

        for (int i = 0;i<p.size();i++){
            if (Math.abs(p.get(i)) == k+1 ) {
                secondIndex = i;
                break;
            }
        }

        List<Integer> tempList = p.subList(k,secondIndex+1);
        Collections.reverse(tempList);

        List<Integer> reverseSignList = new ArrayList<>();
        for (Integer num : tempList) reverseSignList.add(num * -1);

        List<Integer> finalList = new ArrayList<>();

        finalList.addAll(p.subList(0,k));
        finalList.addAll(k,reverseSignList);
        finalList.addAll(p.subList(k+reverseSignList.size(),p.size()));
        p.clear();
        p = finalList;
        writePtoFile();
    }

    private void sort(){

        for (int k = 0;k<p.size();k++){
            if (Math.abs(p.get(k)) != k+1) reverseP(k);

            if (p.get(k) < 0){
                int tmp = Math.abs(p.get(k));
                p.remove(k);
                p.add(k,tmp);
                writePtoFile();
            }
        }
    }

    private void findAdjancencies(){
        for (int i = 1;i<p.size();i++){
            if ( p.get(i) == p.get(i-1)+1 ) numberOfAdjacencies++;
        }
    }


    @Override
    public void execute(String fileName) {
        List<String> lines = loadFromFiles(fileName);
        openWriter(fileName);

        //fill list p
        String inputLine = lines.get(0);
        inputLine = inputLine.replaceAll("\\(","");
        inputLine = inputLine.replaceAll("\\)","");
        String[] inputLineArray = inputLine.split(" ");
        for (String lineComp : inputLineArray) p.add(Integer.parseInt(lineComp));


        if (fileName.equals("Breakpoints.txt")){
            p.add(0,0);
            p.add(p.get(p.size()-1)+1);
            findAdjancencies();
            numberOfBreakpoints = p.size()-1 - numberOfAdjacencies;
        } else{ //it is greedy sort
            sort();

        }
        writer.flush();

    }

    public int getNumberOfBreakpoints() {
        return numberOfBreakpoints;
    }

    public int getNumberOfSteps() {
        return numberOfSteps;
    }
}
