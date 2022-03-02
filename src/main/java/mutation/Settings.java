package mutation;

public class Settings {

    public static int CODEBERT_TIMEOUT = 60;

    public static int MAX_NUM_OF_MUTANTS = Integer.MAX_VALUE;

    public static String printSettings() {
        return "Settings{" +
                "CODEBERT_TIMEOUT=" + CODEBERT_TIMEOUT +
                ", MAX_NUM_OF_MUTANTS=" + MAX_NUM_OF_MUTANTS +
                '}';
    }
}
