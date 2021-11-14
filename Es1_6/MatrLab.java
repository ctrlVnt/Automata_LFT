public class MatrLab{

    /*public static boolean scan (String s){
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);

            switch(state){
            case 0:
            if ((ch >= '0' && ch <= '9') && ch % 2 == 0)
                state = 1;
            else if ((ch >= '0' && ch <= '9') && ch % 2 != 0)
                state = 2;
            else
                state = -1;
            break;

            case 1:
            if ((ch >= '0' && ch <= '9') && ch % 2 == 0)
                state = 1;
            else if ((ch >= '0' && ch <= '9') && ch % 2 != 0)
                state = 2;
            else
                state = -1;
            break;

            case 2:
            if ((ch >= '0' && ch <= '9') && ch % 2 == 0)
                state = 4;
            else if ((ch >= '0' && ch <= '9') && ch % 2 != 0)
                state = 3;
            else
                state = -1;
            break;

            case 3:
            if ((ch >= '0' && ch <= '9') && ch % 2 == 0)
                state = 1;
            else if ((ch >= '0' && ch <= '9') && ch % 2 != 0)
                state = 4;
            else if (ch >= 'L' && ch <= 'Z')
                state = 5;
            else
                state = -1;
            break;

            case 4:
            if ((ch >= '0' && ch <= '9') && ch % 2 == 0)
                state = 1;
            else if ((ch >= '0' && ch <= '9') && ch % 2 != 0)
                state = 2;
            else if (ch >= 'L' && ch <= 'Z')
                state = 5;
            else
                state = -1;
            break;

            case 5:
            if (ch >= 'a' && ch <= 'z')
                state = 5;
            else if (ch == ' ')
                state = 6;
            else
                state = -1;
            break;

            case 6:
            if (ch >= 'A' && ch <= 'Z')
                state = 5;
            else
                state = -1;
            break;
            }
        }
        return state == 5;
    }*/

    public static boolean scan (String s){
        int state = 0;
        int i = 0;

        while(state >= 0 && i < s.length()){
            final char ch = s.charAt(i++);

            switch(state){
                case 0:
                if ((ch >= '0' && ch <= '9') && ch % 2 == 0)
                    state = 1;
                else if ((ch >= '0' && ch <= '9') && ch % 2 != 0)
                    state = 2;
                else
                    state = -1;
                break;

                case 1:
                if ((ch >= '0' && ch <= '9') && ch % 2 == 0)
                    state = 6;
                else if ((ch >= '0' && ch <= '9') && ch % 2 != 0)
                    state = 4;
                else
                    state = -1;
                break;

                case 2:
                if ((ch >= '0' && ch <= '9') && ch % 2 == 0)
                    state = 3;
                else if ((ch >= '0' && ch <= '9') && ch % 2 != 0)
                    state = 5;
                else
                    state = -1;
                break;

                case 3:
                if ((ch >= '0' && ch <= '9') && ch % 2 == 0)
                    state = 6;
                else if ((ch >= '0' && ch <= '9') && ch % 2 != 0)
                    state = 4;
                else if (ch >= 'L' && ch <= 'Z')
                    state = 7;
                else
                    state = -1;
                break;

                case 4:
                if ((ch >= '0' && ch <= '9') && ch % 2 == 0)
                    state = 3;
                else if ((ch >= '0' && ch <= '9') && ch % 2 != 0)
                    state = 5;
                else if (ch >= 'A' && ch <= 'K')
                    state = 7;
                else
                    state = -1;
                break;

                case 5:
                if ((ch >= '0' && ch <= '9') && ch % 2 == 0)
                    state = 3;
                else if ((ch >= '0' && ch <= '9') && ch % 2 != 0)
                    state = 5;
                else if (ch >= 'L' && ch <= 'Z')
                    state = 7;
                else
                    state = -1;
                break;

                case 6:
                if ((ch >= '0' && ch <= '9') && ch % 2 == 0)
                    state = 6;
                else if ((ch >= '0' && ch <= '9') && ch % 2 != 0)
                    state = 4;
                else if (ch >= 'A' && ch <= 'K')
                    state = 7;
                else
                    state = -1;
                break;

                case 7:
                if (ch >= 'a' && ch <= 'z')
                    state = 7;
                else
                    state = -1;
                break;
            }
        }
        return state == 7;
    }


    public static void main(String[]args){
        System.out.println(scan(args[0]) ? "OK" : "NOPE");
    }
}