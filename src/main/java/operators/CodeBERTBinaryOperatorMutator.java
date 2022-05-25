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

	public String getOperator(CtExpression candidate) {
		return "BinaryOperatorMutator";
	}

	public MaskedTokenMutants mutate(CtBinaryOperator original) {
		//compute token to mutate position
		int start = original.getLeftHandOperand().getPosition().getSourceEnd()+1;
		int end = original.getRightHandOperand().getPosition().getSourceStart() -1;
		CompilationUnit origUnit = original.getPosition().getCompilationUnit();
		SourcePosition position = new SourcePositionImpl(origUnit,start,end,origUnit.getLineSeparatorPositions());

		String originalOp = original.getKind().toString();

		String operator =  getOperator(original);
		String maskedExprStr = original.getLeftHandOperand().toString() + " <mask> " + original.getRightHandOperand().toString();
		MaskedTokenMutants maskedTokenMutants = new MaskedTokenMutants(originalClassStr,method,originalOp,maskedExprStr,operator,position);
		String maskedMethodStr = maskedTokenMutants.getMaskedSequence();
		CodeBERT.CodeBERTResult result = CodeBERT.mutate(maskedMethodStr);

		if (result == CodeBERT.CodeBERTResult.SUCCEEDED) {
			String masked_seq = CodeBERT.masked_sequence;
			for (int pos = 0; pos < CODEBERT_NUM_OF_PREDICTIONS; pos++) {
				if (CodeBERT.predictedTokens.size() > pos) {
					String predToken = CodeBERT.predictedTokens.get(pos);
					float score = CodeBERT.predictedScores.get(pos);
					maskedTokenMutants.setMutant(masked_seq,predToken, predToken,pos,score);
				}
			}
			mutants.add(maskedTokenMutants);
		}
		return maskedTokenMutants;
	}


}
