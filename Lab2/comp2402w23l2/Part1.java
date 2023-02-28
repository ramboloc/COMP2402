package comp2402w23l2;

// You may not import any other classes; if you do, the autograder will fail.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.ListIterator;


/**
 *  You can run this file from the command line from *outside* the comp2402w23l2 dir:
 *      % java comp2402w23l2.Part1 <x> <input.txt> <output.txt>
 *  will take input from <input.txt> and will print to <output.txt>, or
 *      % java comp2402w23l2.Part1 <x> <input.txt>
 *  will take input from <input.txt> and will print to the terminal, or
 *      % java comp2402w23l2.Part1 <x>
 *  will take input from the terminal and will print to the terminal.
 *  You can also run the provided python3 script that runs a suite of local tests:
 *      % python3 run_local_tests.py student_config_lab2p1.json
 *  which compiles and runs this program on the files in the tests/ directory and
 *  compares the output to the expected output in the tests/ directory.
 */


public class Part1 {

    /**
     * Read lines one at a time from r.  Outputs to w according to the
     * lab specifications.
     * Assumes every line is an integer; otherwise it throws a NumberFormatException.
     * @param x the input variable x
     * @param r the reader to read from
     * @param w the writer to write to
     * @throws IOException, NumberFormatException
     */

    public static void execute(int x, BufferedReader r, PrintWriter w) throws IOException, NumberFormatException  {
        // TODO(student): Your code goes here.
        if (r == null || !r.ready()) {
            w.println(0);
            return;
        }

        int xPow2 = (x % 240223) * (x % 240223) % 240223;
        RootishArrayStack<Integer> arrPow = new RootishArrayStack<>();
        int ans = 0;
        int idx = 0;
        while(r.ready()) {
            int currInt = Integer.parseInt(r.readLine());
            if (currInt % xPow2 == 0) {
                arrPow.add(currInt);
                ans = 0;
                idx = 0;
                continue;
            }
            if (currInt % x == 0 && idx < arrPow.size()) {
                ans = ans + ((currInt % 240223) * (arrPow.get(idx++) % 240223)) % 240223;
                ans = ans % 240223;
            }
        }

        w.println(ans);
    }

    // YOU SHOULD NOT NEED TO MODIFY BELOW THIS LINE


    /**
     * The driver.  Open a BufferedReader and a PrintWriter, either from System.in
     * and System.out or from filenames specified on the command line, then call doIt.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            BufferedReader r;
            PrintWriter w;
            int x;
            if (args.length == 0) {
                x = 2402;
                r = new BufferedReader(new InputStreamReader(System.in));
                w = new PrintWriter(System.out);
            } else if (args.length == 1) {
                x = Integer.parseInt(args[0]);
                r = new BufferedReader(new InputStreamReader(System.in));
                w = new PrintWriter(System.out);
            } else if (args.length == 2) {
                x = Integer.parseInt(args[0]);
                r = new BufferedReader(new FileReader(args[1]));
                w = new PrintWriter(System.out);
            } else {
                x = Integer.parseInt(args[0]);
                r = new BufferedReader(new FileReader(args[1]));
                w = new PrintWriter(new FileWriter(args[2]));
            }
            long start = System.nanoTime();
            try {
                execute(x, r, w);
            } catch (NumberFormatException e) {
                System.err.println("Your input must be integer only");
                System.err.println(e);
                System.exit(-1);
            }
            w.flush();
            long stop = System.nanoTime();
            System.out.println("Execution time: " + 1e-9 * (stop - start));
        } catch (IOException e) {
            System.err.println(e);
            System.exit(-2);
        }
    }
}
