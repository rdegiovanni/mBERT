package operators;

import org.mdkt.compiler.InMemoryJavaCompiler;
import spoon.Launcher;
import spoon.SpoonModelBuilder;
import spoon.compiler.Environment;
import spoon.reflect.CtModel;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.factory.Factory;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.support.compiler.VirtualFile;
import spoon.support.compiler.jdt.JDTSnippetCompiler;
import static spoon.testing.utils.ModelUtils.createFactory;
import java.io.File;

public class mBERTMutant {
    String originalString = "";
    String maskedExpr = null;
    Object predictedToken = null;
    String predictedString = "";
    String mutantSourceCode = "";
    String mutantOperator = "";
    SourcePosition position;
    String masked_Seq = "";
    float score = 0.0f;
    int pos = -1;

    boolean isEquivalent = false;
    boolean isCompilable = false;
    boolean isUseful = false;

    public mBERTMutant(String operator, String originalString, String masked, Object predictedToken, String predictedString, String mutant, SourcePosition position, float score, int pos, String masked_Seq) {
        this.mutantOperator = operator;
        this.originalString = originalString;
        this.maskedExpr = masked;
        this.predictedToken = predictedToken;
        this.predictedString = predictedString;
        this.mutantSourceCode = mutant;
        this.position = position;
        this.score = score;
        this.pos = pos;
        this.masked_Seq = masked_Seq;
    }

    public boolean compileMutant (String code) {
        try {

            Factory factory = createFactory();
//            // test the order of the model
            CtClass clazz = (CtClass) factory
                    .Code()
                    .createCodeSnippetStatement(code)
                    .compile();;
            isCompilable = true;
            System.out.println("Mutant syntax check:" + isCompilable);

        }
        catch (Exception e) {
            System.out.println("Mutant syntax check:" + false);
            isCompilable = false;
        }
        return isCompilable;
    }

    public String getMutantSourceCode() {
        return mutantSourceCode;
    }

    public void setMutantSourceCode(String mutantSourceCode) {
        this.mutantSourceCode = mutantSourceCode;
    }

    public String getMutantOperator() {
        return mutantOperator;
    }

    public void setMutantOperator(String mutantOperator) {
        this.mutantOperator = mutantOperator;
    }

    public String getMasked_Seq() {
        return masked_Seq;
    }

    public void setMasked_Seq(String masked_Seq) {
        this.masked_Seq = masked_Seq;
    }

    public String getOriginalString() {
        return originalString;
    }

    public void setOriginalString(String originalString) {
        this.originalString = originalString;
    }

    public String getMaskedExpr() {
        return maskedExpr;
    }

    public void setMaskedExpr(String maskedExpr) {
        this.maskedExpr = maskedExpr;
    }

    public Object getPredictedToken() {
        return predictedToken;
    }

    public void setPredictedToken(Object predictedToken) {
        this.predictedToken = predictedToken;
    }

    public String getPredictedString() {
        return predictedString;
    }

    public void setPredictedString(String predictedString) {
        this.predictedString = predictedString;
    }

    public String getMutant() {
        return mutantSourceCode;
    }

    public void setMutant(String mutant) {
        this.mutantSourceCode = mutant;
    }

    public SourcePosition getPosition() {
        return position;
    }

    public void setPosition(SourcePosition position) {
        this.position = position;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public boolean isEquivalent() {
        return isEquivalent;
    }

    public void setEquivalent(boolean equivalent) {
        isEquivalent = equivalent;
    }

    public boolean isCompilable() {
        return isCompilable;
    }

    public void setCompilable(boolean compilable) {
        isCompilable = compilable;
    }

    public boolean isUseful() {
        return isUseful;
    }

    public void setUseful(boolean useful) {
        isUseful = useful;
    }


}
