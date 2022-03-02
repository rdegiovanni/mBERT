//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package spoon.reflect.visitor;

import spoon.SpoonException;
import spoon.reflect.code.BinaryOperatorKind;
import spoon.reflect.code.UnaryOperatorKind;

class OperatorHelper {
    private OperatorHelper() {
    }

    public static boolean isPrefixOperator(UnaryOperatorKind o) {
        return !isSufixOperator(o);
    }

    public static boolean isSufixOperator(UnaryOperatorKind o) {
        return o.name().startsWith("POST");
    }

    public static String getOperatorText(UnaryOperatorKind o) {
        return o.toString();
//        switch(o) {
//            case POS:
//                return "+";
//            case NEG:
//                return "-";
//            case NOT:
//                return "!";
//            case COMPL:
//                return "~";
//            case PREINC:
//                return "++";
//            case PREDEC:
//                return "--";
//            case POSTINC:
//                return "++";
//            case POSTDEC:
//                return "--";
//            case MASK:
//                return "<mask>";
//            case TOKEN:
//                return o.getLabel();
//            default:
//                throw new SpoonException("Unsupported operator " + o.name());
//        }
    }

    public static String getOperatorText(BinaryOperatorKind o) {
        return o.toString();
//        switch(o) {
//            case OR:
//                return "||";
//            case AND:
//                return "&&";
//            case BITOR:
//                return "|";
//            case BITXOR:
//                return "^";
//            case BITAND:
//                return "&";
//            case EQ:
//                return "==";
//            case NE:
//                return "!=";
//            case LT:
//                return "<";
//            case GT:
//                return ">";
//            case LE:
//                return "<=";
//            case GE:
//                return ">=";
//            case SL:
//                return "<<";
//            case SR:
//                return ">>";
//            case USR:
//                return ">>>";
//            case PLUS:
//                return "+";
//            case MINUS:
//                return "-";
//            case MUL:
//                return "*";
//            case DIV:
//                return "/";
//            case MOD:
//                return "%";
//            case INSTANCEOF:
//                return "instanceof";
//            case MASK:
//                return "<mask>";
//            case TOKEN:
//                return o.getLabel();
//            default:
//                throw new SpoonException("Unsupported operator " + o.name());
//        }
    }
}
