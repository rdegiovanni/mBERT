package codebert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import mutation.Settings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CodeBERT {


    public static int numOfTimeout = 0;
    public static int numOfError = 0;
    public static int numOfCalls = 0;

    public static enum CodeBERTResult {
        SUCCEEDED,
        TIMEOUT,
        ERROR;

        public boolean inconclusive () { return this == TIMEOUT || this == ERROR; }
    }

    public static List<String> predictedTokens = new LinkedList<>();
    public static List<Float> predictedScores = new LinkedList<>();
    public static String masked_sequence = "";

    public static CodeBERTResult mutate(String maskedSequence) {
        numOfCalls++;

        String s = null;
        CodeBERTResult result = CodeBERTResult.ERROR;
        predictedTokens.clear();
        predictedScores.clear();
        try {
            // run the Unix "ps -ef" command
            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec(new String[]{"python3","run-codebert.py",maskedSequence});

            boolean timeout = false;
            if(!p.waitFor(Settings.CODEBERT_TIMEOUT, TimeUnit.SECONDS)) {
                timeout = true; //kill the process.
                p.destroy(); // consider using destroyForcibly instead
            }


            String aux;
            if (timeout){
                numOfTimeout++;
                result = CodeBERTResult.TIMEOUT;
                p.destroy();
            }
            else {
                boolean errorFound = false;
                BufferedReader stdError = new BufferedReader(new
                        InputStreamReader(p.getErrorStream()));
                // read any errors from the attempted command
                System.out.println("Here is the standard error of the command (if any):\n");
                while ((s = stdError.readLine()) != null) {
                    System.out.println(s);
                    errorFound = true;
                }
                // Close the ErrorStream
                stdError.close();

                if (!errorFound) {
                    // read the output from the command
                    BufferedReader stdInput = new BufferedReader(new
                            InputStreamReader(p.getInputStream()));
                    System.out.println("Here is the standard output of the command:\n");
                    while ((s = stdInput.readLine()) != null) {
                        System.out.println(s);
                        if (isJSONValid(s)) {
                            JSONObject jObject = new JSONObject(s);
                            if (jObject.has("masked_seq")){
                                masked_sequence = jObject.getString("masked_seq");
                            }
                            else {
                                String predictedToken = jObject.getString("token_str");

                                predictedTokens.add(predictedToken);
                                System.out.println(predictedToken);

                                //get token score
                                float predictedScore = jObject.getFloat("score");
                                predictedScores.add(predictedScore);
                            }
                        }
                    }
                    // Close the InputStream
                    stdInput.close();

                    result = CodeBERTResult.SUCCEEDED;

                }
           }
        }
        catch (IOException | InterruptedException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            numOfError++;
        }
        return result;
    }


    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

}
