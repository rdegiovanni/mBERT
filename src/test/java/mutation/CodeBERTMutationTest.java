package mutation;

import org.junit.Test;
import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

import java.util.List;

public class CodeBERTMutationTest {
    @Test
    public void testGetMethods() throws Exception {
        //String codeToBeMutated = "src/main/resources/transformation/Foo2.java";
        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        Launcher l = new Launcher();
        l.addInputResource(codeToBeMutated);
        l.buildModel();
        CtClass origClass = (CtClass) l.getFactory().Package().getRootPackage()
                .getElements(new TypeFilter(CtClass.class)).get(0);
//        System.out.println(origClass);
//        System.out.println("----------------");
        Object[] methods = origClass.getMethods().toArray();
        for (int i = 0; i < methods.length; i++){
            CtMethod method = (CtMethod) methods[i];
            System.out.println(method);
            System.out.println("----------------");
//            System.out.println(method.getSignature());
//            System.out.println(method.getShortRepresentation());
//            System.out.println(method.getSimpleName());
//            Iterator<CtStatement> it = method.getBody().iterator();
//            while (it.hasNext()) {
//                CtStatement statement = it.next();
//                System.out.println("++++++");
//                CtPath path = statement.getParent().getPath();
//
//                System.out.println(method.getSignature());
//            }
        }
    }

    @Test
    public void testFoo2() throws Exception {
        String codeToBeMutated = "src/main/resources/transformation/Foo2.java";

//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);

        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        mutation.writeMutants();
        for (CtClass m : mutants)
            System.out.println(m.toString());
    }

    @Test
    public void testExample() throws Exception {
        String codeToBeMutated = "src/main/resources/transformation/Example.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);

        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        mutation.writeMutants();
        for (CtClass m : mutants)
            System.out.println(m.toString()+ "\n***************\n");
    }

    @Test
    public void testSimpleExamples() throws Exception {
        String codeToBeMutated = "src/main/resources/transformation/SimpleExamples.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);

        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        for (CtClass m : mutants)
            System.out.println(m.toString()+ "\n***************\n");
    }

    @Test
    public void testList() throws Exception {
//        String codeToBeMutated = "src/main/resources/transformation/Foo2.java";
        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);

        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        mutation.writeMutants();
        for (CtClass m : mutants)
            System.out.println(m.toString() + "\n***************\n");

    }

    @Test
    public void testCli1() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Documents/workspaces/workspace/datasets/mutants_sensitivity/experiment/Cli_1/org/apache/commons/cli/CommandLine.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);
        Settings.MAX_NUM_OF_MUTANTS = 100;
        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        mutation.writeMutants();
        for (CtClass m : mutants)
            System.out.println(m.toString()+ "\n***************\n");
    }

    @Test
    public void testCli12() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Documents/workspaces/workspace/datasets/mutants_sensitivity/experiment/Cli_12/org/apache/commons/cli/GnuParser.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);
        Settings.MAX_NUM_OF_MUTANTS = 200;
        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        mutation.writeMutants();
        for (CtClass m : mutants)
            System.out.println(m.toString()+ "\n***************\n");
    }

    @Test
    public void testMath42() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Documents/workspaces/workspace/datasets/mutants_sensitivity/experiment/Math_42/org/apache/commons/math/optimization/linear/SimplexTableau.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);
        Settings.MAX_NUM_OF_MUTANTS = 100;
        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        mutation.writeMutants();
        for (CtClass m : mutants)
            System.out.println(m.toString()+ "\n***************\n");
    }

    @Test
    public void testMatgh45() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Documents/workspaces/workspace/datasets/mutants_sensitivity/experiment//Math_45/org/apache/commons/math/linear/OpenMapRealMatrix.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);

        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        mutation.writeMutants();
        for (CtClass m : mutants)
            System.out.println(m.toString()+ "\n***************\n");
    }

    @Test
    public void testMath87() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Documents/workspaces/workspace/datasets/mutants_sensitivity/experiment/Math_87/org/apache/commons/math/optimization/linear/SimplexTableau.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);

        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        mutation.writeMutants();
        for (CtClass m : mutants)
            System.out.println(m.toString()+ "\n***************\n");
    }

    @Test
    public void testGson_3() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Documents/workspaces/workspace/datasets/mutants_sensitivity/experiment/Gson_3/com/google/gson/internal/ConstructorConstructor.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);

        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        mutation.writeMutants();
        for (CtClass m : mutants)
            System.out.println(m.toString()+ "\n***************\n");
    }

    @Test
    public void testJacksonCore_3() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Documents/workspaces/workspace/datasets/mutants_sensitivity/experiment/JacksonCore_3/com/fasterxml/jackson/core/json/UTF8StreamJsonParser.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);
