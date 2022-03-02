package spoon.reflect.reference;

import spoon.support.reflect.reference.CtFieldReferenceImpl;
import spoon.support.reflect.reference.CtTypeReferenceImpl;

public class MyFieldReferenceImpl<T> extends CtFieldReferenceImpl<T> {

    String label = "";

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        this.simplename = label;
    }

    @Override
    public String getSimpleName() {
        return label;
    }

    @Override
    public CtReference setSimpleName(String simplename) {
        this.simplename = simplename;
        this.label = simplename;
        return this;
    }

    @Override
    public String toString() {
        return  label;
    }

    @Override
    public String prettyprint() {
        return label;
    }
}
