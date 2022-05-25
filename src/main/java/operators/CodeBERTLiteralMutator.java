package operators;

import codebert.CodeBERT;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.support.reflect.code.CtCodeSnippetExpressionImpl;
import spoon.support.reflect.code.CtCommentImpl;
import spoon.support.reflect.code.CtExpressionImpl;
import spoon.support.reflect.code.CtLiteralImpl;

class CodeBERTLiteralMutator extends CodeBERTOperatorMutator {


    public CodeBERTLiteralMutator(){
    }

    public String getOperator(CtExpression candidate) {
        if (candidate instanceof CtVariableRead ||
                candidate instanceof CtVariableWrite )
            return "IdentifierMutator-Variable";
        else if(candidate instanceof CtConditional)
            return "IdentifierMutator-Conditional";
        else if(candidate instanceof CtThisAccess)
            return "IdentifierMutator-ThisAccess";
        else if(candidate instanceof CtLiteral)
            return "IdentifierMutator-Literal";
        return "IdentifierMutator";
    }

    public MaskedTokenMutants mutate (CtExpression original) {


        String originalStr = original.toString();
        String operator = getOperator(original);
        MaskedTokenMutants maskedTokenMutants = new MaskedTokenMutants(originalClassStr,method,originalStr,"<mask>",operator,original.getPosition());
        String maskedMethodStr = maskedTokenMutants.getMaskedSequence();
        CodeBERT.CodeBERTResult result = CodeBERT.mutate(maskedMethodStr);
        if (result == CodeBERT.CodeBERTResult.SUCCEEDED) {
            String masked_seq = CodeBERT.masked_sequence;
            for (int pos = 0; pos < CODEBERT_NUM_OF_PREDICTIONS; pos++){
                if (CodeBERT.predictedTokens.size() > pos) {
                    String predToken = CodeBERT.predictedTokens.get(pos);
                    float score = CodeBERT.predictedScores.get(pos);
                    CtCodeSnippetExpression token = new CtCodeSnippetExpressionImpl();
                    token.setValue(predToken);
                    maskedTokenMutants.setMutant(masked_seq,predToken,predToken,pos,score);
                }
            }
            mutants.add(maskedTokenMutants);
        }
        return maskedTokenMutants;
    }
}

