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
        System.out.println(expr_val); // print(exprp.val)
        break;

        default:
        error("error in grammar <start>");
	    }
    }

    private int expr(){ 
	int term_val, expr_val;

    switch(look.tag){
        
        // E -> TE'

        case '(':
        case Tag.NUM:
            term_val = term(); //expr.i = term.val
	        expr_val = exprp(term_val); //expr.val = exprp.val
            return expr_val;
        
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
            term_val = term(); //exprp1.i = exprp.i + term.val
            exprp_val = exprp(exprp_i + term_val); // exprp.val = exprp1.val
            return exprp_val;

        // E' -> -TE'

        case '-':
            match('-');
            term_val = term(); //exprp1.i = exprp.i + term.val
            exprp_val = exprp(exprp_i - term_val); // exprp.val = exprp1.val
            return exprp_val;

        // E' -> *vuoto*    

        case Tag.EOF:
        case ')':
            exprp_val = exprp_i; //exprp_val = exprp_i
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
            fact_val = fact(); //termp.i = fact.val
            termp_val = termp(fact_val); //term.val = termp.val
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
                fact_val = fact(); //termp1.i = termp.i * fact.val
                termp_val = termp(termp_i * fact_val); //termp.val = termp1.val
                return termp_val;
    
            // T' -> /FT'
    
            case '/':
                match('/');
                fact_val = fact(); //termp1.i = termp.i / fact.val
                termp_val = termp(termp_i / fact_val); //termp.val = termp1.val
                return termp_val;
    
            // T' -> *vuoto*    
    
            case Tag.EOF:
            case ')':
            case '+':
            case '-':
                termp_val = termp_i; //termp.val = termp.i
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
            expr_val = expr(); //fact.val = expr.val
            match(')');
            return expr_val;

            // F -> ID

            case Tag.NUM:
            NumberTok x = (NumberTok)look;
            num_value = x.number; //fact.val = NUM.value
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
