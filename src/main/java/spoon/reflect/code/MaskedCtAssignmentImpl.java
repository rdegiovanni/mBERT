package spoon.reflect.code;

import spoon.reflect.visitor.DefaultJavaPrettyPrinter;
import spoon.support.reflect.code.CtAssignmentImpl;

public class MaskedCtAssignmentImpl extends CtAssignmentImpl {
    CtAssignment toMutate ;
    public MaskedCtAssignmentImpl(CtAssignment toMutate) {
        this.toMutate = toMutate;

    }

    @Override
    public String toString() {
        String maskedStr = toMutate.getAssigned().toString();
        maskedStr += " <mask> ";
        maskedStr += toMutate.getAssignment().toString();
        return maskedStr;
    }

    @Override
    public String prettyprint() {
        return toString();
    }
}
