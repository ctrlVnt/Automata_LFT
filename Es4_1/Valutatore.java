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

    private int expr() { 
	int term_val, exprp_val;

	// ... completare ...

    	term_val = term();
	exprp_val = exprp(term_val);

	// ... completare ...
	return exprp_val;
    }

    private int exprp(int exprp_i) {
	int term_val, exprp_val;
	switch (look.tag) {
	case '+':
            match('+');
            term_val = term();
            exprp_val = exprp(exprp_i + term_val);
            break;

    	// ... completare ...
	}
    }

    private int term() { 
	// ... completare ...
    }
    
    private int termp(int termp_i) { 
	// ... completare ...
    }
    
    private int fact() { 
	// ... completare ...
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
