package operators;

import codebert.CodeBERT;
import spoon.reflect.code.*;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtCodeSnippetExpressionImpl;
import spoon.support.reflect.code.CtCommentImpl;
import spoon.support.reflect.code.CtOperatorAssignmentImpl;
import spoon.support.reflect.cu.position.SourcePositionImpl;

/** a trivial mutation operator that transforms all binary operators to minus ("-") */
public class CodeBERTAssignmentMutator extends CodeBERTOperatorMutator{

	@Override
	public boolean isToBeProcessed(CtElement candidate) {
		return candidate instanceof CtAssignment;
	}


	public MaskedTokenMutants mutate(CtAssignment original) {
		//compute token to mutate position
		int start = original.getAssigned().getPosition().getSourceEnd()+1;
		int end = original.getAssignment().getPosition().getSourceStart() -1;
		CompilationUnit origUnit = original.getPosition().getCompilationUnit();
		SourcePosition position = new SourcePositionImpl(origUnit,start,end,origUnit.getLineSeparatorPositions());

		String originalStr = original.toString();
		CtOperatorAssignment masked = new CtOperatorAssignmentImpl();
		original.replace(masked);
		masked.setAssigned(original.getAssigned().clone());
		masked.setAssignment(original.getAssignment().clone());
		BinaryOperatorKind maskedOp = BinaryOperatorKind.MASK; //PLUS is just a dummy token to introduce <mask> token
		masked.setKind(maskedOp);
		maskedOp.mask();

		String maskedMethodStr = method.toString();

//		CtComment comment = new CtCommentImpl();
//		CtElement parentStmt = getStatementToComment(masked);
//		if (parentStmt != null) {
//			comment.setContent(parentStmt.toString());
//			comment.setCommentType(CtComment.CommentType.INLINE);
//			parentStmt.addComment(comment);
//		}

		CodeBERT.CodeBERTResult result = CodeBERT.mutate(maskedMethodStr);
		MaskedTokenMutants maskedTokenMutants = new MaskedTokenMutants(originalClassStr,method,originalStr,masked.toString(),position);
		maskedOp.unmask();

		if (result == CodeBERT.CodeBERTResult.SUCCEEDED) {
			for (int pos = 0; pos < CODEBERT_NUM_OF_PREDICTIONS; pos++) {
				if (CodeBERT.predictedTokens.size() > pos) {
					String predToken = CodeBERT.predictedTokens.get(pos);
					maskedOp.setLabel(predToken);
//					CtCodeSnippetExpressionImpl token = new CtCodeSnippetExpressionImpl();
//					token.setValue(predToken);
//					masked.replace(token);
					maskedTokenMutants.setMutant(maskedOp, predToken + "=",pos); //TODO: I am adding = to the predicted token.
//					token.replace(masked);
				}
			}
            maskedOp.setLabel("");
			mutants.add(maskedTokenMutants);
		}
//		if (parentStmt != null)
//			parentStmt.removeComment(comment);
		masked.replace(original);
		return maskedTokenMutants;
	}


}
