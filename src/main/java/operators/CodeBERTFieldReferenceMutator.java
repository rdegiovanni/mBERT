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

    public MaskedTokenMutants mutate (CtFieldReference original) {
        String originalStr = original.getSimpleName();
        SourcePosition origPosition = getSourcePosition(original);

        //compute token to mutate position
        int end = origPosition.getSourceEnd();
        int start = end - (originalStr.length() - 1);
        CompilationUnit origUnit = origPosition.getCompilationUnit();
        SourcePosition position = new SourcePositionImpl(origUnit,start,end,origUnit.getLineSeparatorPositions());


        MyFieldReferenceImpl masked = new MyFieldReferenceImpl();
//                new CtFieldReferenceImpl();
        masked.setSimpleName("<mask>");
//        masked.setParent(original.getParent());
//        masked.setDeclaringType(original.getDeclaringType());
//        masked.get
//        masked.setPosition(original.getPosition());
        // replace original expression with <mask? token
        original.replace(masked);
        String maskedMethodStr = method.toString();

//        CtComment comment = new CtCommentImpl();
//        CtElement parentStmt = getStatementToComment(masked);
//        if (parentStmt != null) {
//            comment.setContent(parentStmt.toString());
//            comment.setCommentType(CtComment.CommentType.INLINE);
//            parentStmt.addComment(comment);
//        }

        MaskedTokenMutants maskedTokenMutants = new MaskedTokenMutants(originalClassStr,method,originalStr,masked.toString(),position);
        CodeBERT.CodeBERTResult result = CodeBERT.mutate(maskedMethodStr);
        if (result == CodeBERT.CodeBERTResult.SUCCEEDED) {
            for (int pos = 0; pos < CODEBERT_NUM_OF_PREDICTIONS; pos++){
                if (CodeBERT.predictedTokens.size() > pos) {
                    String predToken = CodeBERT.predictedTokens.get(pos);
                    try {
                        masked.setSimpleName(predToken);
                        maskedTokenMutants.setMutant(masked.toString(), predToken, pos);
                    }
                    catch (Exception e) {
                        System.out.println("CodeBERTFieldReferenceMutator - invalid method invocation name: " + e.getMessage());
                    }
                }
            }
            mutants.add(maskedTokenMutants);
        }
//        if (parentStmt != null)
//            parentStmt.removeComment(comment);
        masked.replace(original);
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