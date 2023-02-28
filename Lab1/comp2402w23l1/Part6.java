package comp2402w23l1;

// You may not import any other classes; if you do, the autograder will fail.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Iterator;



/**
 *  You can run this file from the command line from *outside* the comp2402w23l1 dir:
 *      % java comp2402w23l1.Part6 <input.txt> <output.txt>
 *  will take input from <input.txt> and will print to <output.txt>, or
 *      % java comp2402w23l1.Part6 <input.txt>
 *  will take input from <input.txt> and will print to the terminal, or
 *      % java comp2402w23l1.Part6
 *  will take input from the terminal and will print to the terminal.
 *  You can also run the provided python3 script that runs a suite of local tests:
 *      % python3 run_local_tests.py student_config_lab1p6.json
 *  which compiles and runs this program on the files in the tests/ directory and
 *  compares the output to the expected output in the tests/ directory.
 */


public class Part6 {

    /**
     * Read lines one at a time from r.  Outputs to w according to the
     * lab specifications.
     * Assumes every line is an integer; otherwise it throws a NumberFormatException.
     * @param r the reader to read from
     * @param w the writer to write to
     * @throws IOException, NumberFormatException
     */
    public static void execute(BufferedReader r, PrintWriter w) throws IOException, NumberFormatException {
        // TODO(student): Your code goes here.
        ArrayList<Integer> arrayList=new ArrayList<>();

        ArrayList<ArrayList<Integer>> arrayLists=new ArrayList<>();
        // Read the number of the first line
        int v;

        for (String line = r.readLine(); line != null; line = r.readLine()){
            arrayList.add(Integer.parseInt(line));
            // arrayDeque2.add(Integer.parseInt(line));
        }
//        int flag=0;
        int temp;
//        int times=arrayDeque2.size();
        if(arrayList.size()>0){

//            v=arrayList.get(arrayList.size()-1);
            v=arrayList.get(arrayList.size()-1);
            for(int i=0;i<v;i++){
                arrayLists.add(new ArrayList<>());
            }
            for(int j=arrayList.size()-1;j>=0;j--){
                temp=arrayList.get(j);
                arrayLists.get(temp%v).add(temp);
            }
            for (ArrayList<Integer> list : arrayLists) {
                arrayList.clear();
                arrayList = list;
                for (Integer integer : arrayList) {
                    w.println(integer);
                }
            }
        }


//        while (!arrayDeque.isEmpty()){
//            w.println(arrayDeque.pollLast());
//        }
    }

    // YOU SHOULD NOT NEED TO MODIFY BELOW THIS LINE

    /**
     * The driver.  Open a BufferedReader and a PrintWriter, either from System.in
     * and System.out or from filenames specified on the command line, then call doIt.
     * @param args
     */
    public static void main(String[] args) {
        try {
            BufferedReader r;
            PrintWriter w;
            if (args.length == 0) {
                r = new BufferedReader(new InputStreamReader(System.in));
                w = new PrintWriter(System.out);
            } else if (args.length == 1) {
                r = new BufferedReader(new FileReader(args[0]));
                w = new PrintWriter(System.out);
            } else {
                r = new BufferedReader(new FileReader(args[0]));
                w = new PrintWriter(new FileWriter(args[1]));
            }
            long start = System.nanoTime();
            try {
                execute(r, w);
            } catch (NumberFormatException e) {
                System.err.println( "Your input must be integer only");
                System.err.println(e);
                System.exit(-1);
            }
            w.flush();
            long stop = System.nanoTime();
            System.out.println("Execution time: " + 1e-9 * (stop-start));
        } catch (IOException e) {
            System.err.println(e);
            System.exit(-2);
        }
    }
}
