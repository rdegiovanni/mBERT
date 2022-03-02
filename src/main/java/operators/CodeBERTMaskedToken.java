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

//    @Override
//    public LiteralBase getBase() {
//        return null;
//    }
//
//    @Override
//    public List<CtTypeReference<?>> getTypeCasts() {
//        return null;
//    }
//
//    @Override
//    public CtExpression addTypeCast(CtTypeReference ctTypeReference) {
//        return null;
//    }
//
//    @Override
//    public CtExpression setTypeCasts(List list) {
//        return null;
//    }
//
//    @Override
//    public R partiallyEvaluate() {
//        return null;
//    }
//
//    @Override
//    public A getAnnotation(Class<A> aClass) {
//        return null;
//    }
//
//    @Override
//    public CtAnnotation<A> getAnnotation(CtTypeReference<A> ctTypeReference) {
//        return null;
//    }
//
//    @Override
//    public boolean hasAnnotation(Class<A> aClass) {
//        return false;
//    }
//
//    @Override
//    public List<CtAnnotation<? extends Annotation>> getAnnotations() {
//        return null;
//    }
//
//    @Override
//    public String getDocComment() {
//        return null;
//    }
//
//    @Override
//    public String getShortRepresentation() {
//        return null;
//    }
//
//    @Override
//    public SourcePosition getPosition() {
//        return null;
//    }
//
//    @Override
//    public void replace(CtElement ctElement) {
//
//    }
//
//    @Override
//    public boolean removeAnnotation(CtAnnotation<? extends Annotation> ctAnnotation) {
//        return false;
//    }
//
//    @Override
//    public boolean isImplicit() {
//        return false;
//    }
//
//    @Override
//    public Set<CtTypeReference<?>> getReferencedTypes() {
//        return null;
//    }
//
//    @Override
//    public CtElement getParent() throws ParentNotInitializedException {
//        return null;
//    }
//
//    @Override
//    public boolean isParentInitialized() {
//        return false;
//    }
//
//    @Override
//    public boolean hasParent(CtElement ctElement) {
//        return false;
//    }
//
//    @Override
//    public void updateAllParentsBelow() {
//
//    }
//
//    @Override
//    public CtRole getRoleInParent() {
//        return null;
//    }
//
//    @Override
//    public void delete() {
//
//    }
//
//    @Override
//    public Object getMetadata(String s) {
//        return null;
//    }
//
//    @Override
//    public Map<String, Object> getAllMetadata() {
//        return null;
//    }
//
//    @Override
//    public Set<String> getMetadataKeys() {
//        return null;
//    }
//
//    @Override
//    public List<CtComment> getComments() {
//        return null;
//    }
//
//    @Override
//    public E removeComment(CtComment ctComment) {
//        return null;
//    }
//
//    @Override
//    public E addComment(CtComment ctComment) {
//        return null;
//    }
//
//    @Override
//    public E setComments(List<CtComment> list) {
//        return null;
//    }
//
//    @Override
//    public E putMetadata(String s, Object o) {
//        return null;
//    }
//
//    @Override
//    public E setAllMetadata(Map<String, Object> map) {
//        return null;
//    }
//
//    @Override
//    public E setParent(E e) {
//        return null;
//    }
//
//    @Override
//    public E getParent(Filter<E> filter) throws ParentNotInitializedException {
//        return null;
//    }
//
//    @Override
//    public P getParent(Class<P> aClass) throws ParentNotInitializedException {
//        return null;
//    }
//
//    @Override
//    public E setAnnotations(List<CtAnnotation<? extends Annotation>> list) {
//        return null;
//    }
//
//    @Override
//    public E setPositions(SourcePosition sourcePosition) {
//        return null;
//    }
//
//    @Override
//    public List<E> getElements(Filter<E> filter) {
//        return null;
//    }
//
//    @Override
//    public E setImplicit(boolean b) {
//        return null;
//    }
//
//    @Override
//    public List<E> getAnnotatedChildren(Class<? extends Annotation> aClass) {
//        return null;
//    }
//
//    @Override
//    public E setPosition(SourcePosition sourcePosition) {
//        return null;
//    }
//
//    @Override
//    public E setDocComment(String s) {
//        return null;
//    }
//
//    @Override
//    public E addAnnotation(CtAnnotation<? extends Annotation> ctAnnotation) {
//        return null;
//    }
//
//    @Override
//    public void replace(Collection<E> collection) {
//
//    }
//
//    @Override
//    public CtLiteral clone() {
//        return null;
//    }
//
//    @Override
//    public CtPath getPath() {
//        return null;
//    }
//
//    @Override
//    public Iterator<CtElement> descendantIterator() {
//        return null;
//    }
//
//    @Override
//    public Iterable<CtElement> asIterable() {
//        return null;
//    }
//
//    @Override
//    public List<CtElement> getDirectChildren() {
//        return null;
//    }
//
//    @Override
//    public E setValueByRole(CtRole ctRole, T t) {
//        return null;
//    }
//
//    @Override
//    public T getValueByRole(CtRole ctRole) {
//        return null;
//    }
//
//    @Override
//    public CtLiteral setBase(LiteralBase literalBase) {
//        return null;
//    }
//
//    @Override
//    public CtLiteral setValue(Object o) {
//        return null;
//    }
//
//    @Override
//    public CtTypeReference getType() {
//        return null;
//    }
//
//    @Override
//    public CtTypedElement setType(CtTypeReference ctTypeReference) {
//        return null;
//    }
//
//    @Override
//    public Factory getFactory() {
//        return null;
//    }
//
//    @Override
//    public void setFactory(Factory factory) {
//
//    }
//
//    @Override
//    public void accept(CtVisitor ctVisitor) {
//
//    }
//
//    @Override
//    public CtQuery filterChildren(Filter<R> filter) {
//        return null;
//    }
//
//    @Override
//    public CtQuery map(CtFunction<I, R> ctFunction) {
//        return null;
//    }
//
//    @Override
//    public CtQuery map(CtConsumableFunction<I> ctConsumableFunction) {
//        return null;
//    }
//
//    @Override
//    public Object S() {
//        return null;
//    }
}
