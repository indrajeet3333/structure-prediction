import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class rna {
    public static int[][] memo = new int[100][100];
    public static int[][] traceback = new int[100][100];
    public static List<String> tb = new ArrayList<>();

    public static boolean checkPair(char a,char b){
        if (a == 'A' && b == 'U')
            return true;
        else if(a == 'U' && b == 'A')
            return true;
        else if(a == 'G' && b == 'C')
            return true;
        else if(a == 'C' && b == 'G')
            return true;
        else
            return false;
    }

    public static int maxBasePairs(String s, int i,int j){
        int max1 = 0,max2 = 0,max3 = 0,maxIO = 0,kValue=0;
        if (memo[i][j] != 0){
            return memo[i][j];
        }
        if (i == j || j == (i-1))
            return 0;
        else{
            if (checkPair(s.charAt(i),s.charAt(j)))
                max1 = maxBasePairs(s,i+1,j-1) + 1;
            else{
                max2 = maxBasePairs(s, i + 1, j - 1);
            }
            for (int k = i; k < j; k++) {
                maxIO = maxBasePairs(s,i,k) + maxBasePairs(s,k+1,j);
                if(maxIO >= max3) {
                    max3 = maxIO;
                    kValue = k;
                }
            }
        }
        memo[i][j] = Math.max(Math.max(max1,max2),max3);

        if(memo[i][j] == max1 && max1 != 0){
            traceback[i][j] = -1;
        }else if (memo[i][j] == max2 && max2 != 0){
            traceback[i][j] = -2;
        }else{
            traceback[i][j] = kValue;
        }

        return memo[i][j];
    }

    public static void printPairs(int i,int j){
        if (i == j || j == (i-1))
            return;
        if (traceback[i][j] == -1){
            tb.set(i, "{");
            tb.set(j, "}");
            printPairs(i+1,j-1);
        }
        else if (traceback[i][j] == -2){
            printPairs(i+1,j-1);
        }
        else{
            printPairs(i,traceback[i][j]);
            printPairs(traceback[i][j]+1,j);
        }
    }

    public static String readInput(String readPath) throws IOException {
        File file = new File(readPath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String read,s = "";
        while ((read = br.readLine()) != null){
            s = read;
        }
        if(s.equals("") || s.equals("name of the sequence")) return "inv";
        return s;
    }

    public static void writeOutput(String s, int max_pairs,String writePath) throws IOException {
        FileWriter fw = new FileWriter(writePath);
        fw.write(s+"\n");
        for (int i = 0; i < tb.size(); i++) {
            fw.write(tb.get(i));
        }
        fw.write("\n");
        fw.write("max count of pairs\n");
        fw.write(max_pairs +"\n");
        fw.close();
    }

    public static void main(String[] args) throws IOException {
        String inFile = "rna_input";                                         //Input file name
        String outFile = "rna_output";                                       //Output file name
        String readPath = "C:\\Users\\Indrajeet\\Desktop\\"+inFile+".txt";   //Path to read input from
        String writePath="C:\\Users\\Indrajeet\\Desktop\\"+outFile+".txt";   //Path to write output to

        String s = readInput(readPath);                                      //Read input from text file at the said path
        if (s.equals("inv")) System.out.println("Invalid Input Detected");   //Invalid Input Check
        int n = s.length();                                                  //Length of the RNA Sequence

        for (int i = 0; i < n; i++) tb.add(".");                             //Create traceback string

        long start = System.nanoTime();                                      //Log start time
        int max_pairs = maxBasePairs(s,0,n-1);                               //Objective function
        long end = System.nanoTime();                                        //Log end time

        printPairs(0,n-1);                                                   //Traceback function

        //Print output to console
        System.out.println(s);                                               //Print the input RNA sequence
        tb.forEach(System.out::print);                                       //Print the parenthesizations
        System.out.println("\nmax count of pairs");                          //Print output message
        System.out.println(max_pairs);                                       //Print the maximum pair count

        writeOutput(s,max_pairs,writePath);                                  //Write output to text file at the said path

        //Ignore - Performance Stats
        //System.out.println("\nPerformance Stats\n" + "Length of RNA Structure: " + n+"\nExecution Time: "+ String.format("%.02f", (float)(end-start)/1000000) + " ms");
    }
}