//        Settings.MAX_NUM_OF_MUTANTS = 100;
        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        mutation.writeMutants();
        for (CtClass m : mutants)
            System.out.println(m.toString()+ "\n***************\n");
    }

    @Test
    public void testJacksonXml_1() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Documents/workspaces/workspace/datasets/mutants_sensitivity/experiment/JacksonXml_1/com/fasterxml/jackson/dataformat/xml/deser/FromXmlParser.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);
        Settings.MAX_NUM_OF_MUTANTS = 100;
        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        mutation.writeMutants();
        for (CtClass m : mutants)
            System.out.println(m.toString()+ "\n***************\n");
    }

    @Test
    public void testJsoup_71() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Documents/workspaces/workspace/datasets/mutants_sensitivity/experiment/Jsoup_71/org/jsoup/nodes/PseudoTextElement.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);
        Settings.MAX_NUM_OF_MUTANTS = 100;
        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        mutation.writeMutants();
        for (CtClass m : mutants)
            System.out.println(m.toString()+ "\n***************\n");
    }



    @Test
    public void testQueueAr_makeEmpty() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Documents/uni/experiments-ideas/ground-truth-subjects/subjects/QueueAr_makeEmpty/src/main/java/DataStructures/QueueAr.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);
        mutation.setMethodToMutate("makeEmpty",59);
//        Settings.MAX_NUM_OF_MUTANTS = 100;
        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        mutation.writeMutants();
//        for (CtClass m : mutants)
//            System.out.println(m.toString()+ "\n***************\n");
    }

    @Test
    public void testQueueAr_getFront() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Documents/uni/experiments-ideas/ground-truth-subjects/subjects/QueueAr_getFront/src/main/java/DataStructures/QueueAr.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);
        mutation.setMethodToMutate("getFront",72);
//        Settings.MAX_NUM_OF_MUTANTS = 100;
        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        mutation.writeMutants();
//        for (CtClass m : mutants)
//            System.out.println(m.toString()+ "\n***************\n");
    }

    @Test
    public void testStackAr_makeEmpty() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Documents/uni/experiments-ideas/ground-truth-subjects/subjects/StackAr_makeEmpty/src/main/java/DataStructures/StackAr.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);
        mutation.setMethodToMutate("makeEmpty",61);
        mutation.setLineToMutate(64);
//        Settings.MAX_NUM_OF_MUTANTS = 100;
        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        mutation.writeMutants();
//        for (CtClass m : mutants)
//            System.out.println(m.toString()+ "\n***************\n");
    }

    @Test
    public void testSimpleMethods_incrementNumberAtIndex() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Documents/uni/experiments-ideas/ground-truth-subjects/subjects/SimpleMethods_incrementNumberAtIndex/src/main/java/examples/SimpleMethods.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);
//        mutation.setMethodToMutate("incrementNumberAtIndex",11);
//        mutation.setLineToMutate(16);
//        Settings.MAX_NUM_OF_MUTANTS = 100;
        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        mutation.writeMutants();
//        for (CtClass m : mutants)
//            System.out.println(m.toString()+ "\n***************\n");
    }

    @Test
    public void testSimpleMethods_addElementToSet() throws Exception {
        String codeToBeMutated = "/Users/renzo.degiovanni/Documents/uni/experiments-ideas/ground-truth-subjects/subjects/SimpleMethods_addElementToSet/src/main/java/examples/SimpleMethods.java";
//        String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";
        CodeBERTMutation mutation = new CodeBERTMutation(codeToBeMutated);
//        mutation.setMethodToMutate("addElementToSet",11);
//        mutation.setLineToMutate(13);
//        Settings.MAX_NUM_OF_MUTANTS = 100;
        mutation.generateMutants();
        List<CtClass> mutants = mutation.getAllMutantClasses();
        mutation.writeMutants();
//        for (CtClass m : mutants)
//            System.out.println(m.toString()+ "\n***************\n");
    }

}
