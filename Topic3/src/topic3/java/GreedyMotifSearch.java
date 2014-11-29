package topic3.java;

import com.sun.xml.internal.ws.util.QNameMap;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


/**
 * @author Matko
 * @version 1.0
 */
public class GreedyMotifSearch {

    int k, t;
    double[][] profile;
    List<String> dnas = new ArrayList<>();
    List<String> bestMotifs = new ArrayList<>();


    //used for PROFILE problem...
    public GreedyMotifSearch(int k) {
        this.profile = new double[4][k];
        this.k = k;
    }

    public GreedyMotifSearch(int k, int t, List<String> dnas) {
        this.t = t;
        this.k = k;
        this.dnas = dnas;

        this.profile = new double[4][k];
        for (int i = 0;i<4;i++){
            for (int j = 0;j<k;j++){
                profile[i][j] = 0.0;
            }
        }

        //greedyMotifSearch();
        randomizedMotifSearch();
    }

    private void randomizedMotifSearch(){

        Random rand = new Random();
        // int randomNum = rand.nextInt((max - min) + 1) + min;

        for(String dna : dnas) {
            int startingPosition = rand.nextInt((dnas.get(0).length() - k + 1));
            bestMotifs.add(dna.substring(startingPosition,startingPosition+k));
        }

        List<String> motifs = new ArrayList<>();
        motifs.addAll(bestMotifs);

        for (int i = 0;i<1000;i++){
            formProfile(motifs);
            motifs.clear();
            motifs = formMotif();
        }
    }

    private void greedyMotifSearch(){

        for (String dna : dnas){
            bestMotifs.add(dna.substring(0,k+1));
        }

        List<String> motifs = new ArrayList<>();
        for (int i = 0; i < dnas.get(0).length()-k+1; i++) {
            String motif = dnas.get(0).substring(i, i + k);
            motifs.clear();
            motifs.add(motif);

            for (int j = 1;j<t;j++){
                formProfile(motifs);
                String mostProbable = profileMostProbableKmer(dnas.get(j));
                motifs.add(mostProbable);
            }


            if (score(motifs) < score(bestMotifs)){
                bestMotifs.clear();
                bestMotifs.addAll(motifs);
            }
        }

    }

    private List<String> formMotif(){

    }

    private void formProfile(List<String> motifs){
        String[][] motifMatrix = new String[motifs.size()][k];

        //initiate matrix
        for (int i = 0;i<motifs.size();i++){
            String[] nucleotides = motifs.get(i).split("");
            for (int j = 0;j<k;j++){
                motifMatrix[i][j] = nucleotides[j];
            }
        }

        //calculate probabilites
        for (int j = 0;j<k;j++){

            double cntA = 1.0;
            double cntC = 1.0;
            double cntG = 1.0;
            double cntT = 1.0;
            for (int i = 0;i<motifs.size();i++){
                switch (motifMatrix[i][j]){
                    case "A": cntA++; break;
                    case "C": cntC++; break;
                    case "G": cntG++; break;
                    case "T": cntT++; break;
                    default: System.exit(1);
                }
            }
            profile[0][j] = cntA / (motifs.size()+1);
            profile[1][j] = cntC / (motifs.size()+1);
            profile[2][j] = cntG / (motifs.size()+1);
            profile[3][j] = cntT / (motifs.size()+1);
        }

    }

    private int score(List<String> motifs){
        int score = 0;

        String[][] motifMatrix = new String[motifs.size()][k];

        //initiate matrix
        for (int i = 0;i<motifs.size();i++){
            String[] nucleotides = motifs.get(i).split("");
            for (int j = 0;j<k;j++){
                motifMatrix[i][j] = nucleotides[j];
            }
        }

        for (int j = 0;j<k;j++){
            int cntA = 0;
            int cntC = 0;
            int cntG = 0;
            int cntT = 0;

            for (int i = 0;i<motifs.size();i++){
                switch (motifMatrix[i][j]){
                    case "A": cntA++; break;
                    case "C": cntC++; break;
                    case "G": cntG++; break;
                    case "T": cntT++; break;
                    default: System.exit(1);
                }
            }

            //find max
            int max = cntA;
            if (cntC > max) max = cntC;
            if (cntG > max) max = cntG;
            if (cntT > max) max = cntT;

            if (max == cntA) score += motifs.size()-cntA;
            else if (max == cntC) score += motifs.size()-cntC;
            else if (max == cntG) score += motifs.size()-cntG;
            else if (max == cntT) score += motifs.size()-cntT;

            }
        return score;
    }

