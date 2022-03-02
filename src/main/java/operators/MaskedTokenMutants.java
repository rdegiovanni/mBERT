package operators;

import org.mdkt.compiler.InMemoryJavaCompiler;
import spoon.Launcher;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.UnaryOperatorKind;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.support.reflect.declaration.CtMethodImpl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MaskedTokenMutants {
    String originalClassName;
    String originalClass;
    CtMethod<?> method = null;
    String methodName;
    Object original = null;
    String originalString = "";
    Object masked = null;

    Object[] predictedTokens = new Object[5];
    String[] predictedStrings = new String[5];
    String[] mutants = new String[5];
    SourcePosition position;

    List<Integer> equivalentMutants = new LinkedList<>();
    List<Integer> compilableMutants = new LinkedList<>();
    List<Integer> usefulMutants = new LinkedList<>();

    public MaskedTokenMutants(String originalClassStr, CtMethod<?> method, Object original, Object masked, SourcePosition position) {
        originalClassName = position.getFile().getName();
        originalClass = originalClassStr; //Launcher.parseClass(originalClassStr); //method.getFactory().Core().clone(method.getParent(CtClass.class));
//        originalClass.setParent(method.getParent());
        this.position = position;
        this.method = method;
        this.methodName = getMethodSignature(method);
        this.original = original;
        this.originalString = original.toString().replaceAll("\\s+","");
        this.masked = masked;
    }

    public String getOriginalClassName() {
        return  originalClassName;
    }

    public SourcePosition getPosition() {
        return position;
    }

    public String getMethodName () {
        return methodName;
    }

    public String getMethodSignature(CtMethod method) {
        String returnType = "";
        if (((CtMethodImpl) method).getType() != null)
            returnType = ((CtMethodImpl) method).getType().getSimpleName();
        String funName = method.getSimpleName();
        String params = "";
        List<CtParameter> parameters = method.getParameters();
        for (CtParameter p : parameters) {
            if (params != "") params += ", ";
            params += p.getType().getSimpleName() + " " + p.getSimpleName();
        }
        String sig = returnType + " " + funName + "(" + params + ")";
        return sig;
    }

    private String createMutant(String mutant, SourcePosition position) {
        int start = position.getSourceStart();
        int end = position.getSourceEnd();
        String mutantClassStr = originalClass.substring(0,start);
        mutantClassStr += mutant;
        mutantClassStr += originalClass.substring(end+1);
        return mutantClassStr;
    }

    public void setMutant(Object mutantToken, String mutantStr, int pos) {
        // creating a new class containing the mutating code
        if (pos < 0 && pos >= mutants.length)
            throw new ArrayIndexOutOfBoundsException("Invalid mutant position.");
        if (mutantToken == null) {
            System.out.println("Invalid token predicted. Original: " + originalString + ". Predicted: " + mutantStr);
            predictedTokens[pos] = null;
            predictedStrings[pos] = mutantStr;//.replaceAll("\\s+","");
            mutants[pos] = null;
        }
        else {
            predictedTokens[pos] = mutantToken;
            predictedStrings[pos] = mutantStr;//.replaceAll("\\s+", "");
            mutants[pos] = createMutant(mutantStr,position);
//            CtClass klass = Launcher.parseClass(method.getParent(CtClass.class).toString()); //method.getFactory().Core().clone(method.getParent(CtClass.class));
            // setting the package
//            klass.setParent(method.getParent()); //BUGGY: *** java.lang.instrument ASSERTION FAILED ***: "!errorOutstanding" with message transform method call failed at JPLISAgent.c line: 873
//            CtClass klass = method.getParent(CtClass.class);
//            klass.getFactory().getEnvironment().setAutoImports(true);
//            try {
//                mutants[pos] = klass.toStringWithImports(); //toString();
//            }
//            catch (Exception e) {
//                mutants[pos] = klass.prettyprint(); //toString();
//            }

            //check is mutant is compilable
            boolean isCompilable = true;//isMutantCompilable(klass);
            if (isCompilable && !compilableMutants.contains(pos)) {
                compilableMutants.add(pos);
            }

            //check if predicted token is equivalent to the original one
            boolean isEquivalent = isMutantEquivalent(originalString,mutantStr);
            if (isEquivalent && !equivalentMutants.contains(pos))
                equivalentMutants.add(pos);

            if (isCompilable && !isEquivalent && !usefulMutants.contains(pos))
                usefulMutants.add(pos);
        }
    }


    public String getMutant(int pos) {
        if (pos < 0 && pos >= mutants.length)
            throw new ArrayIndexOutOfBoundsException("Invalid mutant position.");
        return mutants[pos];
    }

    public List<String> getCompilableMutants() {
        List<String> compiledMutants = new ArrayList<>();
        for (Integer pos : compilableMutants) {
            String klass = mutants[pos];
            if (!compiledMutants.contains(klass))
                compiledMutants.add(klass);
        }
        return compiledMutants;
    }

    public boolean isMutantCompilable(CtClass mutantClass) {
        return true;
//        boolean isCompilable = false;
//        try {
//            Class<?> klass = InMemoryJavaCompiler.newInstance().compile(
//                    mutantClass.getQualifiedName(), "package "
//                            + mutantClass.getPackage().getQualifiedName() + ";"
//                            + mutantClass);
//            isCompilable = true;
//        }
//        catch (Exception e) {
//            System.out.println("mutant not compilable");
//        }
//        return isCompilable;
    }

    public boolean isMutantEquivalent(String orig, String mutant) {
        boolean isEquivalent = false;
        orig = orig.replaceAll("\\s+", "");
        mutant = mutant.replaceAll("\\s+", "");
        if (orig.equals(mutant))
            isEquivalent = true;

        return isEquivalent;
    }

    public List<String> getEquivalentMutants() {
        List<String> equivMutants = new ArrayList<>();
        for (Integer pos : equivalentMutants) {
            String klass = mutants[pos];
            if (!equivMutants.contains(klass))
                equivMutants.add(klass);
        }
        return equivMutants;
    }

    public List<String> getNONEquivalentMutants() {
        List<String> nonEquivMutants = new ArrayList<>();
        for (int i = 0; i<mutants.length; i++) {
            if (!equivalentMutants.contains(i)) {
                String klass = mutants[i];
                if(!nonEquivMutants.contains(klass))
                    nonEquivMutants.add(klass);
            }
        }
        return nonEquivMutants;
    }

    public List<String> getUsefulMutants() {
        List<String> usefulMutants = new ArrayList<>();
        for (Integer pos : this.usefulMutants) {
            String klass = mutants[pos];
            if (!usefulMutants.contains(klass))
                usefulMutants.add(klass);
        }
        return usefulMutants;
    }
}
