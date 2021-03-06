package topic5.java;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * @author Matko
 * @version 1.0
 */
public class ManhattanTourist {

    int n,m;
    int[][] down;
    int[][] right;
    int[][] s;

    public int getLongestManhattanPath(){
        s = new int[n+1][m+1];

        for (int i = 1;i<n+1;i++){
            s[i][0] = s[i-1][0] + down[i-1][0];
        }

        for (int j = 1;j<m+1;j++){
            s[0][j] = s[0][j-1] + right[0][j-1];
        }

        for (int i = 1;i<n+1;i++){
            for (int j = 1;j<m+1;j++){
                s[i][j] = Integer.max(s[i-1][j]+down[i-1][j],s[i][j-1]+right[i][j-1]);
            }
        }

        return s[n][m];
    }

    public void loadFromFiles() throws IOException {

        File dir = new File("C:\\Users\\Matko\\IntelliJProjects\\Bioinformatics-Algorithms\\Topic5\\src\\topic5\\resources");
        File file = new File(dir, "ManhattanTourist.txt");

        List<String> lines = Files.readAllLines(file.toPath());

        String[] nm = lines.get(0).split(" ");
        n = Integer.parseInt(nm[0]);
        m = Integer.parseInt(nm[1]);

        //initiate matrixes
        down = new int[n][m+1];
        right = new int[n+1][m];

        //load down matrix
        for (int i = 1;i<n+1;i++){
            String[] line = lines.get(i).split(" ");
            for (int j = 0;j<m+1;j++){
                down[i-1][j] = Integer.parseInt(line[j]);
            }
        }

        //load right matrix
        for (int i = n+2;i<n+2+n+1;i++){
            String[] line = lines.get(i).split(" ");
            for (int j = 0;j<m;j++){
                right[i-n-2][j] = Integer.parseInt(line[j]);
            }
        }

    }
}
