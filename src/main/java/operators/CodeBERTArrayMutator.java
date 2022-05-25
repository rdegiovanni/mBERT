package operators;

import codebert.CodeBERT;
import spoon.reflect.code.CtArrayAccess;
import spoon.reflect.code.CtCodeSnippetExpression;
import spoon.reflect.code.CtExpression;
import spoon.support.reflect.code.CtCodeSnippetExpressionImpl;
import spoon.support.reflect.code.CtExpressionImpl;

class CodeBERTArrayMutator extends CodeBERTOperatorMutator {


    public CodeBERTArrayMutator(){
    }

    public String getOperator(CtExpression candidate) {
        return "ArrayAccessMutator";
    }

    public MaskedTokenMutants mutate (CtExpression original) {

        CtArrayAccess array_expr = (CtArrayAccess) original;
        CtExpression index_expr = array_expr.getIndexExpression();
        String originalStr = index_expr.toString();

        String operator = getOperator(original);
        String maskedExprStr = array_expr.getTarget().toString() + "[<mask>]";
        MaskedTokenMutants maskedTokenMutants = new MaskedTokenMutants(originalClassStr,method,originalStr,maskedExprStr,operator,index_expr.getPosition());
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
                    maskedTokenMutants.setMutant(masked_seq,token,predToken,pos,score);
                }
            }
            mutants.add(maskedTokenMutants);
        }

        return maskedTokenMutants;
    }
}

