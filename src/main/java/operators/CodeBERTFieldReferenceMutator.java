package operators;

import codebert.CodeBERT;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtExpression;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.MyFieldReferenceImpl;
import spoon.support.reflect.code.CtCodeSnippetExpressionImpl;
import spoon.support.reflect.code.CtExpressionImpl;
import spoon.support.reflect.cu.position.SourcePositionImpl;
import spoon.support.reflect.reference.CtFieldReferenceImpl;

class CodeBERTFieldReferenceMutator extends CodeBERTOperatorMutator {


    public CodeBERTFieldReferenceMutator(){
    }

    public String getOperator(CtFieldReference candidate) {
        return "FieldReferenceMutator";
    }

    public MaskedTokenMutants mutate (CtFieldReference original) {
        String originalStr = original.getSimpleName();
        SourcePosition origPosition = getSourcePosition(original);

        //compute token to mutate position
        int end = origPosition.getSourceEnd();
        int start = end - (originalStr.length() - 1);
        CompilationUnit origUnit = origPosition.getCompilationUnit();
        SourcePosition position = new SourcePositionImpl(origUnit,start,end,origUnit.getLineSeparatorPositions());

        String operator = getOperator(original);
        String maskedExprStr = (original.getParent()==null || original.getParent().toString()=="")? "<mask>": original.getParent().toString().replace(originalStr,"<mask>");
        MaskedTokenMutants maskedTokenMutants = new MaskedTokenMutants(originalClassStr,method,originalStr,maskedExprStr,operator,position);
        String maskedMethodStr = maskedTokenMutants.getMaskedSequence();

        CodeBERT.CodeBERTResult result = CodeBERT.mutate(maskedMethodStr);
        if (result == CodeBERT.CodeBERTResult.SUCCEEDED) {
            String masked_seq = CodeBERT.masked_sequence;
            for (int pos = 0; pos < CODEBERT_NUM_OF_PREDICTIONS; pos++){
                if (CodeBERT.predictedTokens.size() > pos) {
                    String predToken = CodeBERT.predictedTokens.get(pos);
                    float score = CodeBERT.predictedScores.get(pos);
                    try {
                        maskedTokenMutants.setMutant(masked_seq,predToken, predToken, pos, score);
                    }
                    catch (Exception e) {
                        System.out.println("CodeBERTFieldReferenceMutator - invalid method invocation name: " + e.getMessage());
                    }
                }
            }
            mutants.add(maskedTokenMutants);
        }
        return maskedTokenMutants;
    }
}