    private Double calculateProbability(String kMer){
        String[] nucleotides = kMer.split("");
        Double probabilty = 1.0;
        for (int j = 0;j<k;j++){
            switch (nucleotides[j]){
                case "A": probabilty *= profile[0][j]; break;
                case "C": probabilty *= profile[1][j]; break;
                case "G": probabilty *= profile[2][j]; break;
                case "T": probabilty *= profile[3][j]; break;
                default: probabilty *= 1.0;
            }
        }
        return probabilty;
    }

    /**
     * Returns a most probable kmer,
     * UNKNOWN result if more then 1 have the max value
     * NEED TO RESOLVE to return first one from the text!
     * @param text some string
     * @return most probable kmer from the given text
     */
    private String profileMostProbableKmer (String text){

        HashMap<String,Double> probabilites = new HashMap<>();

        for (int i = 0; i < text.length()-k+1; i++) {
            String kMer = text.substring(i, i + k);
            Double probability = calculateProbability(kMer);
            probabilites.put(kMer,probability);
        }

        String mostProbable = "";
        List<String> mostProbables = new ArrayList<>();
        Double maxValueInMap = (Collections.max(probabilites.values()));  // This will return max value in the Hashmap
        for (Map.Entry<String, Double> entry : probabilites.entrySet()) {  // Itrate through hashmap
            if (entry.getValue() >= maxValueInMap) {
                //System.out.println(entry.getKey());
                mostProbables.add(entry.getKey());
            }
        }

        if (mostProbables.size() > 1) {
            HashMap<String, Integer> positions = new HashMap<>();
            for (String probable : mostProbables) {
                int position = text.indexOf(probable);
                positions.put(probable, position);
            }

            Integer minValueInMap = (Collections.min(positions.values()));  // This will return max value in the Hashmap
            for (Map.Entry<String, Integer> entry : positions.entrySet()) {  // Itrate through hashmap
                if (entry.getValue() == minValueInMap) {
                    //System.out.println(entry.getKey());
                    mostProbable = entry.getKey();
                }
            }
        } else mostProbable = mostProbables.get(0);

        return mostProbable;

    }

    public static void main (String[] args) throws IOException{
        File dir = new File("C:\\Users\\Matko\\IntelliJProjects\\Bioinformatics-Algorithms\\Topic3\\src\\topic3\\resources");
        File file1 = new File(dir, "GREEDYMOTIFSEARCH.txt");
        Path filepath = file1.toPath();

        List<String> lines = Files.readAllLines(filepath);

        int k = Integer.parseInt(lines.get(0));
        int t = Integer.parseInt(lines.get(1));

        List<String> dnas = lines.subList(2,lines.size());

        GreedyMotifSearch gms = new GreedyMotifSearch(k,t,dnas);

        for (String motif : gms.bestMotifs){
            System.out.println(motif+" ");
        }


        //INPUT FOR Profile-most Probable k-mer Problem!!!
//        String text = lines.get(0);
//        int k = Integer.parseInt(lines.get(1));
//
//        GreedyMotifSearch gms = new GreedyMotifSearch(k);
//
//        //initialiaze a profile
//        for (int i = 0; i<4;i++){
//            String line = lines.get(i+2);
//            String[] splited = line.split("\\s+");
//            for (int j = 0;j<k;j++){
//                gms.profile[i][j] = Double.parseDouble(splited[j]);
//            }
//        }
//
//        gms.profileMostProbableKmer(text);

    }


}
