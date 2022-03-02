package codebert;

import org.junit.Test;

public class CodeBERTTest {

    @Test
    public void testSimple() throws Exception {
        String codeToBeMutated = "if (x is not None) <mask> (x>1)";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERT.CodeBERTResult result = CodeBERT.mutate(codeToBeMutated);
        System.out.println(result);
//        mutation.generateMutants();
//        List<CtClass> mutants = mutation.getMutants();
//
//        for (CtClass m : mutants)
//            System.out.println(m.toString());
    }

    @Test
    public void testSimple2() throws Exception {
        String codeToBeMutated = "x = 1 + <mask>)";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERT.CodeBERTResult result = CodeBERT.mutate(codeToBeMutated);
        System.out.println(result);
//        mutation.generateMutants();
//        List<CtClass> mutants = mutation.getMutants();
//
//        for (CtClass m : mutants)
//            System.out.println(m.toString());
    }
}
