//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package spoon.reflect.code;

import spoon.SpoonException;

public enum UnaryOperatorKind {
    POS,
    NEG,
    NOT,
    COMPL,
    PREINC,
    PREDEC,
    POSTINC,
    POSTDEC,
    MASK;

    private UnaryOperatorKind() {

    }

    private String label = "";
    private boolean isMasked = false;
    private boolean isTokenSetted = false;

    public void mask() {
        isMasked = true;
    }

    public void unmask() {
        isMasked = false;
    }

    public void unsetLabel() {
        isTokenSetted = false;
    }
    public void setLabel(String label) {
        //TODO: remove space?
        this.label = label; //.replaceAll("\\s+", "");
        if (label == null || label == "")
            isTokenSetted = false;
        else
            isTokenSetted = true;
    }

    public String getLabel() {
        return label;
    }
//
//    public static UnaryOperatorKind getType (String op, boolean pre) {
//        if (op == null) return null;
//        op = op.replaceAll("\\s+", "");
//        switch (op) {
//            case "+" : return POS;
//            case "-" : return NEG;
//            case "!" : return NOT;
//            case "~" : return COMPL;
//            case "++" : if (pre) return PREINC; else return POSTINC;
//            case "--" : if (pre) return PREDEC; else return POSTDEC;
//            default: return null;
//        }
//    }

    public static boolean isPre (UnaryOperatorKind op) {
        return !(op == POSTDEC || op == POSTINC);
    }


    @Override
    public String toString () {
        if (isMasked)// || this == MASK)
            return "<mask>";
        else if (isTokenSetted)
            return label;
        else {
            switch (this) {
                case POS:
                    return "+";
                case NEG:
                    return "-";
                case NOT:
                    return "!";
                case COMPL:
                    return "~";
                case PREINC:
                    return "++";
                case PREDEC:
                    return "--";
                case POSTINC:
                    return "++";
                case POSTDEC:
                    return "--";
                //            case MASK:
                //                return "<mask>";
                //            case TOKEN:{
                //                if (label == null || label == "")
                //                    return "<token>";
                //                else
                //                    return label;
                //            }
                default:
                    throw new SpoonException("Unsupported operator " + this.name());
            }
        }
    }
}
