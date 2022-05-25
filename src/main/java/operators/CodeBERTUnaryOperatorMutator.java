package operators;

import codebert.CodeBERT;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.UnaryOperatorKind;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.support.reflect.code.CtCommentImpl;
import spoon.support.reflect.code.CtUnaryOperatorImpl;
import spoon.support.reflect.cu.position.SourcePositionImpl;

public class CodeBERTUnaryOperatorMutator extends CodeBERTOperatorMutator{

    @Override
    public boolean isToBeProcessed(CtElement candidate) {
        return candidate instanceof CtUnaryOperator;
    }

    public String getOperator(CtExpression candidate) {
        return "UnaryOperatorMutator";
    }
    public MaskedTokenMutants mutate(CtUnaryOperator original) {
        //compute token to mutate position
        int start = original.getPosition().getSourceStart();
        int end = original.getPosition().getSourceEnd();
        if (UnaryOperatorKind.isPre(original.getKind()))
            end = original.getOperand().getPosition().getSourceStart()-1;
        else
            start = original.getOperand().getPosition().getSourceEnd()+1;

        CompilationUnit origUnit = original.getPosition().getCompilationUnit();
        SourcePosition position = new SourcePositionImpl(origUnit,start,end,origUnit.getLineSeparatorPositions());

        String originalOp = original.getKind().toString();

        String operator = getOperator(original);
        String maskedExprStr = UnaryOperatorKind.isPre(original.getKind())? "<mask>"+original.getOperand().toString(): original.getOperand().toString()+"<mask>";
        MaskedTokenMutants maskedTokenMutants = new MaskedTokenMutants(originalClassStr,method,originalOp,maskedExprStr,operator,position);
        String maskedMethodStr = maskedTokenMutants.getMaskedSequence();
        CodeBERT.CodeBERTResult result = CodeBERT.mutate(maskedMethodStr);

        if (result == CodeBERT.CodeBERTResult.SUCCEEDED) {
            String masked_seq = CodeBERT.masked_sequence;
            for (int pos = 0; pos < CODEBERT_NUM_OF_PREDICTIONS; pos++) {
                if (CodeBERT.predictedTokens.size() > pos) {
                    String predToken = CodeBERT.predictedTokens.get(pos);
                    float score = CodeBERT.predictedScores.get(pos);
                    maskedTokenMutants.setMutant(masked_seq,predToken,predToken,pos,score);
                }
            }
            mutants.add(maskedTokenMutants);
        }
        return maskedTokenMutants;
    }
}
