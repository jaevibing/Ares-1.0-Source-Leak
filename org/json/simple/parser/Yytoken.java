package org.json.simple.parser;

public class Yytoken
{
    public static final int TYPE_VALUE = 0;
    public static final int TYPE_LEFT_BRACE = 1;
    public static final int TYPE_RIGHT_BRACE = 2;
    public static final int TYPE_LEFT_SQUARE = 3;
    public static final int TYPE_RIGHT_SQUARE = 4;
    public static final int TYPE_COMMA = 5;
    public static final int TYPE_COLON = 6;
    public static final int TYPE_EOF = -1;
    public int type;
    public Object value;
    
    public Yytoken(final int a1, final Object a2) {
        this.type = 0;
        this.value = null;
        this.type = a1;
        this.value = a2;
    }
    
    public String toString() {
        final StringBuffer v1 = /*EL:29*/new StringBuffer();
        /*SL:30*/switch (this.type) {
            case 0: {
                /*SL:32*/v1.append("VALUE(").append(this.value).append(")");
                /*SL:33*/break;
            }
            case 1: {
                /*SL:35*/v1.append("LEFT BRACE({)");
                /*SL:36*/break;
            }
            case 2: {
                /*SL:38*/v1.append("RIGHT BRACE(})");
                /*SL:39*/break;
            }
            case 3: {
                /*SL:41*/v1.append("LEFT SQUARE([)");
                /*SL:42*/break;
            }
            case 4: {
                /*SL:44*/v1.append("RIGHT SQUARE(])");
                /*SL:45*/break;
            }
            case 5: {
                /*SL:47*/v1.append("COMMA(,)");
                /*SL:48*/break;
            }
            case 6: {
                /*SL:50*/v1.append("COLON(:)");
                /*SL:51*/break;
            }
            case -1: {
                /*SL:53*/v1.append("END OF FILE");
                break;
            }
        }
        /*SL:56*/return v1.toString();
    }
}
