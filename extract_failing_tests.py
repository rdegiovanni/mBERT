import subprocess
import os
import argparse


def run_cmd_task(project_path, command):
    try:
        result = subprocess.run(command, cwd=project_path, check=True, capture_output=True, text=True)
        # print(result.stdout)

        if "BUILD SUCCESS" in result.stdout:  # Check if the build was successful
            print("<<< INFO: Build successful! <<< " + command.__str__())
            return "BUILD SUCCESS"
        else:
            print("<<< ERROR: Build failure!")  # Unnecessary, but just to be explicit. :)
            # print(result.stdout)
            return "BUILD FAILURE"  # if build fails it will raise called error, it will be caught by the except block below
    except subprocess.CalledProcessError as e:
        print(f"<<< ERROR: Command failed with exit code {e.returncode}")  # Handle error if the Maven command fails
        print("<<< ERROR: Build failed! <<< " + command.__str__())
        # print(e.output)
        # print(e.stdout)
        # print(e.stderr)
        return e.stdout


def extract_errors(output):
    # Use sed to extract characters between "[ERROR] " and "-- Time elapsed"
    sed_command = "sed -n 's/\[ERROR\] \(.*\) -- Time elapsed.*/\\1/p'"
    try:
        result = subprocess.run(sed_command, input=output, shell=True, check=True, capture_output=True, text=True)
        return result.stdout.strip()
    except subprocess.CalledProcessError as e:
        print(f"<<< ERROR: sed command failed with exit code {e.returncode}")
        print(e.stderr)
        return None


if __name__ == "__main__":
    # Example: Provide the project path as a command-line argument
    # project_path = input("Enter the project path: ")
    arguments = argparse.ArgumentParser()
    arguments.add_argument("-p", "--project_path", help="Path to the project")
    arguments.add_argument("-m", "--mutant_name", help="Mutant name")
    arguments.add_argument("-s", "--source_file_name", help="Source file name")
    arguments.add_argument("-f", "--killing_matrix_file", help="Path to the killing matrix file")
    arguments.add_argument("-x", "--mutant_path", help="Path to the mutant file")
    args = arguments.parse_args()

    print("<<< INFO: Executing mutant {} - of source file {} >>>".format(args.mutant_name, args.source_file_name))
    print("<<< INFO: All args: " + args.__str__())

    # if csv file is empty, append it with the header
    if os.stat(args.killing_matrix_file).st_size == 0:
        with open(args.killing_matrix_file, "a") as killing_matrix_file:
            killing_matrix_file.write("Source_file_name,Mutant_id,Failing_tests\n")

    # Run Maven clean compile lifecycle task
    compile_command = ['mvn', 'clean', 'compile']
    compile_output = run_cmd_task(args.project_path, compile_command)  # output "BUILD SUCCESS" or mvn error message. For compile we don't need to check
    # for error
    if compile_output == "BUILD SUCCESS":  # If build successful, run Maven test task
        test_command = ['mvn', 'test']
        test_output = run_cmd_task(args.project_path, test_command)
        errors = extract_errors(test_output)  # If test not successful, extract and print errors (if any)
        if errors is None:
            print("<<< ERROR: Cannot parse and extract errors")
        else:
            failing_tests = ""
            if errors != "":
                print("<<< INFO: Failing tests for mutant:")
                print(errors.splitlines().__str__())
                failing_tests = "|".join(errors.splitlines())

            string_to_write_in_csv = args.source_file_name + "," + args.mutant_name + "," + failing_tests + "\n"
            print(string_to_write_in_csv)
            with open(args.killing_matrix_file, "a") as killing_matrix_file:
                killing_matrix_file.write(string_to_write_in_csv)
    else:
        print("<<< INFO: Mutant {} failed to compile >>> Delete mutant".format(args.mutant_name))
        run_command = ['rm', args.mutant_path]
        result = subprocess.run(run_command, check=True, capture_output=True, text=True)
        if result.returncode == 0:
            print("<<< INFO: Mutant {} deleted >>>".format(args.mutant_name))
