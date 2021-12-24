import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    
    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count=0;

    public Translator(Lexer l, BufferedReader br) {
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

    public void prog() {    
        switch(look.tag){    
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.IF:
            case '{':
            int lnext_prog = code.newLabel();
            statlist(lnext_prog);
            code.emitLabel(lnext_prog);
            match(Tag.EOF);
            try {
                code.toJasmin();
            }
            catch(java.io.IOException e) {
                System.out.println("IO error\n");
            }
        }
    }

    public void stat() {
        switch(look.tag) {
	
            case Tag.READ:
                match(Tag.READ);
                match('(');
	            idlist(/* completare */);
                match(')');
                break;

            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
	            exprlist(/* completare */);
                match(')');
                break;

            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist(/* completare */);
                break;

            case Tag.WHILE:
                match(Tag.WHILE);
                match('(');
                bexpr();
                match(')');
                stat(); /*probabile etichetta*/
                break;

                case Tag.IF:
                match(Tag.IF);
                match('(');
                bexpr();
                match(')');
                stat();
                Fprimo(); /*occhio*/
                break;
                
                case '{':
                match('{');
                statlist();
                match('}');
                break;

            default:
                error("error in grammar <stat>");
        }
     }

    private void idlist(String x) {
        switch(look.tag) {
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word)look).lexeme);
                    if (id_addr==-1) {
                        id_addr = count;
                        st.insert(((Word)look).lexeme,count++);
                    }
                match(Tag.ID);
            default:
                error("error in grammar <idlist>");
    	}
    }

    private void expr() {
        switch(look.tag) {
	
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;

            case '+':
                match('+');
                match('(');
                exprlist();
                match(')');
                code.emit(OpCode.iadd);
                break;

            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;

            case '*':
                match('*');
                match('(');
                exprlist();
                match(')');
                code.emit(OpCode.imul);
                break;

            case Tag.NUM:
                match(Tag.NUM);
                NumberTok x = (NumberTok)look;
                int num_value = x.number;
                code.emit(OpCode.iload, num_value);
                break;
    
                
    
            case Tag.ID:
                match(Tag.ID);
                /*idlist(look.toString());*/
                idlist();
                /*Word a = (Word)look;
                String c = a.lexeme;
                code.emit(OpCode.iload, c);*/
                break;
            
            default:
            error("error in grammar <expr>");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "test.lft";
        //String path = "max_tre_num.lft";
        //String path = "factorial.lft";
        //String path = "euclid.lft";
        //String path = "esempio_semplice.lft";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator traduttore = new Translator(lex, br);
            traduttore.prog();
            System.out.println("Translation success!");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}

