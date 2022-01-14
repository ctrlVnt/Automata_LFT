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

    public void prog() { // P -> SL $
        switch(look.tag){
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.IF:
            case '{':
            statlist();
            match(Tag.EOF);
            break;

            default:
            error("error in grammar <prog>");
        }
    }

    private void statlist() {
        switch(look.tag){

            //SL -> S SL'

            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.IF:
            case '{':
            stat();
            statlistp();
            break;

            default:
            error("error in grammar <statlist>");
        }
    }

    private void statlistp() {
        switch(look.tag){

            // SL -> ; S SL'

            case ';':
            match(';');
            stat();
            statlistp();
            break;

            // SL -> *vuoto*
            
            case Tag.EOF:
            case '}':
            break;

            default:
            error("error in grammar <statlistp>");
        }
    }

    private void stat() {
        switch(look.tag){

            // S -> assign E to id

            case Tag.ASSIGN:
            match(Tag.ASSIGN);
            expr();
            match(Tag.TO);
            idlist();
            break;

            // S -> print (EL)

            case Tag.PRINT:
            match(Tag.PRINT);
            match('(');
            exprlist();
            match(')');
            break;
            
            // S -> read (id)

            case Tag.READ:
            match(Tag.READ);
            match('(');
            idlist();
            match(')');
            break;

            // S -> while (B) S

            case Tag.WHILE:
            match(Tag.WHILE);
            match('(');
            bexpr();
            match(')');
            stat();
            break;

            // S -> if (B) S F'

            case Tag.IF:
            match(Tag.IF);
            match('(');
            bexpr();
            match(')');
            stat();
            Fprimo();
            break;

            // S -> {SL}
            
            case '{':
            match('{');
            statlist();
            match('}');
            break;

            default:
            error("error in grammar <stat>");
        }
       
    }

    private void Fprimo() {
        switch(look.tag){

            // F' -> end
            
            case Tag.END:
            match(Tag.END);
            break;

            // F' -> else S end

            case Tag.ELSE:
            match(Tag.ELSE);
            stat();
            match(Tag.END);
            break;

            default:
            error("error in grammar <Fprimo>");
        }
    }

    private void idlist() {
        switch(look.tag){

            // id -> ID id'

            case Tag.ID:
            match(Tag.ID);
            idlistp();
            break;

            default:
            error("error in grammar <idlist>");
        }
    }

    private void idlistp() {
        switch(look.tag){

            // id' -> , ID id'

            case ',':
            match(',');
            match(Tag.ID);
            idlistp();
            break;

            // id' -> *vuoto*

            case Tag.EOF:
            case ';':
            case ')':
            case Tag.END:
            case Tag.ELSE:
            case '}':
            break;

            default:
            error("error in grammar <idlistp>");
        }
    }

    private void bexpr() {
        switch(look.tag){

            // B -> RELOOP E E

            case Tag.RELOP:
            match(Tag.RELOP);
            expr();
            expr();
            break;

            default:
            error("error in grammar <bexpr>");
        }
    }

    private void expr() {
        switch(look.tag){

            // E -> + (EL)

            case '+':
            match('+');
            match('(');
            exprlist();
            match(')');
            break;

            // E -> - E E

            case '-':
            match('-');
            expr();
            expr();
            break;

            // E -> * (EL)

            case '*':
            match('*');
            match('(');
            exprlist();
            match(')');
            break;

            // E -> / E E

            case '/':
            match('/');
            expr();
            expr();
            break;

            // E -> NUM

            case Tag.NUM:
            match(Tag.NUM);
            break;

            // E -> ID

            case Tag.ID:
            match(Tag.ID);
            break;

            default:
            error("error in grammar <expr>");
        }

    }

    private void exprlist() {
        switch(look.tag){

            // EL -> E EL'

            case '+':
            case '-':
            case '*':
            case '/':
            case Tag.NUM:
            case Tag.ID:
            expr();
            exprlistp();
            break;

            default:
            error("error in grammar <exprlist>");
        }
    }

    private void exprlistp() {
        switch(look.tag){

            // EL' -> , E EL'

            case ',':
            match(',');
            expr();
            exprlistp();
            break;

            // EL' -> *vuoto*

            case ')':
            break;

            default:
            error("error in grammar <exprlistp>");
        }

    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "Test.lft";
        //String path = "max_tre_num.lft";
        //String path = "factorial.lft";
        //String path = "euclid.lft";
        //String path = "esempio_semplice.lft";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}