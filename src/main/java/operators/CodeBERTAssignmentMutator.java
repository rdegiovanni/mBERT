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

	public String getOperator(CtExpression candidate) {
		return "AssignmentMutator";
	}

	public MaskedTokenMutants mutate(CtAssignment original) {
		//compute token to mutate position
		int start = original.getAssigned().getPosition().getSourceEnd()+1;
		int end = start; //original.getAssignment().getPosition().getSourceStart() -1;
		CompilationUnit origUnit = original.getPosition().getCompilationUnit();
		SourcePosition position = new SourcePositionImpl(origUnit,start,end,origUnit.getLineSeparatorPositions());

		String originalStr = original.toString();

		String operator = getOperator(original);

		String maskedExprStr = original.getAssigned().toString() + " <mask>= " + original.getAssignment().toString();
		MaskedTokenMutants maskedTokenMutants = new MaskedTokenMutants(originalClassStr,method,originalStr,maskedExprStr,operator,position);
		String maskedMethodStr = maskedTokenMutants.getMaskedSequence();
		CodeBERT.CodeBERTResult result = CodeBERT.mutate(maskedMethodStr);

		if (result == CodeBERT.CodeBERTResult.SUCCEEDED) {
			String masked_seq = CodeBERT.masked_sequence;
			for (int pos = 0; pos < CODEBERT_NUM_OF_PREDICTIONS; pos++) {
				if (CodeBERT.predictedTokens.size() > pos) {
					String predToken = CodeBERT.predictedTokens.get(pos);
					float score = CodeBERT.predictedScores.get(pos);
					maskedTokenMutants.setMutant(masked_seq,predToken,predToken,pos,score);
				}
			}
			mutants.add(maskedTokenMutants);
		}
		return maskedTokenMutants;
	}


}
