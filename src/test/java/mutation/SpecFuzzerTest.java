package mutation;

import org.junit.Test;
import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;

public class SpecFuzzerTest {

    @Test
    public void testStackAr_pop() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Documents/workspaces/workspace/mutation-for-assertion-assessment/specfuzzer_subjects/StackAr_pop/src/main/java/DataStructures/StackAr.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);
        mutation.setMethodToMutate("pop",98);
//        Settings.MAX_NUM_OF_MUTANTS = 100;
        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        String outdir = "/Users/renzo.degiovanni/Documents/workspaces/workspace/codebertmutation/generated-mutants/StackAr_pop/";
        mutation.writeMutants(outdir);
//        for (CtClass m : mutants)
//            System.out.println(m.toString()+ "\n***************\n");
    }


    @Test
    public void testStackAr() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Documents/workspaces/workspace/mutation-for-assertion-assessment/specfuzzer_subjects/StackAr_pop/src/main/java/DataStructures/StackAr.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);
        mutation.setMethodToMutate("pop",82);
//        mutation.setMethodToMutate("topAndPop",105);
//        Settings.MAX_NUM_OF_MUTANTS = 100;
        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        String outdir = "/Users/renzo.degiovanni/Documents/workspaces/workspace/codebertmutation/generated-mutants/StackAr_pop/";
        mutation.writeMutants(outdir);
//        for (CtClass m : mutants)
//            System.out.println(m.toString()+ "\n***************\n");
    }

    @Test
    public void testAngle() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Documents/workspaces/workspace/mutation-for-assertion-assessment/specfuzzer_subjects/Angle_getTurn/src/main/java/jts/Angle.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);
//        mutation.setMethodToMutate("pop",82);
        mutation.setMethodToMutate("getTurn",42);
//        Settings.MAX_NUM_OF_MUTANTS = 100;
        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        String outdir = "/Users/renzo.degiovanni/Documents/workspaces/workspace/codebertmutation/generated-mutants/Angle_getTurn/";
        mutation.writeMutants(outdir);
//        for (CtClass m : mutants)
//            System.out.println(m.toString()+ "\n***************\n");
    }

    @Test
    public void testComposite() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Documents/workspaces/workspace/mutation-for-assertion-assessment/specfuzzer_subjects/composite_addChild/src/main/java/examples/Composite.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);
        mutation.setMethodToMutate("addChild",70);
        mutation.setMethodToMutate("setParent",81);
        mutation.setMethodToMutate("update",91);
//        Settings.MAX_NUM_OF_MUTANTS = 100;
        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        String outdir = "/Users/renzo.degiovanni/Documents/workspaces/workspace/codebertmutation/generated-mutants/codebert" +
                "/";
        mutation.writeMutants(outdir);
//        for (CtClass m : mutants)
//            System.out.println(m.toString()+ "\n***************\n");
    }

    @Test
    public void testPaper() throws Exception {
        String codeToBeMutated = "examples/SimpleMethods.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);
//        mutation.setMethodToMutate("pop",82);
//        mutation.setMethodToMutate("isLeapYear",30);
//        mutation.setMethodToMutate("printArray",41);
//        mutation.setMethodToMutate("multiply",46);
        mutation.setMethodToMutate("insert",63);

//        mutation.setMethodToMutate("peakElement",93);
        //        Settings.MAX_NUM_OF_MUTANTS = 100;
        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        String outdir = "/Users/renzo.degiovanni/Documents/workspaces/workspace/codebertmutation/generated-mutants/insert/";
        mutation.writeMutants(outdir);
//        for (CtClass m : mutants)
//            System.out.println(m.toString()+ "\n***************\n");
    }

    @Test
    public void testFacu() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Downloads/SortedList.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);
//        Settings.MAX_NUM_OF_MUTANTS = 100;
        mutation.setMethodToMutate("insert",31);
        mutation.generateMutants();
        String outdir = "/Users/renzo.degiovanni/Documents/workspaces/workspace/codebertmutation/generated-mutants/sorted_list/";
        mutation.writeMutants(outdir);
    }
}
