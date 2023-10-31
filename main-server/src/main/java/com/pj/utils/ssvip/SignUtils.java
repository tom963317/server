package com.pj.utils.ssvip;

public class SignUtils {

    /* renamed from: K */
    public static String m4834K(String str) {
        String str2;
        if (str.isEmpty()) {
            return null;
        }
        StringBuilder str3 = new StringBuilder(BuildConfig.FLAVOR);
        for (int i = 0; i < str.length(); i++) {
            String valueOf = String.valueOf(str.charAt(i));
            char c = 65535;
            switch (valueOf.hashCode()) {
                case 34:
                    if (valueOf.equals("\"")) {
                        c = 0;
                        break;
                    }
                    break;
                case 37:
                    if (valueOf.equals("%")) {
                        c = 1;
                        break;
                    }
                    break;
                case 39:
                    if (valueOf.equals("'")) {
                        c = 2;
                        break;
                    }
                    break;
                case 44:
                    if (valueOf.equals(",")) {
                        c = 3;
                        break;
                    }
                    break;
                case 45:
                    if (valueOf.equals("-")) {
                        c = 4;
                        break;
                    }
                    break;
                case 46:
                    if (valueOf.equals(".")) {
                        c = 5;
                        break;
                    }
                    break;
                case 47:
                    if (valueOf.equals("/")) {
                        c = 6;
                        break;
                    }
                    break;
                case 48:
                    if (valueOf.equals("0")) {
                        c = 7;
                        break;
                    }
                    break;
                case 49:
                    if (valueOf.equals("1")) {
                        c = '\b';
                        break;
                    }
                    break;
                case 50:
                    if (valueOf.equals("2")) {
                        c = '\t';
                        break;
                    }
                    break;
                case 51:
                    if (valueOf.equals("3")) {
                        c = '\n';
                        break;
                    }
                    break;
                case 52:
                    if (valueOf.equals("4")) {
                        c = 11;
                        break;
                    }
                    break;
                case 53:
                    if (valueOf.equals("5")) {
                        c = '\f';
                        break;
                    }
                    break;
                case 54:
                    if (valueOf.equals("6")) {
                        c = '\r';
                        break;
                    }
                    break;
                case 55:
                    if (valueOf.equals("7")) {
                        c = 14;
                        break;
                    }
                    break;
                case 56:
                    if (valueOf.equals("8")) {
                        c = 15;
                        break;
                    }
                    break;
                case 57:
                    if (valueOf.equals("9")) {
                        c = 16;
                        break;
                    }
                    break;
                case 58:
                    if (valueOf.equals(":")) {
                        c = 17;
                        break;
                    }
                    break;
                case 65:
                    if (valueOf.equals("A")) {
                        c = 18;
                        break;
                    }
                    break;
                case 66:
                    if (valueOf.equals("B")) {
                        c = 19;
                        break;
                    }
                    break;
                case 67:
                    if (valueOf.equals("C")) {
                        c = 20;
                        break;
                    }
                    break;
                case 68:
                    if (valueOf.equals("D")) {
                        c = 21;
                        break;
                    }
                    break;
                case 69:
                    if (valueOf.equals("E")) {
                        c = 22;
                        break;
                    }
                    break;
                case 70:
                    if (valueOf.equals("F")) {
                        c = 23;
                        break;
                    }
                    break;
                case 71:
                    if (valueOf.equals("G")) {
                        c = 24;
                        break;
                    }
                    break;
                case 72:
                    if (valueOf.equals("H")) {
                        c = 25;
                        break;
                    }
                    break;
                case 73:
                    if (valueOf.equals("I")) {
                        c = 26;
                        break;
                    }
                    break;
                case 74:
                    if (valueOf.equals("J")) {
                        c = 27;
                        break;
                    }
                    break;
                case 75:
                    if (valueOf.equals("K")) {
                        c = 28;
                        break;
                    }
                    break;
                case 76:
                    if (valueOf.equals("L")) {
                        c = 29;
                        break;
                    }
                    break;
                case 77:
                    if (valueOf.equals("M")) {
                        c = 30;
                        break;
                    }
                    break;
                case 78:
                    if (valueOf.equals("N")) {
                        c = 31;
                        break;
                    }
                    break;
                case 79:
                    if (valueOf.equals("O")) {
                        c = ' ';
                        break;
                    }
                    break;
                case 80:
                    if (valueOf.equals("P")) {
                        c = '!';
                        break;
                    }
                    break;
                case 81:
                    if (valueOf.equals("Q")) {
                        c = '\"';
                        break;
                    }
                    break;
                case 82:
                    if (valueOf.equals("R")) {
                        c = '#';
                        break;
                    }
                    break;
                case 83:
                    if (valueOf.equals("S")) {
                        c = '$';
                        break;
                    }
                    break;
                case 84:
                    if (valueOf.equals("T")) {
                        c = '%';
                        break;
                    }
                    break;
                case 85:
                    if (valueOf.equals("U")) {
                        c = '&';
                        break;
                    }
                    break;
                case 86:
                    if (valueOf.equals("V")) {
                        c = '\'';
                        break;
                    }
                    break;
                case 87:
                    if (valueOf.equals("W")) {
                        c = '(';
                        break;
                    }
                    break;
                case 88:
                    if (valueOf.equals("X")) {
                        c = ')';
                        break;
                    }
                    break;
                case 89:
                    if (valueOf.equals("Y")) {
                        c = '*';
                        break;
                    }
                    break;
                case 90:
                    if (valueOf.equals("Z")) {
                        c = '+';
                        break;
                    }
                    break;
                case 91:
                    if (valueOf.equals("[")) {
                        c = ',';
                        break;
                    }
                    break;
                case 93:
                    if (valueOf.equals("]")) {
                        c = '-';
                        break;
                    }
                    break;
                case 95:
                    if (valueOf.equals("_")) {
                        c = '.';
                        break;
                    }
                    break;
                case 97:
                    if (valueOf.equals("a")) {
                        c = '/';
                        break;
                    }
                    break;
                case 98:
                    if (valueOf.equals("b")) {
                        c = '0';
                        break;
                    }
                    break;
                case 99:
                    if (valueOf.equals("c")) {
                        c = '1';
                        break;
                    }
                    break;
                case 100:
                    if (valueOf.equals("d")) {
                        c = '2';
                        break;
                    }
                    break;
                case 101:
                    if (valueOf.equals("e")) {
                        c = '3';
                        break;
                    }
                    break;
                case 102:
                    if (valueOf.equals("f")) {
                        c = '4';
                        break;
                    }
                    break;
                case 103:
                    if (valueOf.equals("g")) {
                        c = '5';
                        break;
                    }
                    break;
                case 104:
                    if (valueOf.equals("h")) {
                        c = '6';
                        break;
                    }
                    break;
                case 105:
                    if (valueOf.equals("i")) {
                        c = '7';
                        break;
                    }
                    break;
                case 106:
                    if (valueOf.equals("j")) {
                        c = '8';
                        break;
                    }
                    break;
                case 107:
                    if (valueOf.equals("k")) {
                        c = '9';
                        break;
                    }
                    break;
                case 108:
                    if (valueOf.equals("l")) {
                        c = ':';
                        break;
                    }
                    break;
                case 109:
                    if (valueOf.equals("m")) {
                        c = ';';
                        break;
                    }
                    break;
                case 110:
                    if (valueOf.equals("n")) {
                        c = '<';
                        break;
                    }
                    break;
                case 111:
                    if (valueOf.equals("o")) {
                        c = '=';
                        break;
                    }
                    break;
                case 112:
                    if (valueOf.equals("p")) {
                        c = '>';
                        break;
                    }
                    break;
                case 113:
                    if (valueOf.equals("q")) {
                        c = '?';
                        break;
                    }
                    break;
                case 114:
                    if (valueOf.equals("r")) {
                        c = '@';
                        break;
                    }
                    break;
                case 115:
                    if (valueOf.equals("s")) {
                        c = 'A';
                        break;
                    }
                    break;
                case 116:
                    if (valueOf.equals("t")) {
                        c = 'B';
                        break;
                    }
                    break;
                case 117:
                    if (valueOf.equals("u")) {
                        c = 'C';
                        break;
                    }
                    break;
                case 118:
                    if (valueOf.equals("v")) {
                        c = 'D';
                        break;
                    }
                    break;
                case 119:
                    if (valueOf.equals("w")) {
                        c = 'E';
                        break;
                    }
                    break;
                case 120:
                    if (valueOf.equals("x")) {
                        c = 'F';
                        break;
                    }
                    break;
                case 121:
                    if (valueOf.equals("y")) {
                        c = 'G';
                        break;
                    }
                    break;
                case 122:
                    if (valueOf.equals("z")) {
                        c = 'H';
                        break;
                    }
                    break;
                case 123:
                    if (valueOf.equals("{")) {
                        c = 'I';
                        break;
                    }
                    break;
                case 125:
                    if (valueOf.equals("}")) {
                        c = 'J';
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    str2 = "MD";
                    break;
                case 1:
                    str2 = "PD";
                    break;
                case 2:
                    str2 = "Cz";
                    break;
                case 3:
                    str2 = "zM";
                    break;
                case 4:
                    str2 = "cS";
                    break;
                case 5:
                    str2 = "TM";
                    break;
                case 6:
                    str2 = "SD";
                    break;
                case 7:
                    str2 = "lp";
                    break;
                case '\b':
                    str2 = "6B";
                    break;
                case '\t':
                    str2 = "fN";
                    break;
                case '\n':
                    str2 = "iV";
                    break;
                case 11:
                    str2 = "qa";
                    break;
                case '\f':
                    str2 = "da";
                    break;
                case '\r':
                    str2 = "PE";
                    break;
                case 14:
                    str2 = "JM";
                    break;
                case 15:
                    str2 = "lZ";
                    break;
                case 16:
                    str2 = "LY";
                    break;
                case 17:
                    str2 = "9Y";
                    break;
                case 18:
                    str2 = "yz";
                    break;
                case 19:
                    str2 = "qE";
                    break;
                case 20:
                    str2 = "Lf";
                    break;
                case 21:
                    str2 = "WX";
                    break;
                case 22:
                    str2 = "yJ";
                    break;
                case 23:
                    str2 = "zL";
                    break;
                case 24:
                    str2 = "fm";
                    break;
                case 25:
                    str2 = "vx";
                    break;
                case 26:
                    str2 = "S5";
                    break;
                case 27:
                    str2 = "cs";
                    break;
                case 28:
                    str2 = "DT";
                    break;
                case 29:
                    str2 = "7d";
                    break;
                case 30:
                    str2 = "jg";
                    break;
                case 31:
                    str2 = "yd";
                    break;
                case ' ':
                    str2 = "Wk";
                    break;
                case '!':
                    str2 = "b8";
                    break;
                case '\"':
                    str2 = "Cc";
                    break;
                case '#':
                    str2 = "6R";
                    break;
                case '$':
                    str2 = "Ie";
                    break;
                case '%':
                    str2 = "ve";
                    break;
                case '&':
                    str2 = "4S";
                    break;
                case '\'':
                    str2 = "Mo";
                    break;
                case '(':
                    str2 = "tk";
                    break;
                case ')':
                    str2 = "Qt";
                    break;
                case '*':
                    str2 = "7n";
                    break;
                case '+':
                    str2 = "YY";
                    break;
                case ',':
                    str2 = "eh";
                    break;
                case '-':
                    str2 = "ha";
                    break;
                case '.':
                    str2 = "TD";
                    break;
                case '/':
                    str2 = "nd";
                    break;
                case '0':
                    str2 = "3u";
                    break;
                case '1':
                    str2 = "W8";
                    break;
                case '2':
                    str2 = "BH";
                    break;
                case '3':
                    str2 = "oa";
                    break;
                case '4':
                    str2 = "0L";
                    break;
                case '5':
                    str2 = "aA";
                    break;
                case '6':
                    str2 = "q2";
                    break;
                case '7':
                    str2 = "Tv";
                    break;
                case '8':
                    str2 = "uk";
                    break;
                case '9':
                    str2 = "U3";
                    break;
                case ':':
                    str2 = "Ha";
                    break;
                case ';':
                    str2 = "Sv";
                    break;
                case '<':
                    str2 = "GB";
                    break;
                case '=':
                    str2 = "nQ";
                    break;
                case '>':
                    str2 = "Rv";
                    break;
                case '?':
                    str2 = "XL";
                    break;
                case '@':
                    str2 = "op";
                    break;
                case 'A':
                    str2 = "Fx";
                    break;
                case 'B':
                    str2 = "nJ";
                    break;
                case 'C':
                    str2 = "Pw";
                    break;
                case 'D':
                    str2 = "xt";
                    break;
                case 'E':
                    str2 = "Kq";
                    break;
                case 'F':
                    str2 = "SF";
                    break;
                case 'G':
                    str2 = "MQ";
                    break;
                case 'H':
                    str2 = "BZ";
                    break;
                case 'I':
                    str2 = "zT";
                    break;
                case 'J':
                    str2 = "KS";
                    break;
                default:
                    str2 = BuildConfig.FLAVOR;
                    break;
            }
            str3.append(str2);
        }
        return str3.toString();
    }

}
