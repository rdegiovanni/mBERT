//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package spoon.reflect.code;

import spoon.SpoonException;

public enum BinaryOperatorKind {
    OR,
    AND,
    BITOR,
    BITXOR,
    BITAND,
    EQ,
    NE,
    LT,
    GT,
    LE,
    GE,
    SL,
    SR,
    USR,
    PLUS,
    MINUS,
    MUL,
    DIV,
    MOD,
    INSTANCEOF,
    MASK; //Special <mask> token
//    TOKEN; //it saves other predicted tokens

    private BinaryOperatorKind() {
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

//    public static BinaryOperatorKind getType (String op) {
//        if (op == null) return null;
//        op = op.replaceAll("\\s+", "");
//        switch(op) {
//            case "||":
//                return OR;
//            case "&&":
//                return AND;
//            case "|":
//                return BITOR;
//            case "^":
//                return BITXOR;
//            case "&":
//                return BITAND;
//            case "==":
//                return EQ;
//            case "!=":
//                return NE;
//            case "<":
//                return LT;
//            case ">":
//                return GT;
//            case "<=":
//                return LE;
//            case ">=":
//                return GE;
//            case "<<":
//                return SL;
//            case ">>":
//                return SR;
//            case ">>>":
//                return USR;
//            case "+":
//                return PLUS;
//            case "-":
//                return MINUS;
//            case "*":
//                return MUL;
//            case "/":
//                return DIV;
//            case "%":
//                return MOD;
//            case "instanceof":
//                return INSTANCEOF;
//            case "<mask>":
//                return MASK;
//            default:
//                return TOKEN;
//        }
//    }

    @Override
    public String toString() {
        if (isMasked)// || this == MASK)
            return "<mask>";
        else if (isTokenSetted)
            return label;
        else {
            switch (this) {
                case OR:
                    return "||";
                case AND:
                    return "&&";
                case BITOR:
                    return "|";
                case BITXOR:
                    return "^";
                case BITAND:
                    return "&";
                case EQ:
                    return "==";
                case NE:
                    return "!=";
                case LT:
                    return "<";
                case GT:
                    return ">";
                case LE:
                    return "<=";
                case GE:
                    return ">=";
                case SL:
                    return "<<";
                case SR:
                    return ">>";
                case USR:
                    return ">>>";
                case PLUS:
                    return "+";
                case MINUS:
                    return "-";
                case MUL:
                    return "*";
                case DIV:
                    return "/";
                case MOD:
                    return "%";
                case INSTANCEOF:
                    return "instanceof";
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
