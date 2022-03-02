import static org.junit.Assert.assertEquals;

import java.util.List;

import operators.BinaryOperatorMutator;
import operators.CodeBERTBinaryOperatorMutator;
import org.junit.Assert;
import org.junit.Test;

import spoon.reflect.declaration.CtClass;
import utils.MutationTester;
import utils.TestDriver;

public class MutationTesterTest {

	@Test
	public void testMutationTester() throws Exception {
		// mutation testing requires three things
		// 1. the code to be mutated
		// 2. the test driver to kill the mutants 
		// 3. the mutation operator
		String codeToBeMutated = "src/main/resources/transformation/Foo2.java";
		
		TestDriver<IFoo> testDriverForIFooObjects = new TestDriver<IFoo>() {
			@Override
			public void test(IFoo t) {
				assertEquals(2, t.m());
				assertEquals(6, t.n());
			}
		};
		
		BinaryOperatorMutator mutationOperator = new BinaryOperatorMutator();

		// we instantiate the mutation tester
		MutationTester<IFoo> mutationTester = new MutationTester<>(codeToBeMutated, testDriverForIFooObjects, mutationOperator);
		
		// generating the mutants
		mutationTester.generateMutants();
		List<CtClass> mutants = mutationTester.getMutants();
		assertEquals(2, mutants.size());
		for (CtClass m : mutants)
			System.out.println(m.toString());

		// killing the mutants, no exception should be thrown
		try {
			mutationTester.killMutants();
		} catch (utils.MutantNotKilledException e) {
			Assert.fail();
		}

		// another couple of assertions for testing that mutants are actually mutants
		// testing the first mutant
		// 1-1 = 0
		assertEquals(0, mutationTester.mutantInstances.get(0).m());
		assertEquals(6, mutationTester.mutantInstances.get(0).n());

		// testing the second mutant
		assertEquals(2, mutationTester.mutantInstances.get(1).m());
		// 2-3 = -1
		assertEquals(-1, mutationTester.mutantInstances.get(1).n());
	}

	@Test
	public void testCodeBERTMutationTester() throws Exception {
		// mutation testing requires three things
		// 1. the code to be mutated
		// 2. the test driver to kill the mutants
		// 3. the mutation operator
		String codeToBeMutated = "src/main/resources/transformation/Foo2.java";


		CodeBERTBinaryOperatorMutator mutationOperator = new CodeBERTBinaryOperatorMutator();

		// we instantiate the mutation tester
		MutationTester<IFoo> mutationTester = new MutationTester<>(codeToBeMutated, null, mutationOperator);

		// generating the mutants
		mutationTester.generateMutants();
		List<CtClass> mutants = mutationTester.getMutants();
		for (CtClass m : mutants)
			System.out.println(m.toString());
//
//		// killing the mutants, no exception should be thrown
//		try {
//			mutationTester.killMutants();
//		} catch (utils.MutantNotKilledException e) {
//			Assert.fail();
//		}
//
//		// another couple of assertions for testing that mutants are actually mutants
//		// testing the first mutant
//		// 1-1 = 0
//		assertEquals(0, mutationTester.mutantInstances.get(0).m());
//		assertEquals(6, mutationTester.mutantInstances.get(0).n());
//
//		// testing the second mutant
//		assertEquals(2, mutationTester.mutantInstances.get(1).m());
//		// 2-3 = -1
//		assertEquals(-1, mutationTester.mutantInstances.get(1).n());
	}

	@Test
	public void testLinkedList() throws Exception {
		// mutation testing requires three things
		// 1. the code to be mutated
		// 2. the test driver to kill the mutants
		// 3. the mutation operator
		String codeToBeMutated = "src/main/resources/data-structures/singly-linked-list/MyLinkedList.java";

		BinaryOperatorMutator mutationOperator = new BinaryOperatorMutator();

		// we instantiate the mutation tester
		MutationTester mutationTester = new MutationTester<>(codeToBeMutated, null, mutationOperator);

		// generating the mutants
		mutationTester.generateMutants();
		List<CtClass> mutants = mutationTester.getMutants();

		for (CtClass m : mutants)
			System.out.println(m.toString());
//
//		// killing the mutants, no exception should be thrown
//		try {
//			mutationTester.killMutants();
//		} catch (utils.MutantNotKilledException e) {
//			Assert.fail();
//		}
//
//		// another couple of assertions for testing that mutants are actually mutants
//		// testing the first mutant
//		// 1-1 = 0
//		assertEquals(0, mutationTester.mutantInstances.get(0).m());
//		assertEquals(6, mutationTester.mutantInstances.get(0).n());
//
//		// testing the second mutant
//		assertEquals(2, mutationTester.mutantInstances.get(1).m());
//		// 2-3 = -1
//		assertEquals(-1, mutationTester.mutantInstances.get(1).n());
	}
}
