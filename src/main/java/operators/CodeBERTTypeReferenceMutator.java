package operators;

import codebert.CodeBERT;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.reference.MyTypeReferenceImpl;
import spoon.support.reflect.cu.position.SourcePositionImpl;
import spoon.support.reflect.reference.CtTypeReferenceImpl;

class CodeBERTTypeReferenceMutator extends CodeBERTOperatorMutator {


    public CodeBERTTypeReferenceMutator(){
    }

    public String getOperator(CtTypeReference candidate) {
        return "TypeReferenceMutator";
    }

    public MaskedTokenMutants mutate (CtTypeReference original) {
        String originalStr = original.toString();
        SourcePosition position = getSourcePosition(original);

        String operator = getOperator(original);
        String maskedExprStr = original.toString().substring(0,original.toString().lastIndexOf("."))+"<mask>";
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
                        maskedTokenMutants.setMutant(masked_seq, predToken, predToken, pos, score);
                    }
                    catch (Exception e) {
                        System.out.println("CodeBERTTypeReferenceMutator - invalid method invocation name: " + e.getMessage());
                    }
                }
            }
            mutants.add(maskedTokenMutants);
        }
        return maskedTokenMutants;
    }
}

