#!/usr/bin/python3

# LOCAL TESTING FOR STUDENT PURPOSES

import sys
import json
import os,subprocess
import datetime
import time
from subprocess import STDOUT,PIPE

global pkg_name
global main_java_file
test_dir = "./tests"

def required_files() -> []:
    return [main_java_file]

def required_java_files() -> []:
    paths = []
    for f in required_files():
        paths.append(f'''{f}.java''')
    return paths


def check_files(paths, base="."):
    """Checks that the files in the given list exist in the base directory.

    Returns a list of missing files.

    eg. check_files(['src/calculator.py'])
    """
    missing_files = []
    for path in paths:
        target_path = os.path.join(base, path)
        if not os.path.isfile(target_path):
            missing_files.append(path)

    if len(missing_files) == 0:
        return None
    return missing_files




def compare_files(actual, expected, max_len=1000):
    line_no = 0
    try:
        with open(actual) as f_actual:
            with open(expected) as f_expected:
                line_actual = f_actual.readline()
                line_expected = f_expected.readline()
                while line_actual or line_expected:
                    if line_actual != line_expected:
                        print("*** TEST FAILED on output at line {} ***".format(line_no))
                        print("=== Actual output (not as expected) ===")
                        print(line_actual)

                        print("=== Expected output ===")
                        print(line_expected)
                        print("\tYour output is not correct; debug it on small examples.")
                        return False
                    line_actual = f_actual.readline()
                    line_expected = f_expected.readline()
                    line_no += 1
    except:
        print("[ERROR] An exception occurred while opening output files")
        return None
    return True



def compile_java_files():
    # first check for any missing files
    missing_files = check_files(required_java_files(), f'''{pkg_name}/''')
    if missing_files:
        print("[ERROR] Missing java files from directory: {}".format(missing_files))
        return False

    # compile the required java files
    for f in required_files():
        java_file = f'''{pkg_name}/{f}.java'''

        print("Compiling {}".format(java_file))
        try:
            javac_cmd = ['javac', '-encoding', 'UTF-8', java_file]
            res = subprocess.check_output(javac_cmd, stderr=STDOUT)
        except subprocess.CalledProcessError as e:
            print("[ERROR] Couldn't compile {}".format(java_file))
            print("\twith the command {}".format(' '.join(javac_cmd)))
            print(e.output.decode("utf-8"))
            return False
        print("Compilation of {} successful.\n".format(java_file))
    return True

def test_f(test):
    # check that any required infile & outfiles exist
    if test['infile'] and test['outfile']:
        paths = [test['infile'], test['outfile']]
        missing_files = check_files(paths, test_dir)
        if missing_files:
            print("[ERROR]: Missing files from test directory: {}.".format(missing_files))
            return False
    else:
        print("[ERROR]: Test {} is missing an input or output file.".format(test['name']))
        return False

    actual_output = f'''{pkg_name}/out.txt'''

    # check that the java class file exists in the right spot
    java_class = f'''{pkg_name}/{main_java_file}.class'''
    missing_file = check_files([java_class])
    if missing_file:
        print( "Missing class file: {}; does your file have the correct \"package {} \"declaration?".format(missing_file, pkg_name))
        return False

    print(f'''Running test {test['name']}''')
    class_name = java_class.split('.')[0]
    try:
        flags = test['flags'] if 'flags' in test else []
        params = test['params'] if 'params' in test else []
        java_cmd = ['java'] + flags + [class_name] + params + [f'''{test_dir}/{test['infile']}''', actual_output]
        # java_cmd = ['java'] + test['flags'] + ['-cp', 'classes', class_name] +test['params'] + [f'''{test_dir()}/{test['infile']}''', actual_output]
        print("Running {}".format(' '.join(java_cmd)))

        start_time = datetime.datetime.now()
        res = subprocess.check_output(java_cmd, stderr=STDOUT)
        end_time = datetime.datetime.now()

    except subprocess.CalledProcessError as e:
        # Note: CalledProcessError raised if non-zero exit code res.
        # import traceback
         #traceback.print_exc()
        print(e.output.decode("utf-8")[:400])
        exception_name = "unrecognized runtime exception"
        exception_name = "OutOfMemoryError" if "OutOfMemory" in e.output.decode("utf-8") else exception_name
        exception_name = "StackOverflowError" if "StackOverflow" in e.output.decode("utf-8") else exception_name
        exception_name = "NumberFormatException" if "NumberFormatException" in e.output.decode("utf-8") else exception_name
        exception_name = "IOException" if "IOException" in e.output.decode("utf-8") else exception_name
        print(f'''\t{class_name} exited with a non-zero exit code {e.returncode} ({exception_name})''')
        os.remove(actual_output)
        return 0

    res = compare_files(actual_output, f'''{test_dir}/{test['outfile']}''')
    os.remove(actual_output)

    if res:
        print(f'''\tTEST PASSED - {test['name']}.''')
        print("\tTime taken: {}".format(end_time - start_time))
    else:
        print(f'''\tTest FAILED - {test['name']}.''')



def load_test(json_data):
    return {
        'name': json_data["name"] if("name" in json_data) else None,
        'params': json_data["params"] if ("params" in json_data) else [],
        'infile': json_data['infile'] if ("infile" in json_data) else None,
        'outfile': json_data['outfile'] if ("outfile" in json_data) else None,
        'flags': json_data['flags'] if ("flags" in json_data) else [],
    }

def load_config(f):
    # load the JSON file
    data = json.load(f)

    # print the data if you want to see it
    # print(json.dumps(data, indent=4))

    # load the config data specific to this lab and this part;
    # configure this by changing the JSON file
    global pkg_name
    pkg_name = data['pkg_name']
    global main_java_file
    main_java_file = data['main_java_file']

    # compile the required java files
    compile_java_files()

    # run the tests that are listed in the config file
    tests = data['test_cases']
    for tst in data['test_cases']:
        t = load_test(tst)
        print("*" * 80)
        print(f'''\nTest {t.get('name')} loaded.''')
        # print(t)
        test_f(t)
        time.sleep(2)
    return True



if __name__ == "__main__":
    # We need a config file to run the test script
    if len(sys.argv) < 2:
        print("[ERROR] No json config file provided as first parameter.")
        sys.exit(1)

    fname = sys.argv[1]
    try:
        f = open(fname, 'r')
        load_config(f)
    except FileNotFoundError as e:
        print("[ERROR] Failed to load config file: {}".format(e))
        sys.exit(1)
    except KeyError as e:
        print("[ERROR]: Failed to find necessary key in config file: {}".format(e))
        sys.exit(1)


