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

        // P -> SL $

        switch(look.tag){    
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.IF:
            case '{':
            int lnext_prog = code.newLabel();
            statlist(lnext_prog);
            match(Tag.EOF);
            try {
                code.toJasmin();
            }
            catch(java.io.IOException e) {
                System.out.println("IO error\n");
            }
            break;
            
            default:
            error("error in grammar <prog>");
        }
    }

    private void statlist(int lnext) {
        switch(look.tag){
            
            //SL -> S SL'

            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.WHILE:
            case Tag.IF:
            case '{':
            stat(lnext);
            statlistp(lnext);
            break;

            default:
            error("error in grammar <statlist>");
        }
    }

    private void statlistp(int lnext) {
        switch(look.tag){

            // SL -> ; S SL'

            case ';':
            match(';');
            stat(lnext);
            statlistp(lnext);
            break;

            // SL -> *vuoto*
            
            case Tag.EOF:
            case '}':
            break;

            default:
            error("error in grammar <statlistp>");
        }
    }

    public void stat(int lnext) {

        int ioOp;

        switch(look.tag) {

            // S -> read (id)
	
            case Tag.READ:
            code.emit(OpCode.invokestatic, 0); //{print("invokestatic Output/read()I")}
            ioOp = 1;
            match(Tag.READ);
            match('(');
            idlist(ioOp);
            match(')');
            break;

            // S -> print (EL)

            case Tag.PRINT:
            match(Tag.PRINT);
            match('(');
            exprlist('n');
            code.emit(OpCode.invokestatic, 1); //{print("invokestatic Output/print(I)V")}
            match(')');
            break;

            // S -> assign E to id

            case Tag.ASSIGN:
            ioOp = 0;
            match(Tag.ASSIGN);
            expr();
            match(Tag.TO);
            idlist(ioOp);
            break;

            // S -> while (B) S

            case Tag.WHILE:
            int btrue = code.newLabel();
            match(Tag.WHILE);
            match('(');
            int while_true = code.newLabel();
            int while_false = lnext;
            code.emitLabel(btrue); //{print(btrue)}
            bexpr(while_true, while_false);
            code.emitLabel(while_true); //{print(while_true)}
            match(')');
            stat(lnext);
            code.emit(OpCode.GOto, btrue); //{print("goto " + btrue)}
            code.emitLabel(while_false);
            break;

            // S -> if (B) S F'

            case Tag.IF:
            match(Tag.IF);
            match('(');
            int if_true = code.newLabel();
            int if_false = code.newLabel();
            bexpr(if_true, if_false);
            match(')');
            code.emitLabel(if_true); //{print(iftrue)}
            stat(if_true);
            Fprimo(if_false);
            break;

            // S -> {SL}

            case '{':
            match('{');
            statlist(lnext);
            match('}');
            break;

            default:
            error("error in grammar <stat>");
        }
     }

     private void Fprimo(int lnext){
        switch(look.tag){

            // F' -> end
            
            case Tag.END:
            match(Tag.END);
            code.emitLabel(lnext); //{print(lnext)}
            break;

            // F' -> else S end

            case Tag.ELSE:
            int jump = code.newLabel();
            code.emit(OpCode.GOto, jump); //{print("goto " + jump)}
            code.emitLabel(lnext); //{print(lnext)}
            match(Tag.ELSE);
            stat(lnext);
            match(Tag.END);
            code.emitLabel(jump);
            break;

            default:
            error("error in grammar <Fprimo>");
        }
    }

    private void idlist(int op) {
        switch(look.tag) {

            // id -> ID id'

            case Tag.ID:
            int id_addr = st.lookupAddress(((Word)look).lexeme);
                if (id_addr==-1) {
                    id_addr = count;
                    st.insert(((Word)look).lexeme,count++);
                }
            match(Tag.ID);
            code.emit(OpCode.istore, id_addr); //{print("istore " + id_addr)}
            idlistp(op);
            break;

            default:
            error("error in grammar <idlist>");
    	}
    }

    private void idlistp(int op) {
        switch(look.tag){

            // id' -> , ID id'

            case ',':
            if(op == 0){
                code.emit(OpCode.iload, count -1); //{print("iload " + (count - 1))}
            }else{
                code.emit(OpCode.invokestatic, 0); //{print("invokestatic Output/read()I")}
            }
            match(',');
            int id_addr = st.lookupAddress(((Word)look).lexeme);
            if (id_addr==-1) {
                id_addr = count;
                st.insert(((Word)look).lexeme,count++);
            }
            code.emit(OpCode.istore, id_addr); //{print("istore " + id_addr)}
            match(Tag.ID);
            idlistp(op);
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

    private void bexpr(int case_true, int case_false) {

        OpCode opCode = OpCode.if_icmpeq; //scelgo a caso, temporaneo
        
        switch(look.tag){

            // B -> RELOOP E E

            case Tag.RELOP:
            switch (((Word) look).lexeme) {
                case "==":
                    opCode = OpCode.if_icmpeq;
                    break;
                case "<":
                    opCode = OpCode.if_icmplt;
                    break;
                case "<=":
                    opCode = OpCode.if_icmple;
                    break;
                case "<>":
                    opCode = OpCode.if_icmpne;
                    break;
                case ">":
                    opCode = OpCode.if_icmpgt;
                    break;
                case ">=":
                    opCode = OpCode.if_icmpge;
                    break;
                default:
                    error("error in Tag.RELOP");
            }
            match(Tag.RELOP);
            expr();
            expr();
            code.emit(opCode, case_true);   //{print(opCode + case_true)}
            code.emit(OpCode.GOto, case_false); //{print("goto " + case_false)}
            break;

            default:
                error("error in grammar <bexpr>");
        }
    }

    private void expr() {

        char myOp = ' ';

        switch(look.tag) {

            // E -> - E E
	
            case '-':
            match('-');
            expr();
            expr();
            code.emit(OpCode.isub); //{print("isub")}
            break;

            // E -> + (EL)

            case '+':
            match('+');
            myOp = '+';
            match('(');
            exprlist(myOp);
            match(')');
            break;

            // E -> / E E

            case '/':
            match('/');
            expr();
            expr();
            code.emit(OpCode.idiv); //{print("idiv")}
            break;

            // E -> * (EL)

            case '*':
            match('*');
            myOp = '*';
            match('(');
            exprlist(myOp);
            match(')');
            break;

            // E -> NUM

            case Tag.NUM:
            NumberTok x = (NumberTok)look;
            int num_value = x.number;
            match(Tag.NUM);
            code.emit(OpCode.ldc, num_value); //{print("ldc " + num_value)}
            break;
    
            // E -> ID
    
            case Tag.ID:
            int read_id_address = st.lookupAddress(((Word) look).lexeme);
            if (read_id_address == -1){
                error("var " + ((Word) look).lexeme + " has not been declared");
            }
            code.emit(OpCode.iload, read_id_address); //{print("iload " + read_id_address)}
            match(Tag.ID);
            break;
            
            default:
            error("error in grammar <expr>");
        }
    }

    private void exprlist(char op) {
        switch(look.tag){

            // EL -> E EL'

            case '+':
            case '-':
            case '*':
            case '/':
            case Tag.NUM:
            case Tag.ID:
            expr();
            exprlistp(op);
            break;

            default:
            error("error in grammar <exprlist>");
        }
    }

    private void exprlistp(char op) {
        switch(look.tag){

            // EL' -> , E EL'

            case ',':
            if(op == 'n'){
                code.emit(OpCode.invokestatic, 1); //{print("invokestatic Output/print(I)V")}
            }
            match(',');
            expr();
            exprlistp(op);
            if(op == '+'){
                code.emit(OpCode.iadd); //{print("iadd")}
            }else if (op == '*'){
                code.emit(OpCode.imul); //{print("imul")}
            }
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
        String path = "test.lft";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator traduttore = new Translator(lex, br);
            traduttore.prog();
            System.out.println("Translation success!");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}

