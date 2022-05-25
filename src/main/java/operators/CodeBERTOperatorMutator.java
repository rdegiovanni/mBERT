package operators;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.*;
import spoon.reflect.cu.SourcePosition;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtFieldReference;
import spoon.reflect.reference.CtReference;
import spoon.reflect.reference.CtTypeReference;
import spoon.support.reflect.code.CtStatementImpl;
import spoon.support.reflect.reference.CtFieldReferenceImpl;

import java.util.LinkedList;
import java.util.List;

public class CodeBERTOperatorMutator extends AbstractProcessor<CtElement> {
    public static final int CODEBERT_NUM_OF_PREDICTIONS = 5;
    CtMethod<?> method = null;
    public List<MaskedTokenMutants> mutants = new LinkedList<>();
    public List<String> unhandled_mutations = new LinkedList<>();
    public String originalClassStr;

    @Override
    public boolean isToBeProcessed(CtElement candidate) {
        //first we list exceptions
//        if (isImplicit(candidate))
//            return false;
        if (candidate.isImplicit() || candidate.getPosition() == null)
            return false;
        if (candidate instanceof CtConstructorCall ||
                candidate instanceof CtTypeAccess ||
                candidate instanceof CtNewArray ||
                candidate instanceof CtAnnotation ||
                inheritsFromAssertion(candidate)
        )
            return false;
        if (candidate instanceof CtExpression
                ||  candidate instanceof CtFieldReference)
            return true;
        if (candidate instanceof CtTypeReference && candidate.getParent() != null
                && candidate.getParent() instanceof CtTypeAccess
                && !inheritsFromConstructorCall(candidate)
                && candidate.getPosition().isValidPosition()) {
            return true;
        }
        return false;
    }

    @Override
    public void process(CtElement candidate) {
        MaskedTokenMutants predictedMutants = null;
        if (candidate instanceof CtBinaryOperator) {
            CodeBERTBinaryOperatorMutator mutator = new CodeBERTBinaryOperatorMutator();
            mutator.setMethod(this.method);
            mutator.setOriginalClassStr(this.originalClassStr);
            predictedMutants = mutator.mutate((CtBinaryOperator) candidate);
        }
        else if (candidate instanceof CtUnaryOperator) {
            CodeBERTUnaryOperatorMutator mutator = new CodeBERTUnaryOperatorMutator();
            mutator.setMethod(this.method);
            mutator.setOriginalClassStr(this.originalClassStr);
            predictedMutants = mutator.mutate((CtUnaryOperator) candidate);
        }
        else if (candidate instanceof CtAssignment) {
            CodeBERTAssignmentMutator mutator = new CodeBERTAssignmentMutator();
            mutator.setMethod(this.method);
            mutator.setOriginalClassStr(this.originalClassStr);
            predictedMutants = mutator.mutate((CtAssignment) candidate);
        }
        else if (candidate instanceof CtVariableRead ||
                candidate instanceof CtVariableWrite ||
                candidate instanceof CtConditional ||
                candidate instanceof CtThisAccess ||
                candidate instanceof CtLiteral) {
            CodeBERTLiteralMutator mutator = new CodeBERTLiteralMutator();
            mutator.setMethod(this.method);
            mutator.setOriginalClassStr(this.originalClassStr);
            predictedMutants =  mutator.mutate((CtExpression)candidate);
        }
        else if (candidate instanceof CtArrayRead ||
                candidate instanceof CtArrayWrite ) {
            CodeBERTArrayMutator mutator = new CodeBERTArrayMutator();
            mutator.setMethod(this.method);
            mutator.setOriginalClassStr(this.originalClassStr);
            predictedMutants =  mutator.mutate((CtExpression)candidate);
        }
        else if (candidate instanceof CtFieldReference) {
            CodeBERTFieldReferenceMutator mutator = new CodeBERTFieldReferenceMutator();
            mutator.setMethod(this.method);
            mutator.setOriginalClassStr(this.originalClassStr);
            predictedMutants = mutator.mutate((CtFieldReference) candidate);
        }
        else if (candidate instanceof CtTypeReference) {
            CodeBERTTypeReferenceMutator mutator = new CodeBERTTypeReferenceMutator();
            mutator.setMethod(this.method);
            mutator.setOriginalClassStr(this.originalClassStr);
            predictedMutants = mutator.mutate((CtTypeReference) candidate);
        }
        else if (candidate instanceof CtInvocation) {
            CodeBERTInvocationMutator mutator = new CodeBERTInvocationMutator();
            mutator.setMethod(this.method);
            mutator.setOriginalClassStr(this.originalClassStr);
            predictedMutants =  mutator.mutate((CtInvocation) candidate);
        }
        else {
//            throw new IllegalStateException("Unhandled expression to be mutated: " + candidate.getClass().toString());
            System.out.println("Unhandled expression to be mutated: " + candidate.getClass().toString());
            unhandled_mutations.add(candidate.getClass().toString());
        }

        if (predictedMutants != null)
            mutants.add(predictedMutants);
    }

    public void setMethod(CtMethod<?> element) {
        method = element;
    }

    public void setOriginalClassStr(String originalClassStr) {
        this.originalClassStr = originalClassStr;
    }

    public int numOfMutants () {
        return mutants.size();
    }

    CtElement getStatementToComment (CtElement e) {
        if (e == null || e instanceof CtMethod || e instanceof CtClass)
            return null;
        if (e instanceof CtStatement || e instanceof CtBinaryOperator || e instanceof CtUnaryOperator)
            return e;
        return getStatementToComment(e.getParent());
    }

    public SourcePosition getSourcePosition (CtElement e) {
        if (e == null)
            return null;
        if (e.getPosition() != null && e.getPosition().isValidPosition())
            return e.getPosition();
        if (e.getParent() != null)
            return getSourcePosition(e.getParent());
        return null;
    }

    public boolean isImplicit (CtElement e) {
        if (e == null || e instanceof CtMethod || e instanceof CtClass || e instanceof CtReturn)
            return false;
        if (e.isImplicit())
            return true;
        if (e.getParent() == null)
            return false;

        return isImplicit(e.getParent());
    }

    public boolean inheritsFromConstructorCall (CtElement e) {
        if (e == null || e instanceof CtMethod || e instanceof CtClass)
            return false;
        if (e instanceof CtConstructorCall)
            return true;
        if (e.getParent() == null)
            return false;

        return inheritsFromConstructorCall(e.getParent());
    }

    public boolean inheritsFromAssertion (CtElement e) {
        if (e == null || e instanceof CtMethod || e instanceof CtClass)
            return false;
        if (e instanceof CtAssert)
            return true;
        if (e.getParent() == null)
            return false;

        return inheritsFromAssertion(e.getParent());
    }

    public List<MaskedTokenMutants> getAllMaskedMutants() {
        return mutants;
    }

    public List<mBERTMutant> getAllMutants() {
        List<mBERTMutant> all_mutants = new LinkedList<>();
        for(MaskedTokenMutants m : mutants) {
            all_mutants.addAll(m.getUsefulMutants());
        }
        return all_mutants;
    }


}
