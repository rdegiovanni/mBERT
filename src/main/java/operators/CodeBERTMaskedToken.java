package operators;

import codebert.CodeBERT;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtElement;
import spoon.support.reflect.code.CtCodeSnippetExpressionImpl;
import spoon.support.reflect.code.CtCommentImpl;
import spoon.support.reflect.code.CtExpressionImpl;
import spoon.support.reflect.code.CtLiteralImpl;

public class CodeBERTMaskedToken extends CtLiteralImpl<UnaryOperatorKind> {

    public  CodeBERTMaskedToken() {
//        this.literal = lit.clone();
    }

    private CtExpression literal;

    @Override
    public String toString() {
        return "<mask>";
    }

    @Override
    public String toStringDebug() {
        return null;
    }

    @Override
    public String prettyprint() {
        return "<mask>";
    }

    @Override
    public UnaryOperatorKind getValue() {
        UnaryOperatorKind masked = UnaryOperatorKind.POS;
        masked.mask();
        return masked;
    }


}
