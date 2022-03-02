package mutation;

import operators.CodeBERTOperatorMutator;
import operators.MaskedTokenMutants;
import org.mdkt.compiler.InMemoryJavaCompiler;
import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtStatement;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.Filter;
import spoon.reflect.visitor.filter.TypeFilter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CodeBERTMutation<T> {

	/** the Java source code file to be mutated */
	private String sourceCodeToBeMutated;
	/** directory where mutants will be saved */
	private String directoryToWriteMutants;
	/** methods to mutate  */
	private List<String> methodsToMutate = new LinkedList<>();
	private List<Integer> methodsLineToMutate = new LinkedList<>();

	/** specific lines to mutate  */
	private List<Integer> linesToMutate = new LinkedList<>();

	/** mutation operator */
	private CodeBERTOperatorMutator mutator;

	/** the produced mutants */
	private final List<String> mutants = new ArrayList<>();
	private final List<MaskedTokenMutants> maskedMutants = new ArrayList<>();
	private final List<String> unhandled_mutations = new LinkedList<>();
	// public for testing
//	public final List<T> mutantInstances = new ArrayList<>();

	public CodeBERTMutation(String src) {
		this.sourceCodeToBeMutated = src;
	}
	public CodeBERTMutation(String src, String outfolder) {
		this.sourceCodeToBeMutated = src;
		this.directoryToWriteMutants = outfolder;
	}

	public void setMethodToMutate(String method, int line) {
		if (method != null && method != "") {
			methodsToMutate.add(method);
			methodsLineToMutate.add(line);
		}
	}

	public void setLineToMutate(int line) {
		linesToMutate.add(line);
	}

	public void setLinesToMutate(List<Integer> lines) {
		if (lines!=null && !lines.isEmpty())
			linesToMutate.addAll(lines);
	}

	/** returns a list of mutant classes */
	public void generateMutants() throws IOException {
		Launcher l = new Launcher();
		l.addInputResource(sourceCodeToBeMutated);
		l.getEnvironment().setCommentEnabled(false);
		CtModel model = l.buildModel();

		//read entire source code into string to produce the mutants later.
		String originalClassStr = Files.readString(Path.of(sourceCodeToBeMutated));


//		l.getFactory().getEnvironment().setAutoImports(false);
//		l.getFactory().getEnvironment().setNoClasspath(true);
//		l.getFactory().getEnvironment().setPrettyPrintingMode(Environment.PRETTY_PRINTING_MODE.AUTOIMPORT);

		CtClass origClass = (CtClass) l.getFactory().Package().getRootPackage()
				.getElements(new TypeFilter(CtClass.class)).get(0);

		// iterate on each method
		List<CtElement> methodsToBeMutated = origClass.getElements(new Filter<CtElement>() {
			@Override
			public boolean matches(CtElement arg0) {
				return arg0 instanceof CtMethod;
			}
		});
		
		for (CtElement met : methodsToBeMutated) {
			// iterate on each method
			CtMethod method = (CtMethod) met;

			if (!methodToMutate(method))
				continue;


			/** mutation operator */
			mutator = new CodeBERTOperatorMutator();
			mutator.setMethod(method);
			mutator.setOriginalClassStr(originalClassStr);
			List<CtElement> elementsToBeMutated = method.getElements(new Filter<CtElement>() {
								@Override
				public boolean matches(CtElement arg0) {
					return mutator.isToBeProcessed(arg0);
				}
			});;

			for (CtElement e : elementsToBeMutated) {
				// this loop is the trickiest part
				if (!mutator.isToBeProcessed(e))
					continue;
				//filter lines to mutate
				if (!lineToMutate(mutator.getSourcePosition(e)))
					continue;

				// mutate the element
				mutator.process(e);
//					System.out.println(op);

				// if maximum number of mutants has been reached, stop.
				int curr_iteration_num_of_mutants = mutator.numOfMutants() * 5;
				if (mutants.size() + curr_iteration_num_of_mutants > Settings.MAX_NUM_OF_MUTANTS)
					break;
			}
			for(String mut : mutator.getAllMutants()) {
				if (!mutants.contains(mut))
					mutants.add(mut);
			}
			for(MaskedTokenMutants mut : mutator.getAllMaskedMutants()) {
				if (!maskedMutants.contains(mut))
					maskedMutants.add(mut);
			}
			unhandled_mutations.addAll(mutator.unhandled_mutations);

			// if maximum number of mutants has been reached, stop.
			if (mutants.size() > Settings.MAX_NUM_OF_MUTANTS)
				break;
		}
	}

	public boolean lineToMutate(SourcePosition e) {
		//is the line selected to be mutated?
		if (linesToMutate.isEmpty())
			return true;
		if (linesToMutate.contains(e.getLine()))
			return true;
		return false;
	}

	public boolean methodToMutate(CtMethod method) {
		//filter methods to mutate
		if (methodsToMutate.isEmpty())
			return true;
		boolean mutate = false;
		for (int index = 0; index < methodsToMutate.size(); index++) {
			String m = methodsToMutate.get(index);
			int line = methodsLineToMutate.get(index);
			if (method.getSimpleName().equals(m)) {
				SourcePosition pos = method.getPosition();
				if (line <= 0 || line == pos.getLine()) {
					mutate = true;
					break;
				}
			}
		}
		return mutate;
	}

	public List<MaskedTokenMutants> getAllMaskedMutants() {
		return maskedMutants;
	}

	public List<String> getUnhandledMutations () {
		return  unhandled_mutations;
	}

	public List<CtClass> getAllMutantClasses() {
		List<CtClass> all_mutants = new LinkedList<>();
		for(String m : mutants) {
			if (m != null) {
				CtClass klass = Launcher.parseClass(m);
				all_mutants.add(klass);
			}
		}
		return all_mutants;
	}

	/*
	 * Return every useful (compilable and non-equivalent) mutant
	 */
	public List<String> getAllMutants() {
		return mutants;
	}

	/*
	 * Return every mutant with a detailed information: position, (non)equivalent, (un)compilable, etc.
	 */
	public List<MaskedTokenMutants> getAllDetailedMutants() {
		return maskedMutants;
	}

//	public List<CtClass> getFirstMutants() {
//		List<CtClass> all_mutants = new LinkedList<>();
//		for(MaskedTokenMutants m : maskedMutants) {
//			if (m.getFirstMutant() != null)
//				all_mutants.add(m.getFirstMutant());
//		}
//		return all_mutants;
//	}
//
//	public List<CtClass> getSecondMutants() {
//		List<CtClass> all_mutants = new LinkedList<>();
//		for(MaskedTokenMutants m : maskedMutants) {
//			if (m.getSecondMutant() != null)
//				all_mutants.add(m.getSecondMutant());
//		}
//		return all_mutants;
//	}
//
//	public List<CtClass> getThirdMutants() {
//		List<CtClass> all_mutants = new LinkedList<>();
//		for(MaskedTokenMutants m : maskedMutants) {
//			if (m.getThirdMutant() != null)
//				all_mutants.add(m.getThirdMutant());
//		}
//		return all_mutants;
//	}
//
//	public List<CtClass> getFourthMutants() {
//		List<CtClass> all_mutants = new LinkedList<>();
//		for(MaskedTokenMutants m : maskedMutants) {
//			if (m.getFourthMutant() != null)
//				all_mutants.add(m.getFourthMutant());
//		}
//		return all_mutants;
//	}
//
//	public List<CtClass> getFifthMutants() {
//		List<CtClass> all_mutants = new LinkedList<>();
//		for(MaskedTokenMutants m : maskedMutants) {
//			if (m.getFifthMutant() != null)
//				all_mutants.add(m.getFifthMutant());
//		}
//		return all_mutants;
//	}
//
	private void replace(CtElement e, CtElement op) {
		if (e instanceof CtStatement && op instanceof CtStatement) {
			e.replace(op);
			return;
		}
		if (e instanceof CtExpression && op instanceof CtExpression) {
			e.replace(op);
			return;
		}
		throw new IllegalArgumentException(e.getClass()+" "+op.getClass());
	}

//	/** tries to kill all generated mutants, throws an AssertionError if one mutant is not killed */
//	public void killMutants() throws Exception {
//
//		List<Class<?>> compiledMutants = compileMutants(mutants);
//
//		List<T> mutantInstances = instantiateMutants(compiledMutants);
//
//		runTestsOnEachMutantInstance(mutantInstances);
//
//	}
//
////	/** applies the test driver of this mutation tester on each mutant instance */
////	public void runTestsOnEachMutantInstance(List<T> mutantInstances) throws Exception {
////		// now we run the mutants against the test class
////		for (T t : mutantInstances) {
////			try {
////				testDriver.test(t);
////				throw new MutantNotKilledException();
////			} catch (AssertionError expected) {
////				System.out.println("mutant killed!");
////			}
////		}
////	}
//
//	/** instantiate the mutant classes using the default zero-arg constructor */
//	public List<T> instantiateMutants(List<Class<?>> compiledMutants)
//			throws Exception {
//		// we run each mutant one by one and check whether they are killed
//
//		// instantiating the mutant classes
//		for (Class mutantClass : compiledMutants) {
//			mutantInstances.add((T) mutantClass.newInstance());
//		}
//		return mutantInstances;
//	}
//
	/** compiles the mutants on the fly */
	public List<Class<?>> compileMutants(List<CtClass> mutants) throws Exception {
		List<Class<?>> compiledMutants = new ArrayList<>();
		for (CtClass mutantClass : mutants) {
			Class<?> klass = InMemoryJavaCompiler.newInstance().compile(
					mutantClass.getQualifiedName(), "package "
							+ mutantClass.getPackage().getQualifiedName() + ";"
							+ mutantClass);
			compiledMutants.add(klass);
		}
		return compiledMutants;
	}

	public void writeMutants (String directoryOfMutants) throws IOException {
		this.directoryToWriteMutants = directoryOfMutants;
		writeMutants();
	}

	public void writeMutants () throws IOException {
		String basename = sourceCodeToBeMutated.substring(sourceCodeToBeMutated.lastIndexOf('/')+1, sourceCodeToBeMutated.lastIndexOf('.'));
		if (directoryToWriteMutants == null || directoryToWriteMutants == "")
			directoryToWriteMutants = "generated-mutants/";
		File outfolder = new File(directoryToWriteMutants);

		if (!outfolder.exists())
			outfolder.mkdir();

		//create mutant mapping file
		String map_name = directoryToWriteMutants + "/"+ basename +"-mapping.csv";
		File map_file = new File(map_name);
		FileWriter map_fw = new FileWriter(map_file.getAbsoluteFile());
		BufferedWriter map_bw = new BufferedWriter(map_fw);
		map_bw.write("id,classname,methodname,location,start,end\n");

		//txt format
		String txt_name = directoryToWriteMutants + "/map-"+ basename +".txt";
		File txt_file = new File(txt_name);
		FileWriter txt_fw = new FileWriter(txt_file.getAbsoluteFile());
		BufferedWriter txt_bw = new BufferedWriter(txt_fw);

		List<String> toPrintMutants = new LinkedList<>(mutants);
		int id = 0;
		for (int i = 0; i < maskedMutants.size(); i++) {
			MaskedTokenMutants mutant = maskedMutants.get(i);
			SourcePosition position = mutant.getPosition();
			for(String useful_mutant : mutant.getUsefulMutants()) {
				if (!toPrintMutants.contains(useful_mutant))
					continue;
				toPrintMutants.remove(useful_mutant);

				File mut_folder = new File(directoryToWriteMutants+ "/"+ id );
				if (!mut_folder.exists())
					mut_folder.mkdir();

				String mut_name = directoryToWriteMutants + "/"+ id + "/" + basename + ".java";
				File file = new File(mut_name);
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(useful_mutant);
				bw.close();

				//write mutant mapping information
				map_bw.write(id + "," + mutant.getOriginalClassName()  +","+ mutant.getMethodName() +","+ position.getLine() +","+ position.getSourceStart() +","+ position.getSourceEnd() + "\n");

				txt_bw.write(id + " | " + basename + ".java | " + mutant.getMethodName() +" | " + position.getLine() + "\n");
				id++;
			}
		}
		map_bw.close();
		txt_bw.close();

		if (!unhandled_mutations.isEmpty()) {
			//txt format
			String unhandled_name = directoryToWriteMutants + "/unhandled_mutations.txt";
			File unhandled_file = new File(unhandled_name);
			FileWriter unhandled_fw = new FileWriter(unhandled_file.getAbsoluteFile());
			BufferedWriter unhandled_bw = new BufferedWriter(unhandled_fw);
			for(String u : unhandled_mutations) {
				unhandled_bw.write(u + "\n");
			}
			unhandled_bw.close();
		}
	}

}
