import java.io.*; 

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) { 
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

    public void start() { 
	int expr_val;

    switch(look.tag){

        // S -> E

        case '(':
        case Tag.NUM:
        expr_val = expr();
	    match(Tag.EOF);
        System.out.println(expr_val);
        break;

        default:
        error("error in grammar <start>");
	    }
    }

    private int expr(){ 
	int term_val, exprp_val;

    switch(look.tag){
        
        // E -> TE'

        case '(':
        case Tag.NUM:
            term_val = term();
	        exprp_val = exprp(term_val);
            return exprp_val;
        
        default:
            throw new Error("error in grammar <expr>");
        }
    }

    private int exprp(int exprp_i) {
	int term_val, exprp_val;

	switch (look.tag) {

         // E' -> +TE'

        case '+':
            match('+');
            term_val = term();
            exprp_val = exprp(exprp_i + term_val);
            return exprp_val;

        // E' -> -TE'

        case '-':
            match('-');
            term_val = term();
            exprp_val = exprp(exprp_i - term_val);
            return exprp_val;

        // E' -> *vuoto*    

        case Tag.EOF:
        case ')':
            exprp_val = exprp_i;
            return exprp_val;

        default:
            throw new Error("error in grammar <exprp>");
        }
    }

    private int term() {
    int fact_val, termp_val;

    switch(look.tag){
        
        // T -> FT'

        case '(':
        case Tag.NUM:
            fact_val = fact();
            termp_val = termp(fact_val);
            return termp_val;
        
        default:
            throw new Error("error in grammar <term>");
        }
    }
    
    private int termp(int termp_i) { 
        int fact_val, termp_val;

        switch (look.tag) {
    
            // T' -> *FT'
    
            case '*':
                match('*');
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
                return termp_val;
    
            // T' -> /FT'
    
            case '/':
                match('/');
                fact_val = fact();
                termp_val = termp(termp_i / fact_val);
                return termp_val;
    
            // T' -> *vuoto*    
    
            case Tag.EOF:
            case ')':
            case '+':
            case '-':
                termp_val = termp_i;
                return termp_val;
    
            default:
                throw new Error("error in grammar <termp>");
            }
    }
    
    private int fact() { 
        int expr_val, num_value;

        switch (look.tag) {

            // F -> (E)

            case '(':
            match('(');
            expr_val = expr();
            match(')');
            return expr_val;

            // F -> ID

            case Tag.NUM:
            NumberTok x = (NumberTok)look;
            num_value = x.number;
            match(Tag.NUM);
            return num_value;

            default:
            throw new Error("error in grammar <fact>");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "Test.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
