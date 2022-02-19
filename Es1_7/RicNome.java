public class RicNome{

    public static boolean scan (String s){
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);

            switch(state){
                case 0:
                if (ch == 'R')
                    state = 1;
                else
                    state = 8;
                break;

                case 1:
                if (ch == 'i')
                    state = 2;
                else
                    state = 9;
                break;

                case 2:
                if (ch == 'c')
                    state = 3;
                else
                    state = 10;
                break;

                case 3:
                if (ch == 'c')
                    state = 4;
                else
                    state = 11;
                break;

                case 4:
                if (ch == 'a')
                    state = 5;
                else
                    state = 12;
                break;

                case 5:
                if (ch == 'r')
                    state = 6;
                else
                    state = 13;
                break;

                case 6:
                if (ch == 'd')
                    state = 7;
                else
                    state = 14;
                break;

                case 7:
                state = 20;
                break;

                case 8:
                if (ch == 'i')
                    state = 9;
                else
                    state = -1;
                break;

                case 9:
                if (ch == 'c')
                    state = 10;
                else
                    state = -1;
                break;

                case 10:
                if (ch == 'c')
                    state = 11;
                else
                    state = -1;
                break;

                case 11:
                if (ch == 'a')
                    state = 12;
                else
                    state = -1;
                break;

                case 12:
                if (ch == 'r')
                    state = 13;
                else
                    state = -1;
                break;

                case 13:
                if (ch == 'd')
                    state = 14;
                else
                    state = -1;
                break;

                case 14:
                if (ch == 'o')
                    state = 20;
                else
                    state = -1;
                break;

                case 20:
                state = -1;
                break;

            }
        }
        return state == 20;
    }
    public static void main (String[]args) {
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}