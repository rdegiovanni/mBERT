import mutation.CodeBERTMutation;
import mutation.Settings;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws IOException, InterruptedException {
        boolean printMutants = false;
        boolean saveMutants = false;
        String codeToBeMutated = null;
        String directoryToWriteMutants = null;
        int N = -1;
        List<String> methods = new LinkedList<>();
        List<Integer> methodLines = new LinkedList<>();
        List<Integer> linesToMutate = new LinkedList<>();
        for (int i = 0; i< args.length; i++ ) {
            if (args[i].startsWith("-in=")) {
                codeToBeMutated = args[i].replace("-in=", "");
            } else if (args[i].startsWith("-out=")) {
                directoryToWriteMutants = args[i].replace("-out=", "");
                saveMutants = true;
            } else if (args[i].startsWith("-printMutants")) {
                printMutants = true;

            } else if (args[i].startsWith("-N=")) {
                N = Integer.parseInt(args[i].replace("-N=", ""));
            }
            else if (args[i].startsWith("-m=")) {
                String method = args[i].replace("-m=", "");
                int line = 0;
                if (method.contains(":")) {
                    String [] ml = method.split(":");
                    method = ml[0];
                    line = Integer.parseInt(ml[1]);
                }
                methods.add(method);
                methodLines.add(line);
            }
            else if (args[i].startsWith("-l=")) {
                int line = Integer.parseInt(args[i].replace("-l=", ""));
                linesToMutate.add(line);
            }
        }

        if (codeToBeMutated == null) {
            correctUssage();
            return;
        }

        if (saveMutants && (directoryToWriteMutants == null || directoryToWriteMutants == "")) {
            directoryToWriteMutants = codeToBeMutated.substring(0, codeToBeMutated.lastIndexOf('.')) + "/codebert-mutants/";
        }

        if (N > 0)
            Settings.MAX_NUM_OF_MUTANTS = N;

        System.out.println("Input: " + codeToBeMutated);
        if (saveMutants)
            System.out.println("Output: " + directoryToWriteMutants);

        System.out.println(Settings.printSettings());

        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);

        //set methods to mutate
        for (int index = 0; index < methods.size(); index++) {
            String m = methods.get(index);
            int line = methodLines.get(index);
            mutation.setMethodToMutate(m,line);
        }

        //set lines to mutate
        mutation.setLinesToMutate(linesToMutate);

        mutation.generateMutants();

        if (printMutants) {
            List<String> allMutants = mutation.getAllMutants();
            for (String m : allMutants) {
                System.out.println(m + "\n***************\n");
            }
        }

        if (saveMutants) {
            System.out.println("Saving mutants...");

            File outfolder = new File(directoryToWriteMutants);
            if (!outfolder.exists())
                outfolder.mkdirs();

            mutation.writeMutants(directoryToWriteMutants);
        }

        System.out.println(Settings.printSettings());
    }

    private static void correctUssage() {
        System.out.println("Use ./mBERT.sh \n" +
                "-in=source_file_name \n" +
                "-out=mutants_directory\n" +
                "-N=max_num_of_mutants\n"+
                "-m=method_name\n"+
                "-m=method_name:method_definition_line\n"+
                "-l=line_to_mutate\n");
    }
}
