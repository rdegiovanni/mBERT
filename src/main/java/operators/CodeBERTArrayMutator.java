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

    public MaskedTokenMutants mutate (CtExpression original) {
//        String originalClassStr = method.getParent(CtClass.class).toString();
        CtArrayAccess array_expr = (CtArrayAccess) original;
        CtExpression index_expr = array_expr.getIndexExpression();
        String originalStr = index_expr.toString();
        CtExpressionImpl masked = new CodeBERTMaskedToken();
//        masked.setValue("<masked>");
        // replace original expression with <mask? token
        index_expr.replace(masked);
        String maskedMethodStr = method.toString();

//        CtComment comment = new CtCommentImpl();
//        CtElement parentStmt = getStatementToComment(masked);
//        if (parentStmt != null) {
//            comment.setContent(parentStmt.toString());
//            comment.setCommentType(CtComment.CommentType.INLINE);
//            parentStmt.addComment(comment);
//        }

        MaskedTokenMutants maskedTokenMutants = new MaskedTokenMutants(originalClassStr,method,originalStr,masked.toString(),index_expr.getPosition());
        CodeBERT.CodeBERTResult result = CodeBERT.mutate(maskedMethodStr);
        if (result == CodeBERT.CodeBERTResult.SUCCEEDED) {
            for (int pos = 0; pos < CODEBERT_NUM_OF_PREDICTIONS; pos++){
                if (CodeBERT.predictedTokens.size() > pos) {
                    String predToken = CodeBERT.predictedTokens.get(pos);
                    CtCodeSnippetExpression token = new CtCodeSnippetExpressionImpl();
                    token.setValue(predToken);
                    masked.replace(token);
                    maskedTokenMutants.setMutant(token,predToken,pos);
                    token.replace(masked);
                }
            }
            mutants.add(maskedTokenMutants);
        }
//        if (parentStmt != null)
//            parentStmt.removeComment(comment);
        masked.replace(index_expr);
        return maskedTokenMutants;
    }
}

//if (CodeBERT.predictedTokens.size() >= 1) {
//        String firstPredToken = CodeBERT.predictedTokens.get(0);
//        CtCodeSnippetExpressionImpl firstToken = new CtCodeSnippetExpressionImpl();
//        firstToken.setValue(firstPredToken);
//        masked.replace(firstToken);
//        maskedTokenMutants.setFirstMutant(firstToken,firstPredToken);
//        firstToken.replace(masked);
//        }
//        if (CodeBERT.predictedTokens.size() >= 2) {
//        String secondPredToken = CodeBERT.predictedTokens.get(1);
//        CtCodeSnippetExpressionImpl secondToken = new CtCodeSnippetExpressionImpl();
//        secondToken.setValue(secondPredToken);
//        masked.replace(secondToken);
//        maskedTokenMutants.setSecondMutant(secondToken,secondPredToken);
//        secondToken.replace(masked);
//        }
//        if (CodeBERT.predictedTokens.size() >= 3) {
//        String thirdPredToken = CodeBERT.predictedTokens.get(2);
//        CtCodeSnippetExpressionImpl thirdToken = new CtCodeSnippetExpressionImpl();
//        thirdToken.setValue(thirdPredToken);
//        masked.replace(thirdToken);
//        maskedTokenMutants.setThirdMutant(thirdToken,thirdPredToken);
//        thirdToken.replace(masked);
//        }
//        if (CodeBERT.predictedTokens.size() >= 4) {
//        String fourthPredToken = CodeBERT.predictedTokens.get(3);
//        CtCodeSnippetExpressionImpl fourthToken = new CtCodeSnippetExpressionImpl();
//        fourthToken.setValue(fourthPredToken);
//        masked.replace(fourthToken);
//        maskedTokenMutants.setFourthMutant(fourthToken,fourthPredToken);
//        fourthToken.replace(masked);
//        }
//        if (CodeBERT.predictedTokens.size() >= 5) {
//        String fifthPredToken = CodeBERT.predictedTokens.get(4);
//        CtCodeSnippetExpressionImpl fifthToken = new CtCodeSnippetExpressionImpl();
//        fifthToken.setValue(fifthPredToken);
//        masked.replace(fifthToken);
//        maskedTokenMutants.setFifthMutant(fifthToken,fifthPredToken);
//        fifthToken.replace(masked);
//        }