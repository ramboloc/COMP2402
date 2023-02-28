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
 *      % java comp2402w23l2.Part6 <input.txt> <output.txt>
 *  will take input from <input.txt> and will print to <output.txt>, or
 *      % java comp2402w23l2.Part6 <input.txt>
 *  will take input from <input.txt> and will print to the terminal, or
 *      % java comp2402w23l2.Part6
 *  will take input from the terminal and will print to the terminal.
 *  You can also run the provided python3 script that runs a suite of local tests:
 *      % python3 run_local_tests.py student_config_lab2p6.json
 *  which compiles and runs this program on the files in the tests/ directory and
 *  compares the output to the expected output in the tests/ directory.
 */


public class Part6 {

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
            return;
        }

        ArrayDeque<ArrayDeque<Integer>> arrList = new ArrayDeque<>();
        while(r.ready()) {
            ArrayDeque<Integer> currArr = new ArrayDeque<>();
            for (int i = 0; i < x && r.ready(); i++) {
                int currInt = Integer.parseInt(r.readLine());
                currArr.add(currInt);
            }
            arrList.add(currArr);
        }

        ArrayDeque<Integer> lastArr = arrList.get(arrList.size() - 1);
        int[] actions = new int[lastArr.size()];
        int[] groups = new int[lastArr.size()];
        for (int i = 0; i < actions.length; i++) {
            int curr = lastArr.get(i);
            actions[i] = curr % 2;
            groups[i] = curr / 2;
        }

        ArrayDeque<Integer> srcArr = null;
        ArrayDeque<Integer> targetArr = null;
        for (int i = 0; i < actions.length; i++) {
            int srcGroup = groups[i] % arrList.size();
            if (actions[i] == 0) {
                int targetGroup = srcGroup == arrList.size() - 1 ? 0 : srcGroup + 1;
                srcArr = arrList.get(srcGroup);
                targetArr = arrList.get(targetGroup);
                int ele = srcArr.remove(srcArr.size() - 1);
                targetArr.add(0, ele);
            } else {
                int targetGroup = srcGroup == 0 ? arrList.size() - 1 : srcGroup - 1;
                srcArr = arrList.get(srcGroup);
                targetArr = arrList.get(targetGroup);
                int ele = srcArr.remove(0);
                targetArr.add(ele);
            }
            if (arrList.get(srcGroup).size() == 0) {
                arrList.remove(srcGroup);
            }
        }

        for (int i = 0; i < arrList.size(); i++) {
            ArrayDeque<Integer> currArr = arrList.get(i);
            if (currArr.size() == 0) {
                continue;
            }
            for (int j = currArr.size() - 1; j >= 0; j--) {
                w.println(currArr.get(j));
            }
            w.println("****");
        }
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
