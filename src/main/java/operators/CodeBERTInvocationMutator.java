package operators;

import codebert.CodeBERT;
import spoon.SpoonException;
import spoon.reflect.code.*;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtExecutableReference;
import spoon.reflect.reference.MyExecutableReferenceImpl;
import spoon.support.reflect.code.CtCommentImpl;
import spoon.support.reflect.code.CtUnaryOperatorImpl;
import spoon.support.reflect.cu.position.SourcePositionImpl;
import spoon.support.reflect.reference.CtExecutableReferenceImpl;

public class CodeBERTInvocationMutator extends CodeBERTOperatorMutator{

    @Override
    public boolean isToBeProcessed(CtElement candidate) {
        return candidate instanceof CtInvocation;
    }

    public String getOperator(CtExpression candidate) {
        return "MethodCallMutator";
    }

    public MaskedTokenMutants mutate(CtInvocation original) {
        String originalOp = original.getExecutable().getSimpleName();

        //compute token to mutate position
        int start = original.getPosition().getSourceStart();
        if (original.getTarget()!= null && !original.getTarget().isImplicit() && original.getTarget().toString().length() > 0)
            start = original.getTarget().getPosition().getSourceEnd() + 2;//one more for .
        int end = start + originalOp.length() -1;

        CompilationUnit origUnit = original.getPosition().getCompilationUnit();
        SourcePosition position = new SourcePositionImpl(origUnit,start,end,origUnit.getLineSeparatorPositions());

        String operator = getOperator(original);

        String maskedExprStr = (original.getTarget()== null || original.getTarget().toString()=="")?"<mask>":original.prettyprint().replace(originalOp,"<mask>");
        MaskedTokenMutants maskedTokenMutants = new MaskedTokenMutants(originalClassStr,method,originalOp,maskedExprStr,operator,position);
        String maskedMethodStr = maskedTokenMutants.getMaskedSequence();
        CodeBERT.CodeBERTResult result = CodeBERT.mutate(maskedMethodStr);
        if (result == CodeBERT.CodeBERTResult.SUCCEEDED) {
            String masked_seq = CodeBERT.masked_sequence;
            for (int pos = 0; pos < CODEBERT_NUM_OF_PREDICTIONS; pos++) {
                if (CodeBERT.predictedTokens.size() > pos) {
                    String predToken = CodeBERT.predictedTokens.get(pos);
                    float score = CodeBERT.predictedScores.get(pos);
                    try {
                        maskedTokenMutants.setMutant(masked_seq,predToken, predToken, pos, score);
                    }
                    catch (Exception e) {
                        System.out.println("CodeBERTInvocationMutator - invalid method invocation name: " + e.getMessage());
                    }
                }
            }

            mutants.add(maskedTokenMutants);
        }
        return maskedTokenMutants;
    }
}
