import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
	    throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }

    public void start() { // S -> E
        if (look.tag == Tag.NUM || look.equals(Token.lpt)){
            expr();
            match(Tag.EOF);
        }else{
            error("error in grammar <start>");
        }
    }

    private void expr() { // E -> TE'
        /*if (look.tag == Tag.NUM || look.equals(Token.lpt)){
            term();
        }else if (look.equals(Token.plus) || look.equals(Token.minus) || look.tag == Tag.EOF || look.equals(Token.rpt)){
            exprp();
        }else{
            error("error in grammar <expr>");
        }*/
        switch(look.tag){

            case '(':
            case Tag.NUM:
            term();
            exprp();
            break;

            default:
            error("error in grammar <expr>");
        }
    }

    private void exprp() {
        switch (look.tag) {
        
        // E' -> +TE'
        case '+':
        match('+');
        term();
        exprp();
        break;

        // E' -> -TE'
        case'-':
        match('-');
        term();
        exprp();
        break;

        // E' -> *vuoto*        
        case Tag.EOF:
        case ')':
        break;

        default:
        error("error in grammar <exprp>");
        }
    }

    private void term() { // T -> FT'
        /*if (look.tag == Tag.NUM || look.equals(Token.lpt)){
            fact();
        }else if (look.equals(Token.mult) || look.equals(Token.div) || look.tag == Tag.EOF || look.equals(Token.rpt)){
            termp();
        }else{
            error("error in grammar <term>");
        }*/
        switch(look.tag){

            case '(':
            case Tag.NUM:
            fact();
            termp();
            break;

            default:
            error("error in grammar <expr>");
        }
    }

    private void termp() {
        switch (look.tag) {
            
            // T' -> *FT'
            case '*':
            match('*');
            fact();
            termp();
            break;

            // T' -> /FT'
            case '/':
            match('/');
            fact();
            termp();
            break;

            // T' -> *vuoto*
            case Tag.EOF:
            case ')':
            break;

            default:
            if (look.tag == '+' || look.tag == '-'){ // TOPPA ILLEGALE
                exprp();
                break;
            }
            error("error in grammar <termp>");
        }
    }

    private void fact() {
        switch (look.tag) {

            // F -> (E)
            case '(':
            match('(');
            expr();
            match(')');
            break;

            // F -> ID
            case Tag.NUM:
            match(Tag.NUM);
            break;

            default:
            error("error in grammar <fact>");
        }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "Test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}