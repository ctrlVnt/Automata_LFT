import java.io.*; 
//import java.util.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';
    
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }
        
        if (peek == '/'){
            readch(br);
                if (peek == '/'){
                    while (peek != (char) -1 && peek != '\n'){
                        readch(br);
                    }readch(br);
                }else if (peek == '*'){
                    int stat = 0;
                    while (stat != 3){
                        switch(stat){
                            case 0:
                            readch(br);
                            if (peek == '*')
                                stat = 1;
                            else if (Character.isLetterOrDigit(peek) || Character.isJavaIdentifierStart(peek))
                                stat = 0;
                            else if (peek == (char) -1){
                                System.err.println("Error, comment not close");
                                return null;
                            }
                            break;
            
                            case 1:
                            readch(br);
                            if (peek == '*')
                                stat = 1;
                            else if (Character.isLetterOrDigit(peek) || Character.isJavaIdentifierStart(peek))
                                stat = 0;
                            else if (peek == '/')
                                stat = 2;
                            else if (peek == (char) -1){
                                System.err.println("Error, comment not close");
                                return null;
                            }
                            break;

                            case 2:
                            readch(br);
                            if (peek == '\n')
                                readch(br);
                            stat = 3;
                            break;
                        }
                    }
                }else{
                    peek = ' ';
                    return Token.div;
                }
            }
            

        
        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;

	        case '(':
                peek = ' ';
                return Token.lpt;
            
            case ')':
                peek = ' ';
                return Token.rpt;

            case '{':
                peek = ' ';
                return Token.lpg;

            case '}':
                peek = ' ';
                return Token.rpg;

            case '+':
                peek = ' ';
                return Token.plus;

            case '-':
                peek = ' ';
                return Token.minus;

            case '*':
                peek = ' ';
                return Token.mult;

            case '/':
                peek = ' ';
                return Token.div;
                            
            case ';':
                peek = ' ';
                return Token.semicolon;

            case ',':
                peek = ' ';
                return Token.comma;

            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character" + " after & : "  + peek );
                    return null;
                }

            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character" + " after | : "  + peek );
                    return null;
                }

            case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                }else if (peek == '>'){
                    peek = ' ';
                    return Word.ne;
                } else {
                    return Word.lt;
                }

            case '>':
                readch(br);
                if(peek == '='){
                    peek = ' ';
                    return Word.ge;
                }else{
                    return Word.gt;
                }

            case '=':
            readch(br);
                if (peek == '='){
                    peek = ' ';
                    return Word.eq;
                }else{
                    System.err.print("Erroneous character" + " after = : "  + peek );
                }
          
            case (char)-1:
                return new Token(Tag.EOF);

            default:

                if (Character.isLetter(peek) || peek == '_') {
                String a = "";
                int n = 0;
                while (Character.isLetterOrDigit(peek)|| peek == '_'){
                    if (peek != '_')
                        n += 1;
                    a+=peek;
                    readch(br);
                }

                if (n == 0){
                    System.err.println("Erroneous variable: " + a );
                    return null;
                }

                switch (a){
                case "assign":
                    return Word.assign;

                case "to":
                    return Word.to;

                case "if":
                    return Word.iftok;

                case "else":
                    return Word.elsetok;

                case "while":
                    return Word.whiletok;

                case "begin":
                    return Word.begin;

                case "end":
                    return Word.end;

                case "print":
                    return Word.print;

                case "read":
                    return Word.read;

                default:
                    return new Word(Tag.ID, a);
                }
                
                } else if (Character.isDigit(peek)) { //trovare il modo di farlo senza cast, moltiplicando x10 caratteri successivi
                    String b = "";
                    while (Character.isDigit(peek)){
                        b += peek;
                        readch(br);
                        if (Character.isLetter(peek) || peek == '_'){
                            System.err.println("Erroneous character into number string: " + peek );
                            return null;
                        }
                    }
                    return new NumberTok(Tag.NUM, Integer.parseInt(b));

                } else {
                        System.err.println("Erroneous character: " + peek );
                        return null;
                }
         }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "Test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }

}
