package operators;

import org.mdkt.compiler.InMemoryJavaCompiler;
import spoon.Launcher;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.support.reflect.declaration.CtMethodImpl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MaskedTokenMutants {
    String originalClassName;
    String originalClass;
    CtMethod<?> method = null;
    String methodSig;
    String masked_sequence = "";
    Object original = null;
    String originalString = "";
    String masked = null;
    String operator = "";

    SourcePosition position;
    mBERTMutant[] mutants = new mBERTMutant[5];

    List<Integer> equivalentMutants = new LinkedList<>();
    List<Integer> compilableMutants = new LinkedList<>();
    List<Integer> usefulMutants = new LinkedList<>();

    public MaskedTokenMutants(String originalClassStr, CtMethod<?> method, Object original, String masked, String operator, SourcePosition position) {
        originalClassName = position.getFile().getName();
        originalClass = originalClassStr; //Launcher.parseClass(originalClassStr); //method.getFactory().Core().clone(method.getParent(CtClass.class));
        this.position = position;
        this.operator = operator;
        this.method = method;
        this.methodSig = getMethodSignature(method);
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

    public String getMethodName() {
        return method.getSimpleName();
    }
    public String getMethodSignature() {
        return methodSig;
    }

    public int getMethodLine() {
        return method.getPosition().getLine();
    }

    public String getMethodSignature(CtMethod method) {
        int start = ((CtMethodImpl) method).getType().getPosition().getSourceStart();
        int end = method.getBody().getPosition().getSourceStart();
        String sig = originalClass.substring(start,end);
        sig = sig.replaceFirst("^\\s*", "");
        sig = sig.replaceFirst("\\s++$", "");
        return sig;
    }

    public String getMaskedSequence() {
        int start = position.getSourceStart();
        int end = position.getSourceEnd();
        int method_start = ((CtMethodImpl) method).getType().getPosition().getSourceStart();
        int method_end = method.getPosition().getSourceEnd();
        String mutantClassStr = originalClass.substring(0,start);
        mutantClassStr += "<mask>";
        mutantClassStr += originalClass.substring(end+1,method_end+1);
        String methodStr = mutantClassStr.substring(method_start);
        return methodStr;
    }

    private String createMutantMethod(String mutant, SourcePosition position) {
        int start = position.getSourceStart();
        int end = position.getSourceEnd();
        int method_start = ((CtMethodImpl) method).getType().getPosition().getSourceStart();
        int method_end = method.getPosition().getSourceEnd();
        String mutantClassStr = originalClass.substring(0,start);
        mutantClassStr += mutant;
        mutantClassStr += originalClass.substring(end+1,method_end+1);
        String methodStr = mutantClassStr.substring(method_start);
        return methodStr;
    }

    private String createMutant(String mutant, SourcePosition position) {
        int start = position.getSourceStart();
        int end = position.getSourceEnd();
        String mutantClassStr = originalClass.substring(0,start);
        mutantClassStr += mutant;
        mutantClassStr += originalClass.substring(end+1);
        return mutantClassStr;
    }

    public void setMutant(String masked_sequence, Object mutantToken, String mutantStr, int pos, float score) {
        // creating a new class containing the mutating code
        if (pos < 0 && pos >= mutants.length)
            throw new ArrayIndexOutOfBoundsException("Invalid mutant position.");
        this.masked_sequence = masked_sequence;
        if (mutantToken == null) {
            System.out.println("Invalid token predicted. Original: " + originalString + ". Predicted: " + mutantStr);
//            predictedTokens[pos] = null;
//            predictedStrings[pos] = mutantStr;//.replaceAll("\\s+","");
            mBERTMutant mutant = new mBERTMutant(operator,originalString,masked,mutantToken,mutantStr,null,position,score,pos,masked_sequence);
            mutants[pos] = mutant;
        }
        else {
            String mutantCode = createMutant(mutantStr,position);
            mBERTMutant mutant = new mBERTMutant(operator,originalString,masked,mutantToken,mutantStr,mutantCode,position,score,pos,masked_sequence);

            //check if predicted token is equivalent to the original one
            boolean isEquivalent = isMutantEquivalent(originalString,mutantStr);
            if (isEquivalent && !equivalentMutants.contains(pos))
                equivalentMutants.add(pos);
            mutant.setEquivalent(isEquivalent);

            //check is mutant is compilable
            boolean isCompilable = true;

            if (isCompilable && !compilableMutants.contains(pos)) {
                compilableMutants.add(pos);
            }
            mutant.setCompilable(isCompilable);

            if (isCompilable && !isEquivalent && !usefulMutants.contains(pos)) {
                usefulMutants.add(pos);
                mutant.setUseful(true);
            }
            mutants[pos] = mutant;
        }
    }


    public mBERTMutant getMutant(int pos) {
        if (pos < 0 && pos >= mutants.length)
            throw new ArrayIndexOutOfBoundsException("Invalid mutant position.");
        return mutants[pos];
    }


    public boolean isMutantCompilable(String mutantCode) {
//        return true;
        boolean isCompilable = false;
        try {
           CtClass mutantClass = Launcher.parseClass(mutantCode);
            Class<?> klass = InMemoryJavaCompiler.newInstance().compile(
                    mutantClass.getQualifiedName(), mutantClass.toStringWithImports());
            isCompilable = true;
        }
        catch (Exception e) {

            System.out.println("mutant not compilable");
        }
        return isCompilable;
    }

    public boolean isMutantEquivalent(String orig, String mutant) {
        boolean isEquivalent = false;
        orig = orig.replaceAll("\\s+", "");
        mutant = mutant.replaceAll("\\s+", "");
        if (orig.equals(mutant))
            isEquivalent = true;

        return isEquivalent;
    }



    public List<mBERTMutant> getUsefulMutants() {
        List<mBERTMutant> usefulMutants = new ArrayList<>();
        for (Integer pos : this.usefulMutants) {
            mBERTMutant klass = mutants[pos];
            if (!usefulMutants.contains(klass))
                usefulMutants.add(klass);
        }
        return usefulMutants;
    }
}
