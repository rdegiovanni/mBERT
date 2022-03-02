package operators;

import codebert.CodeBERT;
import spoon.reflect.code.CtComment;
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
        CtUnaryOperator masked = new CtUnaryOperatorImpl();
        original.replace(masked);
        masked.setOperand(original.getOperand().clone());
        masked.setKind(original.getKind());
        UnaryOperatorKind maskedOp = masked.getKind();
        maskedOp.mask();

        String maskedMethodStr = method.toString();

//        CtComment comment = new CtCommentImpl();
//        CtElement parentStmt = getStatementToComment(masked);
//        if (parentStmt != null) {
//            comment.setContent(masked.toString());
//            comment.setCommentType(CtComment.CommentType.INLINE);
//            parentStmt.addComment(comment);
//        }

        CodeBERT.CodeBERTResult result = CodeBERT.mutate(maskedMethodStr);
        MaskedTokenMutants maskedTokenMutants = new MaskedTokenMutants(originalClassStr,method,originalOp,masked.toString(),position);
        maskedOp.unmask();
        if (result == CodeBERT.CodeBERTResult.SUCCEEDED) {
//            boolean pre = UnaryOperatorKind.isPre(originalOp);
            for (int pos = 0; pos < CODEBERT_NUM_OF_PREDICTIONS; pos++) {
                if (CodeBERT.predictedTokens.size() > pos) {
                    String predToken = CodeBERT.predictedTokens.get(pos);
                    maskedOp.setLabel(predToken);
//                    UnaryOperatorKind token = UnaryOperatorKind.getType(predToken,pre);
//                    token.setLabel(predToken);
//                    UnaryOperatorKind bkp = masked.getKind();
//                    masked.setKind(token);
                    maskedTokenMutants.setMutant(maskedOp,predToken,pos);
//                    masked.setKind(bkp);
                }
            }
            maskedOp.setLabel("");
            mutants.add(maskedTokenMutants);
        }
//        if (parentStmt != null)
//            parentStmt.removeComment(comment);
        masked.replace(original);
        return maskedTokenMutants;
    }
}
