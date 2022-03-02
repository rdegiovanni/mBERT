package operators;

import codebert.CodeBERT;
import spoon.reflect.code.*;
import spoon.reflect.cu.CompilationUnit;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtElement;
import spoon.support.reflect.code.CtBinaryOperatorImpl;
import spoon.support.reflect.cu.position.SourcePositionImpl;


/** a trivial mutation operator that transforms all binary operators to minus ("-") */
public class CodeBERTBinaryOperatorMutator extends CodeBERTOperatorMutator{

	@Override
	public boolean isToBeProcessed(CtElement candidate) {
		return candidate instanceof CtBinaryOperator;
	}

	public MaskedTokenMutants mutate(CtBinaryOperator original) {
		//compute token to mutate position
		int start = original.getLeftHandOperand().getPosition().getSourceEnd()+1;
		int end = original.getRightHandOperand().getPosition().getSourceStart() -1;
		CompilationUnit origUnit = original.getPosition().getCompilationUnit();
		SourcePosition position = new SourcePositionImpl(origUnit,start,end,origUnit.getLineSeparatorPositions());
		//mutateLeft
		//mutateOperator
		String originalOp = original.getKind().toString();
		CtBinaryOperator masked = new CtBinaryOperatorImpl();
		original.replace(masked);
		masked.setRightHandOperand(original.getRightHandOperand().clone());
		masked.setLeftHandOperand(original.getLeftHandOperand().clone());
		masked.setType(original.getType());
		BinaryOperatorKind maskedOp = BinaryOperatorKind.MASK;
		masked.setKind(maskedOp);
		maskedOp.mask();

		String maskedMethodStr = method.toString();

//		CtComment comment = new CtCommentImpl();
//		CtElement parentStmt = getStatementToComment(masked);
//		if (parentStmt != null) {
//			comment.setContent(masked.toString());
//			comment.setCommentType(CtComment.CommentType.INLINE);
//			parentStmt.addComment(comment);
//		}

		CodeBERT.CodeBERTResult result = CodeBERT.mutate(maskedMethodStr);
		MaskedTokenMutants maskedTokenMutants = new MaskedTokenMutants(originalClassStr,method,originalOp,masked.toString(),position);
		maskedOp.setLabel(originalOp);
		maskedOp.unmask();
		if (result == CodeBERT.CodeBERTResult.SUCCEEDED) {
			for (int pos = 0; pos < CODEBERT_NUM_OF_PREDICTIONS; pos++) {
				if (CodeBERT.predictedTokens.size() > pos) {
					String predToken = CodeBERT.predictedTokens.get(pos);
//					BinaryOperatorKind token = BinaryOperatorKind.getType(predToken);
					maskedOp.setLabel(predToken);
//					BinaryOperatorKind bkp = masked.getKind();
//					masked.setKind(token);
					maskedTokenMutants.setMutant(maskedOp, predToken,pos);
//					masked.setKind(bkp);
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
