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

    public MaskedTokenMutants mutate(CtInvocation original) {
        String originalOp = original.getExecutable().getSimpleName();

        //compute token to mutate position
        int start = original.getPosition().getSourceStart();
        if (original.getTarget()!= null && !original.getTarget().isImplicit() && original.getTarget().toString().length() > 0)
//            start = original.getTarget().getPosition().getSourceStart() + original.getTarget().toString().length()+1;
            start = original.getTarget().getPosition().getSourceEnd() + 2;//one more for .
        int end = start + originalOp.length() -1;

        CompilationUnit origUnit = original.getPosition().getCompilationUnit();
        SourcePosition position = new SourcePositionImpl(origUnit,start,end,origUnit.getLineSeparatorPositions());

        CtExecutableReference originalExecutableReference = original.getExecutable();
        MyExecutableReferenceImpl masked =  new MyExecutableReferenceImpl();
        masked.setSimpleName("<mask>");
//        masked.setParent(originalExecutableReference.getParent());
//        masked.setDeclaringType(originalExecutableReference.getDeclaringType());
//        masked.setParameters(originalExecutableReference.getParameters());
//        masked.setStatic(originalExecutableReference.isStatic());
//        masked.setType(originalExecutableReference.getType());
//        masked.setActualTypeArguments(originalExecutableReference.getActualTypeArguments());

        original.setExecutable(masked);

        String maskedMethodStr = method.toString();
//        CtComment comment = new CtCommentImpl();
//        CtElement parentStmt = getStatementToComment(masked);
//        if (parentStmt != null) {
//            comment.setContent(masked.toString());
//            comment.setCommentType(CtComment.CommentType.BLOCK);
//            parentStmt.addComment(comment);
//        }
        CodeBERT.CodeBERTResult result = CodeBERT.mutate(maskedMethodStr);
        MaskedTokenMutants maskedTokenMutants = new MaskedTokenMutants(originalClassStr,method,originalOp,masked.toString(),position);
//        maskedOp.unmask();
        if (result == CodeBERT.CodeBERTResult.SUCCEEDED) {
            for (int pos = 0; pos < CODEBERT_NUM_OF_PREDICTIONS; pos++) {
                if (CodeBERT.predictedTokens.size() > pos) {
                    String predToken = CodeBERT.predictedTokens.get(pos);
                    try {
                        masked.setSimpleName(predToken);
                        maskedTokenMutants.setMutant(masked.toString(), predToken, pos);
                    }
                    catch (Exception e) {
                        System.out.println("CodeBERTInvocationMutator - invalid method invocation name: " + e.getMessage());
                    }
                }
            }
//            maskedOp.setLabel("");
            mutants.add(maskedTokenMutants);
        }
//        if (parentStmt != null)
//            parentStmt.removeComment(comment);
        original.setExecutable(originalExecutableReference);
        return maskedTokenMutants;
    }
}
